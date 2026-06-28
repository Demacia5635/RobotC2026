package frc.robot.turret;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.robot.RobotCommon;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.demacia.utils.motors.TalonFXConfig;

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

    public static final double MAX_TURRET_ANGLE = 27;
    public static final double MIN_TURRET_ANGLE = -313;
    public static final double TURRET_ANGLE_RANGE = MAX_TURRET_ANGLE + Math.abs(MIN_TURRET_ANGLE);

    //TODO: Set the limit switch config
    public static final int MAX_LIMIT_SWITCH_ID = 8;
    // public static final int MIN_LIMIT_SWITCH_ID = 0;
    public final static String MAX_LIMIT_SWITCH_NAME = "max limit swich";
    // public final static String MIN_LIMIT_SWITCH_NAME = "min limit swich";

    // public static final LimitSwitchConfig MIN_LIMIT_SWITCH_CONFIG= new LimitSwitchConfig(MAX_LIMIT_SWITCH_ID, MAX_LIMIT_SWITCH_NAME);
    // public static final DigitalInput MAX_LIMIT_SWITCH_CONFIG= new DigitalInput(MAX_LIMIT_SWITCH_ID);

    public static final double MIN_VELOCITY = 0;
    public static final double MAX_CURRENT = 12;

    public static final Pose2d TURRET_POSE_ON_ROBOT = new Pose2d(0.24, 0, new Rotation2d(90));

    //TODO: Set the turret pose to the actaual
    public static final Translation2d TURRET_POSE = RobotCommon.currentRobotPose.getTranslation().minus(TURRET_POSE_ON_ROBOT.getTranslation());

    public enum TurretStates{
        IDLE,
        TEST,
        DELIVERY,
        SHOOTING,
        ONEPOINT,
        TRENCH_LEFT, 
        TRENCH_RIGHT;
    }
}