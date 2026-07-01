package frc.robot.turret.commands;

import frc.demacia.utils.mechanisms.CalibrationCommand;
import static frc.robot.turret.TurretConstants.*;
import frc.robot.turret.subsystems.Turret;

public class TurretCalibrationCommand extends CalibrationCommand{

    public TurretCalibrationCommand(Turret turret) {
        super(
            turret, 
            TURRET_MOTOR_NAME, 
            TURRET_CALIBRATION_POWER, 
            () -> turret.isAtMinLimit(), 
            TURRET_MIN_ANGLE);
    }
}
