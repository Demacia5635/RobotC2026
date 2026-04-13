// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.IntakeConstants.IntakeState;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new IntakeSubsytem. */
  private static IntakeSubsystem instance;
  private TalonFXMotor rollerMotor;
  private TalonFXMotor intakeDeployMotor;
  private IntakeState state;

  public static IntakeSubsystem getInstance() {
    if (instance == null)
      instance = new IntakeSubsystem();
    return instance;
  }
  public IntakeSubsystem() {
    rollerMotor = new TalonFXMotor(IntakeConstants.ROLLER_CONFIG);
    intakeDeployMotor = new TalonFXMotor(IntakeConstants.INTAKE_DEPLOY_CONFIG);
    state = IntakeState.IDLE;
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
    intakeDeployMotor.setAngle(angle);
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

  public double getIntakeDeployCurrent() {
    return intakeDeployMotor.getCurrentCurrent();
  }

  public IntakeState getState() {
    return state;
  }
  public double getVelocity() {
    return rollerMotor.getCurrentVelocity();
  }

  public void setState(IntakeState newState) {
    state = newState;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
