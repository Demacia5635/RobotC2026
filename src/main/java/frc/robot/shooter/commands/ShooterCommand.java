// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.shooter.ShooterConstants;
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
  private Pose2d robotPose = Pose2d.kZero;
  private Translation2d shooterToTarget;
  private double[] lookUpTableValues;

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

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shooter.getShooterState()) {
      case SHOOTER:
        shooterToTarget = robotPose.getTranslation().plus(ShooterConstants.HUB);
        lookUpTableValues = ShooterConstants.LOOK_UP_TABLE.get(shooterToTarget.getNorm());
        shooter.setFlywheelVelocity(lookUpTableValues[0]);
        shooter.setHoodMotion(lookUpTableValues[1]);
        shooter.stopFeeder();
        shooter.setIndexerPower(IndexerConstants.MAX_INDEXER_POWER);
        if(shooter.isReady()){
          shooter.setFeederPower(FeederConstants.MAX_FEEDER_POWER);
        }
        break;
      case IDLE:
        shooter.stopAll();
        break;
      case TEST:
        shooter.setFlywheelVelocity(flywheelVelocity);
        shooter.setHoodMotion(hoodPosition);
        shooter.setIndexerPower(indexerPower);
        shooter.setFeederPower(feederPower);
        break;
      case DELIVERY:
        shooter.setFlywheelPower(FlywheelConstants.MAX_FLYWHEEL_POWER);
        shooter.setFeederPower(FeederConstants.MAX_FEEDER_POWER);
        if(robotPose.getX() > Constants.FIELD_WIDTH / 2.0d){
          shooterToTarget = robotPose.getTranslation().plus(ShooterConstants.DELIVERY_RIGHT_POINT);
        } else{
          shooterToTarget = robotPose.getTranslation().plus(ShooterConstants.DELIVERY_LEFT_POINT);
        }
        shooter.setHoodMotion((Math.asin((shooterToTarget.getNorm() * Constants.G) / (shooter.getFlywheelVelocity() * shooter.getFlywheelVelocity())) / 2.0d));
        if (shooter.isReady(Math.sqrt(Constants.G * (ShooterConstants.HEIGHT * Math.sqrt((shooterToTarget.getNorm() * shooterToTarget.getNorm()) + (ShooterConstants.HEIGHT * ShooterConstants.HEIGHT)))))){
          shooter.setIndexerPower(IndexerConstants.MAX_INDEXER_POWER);
        }
        break;
      case TRANCH:
        shooter.setHoodMotion(HoodConstants.MIN_POSITION);
        shooter.stopFeeder();
        break;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
