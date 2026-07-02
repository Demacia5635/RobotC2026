package frc.robot.turret.subsystems;

import static frc.robot.turret.TurretConstants.*;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.mechanisms.StateBaseMechanism;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.demacia.utils.sensors.SensorInterface;
import frc.robot.Field;
import frc.robot.RobotCommon;
import frc.robot.RobotContainer;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.TurretConstants.TurretStates;
import frc.robot.turret.commands.TurretCalibrationCommand;


public class Turret extends StateBaseMechanism{
    private static Turret turret;
    private double[] wantedTurretAngle;

    @SuppressWarnings("unchecked")
    public Turret() {
        super(TURRET_NAME, 
        new MotorInterface[] {
            new TalonFXMotor(TURRET_MOTOR_CONFIG)
        }, 
        new SensorInterface[] {
            new LimitSwitch(TURRET_LIMIT_SWITCH_CONFIG)
        }, 
        TurretStates.class);

        addLimit(TURRET_MOTOR_NAME, TURRET_MIN_ANGLE, TURRET_MAX_ANGLE); 
        withPowerCommand(TURRET_MOTOR_NAME, () -> RobotContainer.controller.getRightX());
        withAutoCalibration(TURRET_MOTOR_NAME, () -> isAtMinLimit(), TURRET_MIN_ANGLE);

        SmartDashboard.putData(TURRET_NAME + "/turret Calibration Command", new TurretCalibrationCommand(this));
        LogManager.addEntry(getName() + "/is turret ready", () -> isReady()).build();
        wantedTurretAngle = new double[1];
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
                wantedTurretAngle[0] = getTurretAngle(RobotCommon.getHubPose());
                break;
            case DELIVERY:
                wantedTurretAngle[0] = getTurretAngle(RobotCommon.getDeliveryPose());
                break;
            default:
                wantedTurretAngle[0] = 0;
                break;
        }
        return wantedTurretAngle;
    }
    
    private double shooterToPoseAngle(Translation2d point) {
        return Shooter.getInstance().shooterToPose(point).getAngle().getRadians()-(RobotCommon.getIsRed()?180:0);
    }

    private double getTurretAngle(Translation2d point) {
        return shooterToPoseAngle(point) - Chassis.getInstance().getGyroAngle().getRadians();
    }

    public boolean isReady() {
        if (state == TurretStates.DELIVERY){
            return isReady(TURRET_MOTOR_NAME, TURRET_ALLOWED_ERROR) && !isHubInTheWay(RobotCommon.getDeliveryPose());
        }
        return isReady(TURRET_MOTOR_NAME, TURRET_ALLOWED_ERROR);
    }

    public boolean isHubInTheWay(Translation2d point) {
        Translation2d shooterToHub = Shooter.getInstance().shooterToPose(RobotCommon.getHubPose());

        double hubHalfAngle = Math.asin((Field.HubRed.WIDTH / 2.0) / shooterToHub.getNorm());

        double angleDifference = Math.abs(
            Math.atan2(
                Math.sin(shooterToPoseAngle(point) - shooterToPoseAngle(RobotCommon.getHubPose())),
                Math.cos(shooterToPoseAngle(point) - shooterToPoseAngle(RobotCommon.getHubPose()))
            )
        );

        return angleDifference < hubHalfAngle;
    }

    public boolean isAtMinLimit() {
        return ((LimitSwitch) getSensor(TURRET_LIMIT_SWICH_NAME)).get();
    }
}
