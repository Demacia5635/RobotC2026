package frc.robot.intack.commands;

import frc.demacia.utils.mechanisms.CalibrationCommand;
import static frc.robot.intack.IntackConstants.IntackDeployConstants.*;
import frc.robot.intack.subsystems.Intack;

public class IntackDeployCalibrationCommand extends CalibrationCommand{

    public IntackDeployCalibrationCommand(Intack intack) {
        super(
            intack, 
            INTACK_DEPLOY_MOTOR_NAME, 
            INTACK_DEPLOY_CALIBRATION_POWER, 
            () -> intack.isAtMinLimit(), 
            INTACK_DEPLOY_MIN_ANGLE);
    }
}
