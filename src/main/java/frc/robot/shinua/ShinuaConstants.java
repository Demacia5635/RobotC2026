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
    public static final double MECANUM_GEAR_RATIO = 4d;
    // stuck balls time constants
    public static final double BALLS_STUCK_DURATION = 0.1; 
    public static final double BALLS_STUCK_HANDLING_TIME = 1.0;

    public static final TalonFXConfig MECANUM_CONFIG = new TalonFXConfig(MECANUM_ID, SHINUA_CANBUS, MECANUM_NAME)
            .withMeterMotor(BALLS_STUCK_HANDLING_TIME, BALLS_STUCK_DURATION)//MECANUM_GEAR_RATIO
            .withBrake(MECANUM_BRAKE)
            .withInvert(MECANUM_INVERTED)
            .withCurrent(MECANUM_CURRENT_LIMIT);

    // constants for the rollers motor
    public static final int ROLLERS_ID = 40;
    public static final String ROLLERS_NAME = "Rollers Motor";
    public static final boolean ROLLERS_INVERTED = false;
    public static final double ROLLERS_CURRENT_LIMIT = 40.0;
    public static final boolean ROLLERS_BRAKE = true;
    public static final double ROLLERS_BALLS_STUCK_CURRENT = 0;
    public static final double ROLLERS_BALLS_STUCK_VELOCITY = 0;
    public static final double ROLLERS_GEAR_RATIO = 4;
    public static double kp = 0.5;
    public static final double ki = 0.0;
    public static final double kd = 0.0;
    public static double ks = 0.07668;
    public static double kv = 0.07353;
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
        SHINUA_ON(-50, 0.9),
        SHINUA_OFF(0, 0),
        EJECTING(-50, -0.9),
        TESTING(0, 0);

        public double velocityRollers;
        public double dutyMecanum;

        ShinuaState(double velocityRollers, double dutyMecanum) {
            this.velocityRollers = velocityRollers;
            this.dutyMecanum = dutyMecanum;
        }
    
    }
}