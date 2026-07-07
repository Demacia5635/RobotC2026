package frc.robot.intack.subsystems;

import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.mechanisms.StateBaseMechanism;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.demacia.utils.sensors.SensorInterface;
import frc.robot.RobotContainer;
import frc.robot.intack.IntackConstants.IntackStates;
import frc.robot.intack.commands.IntackDeployCalibrationCommand;

import static frc.robot.intack.IntackConstants.*;
import static frc.robot.intack.IntackConstants.IntackDeployConstants.*;
import static frc.robot.intack.IntackConstants.RollersConstants.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intack extends StateBaseMechanism{
    private static Intack intack;

    @SuppressWarnings("unchecked")
    public Intack() {
        super(INTACK_NAME, 
        new MotorInterface[] {
            new TalonFXMotor(INTACK_DEPLOY_MOTOR_CONFIG),
            new TalonFXMotor(ROLLERS_MOTOR_CONFIG),
        }, 
        new SensorInterface[] {
            new LimitSwitch(INTACK_DEPLOY_LIMIT_SWITCH_CONFIG)
        }, 
        IntackStates.class);

        addLimit(INTACK_DEPLOY_MOTOR_NAME, INTACK_DEPLOY_MIN_ANGLE, INTACK_DEPLOY_MAX_ANGLE); 
        withPowerCommand(INTACK_DEPLOY_MOTOR_NAME, () -> RobotContainer.controller.getRightX());
        withAutoCalibration(INTACK_DEPLOY_MOTOR_NAME, () -> isAtMinLimit(), INTACK_DEPLOY_MIN_ANGLE);

        SmartDashboard.putData(INTACK_NAME + "/IntackDeploy Calibration Command", new IntackDeployCalibrationCommand(this));
        LogManager.addEntry(getName() + "/is intack ready", () -> isReady()).build();
        LogManager.addEntry(getName() + "/is intack at min", () -> isAtMinLimit()).build();
    }

    public static Intack getInstance() {
        if (intack == null) {
            intack = new Intack();
        }
        return intack;
    }

    public double distanceFromTarget() {
        return Math.abs(motors.get(INTACK_DEPLOY_MOTOR_NAME).motor.getWantedValue() - motors.get(INTACK_DEPLOY_MOTOR_NAME).motor.getCurrentPosition());
    }

    public boolean isReady() {
        return getMotor(INTACK_DEPLOY_MOTOR_NAME).getCurrentPosition() > INTACK_DEPLOY_MIDDLE_ANGLE - INTACK_DEPLOY_ALLOWED_ERROR;
    }

    public boolean isAtMinLimit() {
        return ((LimitSwitch) getSensor(INTACK_DEPLOY_LIMIT_SWICH_NAME)).get();
    }
}
