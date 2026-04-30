package frc.robot.atou;

import java.util.function.Consumer;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import choreo.trajectory.Trajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.demacia.odometry.RobotPose;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.RobotCommon;
import frc.robot.atou.atouUtill.atouUtil;

public class pathManager {

    
    private double MaxToleranceMeter = 0.5;
    private double MaxToleranceRotation = Math.toRadians(0.7);

    private path pathNamber;
    private dercsean LeftOrRight;

    private AutoFactory factory;
    private AutoFactory firstRightAutoFactory; 
    private AutoFactory firstLeftAutoFactory; 
    private AutoFactory routineSecondPathLeft;
    private AutoFactory routineSecondPathRight;
    private AutoFactory routineThirdPath;
    private AutoFactory routineFourthPathRight;
    private AutoFactory routineFourthPathLeft;

    public <ST> pathManager(Chassis chassis){
        factory = new AutoFactory(() -> RobotCommon.currentRobotPose, (pose) -> RobotPose.getInstance().resetPose(pose), chassis::followTrajectory, RobotCommon.isRed(), chassis);

        firstRightAutoFactory = factory;
        firstLeftAutoFactory = factory;  
        routineSecondPathLeft = factory;
        routineSecondPathRight = factory;
        routineThirdPath = factory;
        routineFourthPathRight = factory;
        routineFourthPathLeft = factory;

        addToNetworkTablePath();
        addToNetworkTableDercsean();
    }

    public void addToNetworkTablePath(){
        SendableChooser<path> pathChooser = new SendableChooser<>();
        pathChooser.addOption("firstPath", path.firstPath);
        pathChooser.addOption("secondPath", path.secondPath);
        pathChooser.addOption("thirdPath", path.thirdPath);
        pathChooser.addOption("fourthPath", path.fourthPath);
        pathChooser.onChange(newPath -> this.pathNamber = newPath);
        SmartDashboard.putData("Path Chooser", pathChooser);
    }

    public void addToNetworkTableDercsean(){
        SendableChooser<dercsean> dercseanChooser = new SendableChooser<>();
        dercseanChooser.addOption("left", dercsean.left);
        dercseanChooser.addOption("right", dercsean.right);
        dercseanChooser.addOption("center", dercsean.center);
        dercseanChooser.onChange(newDercsean -> this.LeftOrRight = newDercsean);
        SmartDashboard.putData("Dercsean Chooser", dercseanChooser);
    }

    public enum path{
    firstPath,
    secondPath,
    thirdPath,
    fourthPath
    }

    public enum dercsean{
        left,
        right,
        center
    }



    public AutoRoutine getAuto(){
        if(pathNamber == path.firstPath){
            if (LeftOrRight == dercsean.right) {
                return firstPathRight();
            }else{
                return firstPathLeft();
            }
        } else if(pathNamber == path.secondPath){
            if (LeftOrRight == dercsean.right) {
                return secondPathRight();
            }else{
                return secondPathLeft();
            }
        } else if(pathNamber == path.thirdPath){
                return thirdPath();
        } else if(pathNamber == path.fourthPath){
            if (LeftOrRight == dercsean.right) {
                return fourthPathRight();
            }else{
                return fourthPathLeft();
            }
        }else{
            return null;
        }
    }

    private AutoRoutine firstPathRight(){
        AutoRoutine routineFirstPathRight = factory.newRoutine("firstAtouPathRight");
        AutoTrajectory traj = routineFirstPathRight.trajectory("firstAtouPathRight");

        routineFirstPathRight.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineFirstPathRight;
    }

    private AutoRoutine firstPathLeft(){
        AutoRoutine routineFirstPathLeft = firstLeftAutoFactory.newRoutine("firstAtouPathLeft");
        AutoTrajectory traj = routineFirstPathLeft.trajectory("firstAtouPathLeft");

        routineFirstPathLeft.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        )
    );
    
        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("shootEnd", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("Depot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineFirstPathLeft;
    }

    private AutoRoutine secondPathRight(){
        AutoRoutine routineSecondPathRight = routineSecondPathRight.newRoutine("secendPathRight");
        AutoTrajectory traj = routineSecondPathRight.trajectory("secendPathRight");

        routineSecondPathRight.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineSecondPathRight;
    }

    private AutoRoutine secondPathLeft(){
        AutoRoutine routineSecondPathLeft = routineSecondPathLeft.newRoutine("secendPathLeft");
        AutoTrajectory traj = routineSecondPathLeft.trajectory("secendPathLeft");

        routineSecondPathLeft.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        )
    );
    
        traj.atPose("startShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("depot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineSecondPathLeft;
    }

    private AutoRoutine thirdPath(){
        AutoRoutine routineThirdPath = routineThirdPath.newRoutine("pathThree");
        AutoTrajectory traj = routineThirdPath.trajectory("pathThree");

        routineThirdPath.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startShooting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopShooting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("depot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineThirdPath;
    }

        private AutoRoutine fourthPathRight(){
        AutoRoutine routineFourthPathRight = routineFourthPathRight.newRoutine("rightPathFour");
        AutoTrajectory traj = routineFourthPathRight.trajectory("rightPathFour");

        routineFourthPathRight.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineFourthPathRight;
    }

    
        private AutoRoutine fourthPathLeft(){
        AutoRoutine routineFourthPathLeft = routineFourthPathLeft.newRoutine("leftPathFour");
        AutoTrajectory traj = routineFourthPathLeft.trajectory("leftPathFour");

        routineFourthPathLeft.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineFourthPathLeft;
    }

}
