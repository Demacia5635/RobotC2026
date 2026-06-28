package frc.robot.stateManger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Field;
import frc.robot.RobotCommon;
import frc.robot.RobotContainer;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.stateManger.utils.stateMangerUtils;
import frc.robot.turret.TurretConstants.TurretStates;

public class StateManger extends SubsystemBase{

    public static boolean isWork;
    private static Timer shiftTimer = new Timer();
    private static boolean isRedWonAuto = true;
    private static int shiftNum = -1;
    
   private static boolean isTrench() {
    Pose2d pose = RobotCommon.getRobotFucerPose();
    double x = pose.getX();
    double y = pose.getY();

    return stateMangerUtils.inRange(x, Field.TrenchBlueAudience.X_BACK + 0.5,  Field.TrenchBlueAudience.X_FRONT - 0.5)
        && stateMangerUtils.inRange(x, Field.TrenchBlueScoring.X_BACK + 0.5,   Field.TrenchBlueScoring.X_FRONT - 0.5)
        && stateMangerUtils.inRange(y, Field.TrenchBlueAudience.Y_FRONT + 0.5,  Field.TrenchBlueAudience.Y_BACK - 0.5)
        && stateMangerUtils.inRange(y, Field.TrenchBlueScoring.Y_FRONT+ 0.5,   Field.TrenchBlueScoring.Y_BACK - 0.5)
        && stateMangerUtils.inRange(x, Field.TrenchRedAudience.X_FRONT + 0.5,  Field.TrenchRedAudience.X_BACK - 0.5)
        && stateMangerUtils.inRange(x, Field.TrenchRedScoring.X_FRONT + 0.5,   Field.TrenchRedScoring.X_BACK - 0.5)
        && stateMangerUtils.inRange(y, Field.TrenchRedAudience.Y_FRONT + 0.5,  Field.TrenchRedAudience.Y_BACK - 0.5)
        && stateMangerUtils.inRange(y, Field.TrenchRedScoring.Y_FRONT + 0.5,   Field.TrenchRedScoring.Y_BACK - 0.5);
    }

    public static double getTimeLeft(){
        double time = DriverStation.getMatchTime();
        return time;
    }

    public static Boolean isRedWonAtou(){
        if(DriverStation.getGameSpecificMessage() != ""){
            switch (DriverStation.getGameSpecificMessage()) {
                case "R":
                    return true;
                default:
                    return false;
            }
        }else{
            return null;
        }
    }

