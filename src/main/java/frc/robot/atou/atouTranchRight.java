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
public class atouTranchRight extends SequentialCommandGroup {
  /** Creates a new atouTranchRight. */
  List<Translation2d> pathOne;
  List<Translation2d> pathTow;
  List<Translation2d> pathThree;
  public atouTranchRight() {
    this.pathOne = new ArrayList<Translation2d>();
    this.pathTow = new ArrayList<Translation2d>();
    this.pathThree = new ArrayList<Translation2d>();

    pathOne();
    pathTow();
    pathFhoor();

    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    IntakeSubsystem.getInstance().setState(IntakeState.INTAKING);
    Shooter.getInstance().setShooterState(ShooterStates.SHOOTER);
    addCommands(new ParallelRaceGroup(new ShooterCommand(), new WaitCommand(2)),new pathCommand(pathOne), new ParallelRaceGroup(new pathCommand(pathTow), new IntakeCommand()) , new pathCommand(pathThree), new ParallelRaceGroup(new ShooterCommand(), new WaitCommand(2)));
  }

  public void pathOne(){
    pathOne.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 4));
    pathOne.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 0.6));
  }

  public void pathTow(){
    pathTow.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 0.6));
    pathTow.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 7));
  }

  public void pathFhoor(){
    pathOne.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 4));
    pathOne.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 0.6));
    pathTow.add(new Translation2d(atouUtils.addWithRedOrBlue(8), 7));
  }
}
