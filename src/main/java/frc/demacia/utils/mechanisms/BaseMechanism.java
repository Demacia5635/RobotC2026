package frc.demacia.utils.mechanisms;

import java.util.HashMap;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.log.LogEntryBuilder.LogLevel;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.sensors.SensorInterface;

/**
 * A base class for robot mechanisms (subsystems) that manage a collection of motors and sensors.
 * <p>
 * This class provides common functionality for:
 * <ul>
 * <li>Storing motors and sensors by name for easy retrieval.</li>
 * <li>Controlling all motors at once (stop, set power, set neutral mode).</li>
 * <li>Automatically creating SmartDashboard buttons for switching Neutral Modes (Brake/Coast).</li>
 * <li>Performing electronics checks on hardware.</li>
 * </ul>
 * </p>
 */
public class BaseMechanism extends SubsystemBase{
    /** The name of the mechanism (used for logging and dashboard) */
    protected String name;
    /** Map of motors belonging to this mechanism, keyed by their name */
    protected HashMap<String, MotorInterface> motors;
    /** Map of sensors belonging to this mechanism, keyed by their name */
    protected HashMap<String, SensorInterface> sensors;
    /** Map of motors limits belonging to this mechanism, keyed by their name */
    protected HashMap<String, Pair<Double, Double>> motorLimits;
    protected HashMap<String, Double> wantedValues;
    protected Runnable outoCalibration;

    protected String[] motorNames;
    protected String[] sensorNames;

    protected boolean hasCalibrated;
    protected int motorsAmounts;
    protected int sensorsAmounts;

    /**
     * Constructs a new BaseMechanism.
     * Initializes the motor and sensor maps and creates debug buttons on the Dashboard.
     * @param name The name of the subsystem
     * @param motors Array of motors to register
     * @param sensors Array of sensors to register
     */
    @SuppressWarnings("unchecked")
    public BaseMechanism(String name, MotorInterface[] motors, SensorInterface[] sensors) {
        this.name = name;
        setName(name);
        motorsAmounts =  motors == null ? 0 : motors.length;
        sensorsAmounts = sensors == null ? 0 : sensors.length;
        
        // Initialize motors
        motorNames = new String[motorsAmounts];
        this.motors = new HashMap<>();
        motorLimits = new HashMap<>();
        wantedValues = new HashMap<>();
        for (int i = 0; i < motorsAmounts; i++){
            motorNames[i] = motors[i].getName();
            this.motors.put(motors[i].getName(), motors[i]);
            motorLimits.put(motors[i].getName(), new Pair<Double,Double>(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
            wantedValues.put(motors[i].getName(), 0.0);
        }

        // Initialize sensors map
        sensorNames = new String[sensorsAmounts];
        this.sensors = new HashMap<>();
        for (int i = 0; i < sensorsAmounts; i++){
            sensorNames[i] = sensors[i].getName();
            this.sensors.put(sensors[i].getName(), sensors[i]);
        }

        // Create individual Brake/Coast buttons for each motor
        for (String motorName : motorNames) {
            SmartDashboard.putData(getName() + "/" + motorName + "/set brake " + motorName, 
                new InstantCommand(() -> setNeutralMode(motorName, true)).ignoringDisable(true));
            SmartDashboard.putData(getName() + "/" + motorName + "/set coast " + motorName, 
                new InstantCommand(() -> setNeutralMode(motorName, false)).ignoringDisable(true));
        }

        // Create global Brake/Coast buttons for the whole mechanism
        SmartDashboard.putData(getName() + "/set coast " + getName(), 
                new InstantCommand(() -> setNeutralMode(false)).ignoringDisable(true));
        SmartDashboard.putData(getName() + "/set brake " + getName(), 
                new InstantCommand(() -> setNeutralMode(true)).ignoringDisable(true));
        
        SmartDashboard.putData(name, this);

        for (int i = 0; i < motorsAmounts; i++){
            final int index = i;
            LogManager.addEntry(getName() + "/" + motorNames[i] + "/" + motorNames[i] + " wanted value: ", () -> wantedValues.get(motorNames[index]))
            .withIsSeparated(true).withLogLevel(LogLevel.LOG_AND_NT).build();
        }

        hasCalibrated = true;
        outoCalibration = () -> {};
    }

    public void withPowerCommand(DoubleSupplier powerSupplier) {
        for (int i = 0; i < motorsAmounts; i++){
            SmartDashboard.putData(getName() + "/" + motorNames[i] + "/set power command " + motorNames[i], 
                new PowerCommand(this, motorNames[i], powerSupplier));
        }
    }

    public void withPowerCommand(String motorName, DoubleSupplier powerSupplier) {
        SmartDashboard.putData(getName() + "/" + motorName + "/set power command " + motorName, 
            new PowerCommand(this, motorName, powerSupplier));
    }

    /**
     * @return The name of the mechanism
     */
    public String getName(){
        return name;
    }
    
    /**
     * Marks that this mechanism requires calibration.
     * Sets the calibration status to false.
     */
    public void withCalibration(){
        hasCalibrated = false;
    }

    /**
     * @return true if the mechanism is calibrated and ready for control, false otherwise.
     */
    public boolean getIsCalibration(){
        return hasCalibrated;
    }

    /**
     * Sets the calibration status of the mechanism.
     * @param hasCalibrated true if calibrated, false otherwise.
     */
    public void setCalibration(boolean hasCalibrated){
        this.hasCalibrated = hasCalibrated;
    }

    public void addLimit(String motorName, double min,  double max) {
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return;
        }
        motorLimits.replace(motorName, new Pair<>(min, max));
    }

    public void addLimit(int motorIndex, double min,  double max) {
        addLimit(motorNames[motorIndex], min,  max);
    }

    public void addLimitMax(String motorName, double max) {
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return;
        }
        motorLimits.replace(motorName, new Pair<Double, Double>(
            motorLimits.get(motorName).getFirst(), 
            max));
    }

