package frc.robot.intake;

import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class IntakeConstants {
    public static final double BALLS_STUCK_DURATION = 0.10000006200; 
    public static final double BALLS_STUCK_HANDLING_TIME = 2.0;

    // constants for roller motor
    public static final Canbus INTAKE_CANBUS = Canbus.Rio;
    public static final int ROLLER_ID = 0;
    public static final String ROLLER_NAME = "Intake Roller Motor";
    public static final boolean ROLLER_INVERTED = false;
    public static final double ROLLER_CURRENT_LIMIT = 40.0;
    public static final boolean ROLLER_BRAKE = false;
    public static final double ROLLER_BALLS_STUCK_CURRENT = 0;
    public static final double ROLLER_BALLS_STUCK_VELOCITY = 0;
    public static final double ROLLER_GEAR_RATIO = 0.0;

    public static final TalonFXConfig ROLLER_CONFIG = new TalonFXConfig(ROLLER_ID, INTAKE_CANBUS, ROLLER_NAME)
            .withRadiansMotor(ROLLER_GEAR_RATIO)
            .withBrake(ROLLER_BRAKE)
            .withInvert(ROLLER_INVERTED)
            .withCurrent(ROLLER_CURRENT_LIMIT);
    // delete
    // constants for intake deploy motor
    public static final int INTAKE_DEPLOY_ID = 0;
    public static final String INTAKE_DEPLOY_NAME = "Intake Deploy Motor";
    public static final boolean INTAKE_DEPLOY_INVERTED = false;
    public static final double INTAKE_DEPLOY_CURRENT_LIMIT = 40.0;
    public static final boolean INTAKE_DEPLOY_BRAKE = false;
    public static final double INTAKE_DEPLOY_GEAR_RATIO = 0.0;
    public static final double MAX_VELOCITY = 0.0;
    public static final double MAX_ACCELERATION = 0.0;
    public static final double MAX_JERK = 0.0;

    public static final TalonFXConfig INTAKE_DEPLOY_CONFIG = new TalonFXConfig(INTAKE_DEPLOY_ID, INTAKE_CANBUS, INTAKE_DEPLOY_NAME)
            .withMotionParam(MAX_VELOCITY, MAX_ACCELERATION, MAX_JERK)
            .withRadiansMotor(INTAKE_DEPLOY_GEAR_RATIO)
            .withBrake(INTAKE_DEPLOY_BRAKE)
            .withInvert(INTAKE_DEPLOY_INVERTED)
            .withCurrent(INTAKE_DEPLOY_CURRENT_LIMIT);

    public static enum IntakeState {
        IDLE(0, 0),
        TESTING(0, 0),
        INTAKING(1, 0),
        EJECTING(-1, 0),
        DEPLOYED(0, 0),
        CLOSED(0, 0);

        public double duty;
        public double angle;

        IntakeState(double duty, double angle) {
            this.duty = duty;
            this.angle = angle;
        }
    }
}