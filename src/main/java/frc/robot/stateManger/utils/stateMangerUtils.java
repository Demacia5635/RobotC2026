package frc.robot.stateManger.utils;

import edu.wpi.first.math.geometry.Translation2d;

public class stateMangerUtils {

    public static boolean inRange(double value, double min, double max) {
        return value > min - 0.5 && value < max + 0.5;
    }

    public static boolean isPastHub(double x, double y, Translation2d hub) {
    double dx = x - hub.getX();
    double dy = y - hub.getY();
    return Math.sqrt(dx*dx + dy*dy) < 0.5;
}
    
}
