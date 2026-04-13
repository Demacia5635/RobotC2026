package frc.robot.shinua;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;

public class ShinuaConstants {
    // constants for the shinua motor
    public static final Canbus SHINUA_CANBUS = Canbus.Rio;
    public static final int SHINUA_ID = 0;
    public static final String SHINUA_NAME = "Shinua Motor";
    public static final boolean SHINUA_INVERTED = false;
    public static final double SHINUA_CURRENT_LIMIT = 40.0;
    public static final boolean SHINUA_BRAKE = false;
    public static final double SHINUA_BALLS_STUCK_CURRENT= 0;
    public static final double SHINUA_BALLS_STUCK_VELOCITY = 0;
    

    public static final TalonFXConfig SHINUA_CONFIG = new TalonFXConfig(SHINUA_ID, SHINUA_CANBUS, SHINUA_NAME)
            .withBrake(SHINUA_BRAKE)
            .withInvert(SHINUA_INVERTED)
            .withCurrent(SHINUA_CURRENT_LIMIT);

    public static enum ShinuaState {
        Testing(0),
        SHINUA_ON(1),
        EJECTING(-1),
        SHINUA_OFF(0);

        public double duty;

        ShinuaState(double duty) {
            this.duty = duty;
        }
    }
}