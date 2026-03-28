// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.intake.IntakeConstants;

public class Intake extends SubsystemBase {
  //TODO: Check if the motor is TalonFX
  private TalonFXMotor intakeMotor;
  public Intake() {
    intakeMotor = new TalonFXMotor(IntakeConstants.INTAKE_CONFIG);
  }

  public void setIntakePower(double power){
    intakeMotor.setDuty(power);
  }
}
