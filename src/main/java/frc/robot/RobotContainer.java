// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// bft-pgmc-wgo
package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.chassis.DriveCommand;
import frc.demacia.utils.controller.CommandController;
import frc.demacia.utils.controller.CommandController.ControllerType;
import frc.demacia.utils.log.LogManager;
import frc.robot.chassis.MK5nChassisConstansRobotC;
import frc.robot.chassis.RobotBChassisConstants;
import frc.robot.intake.commands.IntakeCommand;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shinua.commands.ShinuaCommand;
import frc.robot.shinua.subsystems.ShinuaSubsystem;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.shooter.commands.ShooterCommand;
import frc.robot.shooter.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer implements Sendable {

  public static boolean isComp = false;
  private static boolean hasRemovedFromLog = false;
  public static boolean isRed = false;

  // The robot's subsystems and commands are defined here...
  public static ShinuaSubsystem shinuaSubsystem;
  public static IntakeSubsystem intakeSubsystem;
  public static IntakeCommand intakeCommand;
  public static CommandController controller = new CommandController(0, ControllerType.kPS5); 
  public static Shooter shooter;

  public static SendableChooser<Command> autoChooser;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    SmartDashboard.putData("RC", this);
    // new DemaciaUtils(() -> getIsComp(), () -> getIsRed());
    Chassis.initialize(RobotBChassisConstants.CHASSIS_CONFIG);
    // intakeSubsystem = IntakeSubsystem.getInstance();
    // shooter = Shooter.getInstance();
    autoChooser = new SendableChooser<>();
    
    SmartDashboard.putData("atou chooser", autoChooser);

    // Configure the trigger bindings
    configureBindings();
    setUserButton();
    setDefaultCommands();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be
   * created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
   * an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
   * {@link
   * CommandXboxController
   * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or
   * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */


  private void configureBindings() {
    // controller.povUp().onTrue(new InstantCommand(()->  shooter.setShooterState(ShooterStates.towPoint)));
    // controller.povRight().onTrue(new InstantCommand(()-> shooter.setShooterState(ShooterStates.onePoint)));
    // controller.povLeft().onTrue(new InstantCommand(()-> shooter.setShooterState(ShooterStates.thrrePoint)));
    
  } 

  private void setUserButton(){
    
  }

  private void setDefaultCommands() {
    Chassis.getInstance().setDefaultCommand(new DriveCommand(Chassis.getInstance(), controller));
    shinuaSubsystem.setDefaultCommand(new ShinuaCommand());
    intakeSubsystem.setDefaultCommand(new IntakeCommand());
    shooter.setDefaultCommand(new ShooterCommand());
    // turret.setDefaultCommand(new TurretCommand(turret));
  }

  public static void setIsRed(boolean isRed) {
    RobotContainer.isRed = isRed;
  }

  public static boolean getIsComp() {
    return isComp;
  }

  public static void setIsComp(boolean isComp) {
    RobotContainer.isComp = isComp;
    if (!hasRemovedFromLog && isComp) {
      hasRemovedFromLog = true;
      LogManager.removeInComp();
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("is comp", () -> RobotCommon.isComp, (isComp) -> RobotCommon.isComp = isComp);
    builder.addBooleanProperty("is red", () -> RobotCommon.isRed(), (isRed) -> RobotCommon.setIsRed(isRed));
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomouP
    return autoChooser.getSelected();
  }
}