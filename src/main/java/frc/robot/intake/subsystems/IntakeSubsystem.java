// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.shinua.ShinuaConstants;
import frc.robot.shinua.subsystems.ShinuaSubsystem;

//add calibration, TODO when have requiremant
public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsytem. */
  private static IntakeSubsystem instance;
  private TalonFXMotor rollerMotor;
  private TalonFXMotor intakeDeployMotor;
  private IntakeState state;
  private Timer timerForStuckBalls;
  private boolean startedHandlingBalls = false;
  private final ShinuaSubsystem shinuaSubsystem = ShinuaSubsystem.getInstance();

  private static IntakeSubsystem getInstance() {
    if (instance == null)
      instance = new IntakeSubsystem();
    return instance;
  }

  public IntakeSubsystem() {// TODO call super,
    rollerMotor = new TalonFXMotor(IntakeConstants.ROLLER_CONFIG);
    intakeDeployMotor = new TalonFXMotor(IntakeConstants.INTAKE_DEPLOY_CONFIG);
    timerForStuckBalls = new Timer();
    state = IntakeState.IDLE;
    addNT();
    SmartDashboard.putData(this);
  }

  public void addNT() {
    SendableChooser<IntakeState> stateChooser = new SendableChooser<>();
    for (IntakeState intakeState : IntakeState.values()) {
      stateChooser.addOption(intakeState.name(), intakeState);
    }
    stateChooser.onChange(newState -> this.state = newState);
    SmartDashboard.putData(getName() + " Intake State Chooser", stateChooser);
  }
//TODO use name from constant

  }

  public void checkElectronics() {
    rollerMotor.checkElectronics();
    intakeDeployMotor.checkElectronics();
  }

  public void setNeutralModeRoller(boolean isBrake) {
    rollerMotor.setNeutralMode(isBrake);
  }

  public void setRollerDuty(double duty) {
    rollerMotor.setDuty(duty);
  }

  public void setAngleIntakeDeploy(double angle) {
    angle = MathUtil.clamp(angle, IntakeConstants.DEPLOY_CLOSED_ANGLE, IntakeConstants.DEPLOY_OPEN_ANGLE);
    intakeDeployMotor.setMotion(angle);
  }

  public void stopRoller() {
    rollerMotor.stop();
  }

  public void stopIntakeDeploy() {
    intakeDeployMotor.stop();
  }

  public double getRollerCurrent() {
    return rollerMotor.getCurrentCurrent();
  }

  public double getRollerVelocity() {
    return rollerMotor.getCurrentVelocity();
  }

  public double getIntakeDeployCurrent() {
    return intakeDeployMotor.getCurrentCurrent();
  }

  // TODO check in shinoa, do not nead to check here
  public boolean isBallsStuck() {
    return (shinuaSubsystem.getMecanumCurrent() > ShinuaConstants.MECANUM_BALLS_STUCK_CURRENT
        && Math.abs(shinuaSubsystem.getMecanumVelocity()) < ShinuaConstants.MECANUM_BALLS_STUCK_VELOCITY)
        || (shinuaSubsystem.getRollerCurrent() > ShinuaConstants.ROLLERS_BALLS_STUCK_CURRENT
            && Math.abs(shinuaSubsystem.getRollersVelocity()) < ShinuaConstants.ROLLERS_BALLS_STUCK_VELOCITY)
        || (getRollerCurrent() > IntakeConstants.ROLLER_BALLS_STUCK_CURRENT
            && getRollerVelocity() < IntakeConstants.ROLLER_BALLS_STUCK_VELOCITY);
  }

  public void handleBallsStuck() {// TODO not give power to other subsystem
    shinuaSubsystem.setRollersDuty(-1);
    shinuaSubsystem.setMecanumDuty(-1);
    setRollerDuty(-1);
  }

  public boolean BallsArentStuckAnymore() {
    return timerForStuckBalls.isRunning() && !isBallsStuck();
  }

  private boolean shouldStartStuckBallsTimer() {
    return isBallsStuck() && !timerForStuckBalls.isRunning();
  }

  private boolean shouldHandleBallsStuck() {
    return timerForStuckBalls.hasElapsed(IntakeConstants.BALLS_STUCK_DURATION) && !startedHandlingBalls
        && isBallsStuck();
  }

  private boolean shouldStopHandlingBallsStuck() {
    return startedHandlingBalls && timerForStuckBalls.hasElapsed(IntakeConstants.BALLS_STUCK_HANDLING_TIME);
  }

  public IntakeState getState() {
    return state;
  }

  public void setState(IntakeState newState) {
    state = newState;
  }

  @Override
  public void periodic() {
    if (shouldStartStuckBallsTimer()) {
      timerForStuckBalls.restart();
    }

    if (BallsArentStuckAnymore()) {
      timerForStuckBalls.stop();
      timerForStuckBalls.reset();
      startedHandlingBalls = false;
    }

    if (shouldHandleBallsStuck()) {
      startedHandlingBalls = true;
      handleBallsStuck();
    }

    if (shouldStopHandlingBallsStuck()) {
      timerForStuckBalls.stop();
      timerForStuckBalls.reset();
      startedHandlingBalls = false;
    }
    // This method will be called once per scheduler run
  }
}