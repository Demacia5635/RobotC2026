// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.robot.RobotCommon;
import frc.robot.intake.IntakeConstants.IntakeState;
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
  private TalonFXMotor feeder;

  private DigitalInput hood_limet_switch;

  private ShooterStates shooterState ;
  private double lastWantedFlywheelVelocity = 0;

  /** Creates a new Shooter. */
  private Shooter() {
    super();
    shooterState = ShooterStates.IDLE;
    flywheel = new TalonFXMotor(ShooterConstants.FlywheelConstants.FLYWHEEL_CONFIG);
    hood = new TalonFXMotor(ShooterConstants.HoodConstants.HOOD_CONFIG);
    hood_limet_switch = new DigitalInput(ShooterConstants.HoodConstants.LIMET_SWITCH_CHANEL);
    feeder = new TalonFXMotor(ShooterConstants.FeederConstants.FEEDER_CONFIG);
    SmartDashboard.putData("shooter",this);
    addNT();
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
      builder.addBooleanProperty("is hood lemate switch", ()-> isHoodLimetSwithSee(), null);
      builder.addDoubleProperty("current hood pose", ()-> Math.toDegrees(hood.getPosition().getValueAsDouble()), null);
      builder.addDoubleProperty("distence", ()-> getDis(), null);
  }

  public static Shooter getInstance(){
    if(shooter == null){
      shooter = new Shooter();
    }
    return shooter;
  }

  private void addNT() {
    SendableChooser<ShooterStates> stateChooser = new SendableChooser<>();
    for (ShooterStates intakeState : ShooterStates.values()) {
      stateChooser.addOption(intakeState.name(), intakeState);
    }
    stateChooser.onChange(newState -> this.shooterState = newState);
    SmartDashboard.putData("shooter State Chooser!!!!!!!!!", stateChooser);

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

  public double getFeederCurrent(){
    return feeder.getCurrentCurrent();
  }

  public double getHoodVelocity(){
    return hood.getCurrentVelocity();
  }

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
    feeder.stop();
  }

  public boolean isReady(){
    return Math.abs(flywheel.getCurrentClosedLoopError()) < FlywheelConstants.FLYWHEEL_VELOCITY_OFFSET &&
    Math.abs(hood.getCurrentClosedLoopError()) < HoodConstants.HOOD_POSITION_OFFSET;
  }

  public boolean isReady(double flywheelVelocity){
    return (Math.abs(flywheel.getCurrentVelocity() - flywheelVelocity) < FlywheelConstants.FLYWHEEL_VELOCITY_OFFSET) && 
    (Math.abs(hood.getCurrentClosedLoopError()) < HoodConstants.HOOD_POSITION_OFFSET);
  }

  public void stopFeeder() {
    feeder.stop();
  }

  public void stopHood() {
    hood.stop();
  }

  public boolean isHoodLimetSwithSee(){
    return hood_limet_switch.get();
  }

  public void setHoodPose(double pose){
    hood.setEncoderPosition(pose);
  }

  public double getDis(){
    return (RobotCommon.getHubPose().minus(Chassis.getInstance().getPose().getTranslation().plus(new Translation2d(-0.115, 0).rotateBy(Chassis.getInstance().getGyroAngle())))).getNorm();
  }

  //fieldOdmetry.setRobotPose(DemaciaOdometry.getOdometryInstance(modulePositions).getPose2d());
  @Override
  public void periodic() { 
      
  }
}