    public void addLimitMax(int motorIndex, double max) {
        addLimitMax(motorNames[motorIndex], max);
    }

    public void addLimitMin(String motorName, double min) {
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return;
        }
        motorLimits.replace(motorName, new Pair<Double, Double>(
            min,
            motorLimits.get(motorName).getSecond()));
    }

    public void addLimitMin(int motorIndex, double min) {
        addLimitMin(motorNames[motorIndex], min);
    }

    public void withOutoCalibration(String motorName, BooleanSupplier atLimit, double resetPos) {
        outoCalibration = () -> {
            if (!getIsCalibration() && atLimit.getAsBoolean()){
                getMotor(motorName).setEncoderPosition(resetPos);
                setCalibration(true);
            }
        };
        hasCalibrated = false;
        SmartDashboard.putData(getName() + "/" +getMotor(motorName).getName() + " menual reset", new InstantCommand(() -> {
            getMotor(motorName).setEncoderPosition(resetPos);
            setCalibration(true);}));
    }

    /**
     * Stops all motors in this mechanism.
     */
    public void stop(){
        if (motors == null) return;
        for (MotorInterface motor : motors.values()){
            motor.stop();
            wantedValues.replace(motor.getName(), 0.0);
        }
    }

    /**
     * Stops a specific motor by name.
     * @param motorName The name of the motor to stop
     */
    public void stop(String motorName){
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return;
        }
        motors.get(motorName).stop();
        wantedValues.replace(motorName, 0.0);
    }

    /**
     * Stops a specific motor by index.
     * @param motorIndex The index of the motor to stop
     */
    public void stop(int motorIndex){
        stop(motorNames[motorIndex]);
    }

    /**
     * Sets the duty cycle (power) for all motors.
     * @param power The power to set [-1.0, 1.0]
     */
    public void setPowerAll(double power) {
        if (motors == null || !hasCalibrated) return;
        for (MotorInterface motor : motors.values()){
            motor.setDuty(power);
            wantedValues.replace(motor.getName(), power);
        }
    }

    /**
     * Sets the duty cycle (power) for a specific motor.
     * @param motorName The name of the motor
     * @param power The power to set [-1.0, 1.0]
     */
    public void setPower(String motorName, double power){
        if (canMove(motorName)){
            motors.get(motorName).setDuty(power);
            wantedValues.replace(motorName, power);
        }
    }

    /**
     * Sets the duty cycle (power) for a specific motor.
     * @param motorIndex The index of the motor
     * @param power The power to set [-1.0, 1.0]
     */
    public void setPower(int motorIndex, double power){
        setPower(motorNames[motorIndex], power);
    }

    /**
     * Sets the Voltage for a specific motor.
     * @param motorName The name of the motor
     * @param voltage The Voltage to set
     */
    public void setVoltage(String motorName, double voltage){
        if (canMove(motorName)){
            motors.get(motorName).setVoltage(voltage);
            wantedValues.replace(motorName, voltage);
        }
    }

    /**
     * Sets the Voltage for a specific motor.
     * @param motorIndex The index of the motor
     * @param voltage The Voltage to set
     */
    public void setVoltage(int motorIndex, double voltage){
        setVoltage(motorNames[motorIndex], voltage);
    }

    /**
     * Sets the Velocity for a specific motor.
     * @param motorName The name of the motor
     * @param velocity The Velocity to set
     */
    public void setVelocity(String motorName, double velocity){
        if (canMove(motorName)){
            motors.get(motorName).setVelocity(velocity);
            wantedValues.replace(motorName, velocity);
        }
    }

    /**
     * Sets the Velocity for a specific motor.
     * @param motorIndex The index of the motor
     * @param velocity The Velocity to set
     */
    public void setVelocity(int motorIndex, double velocity){
        setVelocity(motorNames[motorIndex], velocity);
    }

    /**
     * Sets the position using PositionVoltage for a specific motor.
     * @param motorName The name of the motor
     * @param position The position to set
     */
    public void setPositionVoltage(String motorName, double position){
        if (canMove(motorName)){
            motors.get(motorName).setPositionVoltage(
                clampInLimits(motorName, position));
            wantedValues.replace(motorName, position);
        }
    }

    /**
     * Sets the position using PositionVoltage for a specific motor.
     * @param motorIndex The index of the motor
     * @param position The position to set
     */
    public void setPositionVoltage(int motorIndex, double position){
        setPositionVoltage(motorNames[motorIndex], position);
    }

    /**
     * Sets the position for a specific motor.
     * @param motorName The name of the motor
     * @param position The position to set
     */
    public void setMotion(String motorName, double position){
        if (canMove(motorName)){
            motors.get(motorName).setMotion(clampInLimits(motorName, position));
            wantedValues.replace(motorName, position);
        }
    }

    /**
     * Sets the position for a specific motor.
     * @param motorIndex The index of the motor
     * @param position The position to set
     */
    public void setMotion(int motorIndex, double position){
        setMotion(motorNames[motorIndex], position);
    }

    /**
     * Sets the Angle for a specific motor.
     * @param motorName The name of the motor
     * @param angle The Angle to set
     */
    public void setAngle(String motorName, double angle){
        if (canMove(motorName)){
            double targetAngle = clampAngleInLimits(motorName, angle);
            motors.get(motorName).setMotion(targetAngle);
            wantedValues.replace(motorName, targetAngle);
        }
    }

    /**
     * Sets the Angle for a specific motor.
     * @param motorIndex The index of the motor
     * @param angle The Angle to set
     */
    public void setAngle(int motorIndex, double angle){
        setAngle(motorNames[motorIndex], angle);
    }

    private boolean canMove(String motorName) {
        return isValidMotor(motorName) && hasCalibrated;
    }

    private double clampInLimits(String motorName,double position) {
        return MathUtil.clamp(position, motorLimits.get(motorName).getFirst(), motorLimits.get(motorName).getSecond());
    }

    private double clampAngleInLimits(String motorName, double angle) {
        Pair<Double, Double> limits = motorLimits.get(motorName);
    
        double min = limits.getFirst();
        double max = limits.getSecond();
        double range = max - min;

        if (Double.isInfinite(min) || Double.isInfinite(max)) {
            return angle;
        }
    
        return MathUtil.clamp(
            MathUtil.inputModulus(
                angle,
                min - (2*Math.PI - range) / 2.0,
                max + (2*Math.PI - range) / 2.0),
            min,
            max);
    }

    public boolean isReady(double[] allowedErrors){
        if (allowedErrors.length != motorsAmounts){
            LogManager.log("errors amount is not the motors amounts");
            return true;
        }
        for (int i = 0; i < motorsAmounts; i++){
            MotorInterface motor = getMotor(i);
            switch (motor.getCurrentControlMode()) {
                case DISABLE:
                    break;
                case DUTYCYCLE:
                    break;
                case VOLTAGE:
                    if (Math.abs(wantedValues.get(motor.getName()) - motor.getCurrentVoltage()) > allowedErrors[i]){
                        return false;
                    }
                    break;
                case VELOCITY:
                    if (Math.abs(wantedValues.get(motor.getName()) - motor.getCurrentVelocity()) > allowedErrors[i]){
                        return false;
                    }
                    break;
                case POSITION_VOLTAGE, MAGIC_MOTION:
                        if (Math.abs(wantedValues.get(motor.getName()) - motor.getCurrentPosition()) > allowedErrors[i]){
                            return false;
                        }
                    break;
                case ANGLE:
                    if (Math.abs(wantedValues.get(motor.getName()) - motor.getCurrentAngle()) > allowedErrors[i]){
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    
    public boolean isReady(String motorName ,double allowedError){
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return false;
        }
        switch (motors.get(motorName).getCurrentControlMode()) {
            case DISABLE:
                break;
            case DUTYCYCLE:
                break;
            case VOLTAGE:
                if (Math.abs(wantedValues.get(motorName) - motors.get(motorName).getCurrentVoltage()) > allowedError){
                    return false;
                }
                break;
            case VELOCITY:
                if (Math.abs(wantedValues.get(motorName) - motors.get(motorName).getCurrentVelocity()) > allowedError){
                    return false;
                }
                break;
            case POSITION_VOLTAGE, MAGIC_MOTION, ANGLE:
                    if (Math.abs(wantedValues.get(motorName) - motors.get(motorName).getCurrentPosition()) > allowedError){
                        return false;
                    }
                break;
            default:
                break;
        }
        return true;
    }

    public boolean isReady(int motorIndex ,double allowedArror){
        return isReady(motorNames[motorIndex], allowedArror);
    }

    /**
     * Sets the neutral mode (Brake or Coast) for all motors.
     * @param isBrake true for Brake mode, false for Coast mode
     */
    public void setNeutralMode(boolean isBrake) {
        if (motors == null) return;
        for (MotorInterface motor : motors.values()) {
            if (motor != null) motor.setNeutralMode(isBrake);
        }
    }

    /**
     * Sets the neutral mode (Brake or Coast) for a specific motor.
     * @param motorName The name of the motor
     * @param isBrake true for Brake mode, false for Coast mode
     */
    public void setNeutralMode(String motorName, boolean isBrake){
        motors.get(motorName).setNeutralMode(isBrake);
    }

    /**
     * Sets the neutral mode (Brake or Coast) for a specific motor.
     * @param motorIndex The index of the motor
     * @param isBrake true for Brake mode, false for Coast mode
     */
    public void setNeutralMode(int motorIndex, boolean isBrake){
        setNeutralMode(motorNames[motorIndex], isBrake);
    }

    /**
     * Triggers the electronics check for all motors and sensors.
     */
    public void checkElectronics() {
        if (motors == null) return;
        for (MotorInterface motor : motors.values()) {
            if (motor != null) motor.checkElectronics();
        }
        if (sensors == null) return;
        for (SensorInterface sensor : sensors.values()) {
            if (sensor != null) sensor.checkElectronics();
        }
    }

    /**
     * Checks electronics for a specific motor.
     * @param motorName The name of the motor
     */
    public void checkElectronicsMotor(String motorName){
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return;
        }
        motors.get(motorName).checkElectronics();
    }

    /**
     * Checks electronics for a specific motor.
     * @param motorIndex The index of the motor
     */
    public void checkElectronicsMotor(int motorIndex){
        checkElectronicsMotor(motorNames[motorIndex]);
    }

    /**
     * Checks electronics for a specific sensor.
     * @param sensorName The name of the sensor
     */
    public void checkElectronicsSensor(String sensorName){
        if (!isValidSensor(sensorName)){
            LogManager.log("Invalid motor: " + sensorName);
            return;
        }
        sensors.get(sensorName).checkElectronics();
    }

    /**
     * Checks electronics for a specific sensor.
     * @param sensorName The index of the sensor
     */
    public void checkElectronicsSensor(int sensorIndex){
        checkElectronicsSensor(sensorNames[sensorIndex]);
    }

    /**
     * Retrieves a motor object by its name.
     * Logs an error if the motor name is invalid.
     * @param motorName The name of the motor
     * @return The MotorInterface object, or null if not found
     */
    public MotorInterface getMotor(String motorName) {
        if (!isValidMotor(motorName)){
            LogManager.log("Invalid motor: " + motorName);
            return null;
        }
        return motors.get(motorName);
    }

    /**
     * Retrieves a motor object by its index.
     * Logs an error if the motor index is invalid.
     * @param motorIndex The index of the motor
     * @return The MotorInterface object, or null if not found
     */
    public MotorInterface getMotor(int motorIndex) {
        return getMotor(motorNames[motorIndex]);
    }

    public MotorInterface[] getMotors() {
        MotorInterface[] motorArray = new MotorInterface[motorsAmounts];
        for (int i = 0; i < motorsAmounts; i++){
            motorArray[i] = motors.get(motorNames[i]);
        }
        return motorArray;
    }

    /**
     * Retrieves a sensor object by its name.
     * Logs an error if the sensor name is invalid.
     * @param sensorName The name of the sensor
     * @return The SensorInterface object, or null if not found
     */
    public SensorInterface getSensor(String sensorName) {
        if (!isValidSensor(sensorName)){
            LogManager.log("Invalid sensor: " + sensorName);
            return null;
        }
        return sensors.get(sensorName);
    }

    /**
     * Retrieves a sensor object by its index.
     * Logs an error if the sensor index is invalid.
     * @param sensorIndex The index of the sensor
     * @return The SensorInterface object, or null if not found
     */
    public SensorInterface getSensor(int sensorIndex) {
        return getSensor(sensorNames[sensorIndex]);
    }
    
    public SensorInterface[] getSensors() {
        SensorInterface[] sensorArray = new SensorInterface[motorsAmounts];
        for (int i = 0; i < motorsAmounts; i++){
            sensorArray[i] = sensors.get(sensorNames[i]);
        }
        return sensorArray;
    }

    /**
     * Checks if a motor name exists in the map.
     * @param motorName The name to check
     * @return true if valid, false otherwise
     */
    protected boolean isValidMotor(String motorName) {
        return motors.containsKey(motorName);
    }

    /**
     * Checks if a motor index exists.
     * @param motorIndex The index to check
     * @return true if valid, false otherwise
     */
    protected boolean isValidMotor(int motorIndex) {
        return isValidMotor(motorNames[motorIndex]);
    }

    /**
     * Checks if a sensor name exists in the map.
     * @param sensorName The name to check
     * @return true if valid, false otherwise
     */
    protected boolean isValidSensor(String sensorName) {
        return sensors.containsKey(sensorName);
    }

    /**
     * Checks if a sensor index exists in the map.
     * @param sensorIndex The index to check
     * @return true if valid, false otherwise
     */
    protected boolean isValidSensor(int sensorIndex) {
        return isValidSensor(sensorNames[sensorIndex]);
    }

    public void periodic() {
        outoCalibration.run();
    }
}