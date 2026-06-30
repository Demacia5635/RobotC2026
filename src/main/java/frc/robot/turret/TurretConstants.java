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
    
    public static final String TURRET_MOTOR_NAME = "turret motor";
    private static final int TURRET_MOTOR_ID = 20;
    private static final Canbus TURRET_MOTOR_CANBUS = Canbus.Rio;
    private static final boolean TURRET_IS_BREAKE = false;
    private static final boolean TURRET_IS_INVERT = false;
    private static final double TURRET_GEAR_RATIO = 48.0*112.0/27.0;
    private static final double KP = 40;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KS = 0.1;
    private static final double KV = 2.7;
    private static final double KA = 0.15;
    private static final double KG = 0;
    private static final double TURRET_MAX_VELOCITY = 5;
    private static final double TURRET_MAX_ACCELERATION = 20;
    private static final double TURRET_MAX_JERK = 50;
    public static final TalonFXConfig TURRET_MOTOR_CONFIG = new TalonFXConfig(TURRET_MOTOR_ID, TURRET_MOTOR_CANBUS, TURRET_MOTOR_NAME)
    .withBrake(TURRET_IS_BREAKE)
    .withInvert(TURRET_IS_INVERT)
    .withRadiansMotor(TURRET_GEAR_RATIO)
    .withPID(KP, KI, KD, KS, KV, KA, KG)
    .withMotionParam(TURRET_MAX_VELOCITY, TURRET_MAX_ACCELERATION, TURRET_MAX_JERK);
        
    private static final int TURRET_LIMIT_SWICH_CHANNEL = 8;
        private static final String TURRET_LIMIT_SWICH_NAME = "turret limit swich";
        public static final LimitSwitchConfig TURRET_LIMIT_SWITCH = new LimitSwitchConfig(TURRET_LIMIT_SWICH_CHANNEL, TURRET_LIMIT_SWICH_NAME);
    
    public static enum TurretStates implements MechanismState {
        SHOOTING,
        DELIVERY;

    @Override
    public double[] getValues() {
        return Turret.getInstance().getTurretAngle();
    }}

}
