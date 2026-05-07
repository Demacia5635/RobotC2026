package frc.robot.atou;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.demacia.odometry.RobotPose;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.RobotCommon;

public class PathManger {

    private static PathManger instance;

    private final AutoFactory factory;
    private final AutoChooser chooser;

    public static void initialize(Chassis chassis) {
        if (instance == null)
            instance = new PathManger(chassis);
    }

    public static PathManger getInstance() {
        return instance;
    }

    public PathManger(Chassis chassis){
        factory = new AutoFactory(() -> RobotCommon.currentRobotPose, (pose) -> RobotPose.getInstance().resetPose(pose), chassis::followTrajectory, RobotCommon.isRed(), chassis);
        chooser = new AutoChooser();

        chooser.addCmd("path one left", this::pathOneLeft);
        chooser.addCmd("path one right", this::pathOneRight);
        chooser.addCmd("path tree", this::pathTree);

        SmartDashboard.putData("auto chooser", chooser);
    }

    public Command getCommand(){
        return chooser.selectedCommand();
    }

    private Command pathOneLeft(){
        return Commands.sequence(factory.resetOdometry("pathOneLeftFirst"), 
        factory.trajectoryCmd("pathOneLeftFirst"),
        factory.trajectoryCmd("pathOneLeftbackToTranch1"),
        factory.trajectoryCmd("pathOneLeftGoToIntake2"),
        factory.trajectoryCmd("pathOneLeftGoeShoting")
        );
    }

    private Command pathOneRight(){
        return Commands.sequence(
            factory.resetOdometry("pathOneRightFirst"),
            factory.trajectoryCmd("pathOneRightFirst"),
            factory.trajectoryCmd("pathOneRightbackToTranch1"),
            factory.trajectoryCmd("pathOnRightGoToIntake2"),
            factory.trajectoryCmd("pathOneRightGoeShoting")
        );
    }

    private Command pathTree(){
        return Commands.sequence(
            factory.resetOdometry("pathTreeToDepo"),
            factory.trajectoryCmd("pathTreeToHumen"),
            factory.trajectoryCmd("pathTreeToIntake"),
            factory.trajectoryCmd("pathTreeToShoot")
        );
    }
}