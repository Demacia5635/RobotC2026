// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.chassis;

import edu.wpi.first.wpilibj2.command.Command;
import frc.demacia.utils.chassis.SwerveModuleConfig;
import frc.demacia.utils.motors.TalonFXMotor;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class testDriveCommand extends Command {
  /** Creates a new testDriveCommand. */
  MK5nChassisConstansRobotC chassisConstans;
  SwerveModuleConfig[] modules = MK5nChassisConstansRobotC.modules;
  public testDriveCommand(MK5nChassisConstansRobotC chassisConstans) {
    this.chassisConstans = chassisConstans;
    // Use addRequirements() here to declare subsystem dependencies.

  TalonFXMotor[] motors; 

  for(int i =0; i< 4; i++){
    String name = "error";
    switch (i) {
      case 0:
        name="front left motor";
        break;
      case 1:
        name = "front right motor";
        break;
      case 2:
        name = "back left";
        break;
      case 3:
        name = "back right";
        break;
      default:
        name = "";
        break;
    }
    // motors[i] = new TalonFXMotor(MK5nChassisConstansRobotC.modules[i].driveConfig);
  }
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    
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
