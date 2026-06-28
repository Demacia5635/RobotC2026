// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

// bft-pgmc-wgo
package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.chassis.DriveCommand;
import frc.demacia.utils.controller.CommandController;
import frc.demacia.utils.controller.CommandController.ControllerType;
import frc.demacia.utils.leds.LedManager;
import frc.demacia.utils.log.LogManager;
import frc.robot.RobotCommon.StartingPlaces;
import frc.robot.chassis.MK5nChassisConstansRobotC;
import frc.robot.intake.IntakeConstants;
import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.intake.commands.IntakeCommand;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.led.RobotCLedStrip;
import frc.robot.shinua.ShinuaConstants.ShinuaState;
import frc.robot.shinua.commands.ShinuaCommand;
import frc.robot.shinua.subsystems.ShinuaSubsystem;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.shooter.commands.ShooterCommand;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.stateManger.StateManger;
import frc.robot.stateManger.StateManger.Shifts;
import frc.robot.stateManger.utils.stateMangerUtils;
import frc.robot.turret.TurretConstants.TurretStates;
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

  public static boolean isComp = false;
  private static boolean hasRemovedFromLog = false;
  public static boolean isRed = false;

  // The robot's subsystems and commands are defined here...
  public static ShinuaSubsystem shinua;
  public static IntakeSubsystem intake;
  public static IntakeCommand intakeCommand;
  public static CommandController controller = new CommandController(0, ControllerType.kPS5);
  public static Shooter shooter;
  // public static Turret turret;
  public static DriveCommand driveCommand;
  public static Timer autoTimer;
  public static Timer timerToClose;
  public static boolean hasAutoClosed;
  public static boolean hasAutoClosed2;
  public static SendableChooser<Command> autoChooser;
  public static LedManager ledManager;
  private static RobotCLedStrip mainLed;

  public static boolean forcedIsReady = false;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    SmartDashboard.putData("RC", this);
    new StateManger();
    // new DemaciaUtils(() -> getIsComp(), () -> getIsRed());
    Chassis.initialize(MK5nChassisConstansRobotC.CHASSIS_CONFIG);
    intake = IntakeSubsystem.getInstance();
    shooter = Shooter.getInstance();
    shinua = ShinuaSubsystem.getInstance();
    // turret = Turret.getInstance();
    autoChooser = new SendableChooser<>();
    driveCommand = new DriveCommand(Chassis.getInstance(), controller);
    SmartDashboard.putData("atou chooser", autoChooser);
    SmartDashboard.putData("reset Shift", new InstantCommand(() -> StateManger.resetShift()).ignoringDisable(true));
    autoTimer = new Timer();
    timerToClose = new Timer();
    // Configure the trigger bindings

    ledManager = new LedManager();
    mainLed = new RobotCLedStrip(ledManager);
    mainLed.startShift();

    configureBindings();
    // setUserButton();
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
    SmartDashboard.putData("state defance", new InstantCommand(() -> {
      intake.setState(IntakeState.CLOSED);
      intake.setNeutralModeIntakeDeploy(true);
    }));
    Shifts[] lastShift = { StateManger.getCurrenTShifts() };
    new Trigger(() -> {
      Shifts current = StateManger.getCurrenTShifts();
      if (current != lastShift[0]) {
        lastShift[0] = current;
        return true;
      }
      return false;
    }).onTrue(new InstantCommand(() -> mainLed.startShift()).ignoringDisable(true));
  }

  // private void setUserButton() {
  //   new Trigger(()-> !DriverStation.isEnabled() &&
  //   RobotController.getUserButton()).onTrue(new setRobotNetralMode(intake,
  //   shinua, /*turret,*/ shooter, Chassis.getInstance()));
  // }

  private void setDefaultCommands() {
    Chassis.getInstance().setDefaultCommand(driveCommand);
    shinua.setDefaultCommand(new ShinuaCommand());
    intake.setDefaultCommand(new IntakeCommand());
    shooter.setDefaultCommand(new ShooterCommand());
    // turret.setDefaultCommand(new TurretCommand());

    // shinua.setDefaultCommand(new
    // frc.robot.shinua.commands.ControllerCommand(controller));
    // intake.setDefaultCommand(new
    // frc.robot.intake.commands.ControllerCommand(controller));
    // shooter.setDefaultCommand(new
    // frc.robot.shooter.commands.commandContorller(controller));
    // turret.setDefaultCommand(new
    // frc.robot.turret.commands.ControllerCommand(controller));
  }

  private void setController() {
    controller.rightButton().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.INTAKING);
      shooter.setShooterState(ShooterStates.IDLE);
      shinua.setState(ShinuaState.NO_INDEXER);
      // turret.setState(TurretStates.IDLE);
      intake.setNeutralModeIntakeDeploy(true);
    })); //
    controller.leftButton().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.MIDDLE);
      shinua.setState(ShinuaState.ONLY_ROLLERS);
      shooter.setShooterState(ShooterStates.IDLE);
      // turret.setState(TurretStates.IDLE);
      intake.setNeutralModeIntakeDeploy(false);
    }));
    controller.upButton().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.EJECTING);
      shinua.setState(ShinuaState.EJECTING);
      shooter.setShooterState(ShooterStates.IDLE);
      // turret.setState(TurretStates.IDLE);
      intake.setNeutralModeIntakeDeploy(false);
    }));
    controller.downButton().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.INTAKING);
      shinua.setState(ShinuaState.SHINUA_ON);
      shooter.setShooterState(ShooterStates.DELIVERY);
      // turret.setState(TurretStates.DELIVERY);
      intake.setNeutralModeIntakeDeploy(true);
    }));
    controller.rightBumper().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.INTAKING);
      shinua.setState(ShinuaState.ONLY_ROLLERS);
      shooter.setShooterState(ShooterStates.DELIVERY);
      // turret.setState(TurretStates.DELIVERY);
      intake.setNeutralModeIntakeDeploy(true);
    }));
    controller.povUp().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.SHOOTING);
      shinua.setState(ShinuaState.SHINUA_ON);
      shooter.setShooterState(ShooterStates.onePoint);
      // turret.setState(TurretStates.IDLE);
      intake.setNeutralModeIntakeDeploy(false);
    }));

    // controller.povDown().onTrue(new InstantCommand(() -> {
    //   intake.setState(IntakeState.SHOOTING);
    //   shinua.setState(ShinuaState.SHINUA_ON);
    //   shooter.setShooterState(ShooterStates.towPoint);
    //   intake.setNeutralModeIntakeDeploy(false);
    // }));

    controller.povRight().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.SHOOTING);
      shinua.setState(ShinuaState.SHINUA_ON);
      shooter.setShooterState(ShooterStates.thrrePoint);
      // turret.setState(TurretStates.TRENCH_RIGHT);
      intake.setNeutralModeIntakeDeploy(false);
    }));
    controller.leftBumper().onTrue(new InstantCommand(() -> driveCommand.precisionMode = !driveCommand.precisionMode));

    controller.povDown().onTrue(new InstantCommand(() -> {
      shooter.setShooterState(ShooterStates.GET_READY);
      intake.setNeutralModeIntakeDeploy(false);
      // turret.setState(TurretStates.IDLE);
    }));
    controller.povLeft().onTrue(new InstantCommand(() -> {
      intake.setState(IntakeState.SHOOTING);
      shinua.setState(ShinuaState.SHINUA_ON);
      shooter.setShooterState(ShooterStates.thrrePoint);
      // turret.setState(TurretStates.TRENCH_LEFT);
      intake.setNeutralModeIntakeDeploy(false);
    }));
    // controller.getRightTrigger(0.1).onTrue(new InstantCommand(() -> {
    //   turret.add90ToDeliveryAngle();
    //   shinua.setState(ShinuaState.ONLY_ROLLERS);
    //   shooter.setShooterState(ShooterStates.DELIVERY_ONLY_FLYWEEL);
    //   turret.setState(TurretStates.IDLE);
    // }));
    // controller.getLeftTrigger(0.1).onTrue(new InstantCommand(() -> {
    //   turret.subtract90ToDeliveryAngle();
    //   shinua.setState(ShinuaState.ONLY_ROLLERS);
    //   shooter.setShooterState(ShooterStates.DELIVERY_ONLY_FLYWEEL);
    //   turret.setState(TurretStates.IDLE);
    // }));
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

  public static boolean getIsForcedReady() {
    return forcedIsReady;
  }

  public static void setForcedReady(boolean newForcedIsReady) {
    forcedIsReady = newForcedIsReady;
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("is comp", () -> RobotCommon.isComp, (isComp) -> RobotCommon.isComp = isComp);
    builder.addBooleanProperty("is red", () -> RobotCommon.isRed(), (isRed) -> RobotCommon.setIsRed(isRed));
    builder.addBooleanProperty("is force ready", () -> getIsForcedReady(),
        (forcedIsReady) -> setForcedReady(forcedIsReady));
    builder.addDoubleProperty("Time Left", () -> StateManger.getTimeLeft(), null);
    builder.addDoubleProperty("shift time left", () -> StateManger.getTheTimeLeft(), null);
    builder.addBooleanProperty("is our hub", () -> StateManger.isOurHub(), null);
    builder.addStringProperty("get current shift", () -> StateManger.getCurrenTShifts().toString(), null);
  }

  private void resetPreAuto() {
    Chassis.getInstance().setYaw(
        RobotCommon.isRed()
            ? (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? (Rotation2d.kZero)
                : (RobotCommon.getStartingPlace().equals(StartingPlaces.TRANCH_LEFT)
                    ? new Rotation2d(Math.toRadians(90))
                    : new Rotation2d(Math.toRadians(-90))))
            : (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? (Rotation2d.kPi)
                : (RobotCommon.getStartingPlace().equals(StartingPlaces.TRANCH_LEFT)
                    ? new Rotation2d(Math.toRadians(-90))
                    : new Rotation2d(Math.toRadians(90)))));
    shooter.restHoodMotor();
  }

  private void resetPreAutoTurret90Deg() {
    Chassis.getInstance().setYaw(
        RobotCommon.isRed()
            ? (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? (new Rotation2d(Math.toRadians(-90)))
                : (RobotCommon.getStartingPlace().equals(StartingPlaces.TRANCH_LEFT)
                    ? Rotation2d.kZero
                    : Rotation2d.kPi))
            : (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? (new Rotation2d(Math.toRadians(90)))
                : (RobotCommon.getStartingPlace().equals(StartingPlaces.TRANCH_LEFT)
                    ? Rotation2d.kPi
                    : Rotation2d.kZero)));
    shooter.restHoodMotor();
    // turret.turretMotor.setEncoderPosition(Math.toRadians(180));
    // turret.setCaliberation(true);
    intake.setEncoderIntakeDeploy(IntakeConstants.INTAKE_DEPLOY_OFFSET);
    intake.setCalibrated();
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // autoTimer.reset();
    // timerToClose.reset();
    // hasAutoClosed = false;
    // hasAutoClosed2 = false;

    return new SequentialCommandGroup(
        new InstantCommand(() -> {
          resetPreAuto();
          // turret.setState(TurretStates.IDLE);
        }),
        new InstantCommand(() -> {
          shooter.setShooterState(ShooterStates.GET_READY);
          shinua.setState(ShinuaState.ONLY_ROLLERS);
        }), 

        new RunCommand(() -> {
          Chassis.getInstance().setRobotRelVelocities(new ChassisSpeeds(0.5, 0, 0));
        }, 
        Chassis.getInstance())
        .withTimeout(1.05),

        new InstantCommand(() -> Chassis.getInstance().stop()),
        new InstantCommand(() -> {
          shooter.setShooterState(ShooterStates.onePoint);
          shinua.setState(ShinuaState.SHINUA_ON);
          intake.setState(IntakeState.SHOOTING);
        }),

        new WaitCommand(3).andThen(
          new InstantCommand(() -> {
          shinua.setState(ShinuaState.ONLY_ROLLERS);
          intake.setState(IntakeState.CLOSED_SHOOTING);
        })),

        new WaitUntilCommand(() -> intake.getIntakeDeployAngle() < Math.toRadians(-25)).andThen(
          new InstantCommand(()->{
            shinua.setState(ShinuaState.SHINUA_ON);
            intake.setState(IntakeState.SHOOTING);
        })),

        new WaitCommand(3).andThen(
          new InstantCommand(() -> {
            shinua.setState(ShinuaState.ONLY_ROLLERS);
            intake.setState(IntakeState.CLOSED_SHOOTING);
        })),
        new WaitUntilCommand(() -> intake.getIntakeDeployAngle() < Math.toRadians(-25)).andThen(
          new InstantCommand(()->{
            shinua.setState(ShinuaState.SHINUA_ON);
            intake.setState(IntakeState.SHOOTING);
        })));



        // return new SequentialCommandGroup(
        // new InstantCommand(() -> {
        //   resetPreAutoTurret90Deg();
        // }),
        // new InstantCommand(() -> {
        //   shooter.setShooterState(ShooterStates.GET_READY);
        //   shinua.setState(ShinuaState.ONLY_ROLLERS);
        // }), new RunCommand(() -> {
        //   Chassis.getInstance().setRobotRelVelocities(new ChassisSpeeds(0, 0.5, 0));
        // }, Chassis.getInstance()).withTimeout(1.05),
        // new InstantCommand(() -> Chassis.getInstance().stop()),
        // new InstantCommand(() -> {
        //   shooter.setShooterState(ShooterStates.onePoint);
        //   shinua.setState(ShinuaState.SHINUA_ON);
        //   intake.setState(IntakeState.SHOOTING);
        // }),
        // new WaitCommand(3).andThen(new InstantCommand(() -> {
        //   shinua.setState(ShinuaState.ONLY_ROLLERS);
        //   intake.setState(IntakeState.CLOSED_SHOOTING);
        // })), new WaitUntilCommand(() -> intake.getIntakeDeployAngle() < Math.toRadians(-25)).andThen(new InstantCommand(()->{
        //   shinua.setState(ShinuaState.SHINUA_ON);
        //   intake.setState(IntakeState.SHOOTING);
        // })),
        // new WaitCommand(3).andThen(new InstantCommand(() -> {
        //   shinua.setState(ShinuaState.ONLY_ROLLERS);
        //   intake.setState(IntakeState.CLOSED_SHOOTING);
        // })), new WaitUntilCommand(() -> intake.getIntakeDeployAngle() < Math.toRadians(-25)).andThen(new InstantCommand(()->{
        //   shinua.setState(ShinuaState.SHINUA_ON);
        //   intake.setState(IntakeState.SHOOTING);
        // })));

    // An example command will be run in autonomouP
    // return new RunCommand(() -> {
    //   if (autoTimer.hasElapsed(1.05)) {
    //     autoTimer.stop();
    //     Chassis.getInstance().setVelocities(new ChassisSpeeds());

    //     shooter.setShooterState(
    //         RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? ShooterStates.onePoint
    //             : ShooterStates.thrrePoint);

    //     if (timerToClose.hasElapsed(6)) {
    //       timerToClose.stop();

    //       if (intake.getIntakeDeployAngle() < Math.toRadians(-24)) {
    //         intake.setState(IntakeState.SHOOTING);
    //         shinua.setState(ShinuaState.SHINUA_ON);
    //         hasAutoClosed2 = true;
    //       } else if (!hasAutoClosed2) {
    //         shinua.setState(ShinuaState.ONLY_ROLLERS);
    //         intake.setState(IntakeState.CLOSED_SHOOTING);
    //       }
    //     } else if (timerToClose.hasElapsed(3)) {
    //       if (intake.getIntakeDeployAngle() < Math.toRadians(-24)) {
    //         intake.setState(IntakeState.SHOOTING);
    //         shinua.setState(ShinuaState.SHINUA_ON);
    //         hasAutoClosed = true;
    //       } else if (!hasAutoClosed) {
    //         shinua.setState(ShinuaState.ONLY_ROLLERS);
    //         intake.setState(IntakeState.CLOSED_SHOOTING);
    //       }
    //     } else if (!timerToClose.isRunning()) {
    //       timerToClose.restart();
    //     } else {
    //       intake.setState(IntakeState.SHOOTING);
    //       shinua.setState(ShinuaState.SHINUA_ON);
    //     }
    //   } else if (!autoTimer.isRunning()) {
    //     Chassis.getInstance().setYaw(
    //         RobotCommon.isRed()
    //             ? (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? (Rotation2d.kZero)
    //                 : (RobotCommon.getStartingPlace().equals(StartingPlaces.TRANCH_LEFT)
    //                     ? new Rotation2d(Math.toRadians(90))
    //                     : new Rotation2d(Math.toRadians(-90))))
    //             : (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB) ? (Rotation2d.kPi)
    //                 : (RobotCommon.getStartingPlace().equals(StartingPlaces.TRANCH_LEFT)
    //                     ? new Rotation2d(Math.toRadians(-90))
    //                     : new Rotation2d(Math.toRadians(90)))));
    //     shooter.restHoodMotor();

    //     autoTimer.restart();
    //   } else {
    //     shooter.setShooterState(ShooterStates.GET_READY);
    //     shinua.setState(ShinuaState.ONLY_ROLLERS);
    //     if (RobotCommon.getStartingPlace().equals(StartingPlaces.HUB))
    //       Chassis.getInstance().setVelocities(new ChassisSpeeds(0.5, 0, 0));
    //   }
    // }
  }
}