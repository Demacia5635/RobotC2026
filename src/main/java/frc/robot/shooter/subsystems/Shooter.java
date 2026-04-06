// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.shooter.ShooterConstants;
import frc.robot.shooter.ShooterConstants.FeederConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.shooter.ShooterConstants.HoodConstants;
import frc.robot.shooter.ShooterConstants.IndexerConstants;
import frc.robot.shooter.ShooterConstants.ShooterStates;

public class Shooter extends SubsystemBase {
  private TalonFXMotor flywheel;
  private TalonFXMotor hood;
  private TalonFXMotor indexer;
  private TalonFXMotor feeder;
  private ShooterStates shooterState;
  private double targetHoodPosition;
  private double targetFlywheelVelocity;

  /** Creates a new Shooter. */
  public Shooter() {
    shooterState = ShooterStates.IDLE;
    flywheel = new TalonFXMotor(ShooterConstants.FlywheelConstants.FLYWHEEL_CONFIG);
    hood = new TalonFXMotor(ShooterConstants.HoodConstants.HOOD_CONFIG);
    indexer = new TalonFXMotor(ShooterConstants.IndexerConstants.INDEXER_CONFIG);
    feeder = new TalonFXMotor(ShooterConstants.FeederConstants.FEEDER_CONFIG);
    
  }
  public void setFlywheelPower(double power){
    flywheel.setDuty(power);
  }

  public void setHoodPower(double power){
    hood.setDuty(power);
   }
   
  public void setIndexerPower (double power){ 
    indexer.setDuty(power);
  }

  public void setFeederPower (double power){
    feeder.setDuty(power);
  }

  public void setFlywheelVelocity (double velocity){
    targetFlywheelVelocity = velocity;
    flywheel.setVelocity(velocity);
  }

  public double getFlywheelVelocity(){
    return flywheel.getCurrentVelocity();
  }

  public void setHoodMotion(double position){
    position = MathUtil.clamp(position, HoodConstants.MIN_POSITION, HoodConstants.MAX_POSITION);
    targetHoodPosition = position;
    hood.setMotion(position);
  }

  public void setShooterState(ShooterStates state){
    shooterState = state;
  }

  public ShooterStates getShooterState(){
    return shooterState;
  }

  public void stopAll(){
    flywheel.stop();
    hood.stop();
    indexer.stop();
    feeder.stop();
  }

  public boolean isReady(){
    return (Math.abs(targetFlywheelVelocity - flywheel.getCurrentPosition()) < FlywheelConstants.flywheelPositionOffset) && 
    (Math.abs(targetHoodPosition - hood.getCurrentPosition()) < HoodConstants.hoodPositionOffset);
  }

  public boolean isReady(double targetFlywheelVelocity){
    return (flywheel.getCurrentVelocity() - targetFlywheelVelocity > 0) && 
    (Math.abs(targetHoodPosition - hood.getCurrentPosition()) < HoodConstants.hoodPositionOffset);
  }

  public void stopFeeder() {
    feeder.stop();
  }

  @Override
  public void periodic() {
      if (feeder.getCurrentCurrent() > FeederConstants.MAX_FEEDER_CURRENT && Math.abs(feeder.getCurrentVelocity()) < FeederConstants.MIN_FEEDER_VELOCITY){
        stopFeeder();
      }
      if (hood.getCurrentCurrent() > HoodConstants.MAX_HOOD_CURRENT && Math.abs(hood.getCurrentVelocity()) < HoodConstants.MIN_HOOD_VELOCITY){
        hood.stop();
      }
      if (indexer.getCurrentCurrent() > IndexerConstants.MAX_INDEXER_CURRENT && Math.abs(indexer.getCurrentVelocity()) < IndexerConstants.MIN_INDEXER_VELOCITY){
        indexer.stop();
      }
  }
}