    public static Boolean isOurHub() {
        double time = DriverStation.getMatchTime();

        if (time < 0) {
            return false;
        }

        boolean autoWinnerIsRed = isRedWonAtou(); 

        boolean ourAllianceWonAuto = (autoWinnerIsRed && RobotCommon.isRed()) || (!autoWinnerIsRed && !RobotCommon.isRed());

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


    public static Double getTimeUntilNextHubChange() {
        double time = DriverStation.getMatchTime();

        if (time < 0) {
            return null;
        }

        if (time > 105) {
            return time - 105;
        } else if (time > 80) {
            return time - 80;
        } else if (time > 55) {
            return time - 55;
        } else if (time > 30) {
            return time - 30;
        } else {
            return time;
        }
    }


    private static boolean isHub(){
        return stateMangerUtils.isPastBlueHub(RobotCommon.currentRobotPose.getX()) || stateMangerUtils.isPastRedHub(RobotCommon.currentRobotPose.getX());
    }


    private static boolean isDeleveryAndIntakeBlue(){
        return !isHub();
    }

    public static void update(){   
        if(StateManger.isWork){
            if(!isTrench()){
                if(isHub()){
                    RobotContainer.shooter.setShooterState(ShooterStates.SHOOTER);
                    RobotContainer.turret.setState(TurretStates.SHOOTING);
                    RobotContainer.intake.setState(IntakeState.DEPLOYED);
                }
                else if(isDeleveryAndIntakeBlue()){
                    RobotContainer.shooter.setShooterState(ShooterStates.DELIVERY);
                    RobotContainer.turret.setState(TurretStates.DELIVERY);
                    RobotContainer.intake.setState(IntakeState.INTAKING);
                }
        }else{
            RobotContainer.shooter.setShooterState(ShooterStates.TRANCH);
        }
    }else{
        RobotContainer.shooter.setShooterState(ShooterStates.IDLE);
        RobotContainer.turret.setState(TurretStates.IDLE);
        RobotContainer.intake.setState(IntakeState.IDLE);
    }
    }

    public static Shifts getCurrenTShifts(){
        double time = DriverStation.getMatchTime();

        if(time > 105){
            return Shifts.Transition;
        }else if (time < 30) {
            return Shifts.Endgame;
        }else if(DriverStation.isDisabled()){
            return Shifts.Disable;
        }else{
            if(isOurHub()){
                return Shifts.Active;
            }else{
                return Shifts.Disable;
            }
        }
    }

    public static void startingTeam() {
        if (DriverStation.getGameSpecificMessage() != "") {
            switch (DriverStation.getGameSpecificMessage()) {
                case "R":
                    StateManger.isRedWonAuto = true;
                    break;

                case "B":
                    StateManger.isRedWonAuto = false;
                    break;

                default:
                    break;
            }
        }
    }

    public static void resetShift() {
        if (StateManger.shiftNum != 0) {
            StateManger.shiftTimer.stop();
            StateManger.shiftTimer.reset();
            StateManger.shiftNum = -1;
            RobotCommon.setShift(Shifts.Disable);
        }
    }

    private static void updateShift() {
        if (RobotCommon.getShift().equals(Shifts.Disable) && RobotState.isEnabled() && RobotState.isAutonomous()) {
            RobotCommon.setShift(Shifts.Auto);
            StateManger.shiftTimer.start();
            StateManger.shiftNum = 0;
        } else if ((RobotCommon.getShift().equals(Shifts.Auto) && RobotState.isTeleop()) || (RobotCommon.getShift().equals(Shifts.Disable) && RobotState.isEnabled() && RobotState.isTeleop())) {
            RobotCommon.setShift(Shifts.Transition);
            StateManger.shiftNum = 1;
            StateManger.shiftTimer.reset();
            shiftTimer.start();
        } else if (RobotCommon.getShift().equals(Shifts.Transition) && StateManger.shiftTimer.hasElapsed(10)) {
            RobotCommon.setShift(isRedWonAuto && RobotCommon.isRed() ? Shifts.Inactive : Shifts.Active);
            StateManger.shiftTimer.reset();
            StateManger.shiftNum = 2;
        } else if (RobotCommon.getShift().equals(Shifts.Active) && StateManger.shiftTimer.hasElapsed(25) && StateManger.shiftNum != 5) {
            RobotCommon.setShift(Shifts.Inactive);
            StateManger.shiftTimer.reset();
            StateManger.shiftNum++;
        } else if (RobotCommon.getShift().equals(Shifts.Inactive) && StateManger.shiftTimer.hasElapsed(25) && StateManger.shiftNum != 5) {
            RobotCommon.setShift(Shifts.Active);
            StateManger.shiftTimer.reset();
            StateManger.shiftNum++;
        } else if (StateManger.shiftNum == 5 && StateManger.shiftTimer.hasElapsed(25)) {
            RobotCommon.setShift(Shifts.Endgame);
            StateManger.shiftNum = 6;
            StateManger.shiftTimer.reset();
        }
    }

    public static Timer getShiftTimer() {
        return StateManger.shiftTimer;
    }

    public static boolean isRedWonAuto() {
        return StateManger.isRedWonAuto;
    }

    public static void setRedWonAuto(boolean isRedWonAuto) {
        StateManger.isRedWonAuto = isRedWonAuto;
    }

    public static int getShiftNum() {
        return StateManger.shiftNum;
    }

    public static void setShiftNum(int shiftNum) {
        StateManger.shiftNum = shiftNum;
    }

    public static double getTheTimeLeft() {
        switch (StateManger.shiftNum) {
            case 0:
                return 20 - StateManger.shiftTimer.get();
            case 1:
                return 10 - StateManger.shiftTimer.get();
            case 6:
                return 30 - StateManger.shiftTimer.get();

            default:
                return 25 - StateManger.shiftTimer.get();

        }
    }

    @Override
    public void periodic() {
        StateManger.startingTeam();
        StateManger.updateShift();
    }

    public enum Shifts{
        Transition,
        Active,
        Inactive,
        Endgame,
        Disable,
        Auto
    }
}