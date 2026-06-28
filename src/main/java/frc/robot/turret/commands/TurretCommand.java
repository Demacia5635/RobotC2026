// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.turret.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.log.LogManager;
import frc.robot.RobotCommon;
import frc.robot.ShootingWhileDriving;
import frc.robot.Field;
import frc.robot.shooter.ShooterConstants;
import frc.robot.turret.TurretConstants;
import frc.robot.turret.subsystems.Turret;

public class TurretCommand extends Command {
  private Turret turret;
  private double testAngle;
  private double targetAngle;

  public TurretCommand() {
    this.turret = Turret.getInstance();
    addRequirements(turret);
    
    SmartDashboard.putData("Turret Testing", this);
  }
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty("Turret Angle:", ()-> testAngle, (angle)-> testAngle = angle);
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.setTurretMotion(turret.getDeliveryAngle());
    // switch (turret.getTurretState()) {
    //   case IDLE:
    //     turret.stopMotor();
    //   break;
    //   case TEST:
    //     targetAngle = testAngle;
    //     turret.setTurretMotion(targetAngle);
    //     break;
    //   case SHOOTING:
    //     double shooterToHub = RobotCommon.getHubPose().minus(Chassis.getInstance().getPose().getTranslation().plus(ShooterConstants.SHOOTER_OFFSET.rotateBy(Chassis.getInstance().getGyroAngle()))).getAngle().getDegrees()-(RobotCommon.isRed()?0:180);
    //     targetAngle = shooterToHub-Chassis.getInstance().getGyroAngle().getDegrees();
    //     turret.setTurretMotion(MathUtil.clamp(MathUtil.inputModulus(targetAngle, TurretConstants.MIN_TURRET_ANGLE - (360 - TurretConstants.TURRET_ANGLE_RANGE)/2, //(-323) - (-313) will be -313
    //     TurretConstants.MAX_TURRET_ANGLE + (360 - TurretConstants.TURRET_ANGLE_RANGE)/2), //(27) - (37) will be 27
    //     TurretConstants.MIN_TURRET_ANGLE, TurretConstants.MAX_TURRET_ANGLE));
    //     break;
    //   case DELIVERY:
    //     // double shooterToDelivery = RobotCommon.getDeliveryPose().minus(Chassis.getInstance().getPose().getTranslation().plus(ShooterConstants.SHOOTER_OFFSET.rotateBy(Chassis.getInstance().getGyroAngle()))).getAngle().getDegrees()-(RobotCommon.isRed()?0:180);
    //     // targetAngle = shooterToDelivery - Chassis.getInstance().getGyroAngle().getDegrees();
    //     // turret.setTurretMotion(MathUtil.clamp(MathUtil.inputModulus(targetAngle, TurretConstants.MIN_TURRET_ANGLE - (360 - TurretConstants.TURRET_ANGLE_RANGE)/2, //(-323) - (-313) will be -313
    //     // TurretConstants.MAX_TURRET_ANGLE + (360 - TurretConstants.TURRET_ANGLE_RANGE)/2), //(27) - (37) will be 27
    //     // TurretConstants.MIN_TURRET_ANGLE, TurretConstants.MAX_TURRET_ANGLE));
        
    //     break;
    //   case ONEPOINT:
    //     turret.setTurretMotion(0);
    //     break;
    // }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // turret.stopMotor();
  }
}
