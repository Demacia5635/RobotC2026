package frc.robot.shooter.commands;

import frc.demacia.utils.mechanisms.CalibrationCommand;
import static frc.robot.shooter.ShooterConstants.HoodConstants.*;
import frc.robot.shooter.subsystems.Shooter;

public class HoodCalibrationCommand extends CalibrationCommand{

    public HoodCalibrationCommand(Shooter shooter) {
        super(
            shooter, 
            HOOD_MOTOR_NAME, 
            HOOD_CALIBRATION_POWER, 
            () -> shooter.isAtMinLimit(), 
            HOOD_MIN_ANGLE);
    }
}
