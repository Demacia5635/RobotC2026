// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.commands;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.demacia.utils.log.LogManager;
import frc.robot.RobotContainer;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shinua.ShinuaConstants;
import frc.robot.shinua.ShinuaConstants.ShinuaState;
import frc.robot.shinua.subsystems.ShinuaSubsystem;
import frc.robot.shooter.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShinuaCommand extends Command {
  /** Creates a new ShinuaCommand. */
  private final ShinuaSubsystem shinuaSubsystem;
  private double wantedDutyMecanum = 0;
  private double wantedvelDutyRollers = 0;

  private Timer timerForStuckBalls;
  private boolean startedHandlingBalls = false;
  private Timer timerForReleasingPressure;

  public ShinuaCommand() {
    super();
    shinuaSubsystem = ShinuaSubsystem.getInstance();
    timerForStuckBalls = new Timer();
    timerForReleasingPressure = new Timer();
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shinuaSubsystem);
    SmartDashboard.putData("Shinua Testing", this);
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);
    builder.addDoubleProperty("Wanted duty mecanum", () -> wantedDutyMecanum, (x) -> wantedDutyMecanum = x);
    builder.addDoubleProperty("Wanted power rollers!!!", () -> wantedvelDutyRollers, (x) -> wantedvelDutyRollers = x);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shinuaSubsystem.getState()) {
      case SHINUA_ON:
          if (shinuaSubsystem.getRollerCurrent() > ShinuaConstants.MAX_CURRENT){
            timerForStuckBalls.start();
          }
          if (timerForStuckBalls.isRunning() && shinuaSubsystem.getRollerCurrent() < ShinuaConstants.MAX_CURRENT){
            timerForStuckBalls.reset();
            timerForStuckBalls.stop();
            LogManager.log("bad");
          }
          if (timerForStuckBalls.hasElapsed(ShinuaConstants.BALLS_STUCK_DURATION) && !timerForReleasingPressure.hasElapsed(ShinuaConstants.BALLS_STUCK_HANDLING_TIME)) {
            timerForReleasingPressure.start();
            timerForStuckBalls.reset();
            timerForStuckBalls.stop();
            shinuaSubsystem.setMecanumDuty(ShinuaConstants.DUTY_WHEN_MAX_CURRENT);
            LogManager.log("no pow");
          } else if (!((Shooter.getInstance().isReady()) || IntakeSubsystem.getInstance().getIntakeDeployAngle() < 0 || RobotContainer.forcedIsReady)){
            shinuaSubsystem.setMecanumDuty(ShinuaState.NO_INDEXER.dutyMecanum);
            shinuaSubsystem.setVelocityRollers(ShinuaState.NO_INDEXER.velocityRollers);
          } else {
            shinuaSubsystem.setMecanumDuty(shinuaSubsystem.getState().dutyMecanum);
            shinuaSubsystem.setVelocityRollers(shinuaSubsystem.getState().velocityRollers);
          }
          if (timerForReleasingPressure.isRunning() && timerForReleasingPressure.hasElapsed(ShinuaConstants.BALLS_STUCK_HANDLING_TIME)) {
            timerForReleasingPressure.reset();
            timerForReleasingPressure.stop();
            timerForStuckBalls.reset();
            timerForStuckBalls.stop();
            LogManager.log("end");
          }
        break;
      case SHINUA_OFF, EJECTING, NO_INDEXER:
          shinuaSubsystem.setMecanumDuty(shinuaSubsystem.getState().dutyMecanum);
          shinuaSubsystem.setVelocityRollers(shinuaSubsystem.getState().velocityRollers);
        break;
      case TESTING:
        shinuaSubsystem.setMecanumDuty(wantedDutyMecanum);
        shinuaSubsystem.setVelocityRollers(wantedvelDutyRollers);
        // LogManager.log("at tasting"+ "rollers" + wantedvelDutyRollers);
        break;
      default:
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
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
