package frc.robot.shinua.commands;

import frc.demacia.utils.mechanisms.DefaultCommand;
import frc.demacia.utils.motors.MotorInterface.ControlMode;
import frc.robot.intack.subsystems.Intack;
import frc.robot.shinua.subsystems.Shinua;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.subsystems.Turret;

import static frc.robot.shinua.ShinuaConstants.MechanomConstants.*;

public class ShinuaCommand extends DefaultCommand{
    
    public ShinuaCommand() {
        super(Shinua.getInstance(), new ControlMode[] {
            ControlMode.DUTYCYCLE,
            ControlMode.DUTYCYCLE
        });
    }

    @Override
    public void execute() {
        if (Shinua.getInstance().getState() == Shinua.getInstance().IDLE_STATE || (Shooter.getInstance().isReady() && Turret.getInstance().isReady() && Intack.getInstance().isReady())){
            super.execute();
        } else {
            controls[0].run();
            Shinua.getInstance().stop(MECHANOM_MOTOR_NAME);
        }
    }
}
