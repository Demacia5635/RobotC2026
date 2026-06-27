// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.demacia.odometry;

import org.ejml.simple.SimpleMatrix;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.demacia.odometry.DemaciaPoseEstimator.OdometryObservation;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.vision.utils.Vision;
import frc.demacia.vision.utils.VisionConstants;

import frc.robot.Field;

public class RobotPose {

    private static RobotPose instance;

    private Vision vision;
    public DemaciaPoseEstimator poseEstimator;

    private boolean hasUpdatedQuestIntialPose;
    private boolean hasQuestDisconnected;

    private Matrix<N3, N1> visionSTD;
    private Matrix<N3, N1> questSTDWhileShooting;
    
    private BuiltInAccelerometer accelerometer;
    private DemaciaOdometry odometry;

    private RobotPose(Translation2d[] modulePositions, Matrix<N3, N1> stateSTD, DemaciaOdometry odometry) {
        this.vision = new Vision((VisionConstants.Tags.TAGS_ARRAY));
        this.odometry = odometry;
        this.questSTDWhileShooting = new Matrix<N3, N1>(new SimpleMatrix(new double[] { 0.3, 0.3, 0 }));
        this.visionSTD = new Matrix<N3, N1>(new SimpleMatrix(new double[] { 0.3, 0.3, 0 }));
        this.hasUpdatedQuestIntialPose = false;
        this.hasQuestDisconnected = false;
        this.poseEstimator = new DemaciaPoseEstimator(modulePositions, stateSTD, visionSTD);
        this.accelerometer = new BuiltInAccelerometer();
        SmartDashboard.putData("Reset Pose Based Red Hub", new InstantCommand(() -> {
            Chassis.getInstance().setYaw(Rotation2d.kZero);
            resetPose(hubRedResetPose);
        }).ignoringDisable(true));

        odometry.resetPose(vision.getPoseEstimation());

    }

    // public void IsPassBamp(){
    //     poseEstimator.isAfterBamp();
    // }

    public void resetOdometryByCamra(){
        odometry.resetPose(vision.getPoseEstimation());
    }

    private final Pose2d hubRedResetPose = new Pose2d(Field.HubRed.X_BACK + 0.3, Field.HubRed.Y_CENTER,
            Rotation2d.kZero);


    public Pose2d getPose() {
        return poseEstimator.getEstimatedPose();
    }

    public static void initialize(Translation2d[] modulePositions, Matrix<N3, N1> stateSTD, DemaciaOdometry odometry) {

        if (instance == null)
            instance = new RobotPose(modulePositions, stateSTD, odometry);
    }

    public void resetPose() {
        resetPose(Pose2d.kZero);
    }

    public void resetPose(Pose2d pose) {
        System.out.println(pose);
        poseEstimator.resetPose(pose);
    }

    public static RobotPose getInstance() {
        return instance;
    }

    public void addOdometryCalculation(OdometryObservation odometryObservation) {
        poseEstimator.addOdometryCalculation(odometryObservation);
    }

    public void addOdometryCalculation(Rotation2d gyroAngle, SwerveModulePosition[] modulePositions) {
        addOdometryCalculation(new OdometryObservation(Timer.getFPGATimestamp(), gyroAngle, modulePositions));
    }

    public void addVisionMeasurement(Rotation2d gyroAngle) {
    poseEstimator.setVisionMeasurementStdDevs(visionSTD);
    
    Pose2d visionPose = new Pose2d(
        vision.getPoseEstimation().getX(), 
        vision.getPoseEstimation().getY(), 
        gyroAngle);
    
    poseEstimator.addVisionMeasurement(visionPose, Timer.getFPGATimestamp() - 0.05);
}

    public void update(Pose2d odometryPose, Rotation2d gyroAngle,
            SwerveModulePosition[] modulePositions, Translation2d currentVelocity) {
        update(new OdometryObservation(Timer.getFPGATimestamp(), gyroAngle, modulePositions));

    }

    private boolean shouldUpdateVision() {
        return vision.isSeeTag();

    }

    public void setAngle3DLimelight() {
        Rotation2d newAngle = vision.getRobotAngle();
        if (newAngle != null)
            Chassis.getInstance().setYaw(newAngle);

    }

    public void update(OdometryObservation odometryObservation) {

        vision.updateValues();
        
        
        // if (Math.abs(accelerometer.getX()) < 0.3 && Math.abs(accelerometer.getZ()) < 0.3)
        addOdometryCalculation(odometryObservation);
        if (shouldUpdateVision()) {

            addVisionMeasurement(odometryObservation.gyroAngle());
        }
    }
}