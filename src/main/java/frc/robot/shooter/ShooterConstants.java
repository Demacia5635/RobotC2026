package frc.robot.shooter;


import edu.wpi.first.math.geometry.Translation2d;
import frc.demacia.utils.LookUpTable;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class ShooterConstants {

    public final static String NAME = "Shooter";
    public final static double HEIGHT = 4;
    public final static Translation2d DELIVERY_RIGHT_POINT = Translation2d.kZero;
    public final static Translation2d DELIVERY_LEFT_POINT = Translation2d.kZero;
    public final static LookUpTable LOOK_UP_TABLE = new LookUpTable(2); //distance: velocity, angel
    public static final Translation2d HUB = Translation2d.kZero;
    static{
        LOOK_UP_TABLE.add(0, 0, 0);
    }


    public final static class FlywheelConstants {
        public static final Canbus FLYWEEL_CANBUS = Canbus.Rio;
        public static final String FLYWEEL_NAME = "fly weel motor";
        public static final int FLYWEEL_ID = 0;

        public static final TalonFXConfig FLYWHEEL_CONFIG = new TalonFXConfig(FLYWEEL_ID, FLYWEEL_CANBUS, FLYWEEL_NAME);
        public static final double MAX_FLYWHEEL_POWER = 1;
        public static final double flywheelPositionOffset = 0.4;
    }

    public final static class HoodConstants {
        public static final Canbus HOOD_CANBUS = Canbus.Rio;
        public static final String HOOD_NAME = "hood motor";
        public static final int HOOD_ID = 0;

        public static final TalonFXConfig HOOD_CONFIG = new TalonFXConfig(HOOD_ID, HOOD_CANBUS, HOOD_NAME);
        public static final double hoodPositionOffset = 0.4;
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 0;
    }

    public final static class IndexerConstants {
        public static final Canbus INDEXER_CANBUS = Canbus.Rio;
        public static final String INDEXER_NAME = "indexer motor";
        public static final int INDEXER_ID = 0;

        public static final TalonFXConfig INDEXER_CONFIG = new TalonFXConfig(INDEXER_ID, INDEXER_CANBUS, INDEXER_NAME);
        public static final double MAX_INDEXER_POWER = 1;
    }

    public final static class FeederConstants {
        public static final Canbus FEEDER_CANBUS = Canbus.Rio;
        public static final String FEEDER_NAME = "feeder motor";
        public static final int  FEEDER_ID = 0;
        
        public static final TalonFXConfig FEEDER_CONFIG = new TalonFXConfig(FEEDER_ID, FEEDER_CANBUS, FEEDER_NAME);
        public static final double MAX_FEEDER_POWER = 1;
    }
    public enum ShooterStates {
        SHOOTER,
        IDLE,
        TEST,
        DELIVERY,
        TRANCH
    }
}
