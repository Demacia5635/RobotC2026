// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// bft-pgmc-wgo
package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.chassis.DriveCommand;
import frc.demacia.utils.controller.CommandController;
import frc.demacia.utils.controller.CommandController.ControllerType;
import frc.robot.chassis.MK5nChassisConstansRobotC;
import frc.robot.intack.commands.IntackCommand;
import frc.robot.intack.subsystems.Intack;
import frc.robot.shinua.commands.ShinuaCommand;
import frc.robot.shinua.subsystems.Shinua;
import frc.robot.shooter.commands.ShooterCommand;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.commands.TurretCommand;
import frc.robot.turret.subsystems.Turret;

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

  // The robot's subsystems and commands are defined here...
  private Turret turret;
  private Shooter shooter;
  private Intack intack;
  private Shinua shinua;

  public static CommandController controller = new CommandController(0, ControllerType.kPS5);
  // public static Turret turret;
  public static DriveCommand driveCommand;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    SmartDashboard.putData("RC", this);
    Chassis.initialize(MK5nChassisConstansRobotC.CHASSIS_CONFIG);
    driveCommand = new DriveCommand(Chassis.getInstance(), controller);
    turret = Turret.getInstance();
    shooter = Shooter.getInstance();
    intack = Intack.getInstance();
    shinua = Shinua.getInstance();

    configureBindings();
    setDefaultCommands();
    setController();
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
    
  }

  private void setDefaultCommands() {
    Chassis.getInstance().setDefaultCommand(driveCommand);
    turret.setDefaultCommand(new TurretCommand());
    shooter.setDefaultCommand(new ShooterCommand());
    intack.setDefaultCommand(new IntackCommand());
    shinua.setDefaultCommand(new ShinuaCommand());
  }

  private void setController() {
    
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("is red", () -> RobotCommon.isRed(), (isRed) -> RobotCommon.setIsRed(isRed));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return null;
  }
}