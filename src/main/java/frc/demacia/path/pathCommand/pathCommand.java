// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.demacia.path.pathCommand;

import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;  
import frc.demacia.path.DemaciaTrajectoryGood;
import frc.demacia.utils.chassis.Chassis;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class pathCommand extends Command {
  /** Creates a new pathCommand. */
  Chassis chassis;
  DemaciaTrajectoryGood trajectory;
  List<Translation2d> pathPoint;
  public pathCommand(List<Translation2d> pathPoint) {
    this.pathPoint = pathPoint;
    chassis = Chassis.getInstance();
    this.trajectory = DemaciaTrajectoryGood.getInstance();
    addRequirements(chassis);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    DemaciaTrajectoryGood.initialize(pathPoint);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    chassis.setVelocities(trajectory.calculateSpeeds(chassis.getChassisSpeedsFieldRel(), chassis.getPose()));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    chassis.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (chassis.getPose().getTranslation().minus(trajectory.getEndPoint()).getNorm() < 0.08);
  }
}
