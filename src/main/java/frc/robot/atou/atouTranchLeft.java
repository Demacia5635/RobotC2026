// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.atou;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.demacia.path.pathCommand.pathCommand;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.intake.commands.IntakeCommand;
import frc.robot.intake.subsystem.IntakeSubsystem;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.shooter.commands.ShooterCommand;
import frc.robot.shooter.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class atouTranchLeft extends SequentialCommandGroup {
  /** Creates a new atouOne. */
  List<Translation2d> point;
  List<Translation2d> pointTrajectory2;
  List<Translation2d> pointTrajectory3;
  public atouTranchLeft() {
    pointTrajectory3= new ArrayList<Translation2d>();
    pointTrajectory2 = new ArrayList<Translation2d>();
    point = new ArrayList<Translation2d>();

    addPointTrajectoryOne();
    addPointTrajectoryTrhee();
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    IntakeSubsystem.getInstance().setState(IntakeState.INTAKING);
    Shooter.getInstance().setShooterState(ShooterStates.SHOOTER);
    addCommands(new ParallelRaceGroup(new ShooterCommand(), new WaitCommand(2)),new pathCommand(point),new ParallelRaceGroup(new pathCommand(pointTrajectory2), new IntakeCommand()),new ParallelRaceGroup(new ShooterCommand(), new WaitCommand(2)), new pathCommand(pointTrajectory3));
  }

  

  public void addPointTrajectoryOne(){
    point.add(new Translation2d(atouUtils.addWithRedOrBlue(4), 7));
    point.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 7));//8
  }

  public void addPointTrajectoryTow(){
    pointTrajectory2.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 7));
    pointTrajectory2.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 1));
  }

  public void addPointTrajectoryTrhee(){
    pointTrajectory3.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 1));
    pointTrajectory3.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 7));
    pointTrajectory3.add(new Translation2d(atouUtils.addWithRedOrBlue(4), 7));
  }
}
