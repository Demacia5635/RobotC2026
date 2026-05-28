package frc.robot.turret;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import edu.wpi.first.math.geometry.Pose2d;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.sensors.LimitSwitchConfig;

public class TurretConstants {
    //TODO: Set the motor config and delivery points
    public final static String NAME = "Turret";
    public static final Canbus TURRET_CANBUS = Canbus.Rio;
    public static final String TURRET_NAME = "fly weel motor";
    public static final int TURRET_ID = 0;

    public static final TalonFXConfig TURRET_CONFIG= new TalonFXConfig(TURRET_ID, TURRET_CANBUS, TURRET_NAME);

    public static final double MAX_TURRET_ANGLE = 0;
    public static final double MIN_TURRET_ANGLE = 0;

    //TODO: Set the limit switch config
    public static final int MAX_LIMIT_SWITCH_ID = 0;
    public static final int MIN_LIMIT_SWITCH_ID = 0;
    public final static String MAX_LIMIT_SWITCH_NAME = "max limit swich";
    public final static String MIN_LIMIT_SWITCH_NAME = "min limit swich";

    public static final LimitSwitchConfig MIN_LIMIT_SWITCH_CONFIG= new LimitSwitchConfig(MAX_LIMIT_SWITCH_ID, MAX_LIMIT_SWITCH_NAME);
    public static final LimitSwitchConfig MAX_LIMIT_SWITCH_CONFIG= new LimitSwitchConfig(MIN_LIMIT_SWITCH_ID, MIN_LIMIT_SWITCH_NAME);

    public static final double MIN_VELOCITY = 0;
    public static final double MAX_CURRENT = 12;

    //TODO: Set the turret pose to the actaual
    public static final Pose2d TURRET_POSE = new Pose2d();
    public static final double TURRET_ANGLE = 0;

    public enum TurretStates{
        IDLE,
        TEST,
        DELIVERY,
        SHOOTING,
    }
}