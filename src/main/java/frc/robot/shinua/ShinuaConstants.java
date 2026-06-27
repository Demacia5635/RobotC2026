package frc.robot.shinua;

import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;

public class ShinuaConstants {
    // constants for the mecanum motor
    public static final Canbus SHINUA_CANBUS = Canbus.Rio;
    public static final int MECANUM_ID = 41;
    public static final String MECANUM_NAME = "Mecanum Motor";
    public static final boolean MECANUM_INVERTED = false;
    public static final double MECANUM_CURRENT_LIMIT = 40.0;
    public static final boolean MECANUM_BRAKE = true;
    public static final double MECANUM_BALLS_STUCK_CURRENT = 0;
    public static final double MECANUM_BALLS_STUCK_VELOCITY = 0;
    public static final double MECANUM_GEAR_RATIO = 3d;
    public static final double MECANUM_DIMETER = 0.05;
    public static final double DUTY_WHEN_MAX_CURRENT = 0;
    // stuck balls time constants
    public static final double BALLS_STUCK_DURATION = 0.07; 
    public static final double BALLS_STUCK_HANDLING_TIME = 0.2;
    public static final double MAX_CURRENT = 35;

    public static final TalonFXConfig MECANUM_CONFIG = new TalonFXConfig(MECANUM_ID, SHINUA_CANBUS, MECANUM_NAME)
            .withBrake(MECANUM_BRAKE)
            .withInvert(MECANUM_INVERTED)
            .withCurrent(MECANUM_CURRENT_LIMIT)
            .withMeterMotor(MECANUM_GEAR_RATIO, MECANUM_DIMETER);

    // constants for the rollers motor
    public static final int ROLLERS_ID = 40;
    public static final String ROLLERS_NAME = "Rollers Motor";
    public static final boolean ROLLERS_INVERTED = false;
    public static final double ROLLERS_CURRENT_LIMIT = 40.0;
    public static final boolean ROLLERS_BRAKE = true;
    public static final double ROLLERS_BALLS_STUCK_CURRENT = 0;
    public static final double ROLLERS_BALLS_STUCK_VELOCITY = 0;
    public static final double ROLLERS_GEAR_RATIO = 4d;
    public static double kp = 2.9;
    public static double ki = 0.0;
    public static double kd = 0.0;
    public static double ks = 0.63;
    public static double kv = 2.48;
    public static double ka = 0;
    public static double kg = 0.0;


    public static final TalonFXConfig ROLLERS_CONFIG = new TalonFXConfig(ROLLERS_ID, SHINUA_CANBUS, ROLLERS_NAME)
            .withMeterMotor(ROLLERS_GEAR_RATIO,0.05)
            .withBrake(ROLLERS_BRAKE)
            .withInvert(ROLLERS_INVERTED)
            .withCurrent(ROLLERS_CURRENT_LIMIT)
            .withPID(kp, ki, kd,ks, kv, ka, kg)
            .withRampTime(0.8);

    public static enum ShinuaState {
        SHINUA_ON(-0.2, 1),
        SHINUA_OFF(0, 0),
        EJECTING(-1.5, 0.3),
        NO_INDEXER(-1.5 , 0.3),
        TESTING(0, 0), 
        ONLY_ROLLERS(-0.2, 0);

        public double velocityRollers;
        public double dutyMecanum;

        ShinuaState(double velocityRollers, double dutyMecanum) {
            this.velocityRollers = velocityRollers;
            this.dutyMecanum = dutyMecanum;
        }
    
    }
}