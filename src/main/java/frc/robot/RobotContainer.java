// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// bft-pgmc-wgo
package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.chassis.DriveCommand;
import frc.demacia.utils.controller.CommandController;
import frc.demacia.utils.controller.CommandController.ControllerType;
import frc.robot.intake.commands.IntakeCommand;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shinua.commands.ShinuaCommand;
import frc.robot.shinua.subsystems.ShinuaSubsystem;
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
  public static Chassis chassis = new Chassis(null);
  public static IntakeSubsystem intake = new IntakeSubsystem();
  public static ShinuaSubsystem shinua = new ShinuaSubsystem();
  public static Turret turret = new Turret();
  public static Shooter shooter = new Shooter();
  public static stateManger stateManger = new stateManger();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  CommandController driverController = new CommandController(0, ControllerType.kPS5);
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    chassis = new Chassis(null);
    SmartDashboard.putData("RC", this);
    SmartDashboard.putData("Command Scheduler", CommandScheduler.getInstance());
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
    driverController.rightBumper().onFalse(new RunCommand(()-> stateManger.isWork = true, stateManger){
      @Override
      public void end(boolean interrupted) {
        stateManger.isWork = false;
      }

      @Override
      public boolean isFinished() {
        return !driverController.rightBumper().getAsBoolean();
      }

      @Override
      public boolean runsWhenDisabled() {
        return false;
      }
    });
  }

  private void setDefaultCommands() {
    chassis.setDefaultCommand(new DriveCommand(chassis, driverController));
    intake.setDefaultCommand(new IntakeCommand());
    shinua.setDefaultCommand(new ShinuaCommand());
    shooter.setDefaultCommand(new ShooterCommand(shooter));
    turret.setDefaultCommand(new TurretCommand(turret));
  }

  private void setUserButton() {
    // new Trigger(() -> !DriverStation.isEnabled() && RobotController.getUserButton())
    //     .onTrue(new SetRobotNeutralMode(chassis, intake, shinua, turret, shooter).ignoringDisable(true));
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("is comp", () -> RobotCommon.isComp, (isComp) -> RobotCommon.isComp = isComp);
    builder.addBooleanProperty("is red", () -> RobotCommon.isRed(), (isRed) -> RobotCommon.setIsRed(isRed));
    builder.addBooleanProperty("change is Robot Calibrated for testing", () -> RobotCommon.getRobotCalibrated(),
        (isRobotCalibrated) -> RobotCommon.setIsRobotCalibrated(isRobotCalibrated));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}