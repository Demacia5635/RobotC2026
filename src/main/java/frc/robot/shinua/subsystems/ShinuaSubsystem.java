// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.shinua.ShinuaConstants;
import frc.robot.shinua.ShinuaConstants.ShinuaState;

public class ShinuaSubsystem extends SubsystemBase {
  private TalonFXMotor mecanumMotor;
  private TalonFXMotor rollersMotor;
  private ShinuaState state;
  private static ShinuaSubsystem instance;

  public static ShinuaSubsystem getInstance() {
    if (instance == null)
      instance = new ShinuaSubsystem();
    return instance;
  }

  /** Creates a new ShinuaSubsystem. */
  private ShinuaSubsystem() {// TODO call super
    mecanumMotor = new TalonFXMotor(ShinuaConstants.MECANUM_CONFIG);
    rollersMotor = new TalonFXMotor(ShinuaConstants.ROLLERS_CONFIG);
    state = ShinuaState.SHINUA_OFF;
    addNT();
  }

  public void addNT() {
    SendableChooser<ShinuaState> stateChooser = new SendableChooser<>();
    for (ShinuaState shinuaState : ShinuaState.values()) {
      stateChooser.addOption(shinuaState.name(), shinuaState);
    }
    stateChooser.onChange(newState -> this.state = newState);
    SmartDashboard.putData( " Intake State Chooser", stateChooser);

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

  public void setState(ShinuaConstants.ShinuaState state) {
    this.state = state;
  }

  public ShinuaState getState() {
    return state;
  }

  @Override
  public void periodic() { 
    // This method will be called once per scheduler run
  }
}
