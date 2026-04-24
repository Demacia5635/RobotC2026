package frc.robot.shinua;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;

public class ShinuaConstants {
    // constants for the mecanum motor
    public static final Canbus SHINUA_CANBUS = Canbus.Rio;
    public static final int MECANUM_ID = 0;
    public static final String MECANUM_NAME = "Mecanum Motor";
    public static final boolean MECANUM_INVERTED = false;
    public static final double MECANUM_CURRENT_LIMIT = 40.0;
    public static final boolean MECANUM_BRAKE = true;
    public static final double MECANUM_BALLS_STUCK_CURRENT = 0;
    public static final double MECANUM_BALLS_STUCK_VELOCITY = 0;
    public static final double MECANUM_GEAR_RATIO = 0.0;
    // stuck balls time constants
    public static final double BALLS_STUCK_DURATION = 0.1; 
    public static final double BALLS_STUCK_HANDLING_TIME = 1.0;

    public static final TalonFXConfig MECANUM_CONFIG = new TalonFXConfig(MECANUM_ID, SHINUA_CANBUS, MECANUM_NAME)
            .withRadiansMotor(MECANUM_GEAR_RATIO)
            .withBrake(MECANUM_BRAKE)
            .withInvert(MECANUM_INVERTED)
            .withCurrent(MECANUM_CURRENT_LIMIT);

    // constants for the rollers motor
    public static final int ROLLERS_ID = 0;
    public static final String ROLLERS_NAME = "Rollers Motor";
    public static final boolean ROLLERS_INVERTED = false;
    public static final double ROLLERS_CURRENT_LIMIT = 40.0;
    public static final boolean ROLLERS_BRAKE = true;
    public static final double ROLLERS_BALLS_STUCK_CURRENT = 0;
    public static final double ROLLERS_BALLS_STUCK_VELOCITY = 0;
    public static final double ROLLERS_GEAR_RATIO = 0.0;

    public static final TalonFXConfig ROLLERS_CONFIG = new TalonFXConfig(ROLLERS_ID, SHINUA_CANBUS, ROLLERS_NAME)
            .withRadiansMotor(ROLLERS_GEAR_RATIO)
            .withBrake(ROLLERS_BRAKE)
            .withInvert(ROLLERS_INVERTED)
            .withCurrent(ROLLERS_CURRENT_LIMIT);

    public static enum ShinuaState {
        SHINUA_ON(1, 1),
        SHINUA_OFF(0, 0),
        EJECTING(-1, -1),
        TESTING(0, 0);

        public double dutyRollers;
        public double dutyMecanum;

        ShinuaState(double dutyRollers, double dutyMecanum) {
            this.dutyRollers = dutyRollers;
            this.dutyMecanum = dutyMecanum;
        }
    
    }
}