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
import frc.demacia.utils.log.LogManager;
import frc.robot.RobotCommon;
import frc.robot.atou.atouUtill.atouUtil;

public class pathManager {

    
    private double MaxToleranceMeter = 0.5;
    private double MaxToleranceRotation = Math.toRadians(0.7);

    public path pathNamber;
    public dercsean LeftOrRight; // Direction fix

    private AutoFactory factory;

    public pathManager(Chassis chassis){
        factory = new AutoFactory(() -> RobotCommon.currentRobotPose, (pose) -> RobotPose.getInstance().resetPose(pose), chassis::followTrajectory, true, chassis);
        addToNetworkTablePath();
        addToNetworkTableDercsean();
    }

    public void addToNetworkTablePath(){
        SendableChooser<path> pathChooser = new SendableChooser<>();
        pathChooser.addOption("firstPath", path.firstPath);
        pathChooser.addOption("secondPath", path.secondPath);
        pathChooser.addOption("thirdPath", path.thirdPath);
        pathChooser.addOption("fourthPath", path.fourthPath);
        pathChooser.onChange(newPath -> setPath(newPath));
        SmartDashboard.putData("Path Chooser", pathChooser);

        pathNamber = path.secondPath;
    }

    public void addToNetworkTableDercsean(){
        SendableChooser<dercsean> dercseanChooser = new SendableChooser<>();
        dercseanChooser.addOption("left", dercsean.left);
        dercseanChooser.addOption("right", dercsean.right);
        dercseanChooser.addOption("center", dercsean.center);
        dercseanChooser.onChange(newDercsean -> setLeftOrRight(newDercsean));
        SmartDashboard.putData("Dercsean Chooser", dercseanChooser);
        LeftOrRight = dercsean.left;
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

    public void setLeftOrRight(dercsean dercsean){
        this.LeftOrRight = dercsean;
    }

    public void setPath(path path){
        this.pathNamber = path;
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
                LogManager.log("sude run");
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
        AutoRoutine routineFirstPathLeft = factory.newRoutine("firstAtouPathLeft");
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
        AutoRoutine routineSecondPathRight = factory.newRoutine("secendPathRight");
        AutoTrajectory traj = routineSecondPathRight.trajectory("secendPathRight");

        routineSecondPathRight.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        // traj.atPose("startShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        // traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        // traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        // traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineSecondPathRight;
    }

    private AutoRoutine secondPathLeft(){
        AutoRoutine routineSecondPathLeft = factory.newRoutine("secendPathLeft");
        AutoTrajectory traj = routineSecondPathLeft.trajectory("secendPathLeft");

        LogManager.log("------------------------------------------\n" + 
            
        " Trajectory " + traj.getInitialPose() + " t="
          + traj.toString() + " l="
          + traj.getRawTrajectory().getPoses().length + 
          " \n-------------------------------------------------------\n");

        routineSecondPathLeft.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        )
    );
    
        // traj.atPose("startShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        // traj.atPose("depot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        // traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        // traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routineSecondPathLeft;
    }

    private AutoRoutine thirdPath(){
        AutoRoutine routineThirdPath = factory.newRoutine("pathThree");
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
        AutoRoutine routineFourthPathRight = factory.newRoutine("rightPathFour");
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
        AutoRoutine routineFourthPathLeft = factory.newRoutine("leftPathFour");
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
