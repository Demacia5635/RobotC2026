// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.shooter.ShooterConstants;
import frc.robot.shooter.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class hoodCalibrasen extends Command {
  /** Creates a new hoodCalibrasen. */
  Shooter shooter;
  public hoodCalibrasen() {
    shooter = Shooter.getInstance();
    addRequirements(shooter);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setHoodPower(0.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setHoodPose(ShooterConstants.HoodConstants.HOOD_LIMET_SWITCH_POSE);
    shooter.setCaliberation();
    shooter.stopHood();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;//shooter.isHoodLimetSwithSee();
  }
}
