package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.shooter.ShooterConstants;
public class RobotCommon {
    private static boolean isRed = true;
    private static boolean isComp = false;
    private static Shifts shift = Shifts.Disable;

    public static boolean getIsRed(){
        return isRed;
    }

    public static void setIsRed(boolean newIsRed){
        isRed = newIsRed;
    }

    public static boolean getIsComp(){
        return isComp;
    }

    public static void setIsComp(boolean newIsComp){
        isComp = newIsComp;
    }

    public static Translation2d getHubPose(){
        if(getIsRed()) return Field.HubRed.CENTER;
        else return Field.HubBlue.CENTER; 
    }

    public static Translation2d getDeliveryPose() {
        return (Chassis.getInstance().getPose().getY() < Field.FieldDimensions.Y_CENTER)?
            getIsRed()? ShooterConstants.DELIVERY_RED_LEFT:
                ShooterConstants.DELIVERY_BLUE_LEFT:
            getIsRed()? ShooterConstants.DELIVERY_RED_RIGHT:
                ShooterConstants.DELIVERY_BLUE_RIGHT;  
    }

    public static Shifts getShift() {
        return shift;
    }

    public static void setShift(Shifts shift){
        RobotCommon.shift = shift;
    }

    public static boolean isOnBamp() {
        Translation2d pose = Chassis.getInstance().getPose().getTranslation();

        return isInsideRectangle(
                pose,
                Field.BumpRedScoring.X_FRONT,
                Field.BumpRedScoring.X_BACK,
                Field.BumpRedScoring.Y_FRONT,
                Field.BumpRedScoring.Y_BACK)
            || isInsideRectangle(
                pose,
                Field.BumpRedAudience.X_FRONT,
                Field.BumpRedAudience.X_BACK,
                Field.BumpRedAudience.Y_FRONT,
                Field.BumpRedAudience.Y_BACK)
            || isInsideRectangle(
                pose,
                Field.BumpBlueScoring.X_BACK,
                Field.BumpBlueScoring.X_FRONT,
                Field.BumpBlueScoring.Y_FRONT,
                Field.BumpBlueScoring.Y_BACK)
            || isInsideRectangle(
                pose,
                Field.BumpBlueAudience.X_BACK,
                Field.BumpBlueAudience.X_FRONT,
                Field.BumpBlueAudience.Y_FRONT,
                Field.BumpBlueAudience.Y_BACK);
    }

    private static boolean isInsideRectangle(Translation2d point, double minX, double maxX, double minY, double maxY) {
        return point.getX() >= minX &&
            point.getX() <= maxX &&
            point.getY() >= minY &&
            point.getY() <= maxY;
    }

    public static boolean isInAllinceZone() {
        double frontOfRobot;
    
        if (getIsRed()) {
            frontOfRobot = Chassis.getInstance().getPose().getX()
                    + Constants.ROBOT_LENGTH / 2.0;
    
            return frontOfRobot >= Field.Zones.ROBOT_STARTING_LINE_RED_X;
        }
    
        frontOfRobot = Chassis.getInstance().getPose().getX()
                - Constants.ROBOT_LENGTH / 2.0;
    
        return frontOfRobot <= Field.Zones.ROBOT_STARTING_LINE_BLUE_X;
    }

    public enum Shifts{
        Transition,
        Active,
        Inactive,
        Endgame,
        Disable,
        Auto
    }
}