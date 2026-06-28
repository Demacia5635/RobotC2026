// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.turret.subsystems;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.robot.RobotContainer;
import frc.robot.intake.IntakeConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.shooter.ShooterConstants.HoodConstants;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.turret.TurretConstants;
import frc.robot.turret.TurretConstants.TurretStates;
import frc.robot.turret.commands.TurretCalibration;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
  private static Turret turret;
  private TalonFXMotor turretMotor;
  private DigitalInput maxLimitSwitch;
  // private LimitSwitch minLimitSwitch;
  private TurretStates turretStates = TurretStates.IDLE;
  private boolean isCalibrated;
  private double deliveryAngle;
  /** Creates a new Turret. */
  private Turret() {
    turretMotor = new TalonFXMotor(TurretConstants.TURRET_CONFIG);
    maxLimitSwitch = new DigitalInput(TurretConstants.MAX_LIMIT_SWITCH_ID);
    isCalibrated = false;
    SmartDashboard.putData("turret Calibration Command", new TurretCalibration(this));
    SmartDashboard.putData("turret manual reset - 180", new InstantCommand(()->{setCaliberation(true); turretMotor.setEncoderPosition(Math.toRadians(-180));}).ignoringDisable(true));
    SmartDashboard.putData("turret",this);
    turretMotor.setEncoderPosition(Math.toRadians(-180));
    deliveryAngle = -180;
    addNT();
  }

  
  private void addNT() {
    SendableChooser<TurretStates> stateChooser = new SendableChooser<>();
    for (TurretStates intakeState : TurretStates.values()) {
      stateChooser.addOption(intakeState.name(), intakeState);
    }
    stateChooser.onChange(newState -> this.turretStates = newState);
    SmartDashboard.putData("turret State Chooser!", stateChooser);
    stateChooser.setDefaultOption("idle", TurretStates.IDLE);

  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.addBooleanProperty("is limet turret", ()-> getMaxLimitSwich(), null);
      builder.addBooleanProperty("is turret cal", ()-> getIsCaliberation(), null);
      builder.addDoubleProperty("turret ang", ()-> getAngleDeg(), null);
  }

  public void setCaliberation(boolean isCalibrated){
    this.isCalibrated = isCalibrated;
  }

  public boolean getIsCaliberation(){
    return isCalibrated;
  }

  public double getAngleDeg(){
    return Math.toDegrees(turretMotor.getCurrentAngle());
  }

  public void setNatrelMode(boolean isBrake){
    turretMotor.setNeutralMode(isBrake);
  }
  public static Turret getInstance(){
    if (turret == null){
      turret = new Turret();
    }
    return turret;
  }

  public void setTurretPower(double Power){
    turretMotor.setDuty(Power);
  }

  public void setTurretMotion(double position){
    position = MathUtil.clamp(position, TurretConstants.MIN_TURRET_ANGLE, TurretConstants.MAX_TURRET_ANGLE);
    turretMotor.setMotion(Math.toRadians(position));
  }

  public void setPositionByLimit(){
    if(getMaxLimitSwich()) turretMotor.setEncoderPosition(Math.toRadians(TurretConstants.MIN_TURRET_ANGLE));
  }


  public void stopMotor(){
    turretMotor.stop();
  }

  public double getTurretAngleRad(){
    return turretMotor.getCurrentAngle();
  }

  public boolean getMaxLimitSwich(){
    return !maxLimitSwitch.get();
  }

  public void setState(TurretStates state){
     turretStates = state;
  }

  public TurretStates getTurretState(){
    return turretStates;
  }

  public boolean isReady(){
    return Math.abs(turretMotor.getCurrentClosedLoopError()) < 4;
  }

  public void setCalibration(){
    isCalibrated = true;
  }

  public double getDeliveryAngle() {
    return deliveryAngle;
  }

  public void setDeliveryAngle(double deliveryAngle) {
    this.deliveryAngle = deliveryAngle;
  }

  public void add90ToDeliveryAngle() {
    if (getDeliveryAngle() < -90){
      setDeliveryAngle(getDeliveryAngle() + 90);
      LogManager.log("-90");
    }
  }

  public void subtract90ToDeliveryAngle() {
    if (getDeliveryAngle() > -270){
      setDeliveryAngle(getDeliveryAngle() - 90);
      LogManager.log("-270");
    }
  }

  @Override
  public void periodic() {// like shooter, may not work couse not neer the other code

  }
}
