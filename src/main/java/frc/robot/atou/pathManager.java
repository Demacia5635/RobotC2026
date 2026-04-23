package frc.robot.atou;

import java.util.function.Consumer;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import choreo.trajectory.Trajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.RobotCommon;
import frc.robot.atou.atouUtill.atouUtil;

public class pathManager {
    
    private AutoFactory firstRightAutoFactory;
    private AutoFactory firstLeftAutoFactory;
    
    private double MaxToleranceMeter = 0.5;
    private double MaxToleranceRotation = Math.toRadians(0.7);

    private path pathNamber;
    private dercsean LeftOrRight;

    private AutoFactory factory;

    public <ST> pathManager(path pathNamber, dercsean LeftOrRight, Chassis chassis, Consumer<Pose2d> startPoseConsumer){
        factory = new AutoFactory(atouUtil.pose2DtoSupplierPose2d(RobotCommon.currentRobotPose), startPoseConsumer, chassis::followTrajectory, RobotCommon.isRed(), chassis);
        this.pathNamber = pathNamber;
        this.LeftOrRight = LeftOrRight;
    }

    enum path{
    firstPath,
    secondPath,
    thirdPath,
    fourthPath
    }

    enum dercsean{
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
        AutoRoutine routineSecondPathRight = firstRightAutoFactory.newRoutine("secendPathRight");
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
        AutoRoutine routineSecondPathLeft = firstLeftAutoFactory.newRoutine("secendPathLeft");
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
        AutoRoutine routineThirdPath = firstRightAutoFactory.newRoutine("pathThree");
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
        AutoRoutine routineFourthPathRight = firstRightAutoFactory.newRoutine("rightPathFour");
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
        AutoRoutine routineFourthPathLeft = firstLeftAutoFactory.newRoutine("leftPathFour");
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
