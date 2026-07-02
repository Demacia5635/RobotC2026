package frc.robot.shinua;

import frc.demacia.utils.mechanisms.StateBaseMechanism.MechanismState;
import frc.demacia.utils.motors.BaseMotorConfig.Canbus;
import frc.demacia.utils.motors.TalonFXConfig;

public class ShinuaConstants {
    public static final String SHINUA_NAME = "shinua";

    public final static class ShinuaRollersConstants {
        public static final String SHINUA_ROLLERS_MOTOR_NAME = "shinua rollers motor";
        public static final int  SHINUA_ROLLERS_ID = 40;
        public static final Canbus SHINUA_ROLLERS_CANBUS = Canbus.Rio;
        public static final boolean SHINUA_ROLLERS_BRAKE = true;
        public static final boolean SHINUA_ROLLERS_INVERT = false;
        public static final TalonFXConfig SHINUA_ROLLERS_MOTOR_CONFIG = new TalonFXConfig(SHINUA_ROLLERS_ID, SHINUA_ROLLERS_CANBUS, SHINUA_ROLLERS_MOTOR_NAME)
        .withBrake(SHINUA_ROLLERS_BRAKE)
        .withInvert(SHINUA_ROLLERS_INVERT);
    }

    public final static class MechanomConstants {
        public static final String MECHANOM_MOTOR_NAME = "mechanom motor";
        public static final int  MECHANOM_ID = 41;
        public static final Canbus MECHANOM_CANBUS = Canbus.Rio;
        public static final boolean MECHANOM_BRAKE = true;
        public static final boolean MECHANOM_INVERT = false;
        public static final TalonFXConfig MECHANOM_MOTOR_CONFIG = new TalonFXConfig(MECHANOM_ID, MECHANOM_CANBUS, MECHANOM_MOTOR_NAME)
        .withBrake(MECHANOM_BRAKE)
        .withInvert(MECHANOM_INVERT);
    }

    public static enum ShinuaStates implements MechanismState {
        SHINUA_ON(-0.05, 1),
        EJECTING(-0.15, 0.2),
        ONLY_ROLLERS(-0.1, 0),
        INTACKING(-0.3, 0.3);

        public double[] values;

        ShinuaStates(double rollersDuty, double mechanomDuty) {
            values = new double[] {rollersDuty, mechanomDuty};
        }
        
        @Override
        public double[] getValues() {
            return values;
        }
    }
}
