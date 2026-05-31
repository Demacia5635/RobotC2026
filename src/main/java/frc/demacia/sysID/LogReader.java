package frc.demacia.sysID;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ejml.simple.SimpleMatrix;

import frc.demacia.utils.log.LogManager;

/**
 * Reads and analyzes FRC WPILOG files specifically formatted for SysID.
 * This class unpacks raw binary telemetry data, handles endianness conversions,
 * applies moving-average filtering to minimize noise, and utilizes Ordinary Least 
 * Squares (OLS) linear regression to calculate system identification constants 
 * (Ks, Kv, Ka, etc.).
 */
public class LogReader {

    // Threshold below which motor voltages are filtered out as noise/inactivity
    private static final double VOLTAGE_THRESHOLD = 0.5;
    // Window size for the moving-average smoothing of raw acceleration data
    private static final int SMOOTH_WINDOW = 3;
    // Percentage of data points with the highest residual error to drop as outliers
    private static final double OUTLIER_PERCENTAGE = 0.15;

    // Maps a unique WPILOG Record ID to its corresponding entry descriptions
    private static Map<Integer, List<EntryDescription>> entries;

    /**
     * Represents a logged telemetry channel (entry name, data type, and captured data points).
     */
    private static class EntryDescription {
        String name;
        String type;
        List<DataPoint> data = new ArrayList<>();

