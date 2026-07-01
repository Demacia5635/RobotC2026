package frc.robot.shinua.subsystems;

import static frc.robot.shinua.ShinuaConstants.*;
import static frc.robot.shinua.ShinuaConstants.ShinuaRollersConstants.*;

import frc.demacia.utils.mechanisms.StateBaseMechanism;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.SensorInterface;

import static frc.robot.shinua.ShinuaConstants.MechanomConstants.*;

public class Shinua extends StateBaseMechanism{
    private static Shinua shinua;

    public Shinua() {
        super(SHINUA_NAME, 
        new MotorInterface[] {
            new TalonFXMotor(SHINUA_ROLLERS_MOTOR_CONFIG),
            new TalonFXMotor(MECHANOM_MOTOR_CONFIG),
        }, 
        new SensorInterface[] {
            
        }, 
        ShinuaStates.class);
    }

    public static Shinua getInstance() {
        if (shinua == null) {
            shinua = new Shinua();
        }
        return shinua;
    }
}
