// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.demacia.odometry;

import java.util.NavigableMap;
<<<<<<< HEAD
import java.util.Optional;
import java.util.TreeMap;

import edu.wpi.first.math.MathUtil;
=======
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

/** Add your docs here. */
public class DemaciaPoseEstimator {

<<<<<<< HEAD
    private DemaciaOdometry odometry;
    private Matrix<N3, N1> m_q = new Matrix<>(Nat.N3(), Nat.N1());
    private Matrix<N3, N3> m_visionK = new Matrix<>(Nat.N3(), Nat.N3());
    private final double kBufferDuration = 1.5;
    private NavigableMap<Double, VisionUpdate> visionUpdates = new TreeMap<>();
=======
    private final double MAX_HISTORY_SECONDS = 1.5;

    private Matrix<N3, N1> stateSTD;
    private Matrix<N3, N1> visionSTD;

    private DemaciaOdometry odometry;
    private Pose2d odometryPose;
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
    private Pose2d estimatedPose;

    private final TimeInterpolatableBuffer<Pose2d> odometryBuffer;

<<<<<<< HEAD
    public DemaciaPoseEstimator(Translation2d[] modulePositions, Matrix<N3, N1> stateSTD, Matrix<N3, N1> visionSTD) {

        this.odometry = new DemaciaOdometry(modulePositions);
        estimatedPose = odometry.getPose2d();
        for (int i = 0; i < 3; ++i) {
            m_q.set(i, 0, stateSTD.get(i, 0) * stateSTD.get(i, 0));
        }
        setVisionMeasurementStdDevs(visionSTD);
        this.odometryBuffer = TimeInterpolatableBuffer.createBuffer(kBufferDuration);

    }

    public final void setVisionMeasurementStdDevs(Matrix<N3, N1> visionMeasurementStdDevs) {
        var r = new double[3];
        for (int i = 0; i < 3; ++i) {
            r[i] = visionMeasurementStdDevs.get(i, 0) * visionMeasurementStdDevs.get(i, 0);
        }

        // Solve for closed form Kalman gain for continuous Kalman filter with A = 0
        // and C = I. See wpimath/algorithms.md.
        for (int row = 0; row < 3; ++row) {
            if (m_q.get(row, 0) == 0.0) {
                m_visionK.set(row, row, 0.0);
            } else {
                m_visionK.set(
                        row, row, m_q.get(row, 0) / (m_q.get(row, 0) + Math.sqrt(m_q.get(row, 0) * r[row])));
            }
        }
    }

    /**
     * Return the pose at a given timestamp, if the buffer is not empty.
     *
     * @param timestampSeconds The pose's timestamp in seconds.
     * @return The pose at the given timestamp (or Optional.empty() if the buffer is
     *         empty).
     */
    public Optional<Pose2d> sampleAt(double timestampSeconds) {
        // Step 0: If there are no odometry updates to sample, skip.
        if (odometryBuffer.getInternalBuffer().isEmpty()) {
            return Optional.empty();
        }

        // Step 1: Make sure timestamp matches the sample from the odometry pose buffer.
        // (When sampling,
        // the buffer will always use a timestamp between the first and last timestamps)
        double oldestOdometryTimestamp = odometryBuffer.getInternalBuffer().firstKey();
        double newestOdometryTimestamp = odometryBuffer.getInternalBuffer().lastKey();
        timestampSeconds = MathUtil.clamp(timestampSeconds, oldestOdometryTimestamp, newestOdometryTimestamp);

        // Step 2: If there are no applicable vision updates, use the odometry-only
        // information.
        if (visionUpdates.isEmpty() || timestampSeconds < visionUpdates.firstKey()) {
            return odometryBuffer.getSample(timestampSeconds);
        }

        // Step 3: Get the latest vision update from before or at the timestamp to
        // sample at.
        double floorTimestamp = visionUpdates.floorKey(timestampSeconds);
        var visionUpdate = visionUpdates.get(floorTimestamp);

        // Step 4: Get the pose measured by odometry at the time of the sample.
        var odometryEstimate = odometryBuffer.getSample(timestampSeconds);

        // Step 5: Apply the vision compensation to the odometry pose.
        return odometryEstimate.map(odometryPose -> visionUpdate.compensate(odometryPose));
    }

    /** Removes stale vision updates that won't affect sampling. */
    private void cleanUpVisionUpdates() {
        // Step 0: If there are no odometry samples, skip.
        if (odometryBuffer.getInternalBuffer().isEmpty()) {
            return;
        }

        // Step 1: Find the oldest timestamp that needs a vision update.
        double oldestOdometryTimestamp = odometryBuffer.getInternalBuffer().firstKey();

        // Step 2: If there are no vision updates before that timestamp, skip.
        if (visionUpdates.isEmpty() || oldestOdometryTimestamp < visionUpdates.firstKey()) {
            return;
        }

        // Step 3: Find the newest vision update timestamp before or at the oldest
        // timestamp.
        double newestNeededVisionUpdateTimestamp = visionUpdates.floorKey(oldestOdometryTimestamp);

        // Step 4: Remove all entries strictly before the newest timestamp we need.
        visionUpdates.headMap(newestNeededVisionUpdateTimestamp, false).clear();
    }

