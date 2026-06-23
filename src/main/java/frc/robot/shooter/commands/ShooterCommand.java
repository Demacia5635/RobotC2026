// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.shooter.commands;

import org.opencv.objdetect.BarcodeDetector;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.log.LogManager;
import frc.robot.Field;
import frc.robot.Robot;
import frc.robot.RobotCommon;
import frc.robot.ShootingWhileDriving;
import frc.robot.shooter.ShooterConstants;
import frc.robot.shooter.ShooterConstants.FeederConstants;
import frc.robot.shooter.ShooterConstants.FlywheelConstants;
import frc.robot.shooter.ShooterConstants.HoodConstants;
// import frc.robot.shooter.ShooterConstants.IndexerConstants;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.TurretConstants;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class ShooterCommand extends Command {
  private Shooter shooter;
  private double flywheelVelocity = 0;
  private double hoodPosition = 0;
  private double feederPower = 0;
  private double testingFlywheelVelocity = 0;
  private double testingHoodPosition = 0;
  private double testingFeederPower = 0;
  private Translation2d shooterToTarget;//TODO be in robot common
  public static boolean toShoot;

  private double WHEEL_TO_BALL_VELOCITY_RATIO = 0.45;
  private double MAGNUS_CORRECTION = 0.2;
  private double velocityFromBattery = 1;
  private ChassisSpeeds robotSpeeds = RobotCommon.getFieldRelativeSpeeds();
  private double HOOD_OFFSET = Math.toRadians(4);
  private double VELOCITY_CORRECTION = 1;
  private Pose2d nextPose = RobotCommon.getRobotFucerPose();

  /** Creates a new ShooterCommand. */
  public ShooterCommand() {
    this.shooter =  Shooter.getInstance();
    addRequirements(shooter);
    SmartDashboard.putData("shooter command", this);
  }

  @Override
  public void initSendable(SendableBuilder builder) {
      builder.addDoubleProperty("FlywheelVelocity", () -> testingFlywheelVelocity, (vel) -> testingFlywheelVelocity = vel);
      builder.addDoubleProperty("HoodPosition", () -> Math.toDegrees(testingHoodPosition), (position) -> testingHoodPosition = Math.toRadians(position));
      builder.addDoubleProperty("feedrPower", () -> testingFeederPower, (power) -> testingFeederPower = power);
  }


  private double[] setFlywheelAndHood(double lutVel, double lutHoodAngle, Rotation2d heading) {

    // set the horizontal (xy) velocity and the vertical (z) velocity
    double xyVel = lutVel * Math.cos(lutHoodAngle);
    double zVel = lutVel * Math.sin(lutHoodAngle);

    // calculate the x/y velocities correected by robot speeds
    double xVel = xyVel * heading.getCos() - robotSpeeds.vxMetersPerSecond * VELOCITY_CORRECTION;
    double yVel = xyVel * heading.getSin() - robotSpeeds.vyMetersPerSecond * VELOCITY_CORRECTION;

    xyVel = Math.hypot(xVel, yVel);
    // calculate the new ball velocity
    double ballVelocity = Math.hypot(xyVel, zVel);

    ballVelocity -= MAGNUS_CORRECTION * (ballVelocity - lutVel); // correct for Magnus (back spin) effect

    ballVelocity = ballVelocity / WHEEL_TO_BALL_VELOCITY_RATIO; // translate required ball velocity to flywheel
                                                                // velocity

    // calculate the hood angle
    double hoodAngle = Math.atan(zVel / xyVel) - HOOD_OFFSET; // with hood correction
    // check for max angle
    if (hoodAngle > ShooterConstants.HoodConstants.MAX_POSITION) {
      hoodAngle = ShooterConstants.HoodConstants.MAX_POSITION;
      ballVelocity = xyVel / Math.cos(hoodAngle);
    }

    // calculate the heading
    Rotation2d ballHeading = new Rotation2d(xVel, yVel);

    // LogManager.log("new hood angle: " + hoodAngle + " ball heading: " +
    // ballHeading);
    RobotCommon.setFutureAngleFromTargetRobotRelative(MathUtil .inputModulus(ballHeading.getRadians() - nextPose.getRotation().getRadians(), 0, 2 * Math.PI));

    double[] arr = {ballVelocity, hoodAngle};
    return arr;
  }


  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    switch (shooter.getShooterState()) {
      case onePoint:
          flywheelVelocity = 9.0;
          hoodPosition = Math.toRadians(20);
          feederPower = 1;
          //93
        break;
      case towPoint:
        flywheelVelocity = 9.5;
        hoodPosition = Math.toRadians(40);
        feederPower = 1;
        //216
        break;
      case thrrePoint:
        flywheelVelocity = 9.5;
        hoodPosition = Math.toRadians(40);
        feederPower = 1;
        //243
      break;
      case SHOOTER:
        Translation2d toHub = RobotCommon.getHubPose().minus(Chassis.getInstance().getPose().getTranslation().plus(new Translation2d(-0.115, 0).rotateBy(Chassis.getInstance().getGyroAngle())));
        double lut[] = ShooterConstants.LOOK_UP_TABLE.get(toHub.getNorm());
        // Rotation2d heading = toHub.getAngle();
        //double[] arr = setFlywheelAndHood(lut[0], lut[1], heading);
        flywheelVelocity = lut[0];//* WHEEL_TO_BALL_VELOCITY_RATIO* velocityFromBattery;
        hoodPosition = lut[1];// + HOOD_OFFSET;
        feederPower = FeederConstants.MAX_FEEDER_POWER;
        break;
      case IDLE:
        flywheelVelocity = 0;
        hoodPosition = 0;
        feederPower = 0;
        break;
      case TEST:
        flywheelVelocity = testingFlywheelVelocity;
        hoodPosition = testingHoodPosition;
        feederPower = testingFeederPower;
        break;
      case DELIVERY:
        testingFeederPower = FeederConstants.MAX_FEEDER_POWER;
        shooterToTarget = RobotCommon.getDelveryPose().getTranslation();
        shooterToTarget = shooterToTarget.minus(new Translation2d(RobotCommon.getRobotFucerSpeed(0.02).vxMetersPerSecond * 1.2, RobotCommon.getFieldRelativeSpeeds().vyMetersPerSecond * 1.2));
        hoodPosition = 45;
        flywheelVelocity = FlywheelConstants.MAX_FLYWHEEL_POWER;
        feederPower = 1;
        break;
      case TRANCH:
        hoodPosition = HoodConstants.MIN_POSITION;
        feederPower = 0;
        break;
    }
      shooter.setFlywheelVelocity(flywheelVelocity);
      shooter.setHoodMotion(hoodPosition);
      // LogManager.log("hood pose" + hoodPosition);
      shooter.setFeederPower(feederPower);

    if (shooter.getFeederCurrent() > FeederConstants.MAX_FEEDER_CURRENT && Math.abs(shooter.getFeederVelocity()) < FeederConstants.MIN_FEEDER_VELOCITY){
      shooter.stopFeeder();
    }

    if (shooter.getHoodCurrent() > HoodConstants.MAX_HOOD_CURRENT && Math.abs(shooter.getHoodVelocity()) < HoodConstants.MIN_HOOD_VELOCITY){
      shooter.stopHood();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stopAll();
  }
}
