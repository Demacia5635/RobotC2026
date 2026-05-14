// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.shinua.ShinuaConstants.ShinuaState;
import frc.robot.shinua.subsystems.ShinuaSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShinuaCommand extends Command {
  /** Creates a new ShinuaCommand. */
  private final ShinuaSubsystem shinuaSubsystem;
  private double wantedDutyRollers = 0;
  private double wantedDutyMecanum = 0;
  // private Timer timerForStuckBalls;
  // private boolean startedHandlingBalls = false;
  private Timer timerForReleasingPressure;

  public ShinuaCommand(ShinuaSubsystem shinuaSubsystem) {
    this.shinuaSubsystem = shinuaSubsystem;
    addRequirements(shinuaSubsystem);
    SmartDashboard.putData("Shinua Testing", this);
    // timerForStuckBalls = new Timer();
    timerForReleasingPressure = new Timer();
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
    // timerForStuckBalls.reset();
    timerForReleasingPressure.restart();
  }

  // public boolean isBallsStuck() {
  // return shinuaSubsystem.getMecanumCurrent() >
  // ShinuaConstants.MECANUM_BALLS_STUCK_CURRENT
  // && Math.abs(shinuaSubsystem.getMecanumVelocity()) <
  // ShinuaConstants.MECANUM_BALLS_STUCK_VELOCITY
  // || intakeSubsystem.getRollerCurrent() >
  // IntakeConstants.ROLLER_BALLS_STUCK_CURRENT
  // || shinuaSubsystem.getRollerCurrent() >
  // ShinuaConstants.ROLLERS_BALLS_STUCK_CURRENT
  // && Math.abs(shinuaSubsystem.getRollersVelocity()) <
  // ShinuaConstants.ROLLERS_BALLS_STUCK_VELOCITY;
  // }

  // public boolean BallsArentStuckAnymore() {
  // return timerForStuckBalls.isRunning() && !isBallsStuck();
  // }

  // private boolean shouldStartStuckBallsTimer() {
  // return !timerForStuckBalls.isRunning();
  // }

  // private boolean shouldHandleBallsStuck() {
  // return timerForStuckBalls.hasElapsed(ShinuaConstants.BALLS_STUCK_DURATION) &&
  // !startedHandlingBalls;
  // }

  // private boolean shouldStopHandlingBallsStuck() {
  // return startedHandlingBalls &&
  // timerForStuckBalls.hasElapsed(ShinuaConstants.BALLS_STUCK_HANDLING_TIME);
  // }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shinuaSubsystem.getState()) {
      case SHINUA_ON, SHINUA_OFF, EJECTING:
        // if (isBallsStuck()) {
        // if (shouldStartStuckBallsTimer()) {
        // timerForStuckBalls.restart();
        // }
        // if (shouldHandleBallsStuck()) {
        // startedHandlingBalls = true;
        // RobotCommon.setStuck(true);
        // shinuaSubsystem.setMecanumDuty(ShinuaState.EJECTING.dutyMecanum);

        // } else if (shouldStopHandlingBallsStuck()) {
        // startedHandlingBalls = false;
        // RobotCommon.setStuck(false);
        // timerForStuckBalls.stop();
        // timerForStuckBalls.reset();
        // }
        if (timerForReleasingPressure.get() % 5 < 0.5) {
          shinuaSubsystem.setMecanumDuty(ShinuaState.EJECTING.velocityRollers);
        } else {
          shinuaSubsystem.setMecanumDuty(shinuaSubsystem.getState().dutyMecanum);
          shinuaSubsystem.setVelocityRollers(shinuaSubsystem.getState().velocityRollers);
        }
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
