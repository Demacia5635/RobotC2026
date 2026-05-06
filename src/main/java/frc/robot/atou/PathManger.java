package frc.robot.atou;

import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.demacia.odometry.RobotPose;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.RobotCommon;

public class PathManger {

    private final AutoFactory factory;

    public PathManger(Chassis chassis){
        factory = new AutoFactory(() -> RobotCommon.currentRobotPose, (pose) -> RobotPose.getInstance().resetPose(pose), chassis::followTrajectory, RobotCommon.isRed(), chassis);
    }

    public Command pathOneLeft(){
        return Commands.sequence(factory.resetOdometry("pathOneLeftFirst"), 
        factory.trajectoryCmd("pathOneLeftFirst"),
        factory.trajectoryCmd("pathOneLeftbackToTranch1"),
        factory.trajectoryCmd("pathOneLeftGoToIntake2"),
        factory.trajectoryCmd("pathOneLeftGoeShoting")
        );
    }

    public Command pathOneRight(){
        return Commands.sequence(
            factory.resetOdometry("pathOneRightFirst"),
            factory.trajectoryCmd("pathOneRightFirst"),
            factory.trajectoryCmd("pathOneRightbackToTranch1"),
            factory.trajectoryCmd("pathOnRightGoToIntake2"),
            factory.trajectoryCmd("pathOneRightGoeShoting")
        );
    }

    public Command pathTree(){
        return Commands.sequence(
            factory.resetOdometry("pathTreeToDepo"),
            factory.trajectoryCmd(null)
        );
    }
}