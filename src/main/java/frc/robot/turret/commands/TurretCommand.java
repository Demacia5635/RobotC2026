// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.turret.commands;

import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.shooter.ShooterConstants;
import frc.robot.shooter.ShooterConstants.FeederConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.turret.TurretConstants;
import frc.robot.turret.TurretConstants.TurretStates;
import frc.robot.turret.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurretCommand extends Command {
  public Turret turret ;
  public TurretStates turretstates;

  private double turretAngle;
  /** Creates a new TurretCommand. */
  public TurretCommand() {
        addRequirements(turret);

    // Use addRequirements() here to declare subsystem dependencies.
  }
  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addDoubleProperty(getName(), ()-> turretAngle, (angle)-> turretAngle = angle   );
  }
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (turret.getTurretState()) {
      case IDLE:
        turret.stopMotor();
        break;
      case TEST:
        turret.setTurrtMotorMotion(turretAngle);
        break;
      case SHOOTING:
      double Angle =TurretConstants.TURRET_POSE.getTranslation().plus(Constants.HUB_POSE2D.getTranslation()).getAngle().getRadians();
        turret.setTurrtMotorMotion(Angle);
        break;
      case DELIVERY:
      
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