    public void addVisionMeasurement(Pose2d visionRobotPoseMeters, double timestampSeconds) {
        // Step 0: If this measurement is old enough to be outside the pose buffer's
        // timespan, skip.
        if (odometryBuffer.getInternalBuffer().isEmpty()
                || odometryBuffer.getInternalBuffer().lastKey() - kBufferDuration > timestampSeconds) {
            return;
        }

        // Step 1: Clean up any old entries
        cleanUpVisionUpdates();

        // Step 2: Get the pose measured by odometry at the moment the vision
        // measurement was made.
        var odometrySample = odometryBuffer.getSample(timestampSeconds);

        if (odometrySample.isEmpty()) {
            return;
        }

        // Step 3: Get the vision-compensated pose estimate at the moment the vision
        // measurement was
        // made.
        var visionSample = sampleAt(timestampSeconds);

        if (visionSample.isEmpty()) {
            return;
        }

        // Step 4: Measure the transform between the old pose estimate and the vision
        // pose.
        var transform = visionRobotPoseMeters.minus(visionSample.get());

        // Step 5: We should not trust the transform entirely, so instead we scale this
        // transform by a
        // Kalman
        // gain matrix representing how much we trust vision measurements compared to
        // our current pose.
        var k_times_transform =
        m_visionK.times(
            VecBuilder.fill(
                transform.getX(), transform.getY(), transform.getRotation().getRadians()));

        // Step 6: Convert back to Transform2d.
        var scaledTransform = new Transform2d(
                k_times_transform.get(0, 0),
                k_times_transform.get(1, 0),
                Rotation2d.fromRadians(k_times_transform.get(2, 0)));

        // Step 7: Calculate and record the vision update.
        var visionUpdate = new VisionUpdate(visionSample.get().plus(scaledTransform), odometrySample.get());
        visionUpdates.put(timestampSeconds, visionUpdate);

        // Step 8: Remove later vision measurements. (Matches previous behavior)
        visionUpdates.tailMap(timestampSeconds, false).entrySet().clear();

        // Step 9: Update latest pose estimate. Since we cleared all updates after this
        // vision update,
        // it's guaranteed to be the latest vision update.
        estimatedPose = visionUpdate.compensate(odometry.getPose2d());
    }

