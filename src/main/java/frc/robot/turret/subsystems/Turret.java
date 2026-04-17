// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.turret.subsystems;
import frc.demacia.utils.motors.TalonFXMotor;
import frc.demacia.utils.sensors.LimitSwitch;
import frc.robot.turret.TurretConstants;
import frc.robot.turret.TurretConstants.TurretStates;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Turret extends SubsystemBase {
  private TalonFXMotor turretMotor;
  private LimitSwitch maxLimitSwitch;
  private LimitSwitch minLimitSwitch;
  private TurretStates turretStates;
  private boolean isCalibrated;
  /** Creates a new Turret. */
  public Turret() {
    turretMotor = new TalonFXMotor(TurretConstants.TURRET_CONFIG);
    maxLimitSwitch = new LimitSwitch(TurretConstants.MAX_LIMIT_SWITCH_CONFIG);
    minLimitSwitch = new LimitSwitch(TurretConstants.MIN_LIMIT_SWITCH_CONFIG);
    isCalibrated = false;
  }

  public void setNatrelMode(boolean isBrake){
    turretMotor.setNeutralMode(isBrake);
  }

  public void setTurretPower(double Power){
    turretMotor.setDuty(Power);
  }

  public void setTurretMotion(double position){
    if(isCalibrated){ //if not stop
      position = MathUtil.inputModulus(position, 0, 360);
      position = MathUtil.clamp(position, TurretConstants.MIN_TURRET_ANGLE, TurretConstants.MAX_TURRET_ANGLE);
      turretMotor.setMotion(position);
    }
  }

  public void setPositionByLimit(){
    if(getMaxLimitSwich()){
      turretMotor.setEncoderPosition(TurretConstants.MAX_TURRET_ANGLE);
    } else if(getMinLimitSwich()){
      turretMotor.setEncoderPosition(TurretConstants.MIN_TURRET_ANGLE);
    }
  }
  public void stopMotor(){
    turretMotor.stop();
  }

  public double getTurretAngle(){
    return turretMotor.getCurrentAngle();
  }

  public boolean getMaxLimitSwich(){
    return maxLimitSwitch.get();
  }

  public boolean getMinLimitSwich(){
    return minLimitSwitch.get();
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
