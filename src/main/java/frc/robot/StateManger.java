package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotCommon.Shifts;
import frc.robot.intack.IntackConstants.IntackStates;
import frc.robot.intack.subsystems.Intack;
import frc.robot.shinua.ShinuaConstants.ShinuaStates;
import frc.robot.shinua.subsystems.Shinua;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.TurretConstants.TurretStates;
import frc.robot.turret.subsystems.Turret;

public class StateManger extends SubsystemBase{
    private static StateManger stateManger;
    private Timer shiftTimer = new Timer();
    private boolean isRedWonAuto = true;
    private int shiftNum = -1;

    private boolean isSecondaryAction = false;
    private boolean wasInAllianceZone = false;

    private StateManger() {
        
    }

    public static StateManger getInstance() {
        if (stateManger == null){
            stateManger = new StateManger();
        }
        return stateManger;
    }

    public void updateStates() {
        boolean currentInZone = RobotCommon.isInAllinceZone();

        if (currentInZone && !wasInAllianceZone) {
            isSecondaryAction = false;
        }
        wasInAllianceZone = currentInZone;

        if (RobotCommon.isOnBamp()) {
            Shooter.getInstance().setStateIdle();
            Turret.getInstance().setStateIdle();
            Shinua.getInstance().setState(ShinuaStates.ONLY_ROLLERS);
            Intack.getInstance().setState(IntackStates.CLOSED);
        } else if (RobotCommon.getShift().equals(Shifts.Inactive)) {
            if (RobotCommon.isInAllinceZone() || getShiftTimeLeft() < 7) {
                Shooter.getInstance().setStateIdle();
                Turret.getInstance().setStateIdle();
                Shinua.getInstance().setState(ShinuaStates.INTACKING);
                Intack.getInstance().setState(IntackStates.INTAKING);
            } else {
                Shooter.getInstance().setState(ShooterStates.DELIVERY);
                Turret.getInstance().setState(TurretStates.DELIVERY);
                Shinua.getInstance().setState(ShinuaStates.SHINUA_ON);
                Intack.getInstance().setState(IntackStates.INTAKING);
            }
        } else if (RobotCommon.getShift().equals(Shifts.Transition) || RobotCommon.getShift().equals(Shifts.Active) || RobotCommon.getShift().equals(Shifts.Endgame)){
            if (RobotCommon.isInAllinceZone()) {
                if (isSecondaryAction) {
                    Shooter.getInstance().setState(ShooterStates.SHOOTING);
                    Turret.getInstance().setState(TurretStates.SHOOTING);
                    Shinua.getInstance().setState(ShinuaStates.SHINUA_ON);
                    Intack.getInstance().setState(IntackStates.MIDDLE_SHOOTING);
                } else {
                    Shooter.getInstance().setState(ShooterStates.SHOOTING);
                    Turret.getInstance().setState(TurretStates.SHOOTING);
                    Shinua.getInstance().setState(ShinuaStates.SHINUA_ON);
                    Intack.getInstance().setState(IntackStates.INTAKING);
                }
            } else {
                if (isSecondaryAction) {
                    Shooter.getInstance().setState(ShooterStates.DELIVERY);
                    Turret.getInstance().setState(TurretStates.DELIVERY);
                    Shinua.getInstance().setState(ShinuaStates.SHINUA_ON);
                    Intack.getInstance().setState(IntackStates.INTAKING);
                } else {
                    Shooter.getInstance().setStateIdle();
                    Turret.getInstance().setStateIdle();
                    Shinua.getInstance().setState(ShinuaStates.INTACKING);
                    Intack.getInstance().setState(IntackStates.INTAKING);
                }
            }
        }
    }

    public void toggleAction() {
        isSecondaryAction = !isSecondaryAction;
    }

    public void startingTeam() {
        if (DriverStation.getGameSpecificMessage() != "") {
            switch (DriverStation.getGameSpecificMessage()) {
                case "R":
                    isRedWonAuto = true;
                    break;

                case "B":
                    isRedWonAuto = false;
                    break;

                default:
                    break;
            }
        }
    }

    public void resetShift() {
        if (shiftNum != 0) {
            shiftTimer.stop();
            shiftTimer.reset();
            shiftNum = -1;
            RobotCommon.setShift(Shifts.Disable);
        }
    }

    private void updateShift() {
        if (RobotCommon.getShift().equals(Shifts.Disable) && RobotState.isEnabled() && RobotState.isAutonomous()) {
            RobotCommon.setShift(Shifts.Auto);
            shiftTimer.start();
            shiftNum = 0;
        } else if ((RobotCommon.getShift().equals(Shifts.Auto) && RobotState.isTeleop()) || (RobotCommon.getShift().equals(Shifts.Disable) && RobotState.isEnabled() && RobotState.isTeleop())) {
            RobotCommon.setShift(Shifts.Transition);
            shiftNum = 1;
            shiftTimer.reset();
            shiftTimer.start();
        } else if (RobotCommon.getShift().equals(Shifts.Transition) && shiftTimer.hasElapsed(10)) {
            RobotCommon.setShift(isRedWonAuto && RobotCommon.getIsRed() ? Shifts.Inactive : Shifts.Active);
            shiftTimer.reset();
            shiftNum = 2;
        } else if (RobotCommon.getShift().equals(Shifts.Active) && shiftTimer.hasElapsed(25) && shiftNum != 5) {
            RobotCommon.setShift(Shifts.Inactive);
            shiftTimer.reset();
            shiftNum++;
        } else if (RobotCommon.getShift().equals(Shifts.Inactive) && shiftTimer.hasElapsed(25) && shiftNum != 5) {
            RobotCommon.setShift(Shifts.Active);
            shiftTimer.reset();
            shiftNum++;
        } else if (shiftNum == 5 && shiftTimer.hasElapsed(25)) {
            RobotCommon.setShift(Shifts.Endgame);
            shiftNum = 6;
            shiftTimer.reset();
        }
    }

    public Timer getShiftTimer() {
        return shiftTimer;
    }

    public boolean isRedWonAuto() {
        return isRedWonAuto;
    }

    public void setRedWonAuto(boolean isRedWonAuto) {
        this.isRedWonAuto = isRedWonAuto;
    }

    public int getShiftNum() {
        return shiftNum;
    }

    public void setShiftNum(int shiftNum) {
        this.shiftNum = shiftNum;
    }

    public double getShiftTimeLeft() {
        switch (shiftNum) {
            case 0:
                return 20 - shiftTimer.get();
            case 1:
                return 10 - shiftTimer.get();
            case 6:
                return 30 - shiftTimer.get();

            default:
                return 25 - shiftTimer.get();

        }
    }

    public Boolean isOurHub() {
        double time = DriverStation.getMatchTime();

        if (time < 0) {
            return false;
        }

        boolean autoWinnerIsRed = isRedWonAuto; 

        boolean ourAllianceWonAuto = (autoWinnerIsRed && RobotCommon.getIsRed()) || (!autoWinnerIsRed && !RobotCommon.getIsRed());

        if (time > 105) {
            return !ourAllianceWonAuto; 
        } else if (time > 80) {
            return ourAllianceWonAuto; 
        } else if (time > 55) {
            return !ourAllianceWonAuto;
        } else if (time > 30) {
            return ourAllianceWonAuto; 
        } else {
            return true; 
        }
    }

    public static double getTimeLeft(){
        double time = DriverStation.getMatchTime();
        return time;
    }
    
    @Override
    public void periodic() {
        updateStates();
        startingTeam();
        if (RobotState.isTeleop()){
            updateShift();
        }
    }
}
