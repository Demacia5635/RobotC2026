package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
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
        else return Field.HubRed.CENTER; 
    }

    public static boolean getIsComp(){
        return isComp;
    }

    public static void setIsComp(boolean newIsComp){
        isComp = newIsComp;
    }
}