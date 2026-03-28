// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.intake;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;

/** Add your docs here. */
public class IntakeConstants {

    public static final int INTAKE_ID = 0;
    public static final Canbus INTAKE_CANBUS = Canbus.Rio;
    public static final String INTAKE_NAME = "intake";

    public static final TalonFXConfig INTAKE_CONFIG = new TalonFXConfig(INTAKE_ID, INTAKE_CANBUS, INTAKE_NAME);
}
