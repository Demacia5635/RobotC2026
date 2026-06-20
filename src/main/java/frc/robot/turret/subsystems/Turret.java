// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.turret.subsystems;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.robot.intake.commands.CalibrationCommandIntake;
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
  private TurretStates turretStates;
  private boolean isCalibrated;
  /** Creates a new Turret. */
  private Turret() {
    turretMotor = new TalonFXMotor(TurretConstants.TURRET_CONFIG);
    maxLimitSwitch = new DigitalInput(6);
    isCalibrated = false;
    SmartDashboard.putData("turret Calibration Command", new TurretCalibration(this));
    SmartDashboard.putData("turret manual reset - 0", new InstantCommand(()->{setCaliberation(true); setPositionByLimit();}).ignoringDisable(true));
    SmartDashboard.putData("turret",this);
    addNT();
  }

  private void addNT() {
    SendableChooser<TurretStates> stateChooser = new SendableChooser<>();
    for (TurretStates intakeState : TurretStates.values()) {
      stateChooser.addOption(intakeState.name(), intakeState);
    }
    stateChooser.onChange(newState -> this.turretStates = newState);
    SmartDashboard.putData("turret State Chooser!!!!!!!!!", stateChooser);

  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.addBooleanProperty("is limet turret", ()-> getMaxLimitSwich(), null);
      builder.addBooleanProperty("is turret cal", ()-> getIsCaliberation(), null);
      builder.addDoubleProperty("turret ang", ()-> getAngle(), null);
  }

  public void setCaliberation(boolean isCalibrated){
    this.isCalibrated = isCalibrated;
  }

  public boolean getIsCaliberation(){
    return isCalibrated;
  }

  public double getAngle(){
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
    if(getIsCaliberation()){ //if not stop
      position = MathUtil.clamp(position, TurretConstants.MIN_TURRET_ANGLE, TurretConstants.MAX_TURRET_ANGLE);
      turretMotor.setPositionVoltage(Math.toRadians(position));
    }
  }

  public void setPositionByLimit(){
    if(getMaxLimitSwich()) turretMotor.setEncoderPosition(TurretConstants.MIN_TURRET_ANGLE);
  }
  public void stopMotor(){
    turretMotor.stop();
  }

  public double getTurretAngle(){
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

  public void setCalibration(){
    isCalibrated = true;
  }

  @Override
  public void periodic() {// like shooter, may not work couse not neer the other code
      if (turretMotor.getCurrentCurrent() > TurretConstants.MAX_CURRENT && Math.abs(turretMotor.getCurrentVelocity()) < TurretConstants.MIN_VELOCITY){
        stopMotor();
      }
  }
}
