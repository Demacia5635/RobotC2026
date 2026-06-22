// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotCommon;
import frc.robot.ShootingWhileDriving;
import frc.robot.shooter.ShooterConstants.FeederConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.shooter.ShooterConstants.HoodConstants;
// import frc.robot.shooter.ShooterConstants.IndexerConstants;
import frc.robot.shooter.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterCommand extends Command {
  private Shooter shooter;
  private double flywheelVelocity = 0;
  private double hoodPosition = 0;
  private double feederPower = 0;
  private double testingFlywheelVelocity = 0;
  private double testingHoodPosition = 0;
  private double testingFeederPower = 0;
  private Translation2d shooterToTarget;//TODO be in robot common

  /** Creates a new ShooterCommand. */
  public ShooterCommand() {
    this.shooter =  Shooter.getInstance();
    addRequirements(shooter);
    SmartDashboard.putData("shooter command", this);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.addDoubleProperty("FlywheelVelocity", () -> testingFlywheelVelocity, (vel) -> testingFlywheelVelocity = vel);
      builder.addDoubleProperty("HoodPosition", () -> Math.toDegrees(testingHoodPosition), (position) -> testingHoodPosition = Math.toRadians(position));
      builder.addDoubleProperty("feedrPower", () -> testingFeederPower, (power) -> testingFeederPower = power);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shooter.getShooterState()) {
      case onePoint:
          flywheelVelocity = 10.8;
          hoodPosition = 34;
          feederPower = 1;
        break;
      case SHOOTER:
        //TODO: Change the position of the calculate
        ShootingWhileDriving.calculate(RobotCommon.getHubPose());
        flywheelVelocity = ShootingWhileDriving.getFlywheelVel();
        hoodPosition = ShootingWhileDriving.getHoodAngle();
        feederPower = FeederConstants.MAX_FEEDER_POWER;
        break;
      case IDLE:
        flywheelVelocity = 0;
        hoodPosition = 0;
        feederPower = 0;
        break;
      case TEST:
        flywheelVelocity = testingHoodPosition;
        hoodPosition = testingHoodPosition;
        feederPower = testingFeederPower;
        break;
      case DELIVERY:
        testingFeederPower = FeederConstants.MAX_FEEDER_POWER;
        shooterToTarget = RobotCommon.getDelveryPose().getTranslation();
        shooterToTarget = shooterToTarget.minus(new Translation2d(RobotCommon.getRobotFucerSpeed(0.02).vxMetersPerSecond * 1.2, RobotCommon.getFieldRelativeSpeeds().vyMetersPerSecond * 1.2));
        hoodPosition = 45;
        flywheelVelocity = FlywheelConstants.MAX_FLYWHEEL_POWER;
        feederPower = 1;
        break;
      case TRANCH:
        hoodPosition = HoodConstants.MIN_POSITION;
        feederPower = 0;
        break;
    }
    shooter.setFlywheelVelocity(flywheelVelocity);
    shooter.setHoodMotion(hoodPosition);
    shooter.setFeederPower(feederPower);
    if (shooter.getFeederCurrent() > FeederConstants.MAX_FEEDER_CURRENT && Math.abs(shooter.getFeederVelocity()) < FeederConstants.MIN_FEEDER_VELOCITY){
      shooter.stopFeeder();
    }
    if (shooter.getHoodCurrent() > HoodConstants.MAX_HOOD_CURRENT && Math.abs(shooter.getHoodVelocity()) < HoodConstants.MIN_HOOD_VELOCITY){
      shooter.stopHood();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopAll();
  }
}
