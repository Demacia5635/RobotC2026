package frc.demacia.swervePacgeWpilib.wpilib_odmetry;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public class wpilivOdmetry{
    private SwerveDriveOdometry odometry;

    public wpilivOdmetry (SwerveDriveKinematics kinematics, Rotation2d gyroAngle, SwerveModulePosition[] modulePosition){
        odometry = new SwerveDriveOdometry(kinematics, gyroAngle, modulePosition);
    }

    public Pose2d getPose(){
        return odometry.getPoseMeters();
    }

    public void updatePose(Rotation2d gyroAngle,SwerveModulePosition[] modulePositions){
        odometry.update(gyroAngle, modulePositions);
    }

    public void updateVision(Pose2d visionPose, Rotation2d gyroAngle){
        odometry.resetPose(new Pose2d(visionPose.getTranslation(), gyroAngle));
    }

    public SwerveDriveOdometry getOdometry(){
        return odometry;
    }
}