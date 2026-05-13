// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class subsystem extends SubsystemBase {
  /** Creates a new subsystem. */
  private TalonFXMotor motorRoler;
  private TalonFXMotor motorMecanum;

  public subsystem() {
    TalonFXConfig configRoler = new TalonFXConfig(34, Canbus.Rio, "motor1");
    TalonFXConfig configMecanum = new TalonFXConfig(30, Canbus.Rio, "motor2");
    motorRoler = new TalonFXMotor(configRoler);
    motorMecanum = new TalonFXMotor(configMecanum);
  }

  public TalonFXMotor getMotorRoler() {
    return motorRoler;
  }

  public TalonFXMotor getMotorMecanum() {
    return motorMecanum;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
