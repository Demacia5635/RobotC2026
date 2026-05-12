package frc.robot.stateManger.utils;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Field;
import frc.robot.RobotCommon;

public class stateMangerUtils {

    public static boolean inRange(double value, double min, double max) {
        return value > min - 0.5 && value < max + 0.5;
    }

    

    public static boolean isPastRedHub(double x) {
        return  x < Field.HubRed.CENTER.getX() && RobotCommon.isRed();
    }

    public static boolean isPastBlueHub(double x) {
        return  x > Field.HubBlue.CENTER.getX() && !RobotCommon.isRed();
    }
    
}
