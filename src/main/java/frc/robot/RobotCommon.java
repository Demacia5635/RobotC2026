package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.shooter.ShooterConstants;
public class RobotCommon {
    private static boolean isRed = true;
    public static boolean isComp = false;

    public static boolean isRed(){
        return isRed;
    }

    public static void setIsRed(boolean newIsRed){
        isRed = newIsRed;
    }

    public static Translation2d getHubPose(){
        if(isRed()) return Field.HubRed.CENTER;
        else return Field.HubBlue.CENTER; 
    }

    public static Translation2d getDeliveryPose() {
        return (Chassis.getInstance().getPose().getY() < Field.FieldDimensions.Y_CENTER)?
            isRed()? ShooterConstants.DELIVERY_RED_LEFT:
                ShooterConstants.DELIVERY_BLUE_LEFT:
            isRed()? ShooterConstants.DELIVERY_RED_RIGHT:
                ShooterConstants.DELIVERY_BLUE_RIGHT;  
    }

    public static boolean getIsComp(){
        return isComp;
    }

    public static void setIsComp(boolean newIsComp){
        isComp = newIsComp;
    }
}