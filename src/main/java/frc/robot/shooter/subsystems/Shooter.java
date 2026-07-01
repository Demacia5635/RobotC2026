package frc.robot.shooter.subsystems;

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
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.shooter.commands.HoodCalibrationCommand;

import static frc.robot.shooter.ShooterConstants.*;
import static frc.robot.shooter.ShooterConstants.FlywheelConstants.*;
import static frc.robot.shooter.ShooterConstants.hoodConstants.*;

public class Shooter extends StateBaseMechanism{
    private static Shooter shooter;
    private double wantedFlywheelVelocity;
    private double wantedHoodAngle;

    @SuppressWarnings("unchecked")
    public Shooter() {
        super(SHOOTER_NAME, 
        new MotorInterface[] {
            new TalonFXMotor(FLYWHEEL_MOTOR_CONFIG),
            new TalonFXMotor(HOOD_MOTOR_CONFIG)
        }, 
        new SensorInterface[] {
            new LimitSwitch(HOOD_LIMIT_SWITCH_CONFIG)
        }, 
        ShooterStates.class);

        addLimit(HOOD_MOTOR_NAME, HOOD_MIN_ANGLE, HOOD_MAX_ANGLE); 
        withPowerCommand(() -> RobotContainer.controller.getRightX());
        withOutoCalibration(HOOD_MOTOR_NAME, () -> isAtMinLimit(), HOOD_MIN_ANGLE);

        SmartDashboard.putData(SHOOTER_NAME + "/hood Calibration Command", new HoodCalibrationCommand(this));
        LogManager.addEntry(getName() + "/distence from hub", () -> getHubDistacse())
        .build();
    }

    public static Shooter getInstance() {
        if (shooter == null) {
            shooter = new Shooter();
        }
        return shooter;
    }

    public Translation2d shooterToPose(Translation2d point) {
        return point.minus(Chassis.getInstance().getFuturePose(FUTURE_TIME).getTranslation().plus(SHOOTER_OFFSET.rotateBy(Chassis.getInstance().getGyroAngle())));
    }

    public double getHubDistacse() {
        return shooterToPose(RobotCommon.getHubPose()).getNorm();
    }

    public boolean shuoldLowerHood() {
        Translation2d p = Chassis.getInstance()
                .getFuturePose(HOOD_MAX_LOWERING_TIME)
                .getTranslation();
    
        double x = p.getX();
        double y = p.getY();
    
        double fieldW = Field.FieldDimensions.WIDTH;
    
        boolean inTrenchX =
                Math.abs(x - Field.TrenchRedScoring.X_CENTER) <= Field.TrenchRedScoring.WIDTH / 2.0 ||
                Math.abs(x - Field.TrenchBlueScoring.X_CENTER) <= Field.TrenchBlueScoring.WIDTH / 2.0;
    
        boolean inTrenchY =
                y <= Field.TrenchRedScoring.DEPTH + 0.3 ||
                y >= fieldW - Field.TrenchRedAudience.DEPTH - 0.3;
    
        return inTrenchX && inTrenchY;
    }

    public double[] getShooterValues() {
        switch ((ShooterStates) state) {
            case SHOOTING:
                double[] LookUpTableValues = LOOK_UP_TABLE.get(getHubDistacse());
                wantedFlywheelVelocity = LookUpTableValues[0];
                wantedHoodAngle = LookUpTableValues[1];
                break;
            case DELIVERY:
                wantedFlywheelVelocity = shooterToPose(RobotCommon.getDeliveryPose()).getNorm() * FLYWHEEL_DELIVERY_KP;
                wantedHoodAngle = Math.toRadians(45);
                break;
            default:
                wantedFlywheelVelocity = 0;
                wantedHoodAngle = 0;
                break;
        }
        if (shuoldLowerHood()) {
            wantedHoodAngle = 0;
        }
        return new double[] {wantedFlywheelVelocity, wantedHoodAngle};
    }

    public boolean isReady() {
        return isReady(new double[] {
            ((ShooterStates) state).equals(ShooterStates.DELIVERY) ? 
            FLYWHEEL_DELIVERY_ALLOWED_ERROR : 
            FLYWHEEL_ALLOWED_ERROR, 
            HOOD_ALLOWED_ERROR}) && 
            getMotor(FLYWHEEL_MOTOR_NAME).getCurrentVelocity() > FLYWHEEL_ALLOWED_ERROR &&
            getMotor(HOOD_MOTOR_NAME).getCurrentVelocity() > HOOD_ALLOWED_ERROR;
    }

    public boolean isAtMinLimit() {
        return ((LimitSwitch) getSensor(HOOD_LIMIT_SWICH_NAME)).get() || (getMotor(FLYWHEEL_MOTOR_NAME).getCurrentCurrent() > FLYWHEEL_MAX_CURRENT && !getIsCalibration());
    }
}
