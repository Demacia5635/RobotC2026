package frc.robot.shooter;


import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.LookUpTable;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.sensors.LimitSwitchConfig;
import frc.robot.Field;

public class ShooterConstants {

    public final static String NAME = "Shooter";
    public final static double HEIGHT = 4;
    public final static Translation2d DELIVERY_RIGHT_POINT = Translation2d.kZero;
    public final static Translation2d DELIVERY_LEFT_POINT = Translation2d.kZero;
    public final static LookUpTable LOOK_UP_TABLE = new LookUpTable(2); //distance: velocity, angel
    public static final Translation2d HUB = Translation2d.kZero;
    static{
        LOOK_UP_TABLE.add(0.93, 9, Math.toRadians(20));
        LOOK_UP_TABLE.add(2.16, 9.5,Math.toRadians(30));
        LOOK_UP_TABLE.add(2.43,9.5,Math.toRadians(40));
        LOOK_UP_TABLE.add(3.16,9.9,Math.toRadians(50));//TODO not good point
        LOOK_UP_TABLE.add(3.16,9.9,Math.toRadians(50));//TODO not good point
    }


    public final static class FlywheelConstants {
        //TODO: Set the motor config
        public static final Canbus FLYWEEL_CANBUS = Canbus.Rio;
        public static final String FLYWEEL_NAME = "fly wheel motor";
        public static final int FLYWEEL_ID = 30;

        public static final TalonFXConfig FLYWHEEL_CONFIG = new TalonFXConfig(FLYWEEL_ID, FLYWEEL_CANBUS, FLYWEEL_NAME)
        .withInvert(true)
        .withBrake(false)
        .withMeterMotor(1, 2*0.0254)
        .withPID(5, 0, 0, 0.577, 0.69, 0.13, 0)
        .withFeedForward(0.00162, 0)
        .withRampTime(0.6);
        public static final double MAX_FLYWHEEL_POWER = 1;
        public static final double FLYWHEEL_VELOCITY_OFFSET = 1;
        public static double MAX_FLYWHEEL_ACCEL = 8.5; //m/s^2
    }
    

    public final static class HoodConstants {
        //TODO: Set the motor config
        public static final Canbus HOOD_CANBUS = Canbus.Rio;
        public static final String HOOD_NAME = "hood motor";
        public static final int HOOD_ID = 34;

        public static final TalonFXConfig HOOD_CONFIG = new TalonFXConfig(HOOD_ID, HOOD_CANBUS, HOOD_NAME)
        .withInvert(true)
        .withRadiansMotor(72)
        .withPID(60, 6, 0, 0.0561,1.0495 , 0.0501, 0)
        .withMotionParam(6, 30, 240);

        public static final double HOOD_POSITION_OFFSET = Math.toRadians(3);
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = Math.toRadians(60);
        public static final double MAX_HOOD_CURRENT = 0;
        public static final double MIN_HOOD_VELOCITY = 0;

        public static final String LIMET_SWshITCH_NAME = "hood limit switch";
        public static final int LIMET_SWITCH_CHANEL = 5;

        public static final LimitSwitchConfig LIMIT_SWITCH_CONFIG_HOOD= new LimitSwitchConfig(LIMET_SWITCH_CHANEL, LIMET_SWshITCH_NAME);

        public static final double HOOD_LIMET_SWITCH_POSE = 0;
    }

        public final static class FeederConstants {
        //TODO: Set the motor config
        public static final Canbus FEEDER_CANBUS = Canbus.Rio;
        public static final String FEEDER_NAME = "feeder motor";
        public static final int  FEEDER_ID = 33;
        
        public static final TalonFXConfig FEEDER_CONFIG = new TalonFXConfig(FEEDER_ID, FEEDER_CANBUS, FEEDER_NAME).withInvert(true);
        public static final double MAX_FEEDER_POWER = 1;
        public static final double MAX_FEEDER_CURRENT = 0;
        public static final double MIN_FEEDER_VELOCITY = 0;
    }
    public enum ShooterStates {
        GET_READY,
        AUTO_POINT,
        SHOOTER,
        IDLE,
        TEST,
        DELIVERY,
        TRANCH,
        onePoint,
        towPoint,
        thrrePoint,
        DELIVERY_ONLY_FLYWEEL
    }

    public static final Translation2d DELIVERY_RED_LEFT = new Translation2d(Field.Zones.ROBOT_STARTING_LINE_RED_X + Field.Zones.CENTER_LINE_X/4, Field.FieldDimensions.Y_CENTER/2);
    public static final Translation2d DELIVERY_RED_RIGHT = new Translation2d(Field.Zones.ROBOT_STARTING_LINE_RED_X + Field.Zones.CENTER_LINE_X/4, Field.FieldDimensions.Y_CENTER*3/2);
    public static final Translation2d DELIVERY_BLUE_LEFT = new Translation2d(Field.Zones.ROBOT_STARTING_LINE_BLUE_X - Field.Zones.CENTER_LINE_X/4, Field.FieldDimensions.Y_CENTER/2);
    public static final Translation2d DELIVERY_BLUE_RIGHT = new Translation2d(Field.Zones.ROBOT_STARTING_LINE_BLUE_X - Field.Zones.CENTER_LINE_X/4, Field.FieldDimensions.Y_CENTER*3/2);

    public static final Translation2d SHOOTER_OFFSET = new Translation2d(-0.115, 0);
}
