package frc.robot.turret;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.sensors.LimitSwitchConfig;

public class TurretConstants {
    //TODO: Set the motor config and delivery points
    public final static String NAME = "Turret";
    public static final Canbus TURRET_CANBUS = Canbus.Rio;
    public static final String TURRET_NAME = "turret motor";
    public static final int TURRET_ID = 20;
    public static final double kp = 40;
    public static final double ki = 0;
    public static final double kd = 0;
    public static final double ks = 0.1;
    public static final double kv = 2.7;
    public static final double ka = 0.15;
    public static final double kg = 0;
    public static final double TURRET_GEAR_RASIO = 48.0*112.0/27.0;

    public static final TalonFXConfig TURRET_CONFIG= new TalonFXConfig(TURRET_ID, TURRET_CANBUS, TURRET_NAME)
    .withPID(kp, ki, kd, ks, kv, ka, 0)
    .withMotionParam(4, 16, 30)
    .withRadiansMotor(TURRET_GEAR_RASIO)
    .withBrake(false);

    public static final double MAX_TURRET_ANGLE = 340;
    public static final double MIN_TURRET_ANGLE = 0;

    //TODO: Set the limit switch config
    public static final int MAX_LIMIT_SWITCH_ID = 7;
    // public static final int MIN_LIMIT_SWITCH_ID = 0;
    public final static String MAX_LIMIT_SWITCH_NAME = "max limit swich";
    // public final static String MIN_LIMIT_SWITCH_NAME = "min limit swich";

    // public static final LimitSwitchConfig MIN_LIMIT_SWITCH_CONFIG= new LimitSwitchConfig(MAX_LIMIT_SWITCH_ID, MAX_LIMIT_SWITCH_NAME);
    public static final DigitalInput MAX_LIMIT_SWITCH_CONFIG= new DigitalInput(MAX_LIMIT_SWITCH_ID);

    public static final double MIN_VELOCITY = 0;
    public static final double MAX_CURRENT = 12;

    //TODO: Set the turret pose to the actaual
    public static final Pose2d TURRET_POSE = new Pose2d();

    public enum TurretStates{
        IDLE,
        TEST,
        DELIVERY,
        SHOOTING,
    }
}