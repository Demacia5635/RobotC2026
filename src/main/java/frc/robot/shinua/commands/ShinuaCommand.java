// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shinua.ShinuaConstants;
import frc.robot.shinua.ShinuaConstants.ShinuaState;
import frc.robot.shinua.subsystems.ShinuaSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShinuaCommand extends Command {
  /** Creates a new ShinuaCommand. */
  private ShinuaSubsystem shinuaSubsystem;
  private double wantedDuty = 0;

  public ShinuaCommand(ShinuaSubsystem shinuaSubsystem) {
    this.shinuaSubsystem = shinuaSubsystem;
    SmartDashboard.putData("Shinua Testing", this);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Wanted duty shinua", () -> wantedDuty, (x) -> wantedDuty = x);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  private boolean isBallsStuck() {
    return (shinuaSubsystem.getShinuaCurrent() > ShinuaConstants.SHINUA_BALLS_STUCK_CURRENT
        && Math.abs(shinuaSubsystem.getShinuaVelocity()) < ShinuaConstants.SHINUA_BALLS_STUCK_VELOCITY)
        || (IntakeSubsystem.getInstance().getRollerCurrent() > IntakeConstants.ROLLER_BALLS_STUCK_CURRENT
            && IntakeSubsystem.getInstance().getVelocity() < IntakeConstants.ROLLER_BALLS_STUCK_VELOCITY);
  }

  private void handleBallsStuck() {
    shinuaSubsystem.setShinuaDuty(-1);
    IntakeSubsystem.getInstance().setRollerDuty(-1);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shinuaSubsystem.getState()) {
      case SHINUA_ON, SHINUA_OFF:
        if (isBallsStuck()) {
          handleBallsStuck();
        }
        shinuaSubsystem.setShinuaDuty(shinuaSubsystem.getState().duty);
        break;
      case EJECTING:
        shinuaSubsystem.setShinuaDuty(shinuaSubsystem.getState().duty);
      case Testing:
        shinuaSubsystem.setShinuaDuty(wantedDuty);
        break;
      default:
        shinuaSubsystem.setState(ShinuaState.SHINUA_OFF);
        shinuaSubsystem.stopShinua();
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shinuaSubsystem.stopShinua();
    shinuaSubsystem.setState(ShinuaState.SHINUA_OFF);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
