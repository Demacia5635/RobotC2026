// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.commands;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotCommon;
import frc.robot.ShootingWhileDriving;
import frc.robot.shooter.ShooterConstants.FeederConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.shooter.ShooterConstants.HoodConstants;
import frc.robot.shooter.ShooterConstants.IndexerConstants;
import frc.robot.shooter.subsystems.Shooter;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterCommand extends Command {
  private Shooter shooter;
  private double flywheelVelocity = 0;
  private double hoodPosition = 0;
  private double indexerPower = 0;
  private double feederPower = 0;
  private Translation2d shooterToTarget;//TODO be in robot common

  /** Creates a new ShooterCommand. */
  public ShooterCommand(Shooter shooter) {
    this.shooter = shooter;
    
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.addDoubleProperty("FlywheelVelocity", () -> flywheelVelocity, (vel) -> flywheelVelocity = vel);
      builder.addDoubleProperty("HoodPosition", () -> hoodPosition, (position) -> hoodPosition = position);
      builder.addDoubleProperty("IndexerPower", () -> indexerPower, (power) -> indexerPower = power);
      builder.addDoubleProperty("FeederPower", () -> feederPower, (power) -> feederPower = power);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shooter.getShooterState()) {
      case SHOOTER:
        //TODO: Change the position of the calculate
        ShootingWhileDriving.calculate(RobotCommon.getHubPose());
        flywheelVelocity = ShootingWhileDriving.getFlywheelVel();
        hoodPosition = ShootingWhileDriving.getHoodAngle();
        indexerPower = 0;
        feederPower = FeederConstants.MAX_FEEDER_POWER;
        if(shooter.isReady()){
          indexerPower = IndexerConstants.MAX_INDEXER_POWER;
        }
        break;
      case IDLE:
        flywheelVelocity = 0;
        hoodPosition = 0;
        indexerPower = 0;
        feederPower = 0;
        break;
      case TEST:
        break;
      case DELIVERY:
        feederPower = FeederConstants.MAX_FEEDER_POWER;
        shooterToTarget = RobotCommon.getDeliveryPose().getTranslation();
        shooterToTarget = shooterToTarget.minus(new Translation2d(RobotCommon.getChassisFieldRelativeSpeeds().vxMetersPerSecond * 1.2, RobotCommon.getChassisFieldRelativeSpeeds().vyMetersPerSecond * 1.2));
        hoodPosition = 45;
        flywheelVelocity = FlywheelConstants.MAX_FLYWHEEL_POWER;
        if (shooter.isReady()){
          indexerPower = IndexerConstants.MAX_INDEXER_POWER;
        }
        break;
      case TRANCH:
        hoodPosition = HoodConstants.MIN_POSITION;
        feederPower = 0;
        break;
    }
    shooter.setFlywheelVelocity(flywheelVelocity);
    shooter.setHoodMotion(hoodPosition);
    shooter.setIndexerPower(indexerPower);
    shooter.setFeederPower(feederPower);
    if (shooter.getFeederCurrent() > FeederConstants.MAX_FEEDER_CURRENT && Math.abs(shooter.getFeederVelocity()) < FeederConstants.MIN_FEEDER_VELOCITY){
      shooter.stopFeeder();
    }
    if (shooter.getHoodCurrent() > HoodConstants.MAX_HOOD_CURRENT && Math.abs(shooter.getHoodVelocity()) < HoodConstants.MIN_HOOD_VELOCITY){
      shooter.stopHood();
    }
    if (shooter.getIndexerCurrent() > IndexerConstants.MAX_INDEXER_CURRENT && Math.abs(shooter.getIndexerVelocity()) < IndexerConstants.MIN_INDEXER_VELOCITY){
      shooter.stopIndexer();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopAll();
  }
}