    public void addOdometryCalculation(OdometryObservation odometryCalculation, Translation2d currentVelocity) {
        var odometryEstimation = odometry.update(odometryCalculation.gyroAngle(), odometryCalculation.swerveModules());
        odometryBuffer.addSample(odometryCalculation.timeStamp(), odometryEstimation);
=======
    private NavigableMap<Double, VisionUpdate> visionUpdates = new TreeMap<>();

    private final Matrix<N3, N1> stateStdSquared = new Matrix<>(Nat.N3(), Nat.N1());
    private final Matrix<N3, N3> kalmanMatrix = new Matrix<>(Nat.N3(), Nat.N3());

    public DemaciaPoseEstimator(Translation2d[] modulePositions, Matrix<N3, N1> stateSTD, Matrix<N3, N1> visionSTD) {

        this.odometry = new DemaciaOdometry(modulePositions);
        this.stateSTD = stateSTD;
        this.visionSTD = visionSTD;
        this.odometryBuffer = TimeInterpolatableBuffer.createBuffer(MAX_HISTORY_SECONDS);

        for (int i = 0; i < 3; ++i) {
            stateStdSquared.set(i, 0, stateSTD.get(i, 0) * stateSTD.get(i, 0));
        }

        this.estimatedPose = Pose2d.kZero;
        this.odometryPose = Pose2d.kZero;
    }

    public void updateStateSTD(Matrix<N3, N1> stateSTD) {
        this.stateSTD = stateSTD;
    }

    public void updateVisionSTD(Matrix<N3, N1> visionSTD) {
        this.visionSTD = visionSTD;

    }

    public void updateVisionAndStateSTD(Matrix<N3, N1> stateSTD, Matrix<N3, N1> visionSTD) {
        updateStateSTD(stateSTD);
        updateVisionSTD(visionSTD);
    }

    public void addOdomteryCalculation(OdometryObservation odometryCalculation, Translation2d currentVelocity) {
        var odometryEstimation = odometry.update(odometryCalculation.gyroAngle(), odometryCalculation.swerveModules());
        odometryBuffer.addSample(odometryCalculation.timeStamp(), odometryPose);
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025

        if (visionUpdates.isEmpty()) {
            estimatedPose = odometryEstimation;
        } else {
            var visionUpdate = visionUpdates.get(visionUpdates.lastKey());
            estimatedPose = visionUpdate.compensate(odometryEstimation);
        }

    }

<<<<<<< HEAD
=======
    public void addVisionMeasurement(VisionMeasurment visionMeasurment) {

        try {
            if (odometryBuffer.getInternalBuffer().lastKey() - visionMeasurment.timeStamp > MAX_HISTORY_SECONDS
                    || odometryBuffer.getInternalBuffer().isEmpty()) { // case for measurment too old
                return;
            }
        } catch (NoSuchElementException e) {
            return;
        }

        var sample = odometryBuffer.getSample(visionMeasurment.timeStamp());
        if (sample.isEmpty())
            return; // case for no sample

        updateKalmanFilter();

        boolean is2DPose = visionMeasurment.angle.isPresent();

        Transform2d currentPoseToSample = estimatedPose.minus(sample.get());
        Pose2d estimateAtTime = sample.get().plus(currentPoseToSample);
        Transform2d estimateAndVisionAtTimeDiff = new Transform2d(estimateAtTime,
                new Pose2d(visionMeasurment.pos,
                        is2DPose ? estimateAtTime.getRotation() : visionMeasurment.angle.get()));
        var kalmanTimesDiff = kalmanMatrix.times(
                VecBuilder.fill(estimateAndVisionAtTimeDiff.getX(), estimateAndVisionAtTimeDiff.getY(),
                        visionMeasurment.angle.get().getRadians()));

        Transform2d scaledTransform = new Transform2d(kalmanTimesDiff.get(0, 0),
                kalmanTimesDiff.get(1, 0),
                is2DPose ? Rotation2d.kZero : Rotation2d.fromRadians(kalmanTimesDiff.get(2, 0)));

        visionUpdates.put(visionMeasurment.timeStamp(), new VisionUpdate(
                new Pose2d(visionMeasurment.pos(), estimateAtTime.getRotation()), estimateAtTime));

        visionUpdates.tailMap(visionMeasurment.timeStamp(), false).entrySet().clear();

        estimatedPose = estimateAtTime.plus(scaledTransform);

    }

    private void updateKalmanFilter() {
        for (int i = 0; i < 3; ++i) {
            stateStdSquared.set(i, 0, stateSTD.get(i, 0) * stateSTD.get(i, 0));
        }

        double[] visionStdSquared = new double[3];
        for (int i = 0; i < 3; ++i) {
            visionStdSquared[i] = visionSTD.get(i, 0) * visionSTD.get(i, 0);
        }

        // Solve for closed form Kalman gain for continuous Kalman filter with A = 0
        // and C = I. See wpimath/algorithms.md.
        for (int row = 0; row < 3; ++row) {
            if (stateStdSquared.get(row, 0) == 0.0) {
                kalmanMatrix.set(row, row, 0.0);
            } else {
                kalmanMatrix.set(
                        row, row, stateStdSquared.get(row, 0) / (stateStdSquared.get(row, 0)
                                + Math.sqrt(stateStdSquared.get(row, 0) * visionStdSquared[row])));
            }
        }
    }

>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
    public Pose2d getEstimatedPose() {
        return estimatedPose;
    }

    public void resetPose() {
        resetPose(new Pose2d());
    }

    public void resetPose(Pose2d pose) {
        odometry.resetPose(pose);
        odometryBuffer.clear();
        visionUpdates.clear();
        estimatedPose = odometry.getPose2d();
<<<<<<< HEAD
=======

>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
    }

    public record OdometryObservation(double timeStamp, Rotation2d gyroAngle, SwerveModulePosition[] swerveModules) {
    }

<<<<<<< HEAD
    
=======
    public record VisionMeasurment(double timeStamp, Translation2d pos, Optional<Rotation2d> angle) {
    }
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025

    private static final class VisionUpdate {
        // The vision-compensated pose estimate.
        private final Pose2d visionPose;

        // The pose estimated based solely on odometry.
        private final Pose2d odometryPose;

        /**
         * Constructs a vision update record with the specified parameters.
         *
         * @param visionPose   The vision-compensated pose estimate.
         * @param odometryPose The pose estimate based solely on odometry.
         */
        private VisionUpdate(Pose2d visionPose, Pose2d odometryPose) {
            this.visionPose = visionPose;
            this.odometryPose = odometryPose;
        }

        /**
         * Returns the vision-compensated version of the pose. Specifically, changes the
         * pose from being
         * relative to this record's odometry pose to being relative to this record's
         * vision pose.
         *
         * @param pose The pose to compensate.
         * @return The compensated pose.
         */
        public Pose2d compensate(Pose2d pose) {
            var delta = pose.minus(this.odometryPose);
            return this.visionPose.plus(delta);
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
