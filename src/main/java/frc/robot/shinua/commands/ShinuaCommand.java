// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.shinua.ShinuaConstants.ShinuaState;
import frc.robot.shinua.subsystems.ShinuaSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShinuaCommand extends Command {
  /** Creates a new ShinuaCommand. */
  private final ShinuaSubsystem shinuaSubsystem = ShinuaSubsystem.getInstance();
  private double wantedDutyRollers = 0;
  private double wantedDutyMecanum = 0;

  public ShinuaCommand() {
    addRequirements(shinuaSubsystem);
    SmartDashboard.putData("Shinua Testing", this);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Wanted duty rollers", () -> wantedDutyRollers, (x) -> wantedDutyRollers = x);
    builder.addDoubleProperty("Wanted duty mecanum", () -> wantedDutyMecanum, (x) -> wantedDutyMecanum = x);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }


  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shinuaSubsystem.getState()) {
      case SHINUA_ON, SHINUA_OFF, EJECTING:
        if (shinuaSubsystem.isBallsStuck()) {
          shinuaSubsystem.handleBallsStuck();
        }
        shinuaSubsystem.setMecanumDuty(shinuaSubsystem.getState().dutyMecanum);
        shinuaSubsystem.setRollersDuty(shinuaSubsystem.getState().dutyRollers);
        break;
      case TESTING:
        shinuaSubsystem.setMecanumDuty(wantedDutyMecanum);
        shinuaSubsystem.setRollersDuty(wantedDutyRollers);
        break;
      default:
        shinuaSubsystem.setState(ShinuaState.SHINUA_OFF);
        shinuaSubsystem.stopMecanum();
        shinuaSubsystem.stopRollers();
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shinuaSubsystem.stopMecanum();
    shinuaSubsystem.stopRollers();
    shinuaSubsystem.setState(ShinuaState.SHINUA_OFF);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
