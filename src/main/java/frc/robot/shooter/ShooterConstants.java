package frc.robot.shooter;

import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.LookUpTable;
import frc.demacia.utils.mechanisms.StateBaseMechanism.MechanismState;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.sensors.LimitSwitchConfig;
import frc.robot.Field;
import frc.robot.shooter.subsystems.Shooter;

public class ShooterConstants {
    public static final String SHOOTER_NAME = "shooter";
    public static final Translation2d SHOOTER_OFFSET = new Translation2d(-0.115, 0);
    public static final double FUTURE_TIME = 0.04;

    public class FlywheelConstants {
        public static final double FLYWHEEL_DELIVERY_KP = 2;
        public static final double FLYWHEEL_ALLOWED_ERROR = 1;
        public static final double FLYWHEEL_DELIVERY_ALLOWED_ERROR = 1;

        public static final String FLYWHEEL_MOTOR_NAME = "flywheel motor";
        public static final int FLYWHEEL_MOTOR_ID = 30;
        public static final Canbus FLYWHEEL_MOTOR_CANBUS = Canbus.Rio;
        public static final boolean FLYWHEEL_IS_BRAKE = false;
        public static final boolean FLYWHEEL_IS_INVERT = true;
        public static final double FLYWHEEL_GEAR_RATIO = 1;
        public static final double FLYWHEEL_DIAMETER = 1;
        public static final double KP = 40;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double KS = 0.1;
        public static final double KV = 2.7;
        public static final double KA = 0.15;
        public static final double KG = 0;
        public static final double KV2 = 0.00162;
        public static final double FLYWHEEL_RAMP_TIME = 0.6;
        public static final TalonFXConfig FLYWHEEL_MOTOR_CONFIG = new TalonFXConfig(FLYWHEEL_MOTOR_ID, FLYWHEEL_MOTOR_CANBUS, FLYWHEEL_MOTOR_NAME)
        .withBrake(FLYWHEEL_IS_BRAKE)
        .withInvert(FLYWHEEL_IS_INVERT)
        .withMeterMotor(FLYWHEEL_GEAR_RATIO, FLYWHEEL_DIAMETER)
        .withPID(KP, KI, KD, KS, KV, KA, KG)
        .withFeedForward(KV2, 0)
        .withRampTime(FLYWHEEL_RAMP_TIME);

    }

    public class HoodConstants {
        public static final double HOOD_MIN_ANGLE = Math.toRadians(0);
        public static final double HOOD_MAX_ANGLE = Math.toRadians(60);
        public static final double HOOD_MAX_LOWERING_TIME = 0.5;
        public static final double HOOD_MAX_CURRENT = 10;
        public static final double HOOD_CALIBRATION_POWER = -0.2;
        public static final double HOOD_ALLOWED_ERROR = Math.toRadians(4);

        public static final String HOOD_MOTOR_NAME = "hood motor";
        public static final int HOOD_MOTOR_ID = 34;
        public static final Canbus HOOD_MOTOR_CANBUS = Canbus.Rio;
        public static final boolean HOOD_IS_BRAKE = true;
        public static final boolean HOOD_IS_INVERT = true;
        public static final double HOOD_GEAR_RATIO = 72;
        public static final double KP = 60;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double KS = 0.06;
        public static final double KV = 1.05;
        public static final double KA = 0.05;
        public static final double KG = 0;
        public static final double HOOD_MAX_VELOCITY = 6;
        public static final double HOOD_MAX_ACCELERATION = 30;
        public static final double HOOD_MAX_JERK = 240;
        public static final TalonFXConfig HOOD_MOTOR_CONFIG = new TalonFXConfig(HOOD_MOTOR_ID, HOOD_MOTOR_CANBUS, HOOD_MOTOR_NAME)
        .withBrake(HOOD_IS_BRAKE)
        .withInvert(HOOD_IS_INVERT)
        .withRadiansMotor(HOOD_GEAR_RATIO)
        .withPID(KP, KI, KD, KS, KV, KA, KG)
        .withMotionParam(HOOD_MAX_VELOCITY, HOOD_MAX_ACCELERATION, HOOD_MAX_JERK);

        public static final String HOOD_LIMIT_SWICH_NAME = "hood limit swich";
        public static final int HOOD_LIMIT_SWICH_CHANNEL = 5;
        public static final LimitSwitchConfig HOOD_LIMIT_SWITCH_CONFIG = new LimitSwitchConfig(HOOD_LIMIT_SWICH_CHANNEL, HOOD_LIMIT_SWICH_NAME);  
    }

    public final static class FeederConstants {
        public static final double FEEDER_MAX_POWER = 1;

        public static final String FEEDER_NAME = "feeder motor";
        public static final int  FEEDER_ID = 33;
        public static final Canbus FEEDER_CANBUS = Canbus.Rio;
        public static final boolean FEEDER_IS_BRAKE = true;
        public static final boolean FEEDER_IS_INVERT = true;
        public static final TalonFXConfig FEEDER_MOTOR_CONFIG = new TalonFXConfig(FEEDER_ID, FEEDER_CANBUS, FEEDER_NAME)
        .withBrake(FEEDER_IS_BRAKE)
        .withInvert(FEEDER_IS_INVERT);
    }

    public static enum ShooterStates implements MechanismState {
        SHOOTING,
        DELIVERY;

    @Override
    public double[] getValues() {
        return Shooter.getInstance().getShooterValues();
    }}

    public final static LookUpTable LOOK_UP_TABLE = new LookUpTable(2);
    static{
        LOOK_UP_TABLE.add(0.93, 9, Math.toRadians(20));
        LOOK_UP_TABLE.add(2.16, 9.5,Math.toRadians(30));
        LOOK_UP_TABLE.add(2.43,9.5,Math.toRadians(40));
    }

    public static final double SAFE_MARGIN_Y = 0.8;
    public static final Translation2d DELIVERY_RED_LEFT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_RED_X + Field.Zones.CENTER_LINE_X * 0.25,
            SAFE_MARGIN_Y
        );

    public static final Translation2d DELIVERY_RED_RIGHT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_RED_X + Field.Zones.CENTER_LINE_X * 0.25,
            Field.FieldDimensions.WIDTH - SAFE_MARGIN_Y
        );

    public static final Translation2d DELIVERY_BLUE_LEFT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_BLUE_X - Field.Zones.CENTER_LINE_X * 0.25,
            SAFE_MARGIN_Y
        );

    public static final Translation2d DELIVERY_BLUE_RIGHT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_BLUE_X - Field.Zones.CENTER_LINE_X * 0.25,
            Field.FieldDimensions.WIDTH - SAFE_MARGIN_Y
        );
}
