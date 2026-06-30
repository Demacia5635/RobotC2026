package frc.robot.turret.subsystems;

import static frc.robot.turret.TurretConstants.*;

import frc.demacia.utils.mechanisms.StateBaseMechanism;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.demacia.utils.sensors.SensorInterface;
import frc.robot.RobotContainer;
import frc.robot.turret.TurretConstants.TurretStates;


public class Turret extends StateBaseMechanism{
    private static Turret turret;
    private double wantedTurretAngle;

    public Turret() {
        super(TURRET_NAME, 
        new MotorInterface[] {
            new TalonFXMotor(TURRET_MOTOR_CONFIG)
        }, 
        new SensorInterface[] {
            new LimitSwitch(TURRET_LIMIT_SWITCH)
        }, 
        TurretStates.class);

        addLimit(TURRET_MOTOR_NAME, TURRET_MIN_ANGLE, TURRET_MAX_ANGLE); 
        withPowerCommand(() -> RobotContainer.controller.getRightX());
    }

    public static Turret getInstance() {
        if (turret == null) {
            turret = new Turret();
        }
        return turret;
    }

    public double[] getTurretAngle() {
        switch ((TurretStates) state) {
            case SHOOTING:
                break;
            case DELIVERY:
                break;
            default:
                wantedTurretAngle = 0;
                break;
        }
        return new double[] {wantedTurretAngle};
    }


}
