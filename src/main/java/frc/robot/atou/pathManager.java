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
        // firstRightAutoFactory = Choreo.createAutoFactory();
    }

    enum path{
    firstPath,
    secondPath,
    thirdPath,
    fourthPath
    }

    enum dercsean{
        left,
        right
    }

    public AutoRoutine getAuto(){
        if(pathNamber == 1){
            if (LeftOrRight == dercsean.right) {
                return firstPathRight();
            }else{
                return firstPathLeft();
            }
        } else if(pathNamber == 2){
            if (LeftOrRight == dercsean.right) {
                return secondPathRight();
            }else{
                return secondPathLeft();
            }
        } else {
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
}
