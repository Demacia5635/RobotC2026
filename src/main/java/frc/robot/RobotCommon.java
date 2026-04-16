package frc.robot;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
public class RobotCommon {


    public static Pose2d currentRobotPose = Pose2d.kZero;
    private static Pose2d futureRobotPose = Pose2d.kZero; // 0.04 seconds in advance
    private static ChassisSpeeds fieldRelativeSpeeds = new ChassisSpeeds();
    private static ChassisSpeeds robotRelativeSpeeds = new ChassisSpeeds();
    private static Rotation2d robotAngle = Rotation2d.kZero;

    private static Optional<Alliance> alliance = DriverStation.getAlliance();

    private static boolean isRed;

    public static boolean isComp = false; 
    private static boolean isRobotCalibrated = false; 

    public static boolean isRed(){
        if(alliance.get() == Alliance.Red) isRed = true;
        else isRed = false;

        return isRed;        
    }

    public static void setIsRed(boolean newIsRed){
        isRed = newIsRed;
    }

    public static Translation2d getHubPose(){
        if(isRed()) return Field.HubRed.CENTER;
        else return Field.HubRed.CENTER; 
    }

    public static boolean getRobotCalibrated(){
        return isRobotCalibrated;
    }

    public static void setIsRobotCalibrated(boolean newIsRobotCalibrated){
        newIsRobotCalibrated = isRobotCalibrated;
    }

    public static boolean getIsComp(){
        return isComp;
    }

    public static void setIsComp(boolean newIsComp){
        isComp = newIsComp;
    }

    public static boolean getRobotCalibrated(){
        return isRobotCalibrated;
    }

    public static void setIsRobotCalibrated(boolean newIsRobotCalibrated){
        isRobotCalibrated = newIsRobotCalibrated;
    }

    public static boolean getIsComp(){
        return isComp;
    }

    public static void setIsComp(boolean newIsComp){
        isComp = newIsComp;
    }

    public static Pose2d getDeliveryPose(){
        if(isRed()){
            if(currentRobotPose.getX() > Field.FieldDimensions.LENGTH /2){
                return new Pose2d(Field.DELIVERY.DELIVERY_POINT1.getX(), Field.DELIVERY.DELIVERY_POINT1.getY(), Field.DELIVERY.DELIVERY_POINT1.getAngle()); //TODO: update the point
            }
<<<<<<< HEAD:src/main/java/frc/robot/robotCommon.java
            else return new Pose2d(Field.DELIVERY.DELIVERY_POINT2.getX(), Field.DELIVERY.DELIVERY_POINT2.getY(), Field.DELIVERY.DELIVERY_POINT2.getAngle());
=======
            else return Pose2d.kZero; //TODO: update the point
>>>>>>> origin/shooter:src/main/java/frc/robot/RobotCommon.java
        }else{
            if(currentRobotPose.getX() > 8.07 /2){
                    return Pose2d.kZero; //TODO: update the point
                }
                else return Pose2d.kZero; //TODO: update the point
        }
    }

    public static Rotation2d getRobotAngle(){
        return robotAngle;
    }

    public static void setRobotAngle(Rotation2d newRobotAngle){
        robotAngle = newRobotAngle;
    }

    public static Pose2d getRobotFuturePose(){
        return futureRobotPose;
    }

    public static void setRobotFuturePose(Pose2d newRobotFuturePose){
        futureRobotPose = newRobotFuturePose;
    }

    public static ChassisSpeeds getChassisFieldRelativeSpeeds(){
        return fieldRelativeSpeeds;
    }

    public static void setChassisFieldRelativeSpeeds(ChassisSpeeds newChassisFieldRelativeSpeeds){
        fieldRelativeSpeeds = newChassisFieldRelativeSpeeds;
    }

    public static ChassisSpeeds getRobotRelativeSpeeds(){
        return robotRelativeSpeeds;
    }

    public static void setRobotRelativeSpeeds(ChassisSpeeds newRobotRelativeSpeeds){
        robotRelativeSpeeds = newRobotRelativeSpeeds;
    }

}