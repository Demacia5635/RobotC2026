// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.turret.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.ShootingWhileDriving;
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
        break;
      case SHOOTING:
        targetAngle = TurretConstants.TURRET_ANGLE;  //TODO: change it to targetAngle = ShootingWhileDriving.getTurretAngle();
        break;
      case DELIVERY:
        if (TurretConstants.TURRET_POSE.getX() < ShooterConstants.HEIGHT/2) {
          targetAngle = TurretConstants.TURRET_POSE.getTranslation().plus(ShooterConstants.DELIVERY_LEFT_POINT).getAngle().getRadians();
        } else{
          targetAngle = TurretConstants.TURRET_POSE.getTranslation().plus(ShooterConstants.DELIVERY_RIGHT_POINT).getAngle().getRadians();
        }
        break;
    }
    turret.setTurretMotion(targetAngle); //TODO also pid and motion magic
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stopMotor();
  }
}
