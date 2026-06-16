// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeCommand extends Command {
  /** Creates a new IntakeCommand. */ 
  private double wantedAngle = 0;
  private double wantedDuty = 0;
  private final IntakeSubsystem intakeSubsystem;

  public IntakeCommand(IntakeSubsystem intakeSubsystem) {
    this.intakeSubsystem = intakeSubsystem;
    addRequirements(intakeSubsystem);
    SmartDashboard.putData("Intake Testing", this);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Wanted angle", () -> wantedAngle, (x) -> wantedAngle = x);
    builder.addDoubleProperty("Wanted duty intake", () -> wantedDuty, (x) -> wantedDuty = x);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (intakeSubsystem.getState()) {
      case INTAKING, EJECTING, DEPLOYED, CLOSED:
        intakeSubsystem.setRollerDuty(intakeSubsystem.getState().duty);
        intakeSubsystem.setAngleIntakeDeploy(intakeSubsystem.getState().angle);
        break;

      case TESTING:
        intakeSubsystem.setRollerDuty(wantedDuty);
        intakeSubsystem.setAngleIntakeDeploy(Math.toRadians(wantedAngle));
        break;

      case IDLE:
        intakeSubsystem.stopRoller();
        intakeSubsystem.stopIntakeDeploy();
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stopRoller();
    intakeSubsystem.stopIntakeDeploy();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
