package frc.robot.turret.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.turret.subsystems.Turret;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class TurretCalibration extends Command {
  private Turret turret;

  public TurretCalibration(Turret turret) {
    this.turret = turret;
    addRequirements(turret);
  }
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    turret.setTurretPower(0.1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    turret.stopMotor();
    if(!interrupted){
      turret.setCalibration();
      turret.setPositionByLimit();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return turret.getMaxLimitSwich() || turret.getMinLimitSwich();
  }
}
