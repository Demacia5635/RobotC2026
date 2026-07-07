package frc.robot.intack;

import frc.demacia.utils.mechanisms.StateBaseMechanism.MechanismState;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;
import frc.demacia.utils.sensors.LimitSwitchConfig;

public class IntackConstants {
    public static final String INTACK_NAME = "intack";

    public class IntackDeployConstants {
        public static final double INTACK_DEPLOY_MIN_ANGLE = Math.toRadians(-33);
        public static final double INTACK_DEPLOY_MIDDLE_ANGLE = Math.toRadians(20);
        public static final double INTACK_DEPLOY_MAX_ANGLE = Math.toRadians(90);
        public static final double INTACK_DEPLOY_CALIBRATION_POWER = -0.2;
        public static final double INTACK_DEPLOY_ALLOWED_ERROR = Math.toRadians(10);

        public static final String INTACK_DEPLOY_MOTOR_NAME = "intack deploy motor";
        public static final int INTACK_DEPLOY_MOTOR_ID = 50;
        public static final Canbus INTACK_DEPLOY_MOTOR_CANBUS = Canbus.Rio;
        public static final boolean INTACK_DEPLOY_BRAKE = true;
        public static final boolean INTACK_DEPLOY_INVERT = false;
        public static final double INTACK_DEPLOY_GEAR_RATIO = 64;
        public static final double KP = 0.6;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double KS = 0.02;
        public static final double KV = 0.97;
        public static final double KA = 0.05;
        public static final double KG = -0.2;
        public static final double INTACK_DEPLOY_MAX_VELOCITY = 3;
        public static final double INTACK_DEPLOY_MAX_ACCELERATION = 8;
        public static final double INTACK_DEPLOY_MAX_JERK = 40;
        public static final TalonFXConfig INTACK_DEPLOY_MOTOR_CONFIG = new TalonFXConfig(INTACK_DEPLOY_MOTOR_ID, INTACK_DEPLOY_MOTOR_CANBUS, INTACK_DEPLOY_MOTOR_NAME)
        .withBrake(INTACK_DEPLOY_BRAKE)
        .withInvert(INTACK_DEPLOY_INVERT)
        .withRadiansMotor(INTACK_DEPLOY_GEAR_RATIO)
        .withPID(KP, KI, KD, KS, KV, KA, KG)
        .withMotionParam(INTACK_DEPLOY_MAX_VELOCITY, INTACK_DEPLOY_MAX_ACCELERATION, INTACK_DEPLOY_MAX_JERK);

        public static final String INTACK_DEPLOY_LIMIT_SWICH_NAME = "intack diploy limit swich";
        public static final int INTACK_DEPLOY_LIMIT_SWICH_CHANNEL = 6;
        public static final boolean INTACK_DEPLOY_LIMIT_SWITCH_INVERT = true;
        public static final LimitSwitchConfig INTACK_DEPLOY_LIMIT_SWITCH_CONFIG = new LimitSwitchConfig(INTACK_DEPLOY_LIMIT_SWICH_CHANNEL, INTACK_DEPLOY_LIMIT_SWICH_NAME)
        .withInvert(INTACK_DEPLOY_LIMIT_SWITCH_INVERT);  
    }

    public final static class RollersConstants {
        public static final double ROLLERS_MAX_POWER = 1;

        public static final String ROLLERS_NAME = "rollers motor";
        public static final int  ROLLERS_ID = 51;
        public static final Canbus ROLLERS_CANBUS = Canbus.Rio;
        public static final boolean ROLLERS_BRAKE = true;
        public static final boolean ROLLERS_INVERT = false;
        public static final TalonFXConfig ROLLERS_MOTOR_CONFIG = new TalonFXConfig(ROLLERS_ID, ROLLERS_CANBUS, ROLLERS_NAME)
        .withBrake(ROLLERS_BRAKE)
        .withInvert(ROLLERS_INVERT);
    }

    public static enum IntackStates implements MechanismState {
        DEPLOYED(IntackDeployConstants.INTACK_DEPLOY_MAX_ANGLE, 0),
        INTAKING(IntackDeployConstants.INTACK_DEPLOY_MAX_ANGLE, 1),
        CLOSED(IntackDeployConstants.INTACK_DEPLOY_MIN_ANGLE, 0),
        CLOSED_SHOOTING(IntackDeployConstants.INTACK_DEPLOY_MIN_ANGLE, 1),
        MIDDLE(IntackDeployConstants.INTACK_DEPLOY_MIDDLE_ANGLE, 0),
        MIDDLE_SHOOTING(IntackDeployConstants.INTACK_DEPLOY_MIDDLE_ANGLE, 1),
        EJECTING(IntackDeployConstants.INTACK_DEPLOY_MAX_ANGLE, -1);

        public double[] values;

        IntackStates(double angle, double duty) {
            values = new double[] {angle, duty};
        }
        
        @Override
        public double[] getValues() {
            return values;
        }
    }
}
