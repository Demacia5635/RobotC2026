package frc.robot.intake;


import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;

public class IntakeConstants {

    // constants for roller motor
    public static final Canbus INTAKE_CANBUS = Canbus.Rio;
    public static final int ROLLER_ID = 51;
    public static final String ROLLER_NAME = "Intake Roller Motor";
    public static final boolean ROLLER_INVERTED = true;
    public static final double ROLLER_CURRENT_LIMIT = 40.0;
    public static final boolean ROLLER_BRAKE = false;
    public static final double ROLLER_BALLS_STUCK_CURRENT = 0;
    public static final double ROLLER_GEAR_RATIO = 2;

    public static final TalonFXConfig ROLLER_CONFIG = new TalonFXConfig(ROLLER_ID, INTAKE_CANBUS, ROLLER_NAME)
            .withRadiansMotor(ROLLER_GEAR_RATIO)
            .withBrake(ROLLER_BRAKE)
            .withInvert(ROLLER_INVERTED)
            .withCurrent(ROLLER_CURRENT_LIMIT);

    // constants for intake deploy motor
    public static final double INTAKE_DEPLOY_OFFSET = Math.toRadians(-31.05);
    public static final int INTAKE_DEPLOY_ID = 50;
    public static final String INTAKE_DEPLOY_NAME = "Intake Deploy Motor";
    public static final boolean INTAKE_DEPLOY_INVERTED = false;
    public static final double INTAKE_DEPLOY_CURRENT_LIMIT = 15.0;
    public static final boolean INTAKE_DEPLOY_BRAKE = false;
    public static final double INTAKE_DEPLOY_GEAR_RATIO = 64;
    public static final double MAX_VELOCITY = 1;// 3
    public static final double MAX_ACCELERATION = 5;// 15
    public static final double MAX_JERK = 35;// 150
    public static final double DEPLOY_CLOSED_ANGLE = Math.toRadians(-33);
    public static final double DEPLOY_OPEN_ANGLE = Math.toRadians(67);
    public static final double ALLOWED_ERROR = Math.toRadians(7);
    public static final double kp = 0.5;
    public static final double ki = 0.0;
    public static final double kd = 0.0;
    public static final double ks = 0.65244;
    public static final double kv = 0.60098;
    public static final double ka = 0.0948;
    public static final double kg = 0.5;// -0.1745

    public static final TalonFXConfig INTAKE_DEPLOY_CONFIG = new TalonFXConfig(INTAKE_DEPLOY_ID, INTAKE_CANBUS,
            INTAKE_DEPLOY_NAME)
            .withPID(kp, ki, kd, ks, kv, ka, 0)
            .withMotionParam(MAX_VELOCITY, MAX_ACCELERATION, MAX_JERK)
            .withRadiansMotor(INTAKE_DEPLOY_GEAR_RATIO)
            .withBrake(INTAKE_DEPLOY_BRAKE)
            .withInvert(INTAKE_DEPLOY_INVERTED)
            .withCurrent(INTAKE_DEPLOY_CURRENT_LIMIT);

    public static enum IntakeState {
        TESTING(0, 0),
        INTAKING(0.5, DEPLOY_OPEN_ANGLE),//TODO when cage is install change to 1
        EJECTING(-1, DEPLOY_OPEN_ANGLE),
        DEPLOYED(0, DEPLOY_OPEN_ANGLE),
        CLOSED(0, INTAKE_DEPLOY_OFFSET),
        IDLE(0, 0);

        public double duty;
        public double angle;

        IntakeState(double duty, double angle) {
            this.duty = duty;
            this.angle = angle;
        }
    }
}