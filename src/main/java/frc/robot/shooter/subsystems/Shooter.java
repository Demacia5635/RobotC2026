// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.robot.shooter.ShooterConstants;
import frc.robot.shooter.ShooterConstants.FeederConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.shooter.ShooterConstants.HoodConstants;
// import frc.robot.shooter.ShooterConstants.IndexerConstants;
import frc.robot.shooter.ShooterConstants.ShooterStates;

public class Shooter extends SubsystemBase {
  private static Shooter shooter;
  private TalonFXMotor flywheel;
  private TalonFXMotor hood;
  // private TalonFXMotor indexer;
  private TalonFXMotor feeder;
  private ShooterStates shooterState;
  private double lastWantedFlywheelVelocity = 0;

  /** Creates a new Shooter. */
  private Shooter() {
    super();
    shooterState = ShooterStates.IDLE;
    flywheel = new TalonFXMotor(ShooterConstants.FlywheelConstants.FLYWHEEL_CONFIG);
    hood = new TalonFXMotor(ShooterConstants.HoodConstants.HOOD_CONFIG);
    // indexer = new TalonFXMotor(ShooterConstants.IndexerConstants.INDEXER_CONFIG);
    feeder = new TalonFXMotor(ShooterConstants.FeederConstants.FEEDER_CONFIG);
    SmartDashboard.putData("shooter",this);
    shooterState = ShooterStates.TEST;

    SmartDashboard.putData("resetHood",new InstantCommand(() -> restHoodMotor()).ignoringDisable(true));

    SmartDashboard.putData("set hood brake", new InstantCommand(() -> setBrakeHood()).ignoringDisable(true));
    SmartDashboard.putData("set hood coast", new InstantCommand(() -> setCostHood()).ignoringDisable(true));
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.addDoubleProperty("Flywheel Velocity subsystem", () -> flywheel.getVelocity().getValueAsDouble(), null);
      builder.addDoubleProperty("Flywheel AXL", () -> flywheel.getAcceleration().getValueAsDouble(), null);
      builder.addDoubleProperty("Flywheel Voltage", () -> flywheel.getMotorVoltage().getValueAsDouble(), null);
      builder.addDoubleProperty("Flywheel Position", () -> flywheel.getCurrentPosition(), null);
      builder.addDoubleProperty("Feeder Velocity", () -> feeder.getCurrentVelocity(), null);
      builder.addDoubleProperty("Hood Position subsyastem", () -> Math.toDegrees(hood.getCurrentPosition()), null);
      builder.addDoubleProperty("Hood Velocity", () -> hood.getVelocity().getValueAsDouble(), null);
      builder.addDoubleProperty("Hood Acceleration", () -> hood.getAcceleration().getValueAsDouble(), null);
      builder.addDoubleProperty("Hood Voltage", () -> hood.getMotorVoltage().getValueAsDouble(), null);
      builder.addStringProperty("Shooter State", () -> shooterState.name(), null);
      builder.addDoubleProperty("shooter voltage", () -> flywheel.getVoltageSignal().getDouble(), null);
      // builder.addDoubleProperty("shooter max accel", () -> FlywheelConstants.MAX_FLYWHEEL_ACCEL, (maxAccel) -> FlywheelConstants.MAX_FLYWHEEL_ACCEL = maxAccel);
  }

  public static Shooter getInstance(){
    if(shooter == null){
      shooter = new Shooter();
    }
    return shooter;
  }

  public void setCostHood(){
    hood.setNeutralMode(false);
  }

  public void setBrakeHood(){
    hood.setNeutralMode(true);
  }

  public void restHoodMotor(){
    hood.setEncoderPosition(0);
  }

  public void setFlywheelPower(double power){
    flywheel.setDuty(power);
  }

  public void setFlywheelVoltage(double voltage){
    flywheel.setVoltage(voltage);
  }

  public void setHoodPower(double power){
    hood.setDuty(power);
   }
   
  // public void setIndexerPower (double power){ 
  //   indexer.setDuty(power);
  // }



  public void setFeederPower (double power){
    feeder.setDuty(power);
  }

  public void setFlywheelVelocity (double velocity){
    if(Math.abs(velocity - lastWantedFlywheelVelocity) > FlywheelConstants.MAX_FLYWHEEL_ACCEL * 0.02) { 
      velocity = lastWantedFlywheelVelocity + Math.signum(velocity - lastWantedFlywheelVelocity) * FlywheelConstants.MAX_FLYWHEEL_ACCEL * 0.02;
    }
    lastWantedFlywheelVelocity = velocity;
    flywheel.setVelocity(velocity);
  }

  public double getFlywheelVelocity(){
    return flywheel.getCurrentVelocity();
  }

  public double getHoodCurrent(){
    return hood.getCurrentCurrent();
  }

  // public double getIndexerCurrent(){
  //   return indexer.getCurrentCurrent();
  // }

  public double getFeederCurrent(){
    return feeder.getCurrentCurrent();
  }

  public double getHoodVelocity(){
    return hood.getCurrentVelocity();
  }

  // public double getIndexerVelocity(){
  //   return indexer.getCurrentVelocity();
  // }

  public double getFeederVelocity(){
    return feeder.getCurrentVelocity();
  }

  public double getHoodPosition(){
    return hood.getCurrentPosition();
  }

  public void setHoodMotion(double position){
    position = MathUtil.clamp(position, HoodConstants.MIN_POSITION, HoodConstants.MAX_POSITION);
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
    // indexer.stop();
    feeder.stop();
  }

  public boolean isReady(){
    return FlywheelConstants.FLYWHEEL_VELOCITY_OFFSET - flywheel.getCurrentClosedLoopError() > 0 &&
    hood.getCurrentClosedLoopError() < HoodConstants.HOOD_POSITION_OFFSET;
  }

  public boolean isReady(double flywheelVelocity){
    return (Math.abs(flywheel.getCurrentVelocity() - flywheelVelocity) > FlywheelConstants.FLYWHEEL_VELOCITY_OFFSET) && 
    (hood.getCurrentClosedLoopError() < HoodConstants.HOOD_POSITION_OFFSET);
  }

  public void stopFeeder() {
    feeder.stop();
  }

  public void stopHood() {
    hood.stop();
  }

  // public void stopIndexer() {
  //   indexer.stop();
  // }

  @Override
  public void periodic() { //TODO to make you change it in elastic,  may not work because not neer the other code
      if (feeder.getCurrentCurrent() > FeederConstants.MAX_FEEDER_CURRENT && Math.abs(feeder.getCurrentVelocity()) < FeederConstants.MIN_FEEDER_VELOCITY){
        stopFeeder();
      }
      if (hood.getCurrentCurrent() > HoodConstants.MAX_HOOD_CURRENT && Math.abs(hood.getCurrentVelocity()) < HoodConstants.MIN_HOOD_VELOCITY){
        hood.stop();
      }
      double v = flywheel.getVelocity().getValueAsDouble();
      if(Math.abs(v) > 0.1) {
        LogManager.log("v = " + v);
      }
      // LogManager.log("voltage " + flywheel.getMotorVoltage().getValueAsDouble());
      // if (indexer.getCurrentCurrent() > IndexerConstants.MAX_INDEXER_CURRENT && Math.abs(indexer.getCurrentVelocity()) < IndexerConstants.MIN_INDEXER_VELOCITY){
      //   indexer.stop();
      // }
  }
}
