// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class SubsystemIntake extends SubsystemBase {
  /** Creates a new SubsystemIntake. */
  private TalonFXConfig configIntake;
  private TalonFXMotor intakeMotor; 
  public SubsystemIntake() {
     configIntake = new TalonFXConfig(31, Canbus.Rio, "intakeMotor");
     intakeMotor = new TalonFXMotor(configIntake);
  }
  public void setIntakeDuty(double duty) {
      intakeMotor.setDuty(duty);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
