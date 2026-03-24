// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.PubSub;

/** Add your docs here. */
public class robotCommon {

    public static Pose2d RobtoPose = RobotContainer.chassis.getPose();
    public ChassisSpeeds chassisSpeedsField = RobotContainer.chassis.getChassisSpeedsFieldRel();
    public ChassisSpeeds chassisSpeedsRobot = RobotContainer.chassis.getChassisSpeedsRobotRel();
    
}
