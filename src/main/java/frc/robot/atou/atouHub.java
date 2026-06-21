// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.atou;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.demacia.path.pathCommand.pathCommand;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.intake.commands.IntakeCommand;
import frc.robot.intake.subsystem.IntakeSubsystem;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.shooter.commands.ShooterCommand;
import frc.robot.shooter.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class atouHub extends ParallelRaceGroup {
  /** Creates a new atouHub. */
  List<Translation2d> pathPoint;
  List<Translation2d> pathPont2;
  public atouHub() {
    this.pathPoint = new ArrayList<Translation2d>();
    this.pathPont2 = new ArrayList<Translation2d>();

    pathOne();
    pathTow();
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    IntakeSubsystem.getInstance().setState(IntakeState.INTAKING);
    Shooter.getInstance().setShooterState(ShooterStates.SHOOTER);
    addCommands(new pathCommand(pathPoint), new ParallelRaceGroup(new pathCommand(pathPont2) , new IntakeCommand()), new ShooterCommand());
    addRequirements(Chassis.getInstance(), IntakeSubsystem.getInstance());
  }

  public void pathOne(){
    pathPoint.add(new Translation2d(0.3, 7));
    pathPoint.add(new Translation2d(3.5, 4));
  }

  public void pathTow(){
    pathPont2.add(new Translation2d(3.5, 4));
    pathPont2.add(new Translation2d(0.2, 6));
  }

}
