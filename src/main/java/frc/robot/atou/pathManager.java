package frc.robot.atou;

import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.wpilibj2.command.Commands;

public class pathManager {
    
    private AutoFactory firstRightAutoFactory;
    private AutoFactory firstLeftAutoFactory;

    private double MaxToleranceMeter = 0.5;
    private double MaxToleranceRotation = 0.2;

    private path pathNamber;
    private dercsean LeftOrRight;

    public pathManager(path pathNamber, dercsean LeftOrRight) {
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
        AutoRoutine routine = firstRightAutoFactory.newRoutine("firstAtouPathRight");
        AutoTrajectory traj = routine.trajectory("firstAtouPathRight");

        routine.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routine;
    }

    private AutoRoutine firstPathLeft(){
        AutoRoutine routine = firstLeftAutoFactory.newRoutine("firstAtouPathLeft");
        AutoTrajectory traj = routine.trajectory("firstAtouPathLeft");

        routine.active().onTrue(Commands.sequence(
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

        return routine;
    }

    private AutoRoutine secondPathRight(){
        AutoRoutine routine = firstRightAutoFactory.newRoutine("secendPathRight");
        AutoTrajectory traj = routine.trajectory("secendPathRight");

        routine.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routine;
    }

    private AutoRoutine secondPathLeft(){
        AutoRoutine routine = firstLeftAutoFactory.newRoutine("secendPathLeft");
        AutoTrajectory traj = routine.trajectory("secendPathLeft");

        routine.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        )
    );
    
        traj.atPose("startShoting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("depot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routine;
    }

    private AutoRoutine thirdPath(){
        AutoRoutine routine = firstRightAutoFactory.newRoutine("pathThree");
        AutoTrajectory traj = routine.trajectory("pathThree");

        routine.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startShooting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopShooting", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("humanPlayer", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("depot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routine;
    }

        private AutoRoutine fourthPathRight(){
        AutoRoutine routine = firstRightAutoFactory.newRoutine("rightPathFour");
        AutoTrajectory traj = routine.trajectory("rightPathFour");

        routine.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routine;
    }

    
        private AutoRoutine fourthPathLeft(){
        AutoRoutine routine = firstLeftAutoFactory.newRoutine("leftPathFour");
        AutoTrajectory traj = routine.trajectory("leftPathFour");

        routine.active().onTrue(Commands.sequence(
            traj.resetOdometry(),
            traj.cmd()
        ));

        traj.atPose("startIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("stopIntake", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);
        traj.atPose("shoot", MaxToleranceMeter, MaxToleranceRotation).onTrue(null);

        return routine;
    }

}
