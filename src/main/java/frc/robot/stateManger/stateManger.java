package frc.robot.stateManger;

import java.io.ObjectInputFilter.Status;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
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
        return DriverStation.getMatchTime();
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
        if(isWork){
            if(!isTrench()){
                if(isHub()){
                    RobotContainer.shooter.setShooterState(ShooterStates.SHOOTER);
                    RobotContainer.turret.setState(TurretStates.SHOOTING);
                    RobotContainer.intakeSubsystem.setState(IntakeState.DEPLOYED);
                }
                else if(isDeleveryAndIntakeBlue()){
                    RobotContainer.shooter.setShooterState(ShooterStates.DELIVERY);
                    RobotContainer.turret.setState(TurretStates.DELIVERY);
                    RobotContainer.intakeSubsystem.setState(IntakeState.INTAKING);
                }
        }else{
            RobotContainer.shooter.setShooterState(ShooterStates.TRANCH);
        }
    }else{
        RobotContainer.shooter.setShooterState(ShooterStates.IDLE);
        RobotContainer.turret.setState(TurretStates.IDLE);
        RobotContainer.intakeSubsystem.setState(IntakeState.IDLE);
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

public enum Shifts{
    Transition,
    Active,
    Inactive,
    Endgame,
    Disable
}

}