package frc.robot.intack.commands;

import static frc.robot.intack.IntackConstants.IntackDeployConstants.*;

import frc.demacia.utils.mechanisms.DefaultCommand;
import frc.demacia.utils.motors.MotorInterface.ControlMode;
import frc.robot.intack.IntackConstants.IntackStates;
import frc.robot.intack.subsystems.Intack;

public class IntackCommand extends DefaultCommand{
    
    public IntackCommand() {
        super(Intack.getInstance(), new ControlMode[] {
            ControlMode.ANGLE,
            ControlMode.DUTYCYCLE
        });
    }

    @Override
    public void execute() {
        if (Intack.getInstance().getState().equals(IntackStates.INTAKING) && Intack.getInstance().isReady(INTACK_DEPLOY_MOTOR_NAME ,INTACK_DEPLOY_ALLOWED_ERROR)){
            Intack.getInstance().setPower(INTACK_DEPLOY_MOTOR_NAME, 
                0.1 + Intack.getInstance().distanceFromTarget() * 0.02);
            controls[1].run();
        } else if (Intack.getInstance().getState().equals(IntackStates.CLOSED) && Intack.getInstance().isReady(INTACK_DEPLOY_MOTOR_NAME ,INTACK_DEPLOY_ALLOWED_ERROR)) {
            Intack.getInstance().stop(INTACK_DEPLOY_MOTOR_NAME);
            controls[1].run();
        } else {
            super.execute();
        }
    }
}
