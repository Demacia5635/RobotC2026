package frc.robot.atou;

import static edu.wpi.first.units.Units.Rotation;

import choreo.Choreo;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

public class pathManager {
    
    private AutoFactory firstRightAutoFactory;
    private AutoFactory firstLeftAutoFactory;

    private double MaxToleranceMeter = 0.5;
    private double MaxToleranceRotation = 0.2;

    private int pathNamber;

    public pathManager(int pathNamber) {
        this.pathNamber = pathNamber;
        // firstRightAutoFactory = Choreo.createAutoFactory();
    }

    public AutoRoutine getAuto(){
        if(pathNamber == 1){
            return firstPathRight();
        } else if(pathNamber == 2){
            return firstPathLeft();
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

    


}
