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
  private TalonFXMotor turrtMotor;
  private LimitSwitch maxLimitSwitch;
  private LimitSwitch minLimitSwitch;

  private TurretStates turretStates;
  /** Creates a new Turret. */
  public Turret() {
   turrtMotor = new TalonFXMotor(TurretConstants.TURRET_CONFIG);
   maxLimitSwitch = new LimitSwitch(TurretConstants.MAX_LIMIT_SWITCH_CONFIG);
   minLimitSwitch = new LimitSwitch(TurretConstants.MIN_LIMIT_SWITCH_CONFIG);
  }

  public void setTurrtMotorPower(double Power){
    turrtMotor.setDuty(Power);
  }

  public void setTurrtMotion(double position){
    position = MathUtil.clamp(position, TurretConstants.MIN_TURRET_ANGEL, TurretConstants.MAX_TURRET_ANGEL);
    position = MathUtil.angleModulus(position);
    turrtMotor.setMotion(position);
  }
  public void stopMotor(){
    turrtMotor.stop();
  }
  public double getTurretAngle(){
    return turrtMotor.getCurrentAngle();
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

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
