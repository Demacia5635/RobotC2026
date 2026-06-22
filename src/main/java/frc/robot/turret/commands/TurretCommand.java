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
    switch (turret.getTurretState()) {
      case IDLE:
        turret.stopMotor();
      break;
      case TEST:
        targetAngle = testAngle;
        turret.setTurretMotion(targetAngle);
        break;
      case SHOOTING:
        double shooterToHub = RobotCommon.getHubPose().minus(Chassis.getInstance().getPose().getTranslation().plus(new Translation2d(-0.17, 0).rotateBy(Chassis.getInstance().getGyroAngle()))).getAngle().getDegrees()-180;
        targetAngle = shooterToHub-Chassis.getInstance().getGyroAngle().getDegrees()+313;
        turret.setTurretMotion((targetAngle < 0 && targetAngle > -5)? targetAngle : MathUtil.clamp(MathUtil.inputModulus(targetAngle, 0, 360), 0, 340));
        break;
      case DELIVERY:
        // if (TurretConstants.TURRET_POSE.getX() < ShooterConstants.HEIGHT/2) {
        //   targetAngle = TurretConstants.TURRET_POSE.getTranslation().plus(ShooterConstants.DELIVERY_LEFT_POINT).getAngle().getRadians();
        //   turret.setTurretMotion(targetAngle);
        // } else{
        //   targetAngle = TurretConstants.TURRET_POSE.getTranslation().plus(ShooterConstants.DELIVERY_RIGHT_POINT).getAngle().getRadians();
        //   turret.setTurretMotion(targetAngle);
        // }
        double shooterToDelivery = RobotCommon.getDeliveryPose().minus(Chassis.getInstance().getPose().getTranslation().plus(new Translation2d(-0.17, 0).rotateBy(Chassis.getInstance().getGyroAngle()))).getAngle().getDegrees()-180;
        targetAngle = shooterToDelivery-Chassis.getInstance().getGyroAngle().getDegrees()+313;
        turret.setTurretMotion((targetAngle < 0 && targetAngle > -5)? targetAngle : MathUtil.clamp(MathUtil.inputModulus(targetAngle, 0, 360), 0, 340));
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stopMotor();
  }
}
