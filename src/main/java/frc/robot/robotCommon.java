package frc.robot;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
public class robotCommon {


    private static Pose2d currentRobotPose = Pose2d.kZero;
    private static Pose2d futureRobotPose = Pose2d.kZero; // 0.04 seconds in advance
    private static ChassisSpeeds fieldRelativeSpeeds = new ChassisSpeeds();
    private static ChassisSpeeds robotRelativeSpeeds = new ChassisSpeeds();
    private static Rotation2d robotAngle = Rotation2d.kZero;

    private static Optional<Alliance> alliance = DriverStation.getAlliance();

    private boolean isComp = false; //how the to do it?!?!
    private boolean isRobotCalibrated = false; //how the to do it?!?!

    public static boolean isRed(){
        if(alliance.get() == Alliance.Red) return true;
        else return false;
    }

    public static Translation2d getHubPose(){
        if(isRed()) return Translation2d.kZero; //TODO: update point
        else return Translation2d.kZero; //TODO: update point
    }

    public static Pose2d getDelveryPose(){
        if(isRed()){
            if(currentRobotPose.getX() > 8.07 /2){
                return Pose2d.kZero; //TODO: update the point
            }
            else return Pose2d.kZero;
        }else{
            if(currentRobotPose.getX() > 8.07 /2){
                    return Pose2d.kZero; //TODO: update the point
                }
                else return Pose2d.kZero;
        }
    }

    public static Rotation2d getRobotAngle(){
        return robotAngle;
    }

    public static void setRobotAngle(Rotation2d newRobotAngle){
        robotAngle = newRobotAngle;
    }

    public static Pose2d getRobotFucerPose(){
        return futureRobotPose;
    }

    public static void setRobotFucerPose(Pose2d newRobotFucerPose){
        futureRobotPose = newRobotFucerPose;
    }

    public static ChassisSpeeds getChassisfieldRelativeSpeeds(){
        return fieldRelativeSpeeds;
    }

    public static void setChassisfieldRelativeSpeeds(ChassisSpeeds newChassisfieldRelativeSpeeds){
        fieldRelativeSpeeds = newChassisfieldRelativeSpeeds;
    }

    public static ChassisSpeeds getrobotRelativeSpeeds(){
        return robotRelativeSpeeds;
    }

    public static void setrobotRelativeSpeeds(ChassisSpeeds newRobotRelativeSpeeds){
        robotRelativeSpeeds = newRobotRelativeSpeeds;
    }

}