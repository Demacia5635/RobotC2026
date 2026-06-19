package frc.robot.shooter;


import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.LookUpTable;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.demacia.utils.sensors.LimitSwitchConfig;

public class ShooterConstants {

    public final static String NAME = "Shooter";
    public final static double HEIGHT = 4;
    public final static Translation2d DELIVERY_RIGHT_POINT = Translation2d.kZero;
    public final static Translation2d DELIVERY_LEFT_POINT = Translation2d.kZero;
    public final static LookUpTable LOOK_UP_TABLE = new LookUpTable(3); //distance: velocity, angel
    public static final Translation2d HUB = Translation2d.kZero;
    static{
        LOOK_UP_TABLE.add(0, 0, 0, 0);
    }


    public final static class FlywheelConstants {
        //TODO: Set the motor config
        public static final Canbus FLYWEEL_CANBUS = Canbus.Rio;
        public static final String FLYWEEL_NAME = "fly wheel motor";
        public static final int FLYWEEL_ID = 30;

        public static final TalonFXConfig FLYWHEEL_CONFIG = new TalonFXConfig(FLYWEEL_ID, FLYWEEL_CANBUS, FLYWEEL_NAME)
        .withInvert(true)
        .withBrake(false)
        .withMeterMotor(1,2*0.0254)
        .withPID(4.39, 0, 0, 0.07, 0.8, 0.26, 0);
        public static final double MAX_FLYWHEEL_POWER = 1;
        public static final double FLYWHEEL_VELOCITY_OFFSET = 0.4;
        public static double MAX_FLYWHEEL_ACCEL = 8.5; //m/s^2
    }
    

    public final static class HoodConstants {
        //TODO: Set the motor config
        public static final Canbus HOOD_CANBUS = Canbus.Rio;
        public static final String HOOD_NAME = "hood motor";
        public static final int HOOD_ID = 50;

        public static final TalonFXConfig HOOD_CONFIG = new TalonFXConfig(HOOD_ID, HOOD_CANBUS, HOOD_NAME)
        .withInvert(true)
        .withRadiansMotor(72)
        .withPID(60, 6, 0, 0.0561,1.0495 , 0.0501, 0)
        .withMotionParam(6, 30, 240);

        public static final double HOOD_POSITION_OFFSET = 0.4;
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = Math.toRadians(60);
        public static final double MAX_HOOD_CURRENT = 0;
        public static final double MIN_HOOD_VELOCITY = 0;

        public static final String LIMET_SWITCH_NAME = "hood limit switch";
        public static final int LIMET_SWITCH_CHANEL = 8;

        public static final LimitSwitchConfig LIMIT_SWITCH_CONFIG_HOOD= new LimitSwitchConfig(LIMET_SWITCH_CHANEL, LIMET_SWITCH_NAME);

        public static final double HOOD_LIMET_SWITCH_POSE = 0;
    }

        public final static class FeederConstants {
        //TODO: Set the motor config
        public static final Canbus FEEDER_CANBUS = Canbus.Rio;
        public static final String FEEDER_NAME = "feeder motor";
        public static final int  FEEDER_ID = 3;
        
        public static final TalonFXConfig FEEDER_CONFIG = new TalonFXConfig(FEEDER_ID, FEEDER_CANBUS, FEEDER_NAME).withInvert(true);
        public static final double MAX_FEEDER_POWER = 1;
        public static final double MAX_FEEDER_CURRENT = 0;
        public static final double MIN_FEEDER_VELOCITY = 0;
    }
    public enum ShooterStates {
        SHOOTER,
        IDLE,
        TEST,
        DELIVERY,
        TRANCH
    }
}