        EntryDescription(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    /**
     * Holds a single data frame containing a timestamp and its unpacked numeric values.
     */
    private static class DataPoint {
        long timestamp;
        double[] value;

        DataPoint(long timestamp, double[] value) {
            this.timestamp = timestamp;
            this.value = value.clone(); // Cloned to avoid external mutation/reference leaks
        }
    }

    /**
     * Entry point to parse a log file and execute the statistical SysID analysis.
     * 
     * @param fileName Path to the target .wpilog file.
     * @return A map linking each mechanism/group name to its computed physical model coefficients.
     */
    public static Map<String, BucketResult> getResult(String fileName) {
        entries = new HashMap<>();
        try {
            wpilogReader(fileName); // Step 1: Parse binary file into memory structures
            return performAnalysis(); // Step 2: Math and OLS Regression
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Initializes the file stream, validates the WPILOG magic signature, and steps through records.
     */
    private static void wpilogReader(String fileName) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {
            
            // Verify magic header signature
            byte[] signature = readHeader(dataInputStream);
            if (!Arrays.equals(signature, "WPILOG".getBytes())) {
                throw new IOException("Invalid WPILOG file format");
            }
            
            skipHeaderExtra(dataInputStream); // Skip version details and metadata headers
            readRecords(dataInputStream);    // Continually loop through sequential records
        }
    }

    /**
     * Reads the initial 6 bytes of the file which must contain the string "WPILOG".
     */
    private static byte[] readHeader(DataInputStream dataInputStream) throws IOException {
        byte[] signature = new byte[6];
        dataInputStream.readFully(signature);
        return signature;
    }

    /**
     * Parses and bypasses the version markers and optional metadata length tracking in the main header.
     */
    private static void skipHeaderExtra(DataInputStream dataInputStream) throws IOException {
        dataInputStream.readShort(); // Read and discard layout version
        int extraLength = Integer.reverseBytes(dataInputStream.readInt()); // Convert Little Endian to Big Endian length
        if(extraLength > 0) {
            dataInputStream.skipBytes(extraLength);
        }
    }

    /**
     * Sequentially pulls records from the stream until an End-Of-File (EOF) exception is raised.
     */
    private static void readRecords(DataInputStream dataInputStream) throws IOException {
        while (true) {
            try {
                readRecord(dataInputStream);
            } catch (EOFException e) {
                break; // Gracefully handle expected EOF
            }
        }
    }

    /**
     * Unpacks a single record frame according to the variable-length WPILOG binary layout.
     */
    private static boolean readRecord(DataInputStream dataInputStream) throws IOException {
        int headerByte = dataInputStream.readUnsignedByte();
        
        // Extract field byte-lengths from the control bitmask
        int idLength = (headerByte & 0x3) + 1;
        int payloadLength = (headerByte >> 2 & 0x3) + 1;
        int timestampLength = (headerByte >> 4 & 0x7) + 1;

        // Extract values using explicit Little Endian assembly
        int recordId = readLittleEndianInt(dataInputStream, idLength);
        int payloadSize = readLittleEndianInt(dataInputStream, payloadLength);
        long timestamp = readLittleEndianLong(dataInputStream, timestampLength);

        if (recordId == 0) {
            // ID 0 represents a Control Record which initializes a new metadata/telemetry channel
            addEntryFromControlRecord(dataInputStream, payloadSize);
            return false;
        } else {
            // Standard data-bearing telemetry record
            List<EntryDescription> entryList = entries.get(recordId);
            if (entryList != null && !entryList.isEmpty()) {
                String type = entryList.get(0).type.trim();
                boolean isFloat = type.equals("float") || type.equals("float[]");
                boolean isDouble = type.equals("double") || type.equals("double[]");

                if (isFloat || isDouble) {
                    double[] value = null;

                    // Unpack and cast IEEE 754 32-bit floats
                    if (isFloat) {
                        if (payloadSize % 4 == 0) {
                            int count = payloadSize / 4;
                            value = new double[count];
                            for (int i = 0; i < count; i++) {
                                int raw = Integer.reverseBytes(dataInputStream.readInt());
                                value[i] = (double) Float.intBitsToFloat(raw);
                            }
                        } else {
                            dataInputStream.skipBytes(payloadSize);
                        }
                    // Unpack IEEE 754 64-bit doubles
                    } else if (isDouble) {
                        if (payloadSize % 8 == 0) {
                            int count = payloadSize / 8;
                            value = new double[count];
                            for (int i = 0; i < count; i++) {
                                long raw = Long.reverseBytes(dataInputStream.readLong());
                                value[i] = Double.longBitsToDouble(raw);
                            }
                        } else {
                            dataInputStream.skipBytes(payloadSize);
                        }
                    }

                    // Store the processed data point into its mapped entry container
                    if (value != null) {
                        int numEntries = entryList.size();
                        // Handle interleaved multichannel arrays via systematic chunking slices
                        if (numEntries > 1 && value.length % numEntries == 0) {
                            int chunkSize = value.length / numEntries;
                            for (int i = 0; i < numEntries; i++) {
                                double[] slice = Arrays.copyOfRange(value, i * chunkSize, (i + 1) * chunkSize);
                                entryList.get(i).data.add(new DataPoint(timestamp, slice));
                            }
                        } else {
                            for (EntryDescription entry : entryList) {
                                entry.data.add(new DataPoint(timestamp, value));
                            }
                        }
                        return true;
                    }
                } else {
                    dataInputStream.skipBytes(payloadSize); // Skip unsupported non-numeric logs (Strings, Booleans)
                }
            } else {
                dataInputStream.skipBytes(payloadSize); // Skip unmapped record IDs
            }
        }
        return false;
    }

    /**
     * Decodes a Start Control Record (Type 0) and tracks it if identified as a motor system.
     */
    private static void addEntryFromControlRecord(DataInputStream dataInputStream, int payloadSize) throws IOException {
        int recordType = dataInputStream.readUnsignedByte();
        if (recordType == 0) { // Record Type 0 corresponds to a Start Control Event
            int entryId = Integer.reverseBytes(dataInputStream.readInt());
            int nameLength = Integer.reverseBytes(dataInputStream.readInt());
            String name = readString(dataInputStream, nameLength);
            int typeLength = Integer.reverseBytes(dataInputStream.readInt());
            String type = readString(dataInputStream, typeLength);
            int metaLength = Integer.reverseBytes(dataInputStream.readInt());
            String metadata = readString(dataInputStream, metaLength);

            // Handle multi-channel metadata bindings combined with the '|' separator character
            String[] names = name.split(" \\| ");
            String[] metas = metadata.split(" \\| ");
            
            for (int i = 0; i < names.length; i++) {
                String currentName = names[i].trim().split("\\: ")[0];
                String currentMeta = (i < metas.length) ? metas[i].trim() : "";
                System.out.println(currentMeta + " - " + currentName);
                
                // Track only entries explicitly marked with "motor" metadata for SysID calculations
                if (currentMeta.contains("motor")) {
                    entries.putIfAbsent(entryId, new ArrayList<>());
                    entries.get(entryId).add(new EntryDescription(currentName, type));
                }
            }
        } else {
            // Ignore other control operations (such as data structure modifications or entry closures)
            if (payloadSize > 1) {
                dataInputStream.skipBytes(payloadSize - 1);
            }
        }
    }

    /**
     * Utility method to read a UTF-8 encoded string from the incoming stream.
     */
    private static String readString(DataInputStream dataInputStream, int length) throws IOException {
        byte[] bytes = new byte[length];
        dataInputStream.readFully(bytes);
        return new String(bytes, "UTF-8");
    }

    /**
     * Assemblies a variable-length Little Endian byte sequence into an integer primitives.
     */
    private static int readLittleEndianInt(DataInputStream dis, int bytes) throws IOException {
        int result = 0;
        for (int i = 0; i < bytes; i++) {
            result |= (dis.readUnsignedByte() << (i * 8));
        }
        return result;
    }

    /**
     * Assemblies a variable-length Little Endian byte sequence into a long primitives.
     */
    private static long readLittleEndianLong(DataInputStream dis, int bytes) throws IOException {
        long result = 0L;
        for (int i = 0; i < bytes; i++) {
            result |= ((long) dis.readUnsignedByte() << (i * 8));
        }
        return result;
    }

    /**
     * Iterates over all discovered log groups and triggers individual system modeling tasks.
     */
    private static Map<String, BucketResult> performAnalysis() {
        Map<String, BucketResult> results = new HashMap<>();
        Set<String> groups = findGroups();
        
        for (String group : groups) {
            System.out.println(" group = " + group);
            BucketResult result = analyzeGroup(group);
            if (result != null) {
                results.put(group, result);
            }
        }
        return results;
    }

    /**
     * Aggregates unique registered channel string names across mapped data records.
     */
    private static Set<String> findGroups() {
        Set<String> groups = new HashSet<>();
        for (List<EntryDescription> list : entries.values()) {
            for (EntryDescription entry : list) {
                groups.add(entry.name);
            }
        }
        return groups;
    }

    /**
     * Isolates a single group's metrics, enforces temporal sorting, and synchronizes data for model fitting.
     */
    private static BucketResult analyzeGroup(String name) {
        List<DataPoint> allData = new ArrayList<>();
        for (List<EntryDescription> list : entries.values()) {
            for (EntryDescription entry : list) {
                if (entry.name.equals(name)) {
                    allData.addAll(entry.data);
                }
            }
        }

        if (allData.isEmpty()) {
            System.out.println(" no data for " + name);
            return null;
        }

        // Force rigorous chronological timeline ordering
        allData.sort((p1, p2) -> Long.compare(p1.timestamp, p2.timestamp));
        List<SyncedDataPoint> syncedData = synchronizeData(allData);
        
        return calculateResult(syncedData, name);
    }

    /**
     * Encapsulates a temporally matched kinematic frame (Position, Velocity, Acceleration, Voltage).
     */
    public static class SyncedDataPoint {
        double velocity, position, acceleration, rawAcceleration, voltage;
        long timestamp;
        double error; // Keeps track of deviation to evaluate outlier status
        
        SyncedDataPoint(double velocity, double position, double acceleration, double voltage, long timestamp) {
            this.velocity = velocity;
            this.position = position;
            this.acceleration = acceleration;
            this.rawAcceleration = acceleration;
            this.voltage = voltage;
            this.timestamp = timestamp;
        }
    }

    /**
     * Converts generalized data vectors to SyncedDataPoint models using SysID's structural standard index mapping:
     * Index 0 -> Position, Index 1 -> Velocity, Index 2 -> Acceleration, Index 3 -> Voltage.
     */
    private static List<SyncedDataPoint> synchronizeData(List<DataPoint> dataPoints) {
        List<SyncedDataPoint> result = new ArrayList<>();
        for (DataPoint dp : dataPoints) {
            if (dp.value.length >= 4) {
                System.out.println("pizza -" +Arrays.toString(dp.value));
                result.add(new SyncedDataPoint(dp.value[1], dp.value[0], dp.value[2], dp.value[3], dp.timestamp));
            }
        }
        return result;
    }

    /**
     * Computes the mathematical model through a robust two-pass OLS workflow.
     * Pass 1: Fits the initial model to estimate regression residuals.
     * Pass 2: Eliminates statistical outliers based on residuals for refined final coefficient values.
     */
    private static BucketResult calculateResult(List<SyncedDataPoint> rawData, String name) {
        // Apply moving average filter smoothing onto noisy raw acceleration logs
        List<SyncedDataPoint> cleanData = filterAndSmooth(rawData, VOLTAGE_THRESHOLD, SMOOTH_WINDOW);
        
        System.out.println(" calculate for " + name + " size = " + cleanData.size() + " base = " + rawData.size());
        
        if (cleanData.size() < 10) return null; // Abort if insufficient dataset size exists

        // Run regression pass 1
        BucketResult initialResult = solveOLS(cleanData);
        if (initialResult == null) return null;

        // Prune anomalies displaying the worst residual fitting variances
        List<SyncedDataPoint> refinedData = removeOutliers(cleanData, initialResult, OUTLIER_PERCENTAGE);
        if (refinedData.size() < 10) return null;

        // Run regression pass 2 over sanitized subset data
        BucketResult finalModel = solveOLS(refinedData);
        
        // Calculate fitting metrics (Mean Error & Max Deviation) of the final model against the raw scope
        if (finalModel != null) {
            double sumErr = 0;
            double maxErr = 0;
            boolean[] flags = SysidApp.kFlags; // Toggles determining active system parameters

            for(SyncedDataPoint p : rawData) {
                double pred = 0;
                if(flags[0]) pred += finalModel.ks * Math.signum(p.velocity);
                if(flags[1]) pred += finalModel.kv * p.velocity;
                if(flags[2]) pred += finalModel.ka * p.acceleration;
                if(flags[3]) pred += finalModel.kg * 1.0;
                if(flags[4]) pred += finalModel.ksin * Math.cos(p.position);
                if(flags[5]) pred += finalModel.kv2 * p.velocity * Math.abs(p.velocity);
                
                double error = Math.abs(p.voltage - pred);
                sumErr += error;
                if(error > maxErr) maxErr = error;
            }

            finalModel.avgError = sumErr / rawData.size();
            finalModel.maxError = maxErr;
            finalModel.rawPoints = rawData.size();
        }

        return finalModel;
    }

    /**
     * Eliminates dead-band voltages and smooths out numerical derivatives (accelerations)
     * using a centralized sliding-window moving average.
     */
    private static List<SyncedDataPoint> filterAndSmooth(List<SyncedDataPoint> rawData, double voltageThresh, int windowSize) {
        List<SyncedDataPoint> filtered = new ArrayList<>();
        for (int i = 0; i < rawData.size(); i++) {
            SyncedDataPoint current = rawData.get(i);
            
            // Execute neighbor window summation averaging
            double sumAccel = 0;
            int count = 0;
            for (int j = Math.max(0, i - windowSize/2); j < Math.min(rawData.size(), i + windowSize/2 + 1); j++) {
                sumAccel += rawData.get(j).rawAcceleration;
                count++;
            }
            current.acceleration = sumAccel / count;

            // Retain records only if motor driving voltage exceeds the threshold limit
            if (Math.abs(current.voltage) > voltageThresh) {
                filtered.add(current);
            } else {
                if(current.velocity != 0) {
                    System.out.println(" filter voltage = " + current.voltage + " / " + voltageThresh + " vel = " + current.velocity);
                }
            }
        }
        return filtered;
    }

    /**
     * Measures prediction discrepancies from a temporary model and strips away top percentage outliers.
     */
    private static List<SyncedDataPoint> removeOutliers(List<SyncedDataPoint> data, BucketResult model, double percentage) {
        if (percentage <= 0.001) return data;

        double kS = model.ks;
        double kV = model.kv;
        double kA = model.ka;
        double kG = model.kg;
        double kCos = model.ksin;
        double kV2 = model.kv2;
        boolean[] flags = SysidApp.kFlags;

        // Assign residual tracking values across all data rows
        for (SyncedDataPoint p : data) {
            double pred = 0;
            if(flags[0]) pred += kS * Math.signum(p.velocity);
            if(flags[1]) pred += kV * p.velocity;
            if(flags[2]) pred += kA * p.acceleration;
            if(flags[3]) pred += kG * 1.0;
            if(flags[4]) pred += kCos * Math.cos(p.position);
            if(flags[5]) pred += kV2 * p.velocity * Math.abs(p.velocity);
            
            p.error = Math.abs(p.voltage - pred);
        }

        // Sort data points ascendingly based on error magnitude
        Collections.sort(data, (p1, p2) -> Double.compare(p1.error, p2.error));

        // Trim the tail end of the list containing the largest deviation elements
        int removeCount = (int)(data.size() * percentage);
        int keepCount = data.size() - removeCount;
        
        if (keepCount < 1) return new ArrayList<>();
        return new ArrayList<>(data.subList(0, keepCount));
    }

    /**
     * Solves the linear regression matrix form $A \cdot x = b$ using the EJML matrix package,
     * where $b$ acts as target voltage vectors, and $A$ aggregates predictors.
     * Also computes the $R^2$ coefficient of determination score.
     */
    private static BucketResult solveOLS(List<SyncedDataPoint> data) {
        int n = data.size();
        boolean[] flags = SysidApp.kFlags;
        int numParams = 0;
        for(boolean f : flags) if(f) numParams++;

        if(numParams == 0) return null;

        // Initialize EJML Linear Algebra matrix dimensions
        SimpleMatrix A = new SimpleMatrix(n, numParams);
        SimpleMatrix b = new SimpleMatrix(n, 1);

        // Populate system tracking matrices
        for (int i = 0; i < n; i++) {
            SyncedDataPoint p = data.get(i);
            b.set(i, 0, p.voltage);

            int col = 0;
            if(flags[0]) A.set(i, col++, Math.signum(p.velocity));               // Friction (Ks)
            if(flags[1]) A.set(i, col++, p.velocity);                           // Velocity (Kv)
            if(flags[2]) A.set(i, col++, p.acceleration);                       // Acceleration (Ka)
            if(flags[3]) A.set(i, col++, 1.0);                                  // Gravity Offset (Kg)
            if(flags[4]) A.set(i, col++, Math.cos(p.position));                 // Angular Gravity (Kcos)
            if(flags[5]) A.set(i, col++, p.velocity * Math.abs(p.velocity));    // Non-linear Drag (Kv2)
        }

        SimpleMatrix x;
        try {
            x = A.solve(b); // Compute matrix solution vector via least-squares solver
        } catch(Exception e) {
            return null; // Handle singular matrix or math exceptions
        }

        // Extract solved beta weights into standardized fixed configurations array
        double[] k = new double[6];
        int col = 0;
        for(int i=0; i<6; i++) {
            if(flags[i]) k[i] = x.get(col++);
        }

        // Evaluate model fitting efficiency metrics ($R^2$)
        double ssTot = 0, ssRes = 0, meanV = 0;
        for(SyncedDataPoint p : data) meanV += p.voltage;
        meanV /= n;

        for (SyncedDataPoint p : data) {
            double pred = 0;
            if(flags[0]) pred += k[0] * Math.signum(p.velocity);
            if(flags[1]) pred += k[1] * p.velocity;
            if(flags[2]) pred += k[2] * p.acceleration;
            if(flags[3]) pred += k[3] * 1.0;
            if(flags[4]) pred += k[4] * Math.cos(p.position);
            if(flags[5]) pred += k[5] * p.velocity * Math.abs(p.velocity);

            ssTot += Math.pow(p.voltage - meanV, 2); // Total sum of squares
            ssRes += Math.pow(p.voltage - pred, 2);  // Residual sum of squares
        }

        double r2 = 1 - (ssRes / ssTot);

        // Map variables back into the resulting model class structure layout
        return new BucketResult(k[0], k[1], k[5], k[2], k[3], k[4], 0, 0, n, r2);
    }

    /**
     * Immutable storage container representing identification output calculations.
     */
    public static class BucketResult {
        double ks, kv, kv2, ka, kg, ksin, avgError, maxError, rSquared;
        int points, rawPoints;

        BucketResult(double ks, double kv, double kv2, double ka, double kg, double ksin, double avgError, double maxError, int points, double rSquared) {
            this.ks = ks;
            this.kv = kv;
            this.kv2 = kv2;
            this.ka = ka;
            this.kg = kg;
            this.ksin = ksin;
            this.avgError = avgError;
            this.maxError = maxError;
            this.points = points;
            this.rSquared = rSquared;
        }
    }
}