// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.shooter.ShooterConstants;

/** Add your docs here. */
public class ShootingWhileDriving {
    private static ChassisSpeeds robotVel;
    private static Pose2d robotPose;
    private static double robotFutureX;
    private static double robotFutureY;
    private static double[] lut;
    private static double timeOfFlight;
    private static final double TIME_DIFFRENCE = 1;
    private static double turretAngle;
    private static double hoodAngle;
    private static double velocity;
    private static double distance;
    
    public static void calculate(Translation2d targetPose){
        robotVel = RobotCommon.getFieldRelativeSpeeds();
        robotPose = RobotCommon.getRobotFucerPose();
        
        distance = Math.hypot(targetPose.getX() - robotPose.getX(), targetPose.getY() - robotPose.getY());

        timeOfFlight = ShooterConstants.LOOK_UP_TABLE.get(distance)[2];

        robotFutureX =  robotPose.getX() + (timeOfFlight * robotVel.vxMetersPerSecond);
        robotFutureY = robotPose.getY() + (timeOfFlight * robotVel.vyMetersPerSecond);

        for (int i = 0; i < 10; i++) {
            distance = Math.hypot(targetPose.getX() - robotFutureX, targetPose.getY() - robotFutureY);
           double  newTimeOfFlight = ShooterConstants.LOOK_UP_TABLE.get(distance)[2];

            robotFutureX += (timeOfFlight - robotFutureX/robotVel.vxMetersPerSecond) * TIME_DIFFRENCE;
            robotFutureY += (timeOfFlight - robotFutureY/robotVel.vyMetersPerSecond) * TIME_DIFFRENCE;

            if(Math.abs(timeOfFlight - newTimeOfFlight) < 0.1) {
                 break;
            }
            timeOfFlight = newTimeOfFlight; 
        }
        distance = Math.hypot(targetPose.getX() - robotFutureX, targetPose.getY() - robotFutureY);
        lut = ShooterConstants.LOOK_UP_TABLE.get(distance);
        hoodAngle = lut[1];
        velocity = lut[0];
        turretAngle = new Translation2d(targetPose.getX() - robotFutureX, targetPose.getY() - robotFutureY).getAngle().getRadians();
    }

    public static double getHoodAngle(){
        return hoodAngle;
    }

    public static double getFlywheelVel(){
        return velocity;
    }

    public static double getTurretAngle(){
        return turretAngle;
    }
}
