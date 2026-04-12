// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.intake.subsystems.IntakeSubsytem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeCommand extends Command {
  /** Creates a new IntakeCommand. */
  private final IntakeSubsytem IntakeSubsystem;

  private double wantedAngle = 0;
  private double wantedDuty = 0;

  public IntakeCommand(IntakeSubsytem IntakeSubsystem) {
    this.IntakeSubsystem = IntakeSubsystem;
    addRequirements(IntakeSubsystem);
    SmartDashboard.putData("Intake Testing", this);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Wanted angle", () -> wantedAngle, (x) -> wantedAngle = x);
    builder.addDoubleProperty("Wanted duty", () -> wantedDuty, (x) -> wantedDuty = x);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (IntakeSubsystem.getState()) {
      case INTAKING, EJECTING, DEPLOYED, CLOSED:
        IntakeSubsystem.setRollerDuty(IntakeSubsystem.getState().duty);
        IntakeSubsystem.setAngleIntakeDeploy(IntakeSubsystem.getState().angle);
        break;

      case TESTING:
        IntakeSubsystem.setRollerDuty(wantedDuty);
        IntakeSubsystem.setAngleIntakeDeploy(Math.toRadians(wantedAngle));
        break;

      case IDLE:
        IntakeSubsystem.stopRoller();
        IntakeSubsystem.stopIntakeDeploy();
        break;

      default:
        IntakeSubsystem.setState(IntakeState.IDLE);
        IntakeSubsystem.stopRoller();
        IntakeSubsystem.stopIntakeDeploy();
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
