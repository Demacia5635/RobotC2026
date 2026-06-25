// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.demacia.utils.chassis.Chassis;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shinua.subsystems.ShinuaSubsystem;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class setRobotNetralMode extends Command {
  /** Creates a new setRobotNetralMode. */

  private IntakeSubsystem intake;
  private ShinuaSubsystem shinoa;
  private Turret turret;
  private Shooter shooter;
  private Chassis chassis;

  boolean isBrake;

  public setRobotNetralMode(IntakeSubsystem intake, ShinuaSubsystem shinua, Turret turret,Shooter shooter, Chassis chassis) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.chassis = chassis;
    this.intake = intake;
    this.shinoa = shinua;
    this.shooter = shooter;
    this.turret = turret;

    isBrake = true;
  } 

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isBrake = !isBrake;
    chassis.setNeutralMode(isBrake);
    intake.setNeutralModeIntakeDeploy(isBrake);
    shinoa.setNeutralModeMecanum(isBrake);
    shinoa.setNeutralModeRollers(isBrake);
    turret.setNatrelMode(isBrake);
    shooter.setNatralMode(isBrake);
  }
}
