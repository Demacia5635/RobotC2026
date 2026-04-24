// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shinua.ShinuaConstants;
import frc.robot.shinua.ShinuaConstants.ShinuaState;

public class ShinuaSubsystem extends SubsystemBase {
  private TalonFXMotor mecanumMotor;
  private TalonFXMotor rollersMotor;
  private ShinuaState state;
  private static ShinuaSubsystem instance;
  private final IntakeSubsystem intakeSubsystem = IntakeSubsystem.getInstance();
  private Timer timerForStuckBalls;
  private boolean startedHandlingBalls = false;

  private static ShinuaSubsystem getInstance() {
    if (instance == null)
      instance = new ShinuaSubsystem();
    return instance;
  }

  /** Creates a new ShinuaSubsystem. */
  public ShinuaSubsystem() {// TODO call super
    mecanumMotor = new TalonFXMotor(ShinuaConstants.MECANUM_CONFIG);
    rollersMotor = new TalonFXMotor(ShinuaConstants.ROLLERS_CONFIG);
    timerForStuckBalls = new Timer();
    state = ShinuaState.SHINUA_OFF;
    addNT();
  }

  public void addNT() {
    SendableChooser<ShinuaConstants.ShinuaState> stateChooser = new SendableChooser<>();
    stateChooser.addOption("SHINUA_ON", ShinuaConstants.ShinuaState.SHINUA_ON); // TODO add in for
    stateChooser.addOption("SHINUA_OFF", ShinuaConstants.ShinuaState.SHINUA_OFF);
    stateChooser.addOption("EJECTING", ShinuaConstants.ShinuaState.EJECTING);
    stateChooser.addOption("TESTING", ShinuaConstants.ShinuaState.TESTING);
    stateChooser.onChange(newState -> this.state = newState);
    SmartDashboard.putData(getName() + "Shinua State Chooser", stateChooser);

  }

  public void checkElectronics() {
    mecanumMotor.checkElectronics();
    rollersMotor.checkElectronics();
  }

  public void setNeutralModeRollers(boolean isBrake) {
    rollersMotor.setNeutralMode(isBrake);
  }

  public void setNeutralModeMecanum(boolean isBrake) {
    mecanumMotor.setNeutralMode(isBrake);
  }

  public void setMecanumDuty(double duty) {
    mecanumMotor.setDuty(duty);
  }

  public void setRollersDuty(double duty) {
    rollersMotor.setDuty(duty);
  }

  public void stopMecanum() {
    mecanumMotor.stop();
  }

  public void stopRollers() {
    rollersMotor.stop();
  }

  public double getMecanumVelocity() {
    return mecanumMotor.getCurrentVelocity();
  }

  public double getRollersVelocity() {
    return rollersMotor.getCurrentVelocity();
  }

  public double getMecanumCurrent() {
    return mecanumMotor.getCurrentCurrent();
  }

  public double getRollerCurrent() {
    return rollersMotor.getCurrentCurrent();
  }

  public boolean isBallsStuck() {// TODO add abs on intake, to put else where / check only shinoa motor stuck
    return (getMecanumCurrent() > ShinuaConstants.MECANUM_BALLS_STUCK_CURRENT
        && Math.abs(getMecanumVelocity()) < ShinuaConstants.MECANUM_BALLS_STUCK_VELOCITY)
        || (intakeSubsystem.getRollerCurrent() > IntakeConstants.ROLLER_BALLS_STUCK_CURRENT
            && intakeSubsystem.getRollerVelocity() < IntakeConstants.ROLLER_BALLS_STUCK_VELOCITY)
        || (getRollerCurrent() > ShinuaConstants.ROLLERS_BALLS_STUCK_CURRENT
            && Math.abs(getRollersVelocity()) < ShinuaConstants.ROLLERS_BALLS_STUCK_VELOCITY);
  }

  public void handleBallsStuck() {
    setMecanumDuty(-1);
    setRollersDuty(-1);
    intakeSubsystem.setRollerDuty(-1);
  }

  public boolean BallsArentStuckAnymore() {
    return timerForStuckBalls.isRunning() && !isBallsStuck();
  }

  private boolean shouldStartStuckBallsTimer() {
    return isBallsStuck() && !timerForStuckBalls.isRunning();
  }

  private boolean shouldHandleBallsStuck() {
    return timerForStuckBalls.hasElapsed(ShinuaConstants.BALLS_STUCK_DURATION) && !startedHandlingBalls
        && isBallsStuck();
  }

  private boolean shouldStopHandlingBallsStuck() {
    return startedHandlingBalls && timerForStuckBalls.hasElapsed(ShinuaConstants.BALLS_STUCK_HANDLING_TIME);
  }

  public void setState(ShinuaConstants.ShinuaState state) {
    this.state = state;
  }

  public ShinuaState getState() {
    return state;
  }

  @Override
  public void periodic() { // TODO deal in command, nead elses
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
