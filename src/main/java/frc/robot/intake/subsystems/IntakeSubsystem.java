// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.IntakeConstants.IntakeState;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsytem. */
  private static IntakeSubsystem instance;
  private TalonFXMotor rollerMotor;
  private TalonFXMotor intakeDeployMotor;
  private LimitSwitch intakeDeployLimitSwitch;
  private IntakeState state;
  private boolean isCalibrated;

  public static IntakeSubsystem getInstance() {
    if (instance == null)
      instance = new IntakeSubsystem();
    return instance;
  }

  private IntakeSubsystem() {// TODO call super,
    rollerMotor = new TalonFXMotor(IntakeConstants.ROLLER_CONFIG);
    intakeDeployMotor = new TalonFXMotor(IntakeConstants.INTAKE_DEPLOY_CONFIG);
    intakeDeployLimitSwitch = new LimitSwitch(IntakeConstants.INTAKE_DEPLOY_LIMIT_SWITCH);
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
    SmartDashboard.putData(" Intake State Chooser", stateChooser);
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

  public void setIntakeDeployDuty(double duty) {
    intakeDeployMotor.setDuty(duty);
  }

  public void setAngleIntakeDeploy(double angle) {
    angle = MathUtil.clamp(angle, IntakeConstants.DEPLOY_CLOSED_ANGLE, IntakeConstants.DEPLOY_OPEN_ANGLE);
    intakeDeployMotor.setMotion(angle);
  }

  public void setEncoderIntakeDeploy(double angle) {
    intakeDeployMotor.setEncoderPosition(angle);
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

  public double getIntakeDeployAngle() {
    return intakeDeployMotor.getCurrentPosition();
  }

  public IntakeState getState() {
    return state;
  }

  public void setState(IntakeState newState) {
    state = newState;
  }

  public boolean isIntakeDeployClosed() {
    return intakeDeployLimitSwitch.get();
  }

  public boolean isCalibrated() {
    return isCalibrated;
  }

  public void setCalibrated() {
    isCalibrated = true;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}