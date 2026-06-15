// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.intake.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TestCommand extends Command {
  /** Creates a new testIntakeAtouCommand. */
  IntakeSubsystem subsystem;
  double wantedAngle;
  public TestCommand(IntakeSubsystem subsystem) {
    this.subsystem= subsystem;
    addRequirements(subsystem);
    SmartDashboard.putData(this);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      super.initSendable(builder);
      builder.addDoubleProperty("wantedAngle 2",()-> Math.toDegrees(wantedAngle), (x)-> wantedAngle = Math.toRadians(x));
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    subsystem.setAngleIntakeDeploy(wantedAngle);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    subsystem.stopIntakeDeploy();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return subsystem.getCurrentCurrentDeploy() > 17;
  }
}
