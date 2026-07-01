package frc.robot.turret;

import frc.demacia.utils.mechanisms.StateBaseMechanism.MechanismState;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.sensors.LimitSwitchConfig;
import frc.robot.turret.subsystems.Turret;

public class TurretConstants {

    public static final String TURRET_NAME = "turret";
    public static final double TURRET_MIN_ANGLE = Math.toRadians(-207);
    public static final double TURRET_MAX_ANGLE = Math.toRadians(133);
    public static final double TURRET_CALIBRATION_POWER = 0.2;
    public static final double TURRET_ALLOWED_ERROR = Math.toRadians(6);
    
    public static final String TURRET_MOTOR_NAME = "turret motor";
    public static final int TURRET_MOTOR_ID = 20;
    public static final Canbus TURRET_MOTOR_CANBUS = Canbus.Rio;
    public static final boolean TURRET_IS_BREAKE = false;
    public static final boolean TURRET_IS_INVERT = false;
    public static final double TURRET_GEAR_RATIO = 48.0*112.0/27.0;
    public static final double KP = 40;
    public static final double KI = 0;
    public static final double KD = 0;
    public static final double KS = 0.1;
    public static final double KV = 2.7;
    public static final double KA = 0.15;
    public static final double KG = 0;
    public static final double TURRET_MAX_VELOCITY = 5;
    public static final double TURRET_MAX_ACCELERATION = 20;
    public static final double TURRET_MAX_JERK = 50;
    public static final TalonFXConfig TURRET_MOTOR_CONFIG = new TalonFXConfig(TURRET_MOTOR_ID, TURRET_MOTOR_CANBUS, TURRET_MOTOR_NAME)
    .withBrake(TURRET_IS_BREAKE)
    .withInvert(TURRET_IS_INVERT)
    .withRadiansMotor(TURRET_GEAR_RATIO)
    .withPID(KP, KI, KD, KS, KV, KA, KG)
    .withMotionParam(TURRET_MAX_VELOCITY, TURRET_MAX_ACCELERATION, TURRET_MAX_JERK);
        
    public static final int TURRET_LIMIT_SWICH_CHANNEL = 8;
    public static final String TURRET_LIMIT_SWICH_NAME = "turret limit swich";
    public static final LimitSwitchConfig TURRET_LIMIT_SWITCH = new LimitSwitchConfig(TURRET_LIMIT_SWICH_CHANNEL, TURRET_LIMIT_SWICH_NAME);
    
    public static enum TurretStates implements MechanismState {
        SHOOTING,
        DELIVERY;

    @Override
    public double[] getValues() {
        return Turret.getInstance().getTurretAngle();
    }}

}
