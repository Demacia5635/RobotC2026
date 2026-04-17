package frc.robot.stateManger;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Field;
import frc.robot.RobotCommon;
import frc.robot.RobotContainer;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.stateManger.utils.stateMangerUtils;
import frc.robot.turret.TurretConstants.TurretStates;

public class stateManger extends SubsystemBase{

    public static boolean isWork;
    
   private static boolean isTrench() {
    Pose2d pose = RobotCommon.getRobotFucerPose();
    double x = pose.getX();
    double y = pose.getY();

    return stateMangerUtils.inRange(x, Field.TrenchBlueAudience.X_BACK,  Field.TrenchBlueAudience.X_FRONT)
        || stateMangerUtils.inRange(x, Field.TrenchBlueScoring.X_BACK,   Field.TrenchBlueScoring.X_FRONT)
        || stateMangerUtils.inRange(y, Field.TrenchBlueAudience.Y_FRONT,  Field.TrenchBlueAudience.Y_BACK)
        || stateMangerUtils.inRange(y, Field.TrenchBlueScoring.Y_FRONT,   Field.TrenchBlueScoring.Y_BACK)
        || stateMangerUtils.inRange(x, Field.TrenchRedAudience.X_FRONT,  Field.TrenchRedAudience.X_BACK)
        || stateMangerUtils.inRange(x, Field.TrenchRedScoring.X_FRONT,   Field.TrenchRedScoring.X_BACK)
        || stateMangerUtils.inRange(y, Field.TrenchRedAudience.Y_FRONT,  Field.TrenchRedAudience.Y_BACK)
        || stateMangerUtils.inRange(y, Field.TrenchRedScoring.Y_FRONT,   Field.TrenchRedScoring.Y_BACK);
}


    private static boolean isHub(){
        return stateMangerUtils.isPastHub(RobotCommon.currentRobotPose.getX(), RobotCommon.currentRobotPose.getY(), Field.HubBlue.CENTER) ||stateMangerUtils.isPastHub(RobotCommon.currentRobotPose.getX(), RobotCommon.currentRobotPose.getY(), Field.HubRed.CENTER);
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
                    RobotContainer.intake.setState(IntakeState.DEPLOYED);
                }
                else if(isDeleveryAndIntakeBlue()){
                    RobotContainer.shooter.setShooterState(ShooterStates.DELIVERY);
                    RobotContainer.turret.setState(TurretStates.DELIVERY);
                    RobotContainer.intake.setState(IntakeState.INTAKING);
                }
        }
    }  
}
}