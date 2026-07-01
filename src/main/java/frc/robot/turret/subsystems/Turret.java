package frc.robot.turret.subsystems;

import static frc.robot.turret.TurretConstants.*;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.mechanisms.StateBaseMechanism;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.demacia.utils.sensors.SensorInterface;
import frc.robot.Field;
import frc.robot.RobotCommon;
import frc.robot.RobotContainer;
import frc.robot.Field.DELIVERY;
import frc.robot.shooter.ShooterConstants;
import frc.robot.turret.TurretConstants;
import frc.robot.turret.TurretConstants.TurretStates;
import frc.robot.turret.commands.TurretCalibrationCommand;


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

        SmartDashboard.putData("turret Calibration Command", new TurretCalibrationCommand(this));
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
                double shooterToHubAngle = shooterToPoseAngle(RobotCommon.getHubPose());
                wantedTurretAngle = shooterToHubAngle - Chassis.getInstance().getGyroAngle().getRadians();
                break;
            case DELIVERY:
                double shooterToDeliveryAngle = shooterToPoseAngle(RobotCommon.getDeliveryPose());
                wantedTurretAngle = shooterToDeliveryAngle - Chassis.getInstance().getGyroAngle().getRadians();
                break;
            default:
                wantedTurretAngle = 0;
                break;
        }
        return new double[] {wantedTurretAngle};
    }

    private Translation2d shooterToPose(Translation2d point) {
        return point.minus(Chassis.getInstance().getFuturePose(ShooterConstants.FUTURE_TIME).getTranslation().plus(ShooterConstants.SHOOTER_OFFSET.rotateBy(Chassis.getInstance().getGyroAngle())));
    }
    
    private double shooterToPoseAngle(Translation2d point) {
        return shooterToPose(point).getAngle().getRadians()-(RobotCommon.isRed()?180:0);
    } // TODO move to shooter

    public boolean isReady() {
        if (((TurretStates) state).equals(TurretStates.DELIVERY)){
            return isReady(TurretConstants.TURRET_ALLOWED_ERROR) && !isHubInTheWay(RobotCommon.getDeliveryPose());
        }
        return isReady(TurretConstants.TURRET_ALLOWED_ERROR);
    }

    public boolean isHubInTheWay(Translation2d point) {
        Translation2d shooterToHub = shooterToPose(RobotCommon.getHubPose());

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
