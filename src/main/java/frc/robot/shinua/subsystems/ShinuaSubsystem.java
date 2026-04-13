// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shinua.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.shinua.ShinuaConstants;


public class ShinuaSubsystem extends SubsystemBase {
   private TalonFXMotor shinuaMotor;
   private ShinuaConstants.ShinuaState state;
  
  /** Creates a new ShinuaSubsystem. */
  public ShinuaSubsystem() {
   shinuaMotor = new TalonFXMotor(ShinuaConstants.SHINUA_CONFIG);
  }

  public void checkElectronics() {
    shinuaMotor.checkElectronics();
  }
  public void setNeutralModeShinua(boolean isBrake) {
    shinuaMotor.setNeutralMode(isBrake);
  }
  public void setShinuaDuty(double duty) {
    shinuaMotor.setDuty(duty);
  }
  public void stopShinua() {
    shinuaMotor.stop();
  }
  public double getShinuaVelocity() {
    return shinuaMotor.getCurrentVelocity();
  }
  public double getShinuaCurrent() {
    return shinuaMotor.getCurrentCurrent();
  }
  public void setState(ShinuaConstants.ShinuaState state) {
    this.state = state;
  }
  public ShinuaConstants.ShinuaState getState() {
    return state;
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
