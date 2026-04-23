package frc.demacia.vision.subsystem;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
<<<<<<< HEAD
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.log.LogEntryBuilder.LogLevel;
import gg.questnav.questnav.PoseFrame;
import gg.questnav.questnav.QuestNav;


import static frc.demacia.vision.utils.VisionConstants.*;





public class Quest extends SubsystemBase {

  private Field2d robotField;

  private QuestNav questNav;
  private Pose3d currentQuestPose;
  private double timestamp;


  
  public Quest() {
    timestamp = 0;
    questNav = new QuestNav();
    questNav.commandPeriodic();

    robotField = new Field2d();//robot pose

    questNav.commandPeriodic();

    robotField = new Field2d();//robot pose

    currentQuestPose = new Pose3d(); // Initialize to origin - IMPORTANT!

    addLog();
  }
  
  @SuppressWarnings("unchecked")
  private void addLog() {
    LogManager.addEntry("Quest/Latency", questNav::getLatency).withLogLevel(LogLevel.LOG_AND_NT_NOT_IN_COMP);
    LogManager.addEntry("Quest/Battery", questNav::getBatteryPercent).withLogLevel(LogLevel.LOG_AND_NT_NOT_IN_COMP);
    LogManager.addEntry("Quest/LibVersion", questNav::getLibVersion).withLogLevel(LogLevel.LOG_AND_NT_NOT_IN_COMP);

    // SmartDashboard.putData("Quest/Field", field);
    SmartDashboard.putData("Quest/robotField", robotField);

  }


  // Set robot pose (transforms to Quest frame and sends to QuestNav)
  public void setQuestPose(Pose3d currentBotpose) {
    questNav.setPose(currentBotpose.transformBy(ROBOT_TO_QUEST3D));// the transformBy is to switch x & y and gives back
   }                                                               // the hight of the quest

  /**
   * * @return the center of the robot form quest
   */
  public Pose2d getRobotPose2d() {
    // return new Pose2d(currentQuestPose.transformBy(ROBOT_TO_QUEST3D.inverse()).toPose2d().getTranslation(),gyroAngle.get().rotateBy(Rotation2d.fromDegrees(90)));// the transformBy is to switch x & y
    return currentQuestPose.transformBy(ROBOT_TO_QUEST3D.inverse()).toPose2d();
  }


=======
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import gg.questnav.questnav.PoseFrame;
import gg.questnav.questnav.QuestNav;
import static frc.demacia.vision.utils.VisionConstants.*;

public class Quest extends SubsystemBase {
  private Field2d field;
  private QuestNav questNav;
  private Pose3d currentQuestPose;
  private boolean isCalibrated;
  private double timestamp;

  public Quest() {
    isCalibrated = false;
    timestamp = 0;
    questNav = new QuestNav();
    field = new Field2d();
    currentQuestPose = new Pose3d(); // Initialize to origin - IMPORTANT!
    
    SmartDashboard.putData("Quest Field", field);
  }
  public boolean isCalibrated(){
    return isCalibrated;
  }
  
  // Set robot pose (transforms to Quest frame and sends to QuestNav)
  public void setQuestPose(Pose3d currentBotpose){
    currentQuestPose = currentBotpose.transformBy(ROBOT_TO_QUEST);
    questNav.setPose(currentQuestPose);
    isCalibrated = true;// i know it is inefficent
  }

  // Get robot pose (transforms from Quest frame to robot frame)
  public Pose2d getRobotPose() { 
    return currentQuestPose.transformBy(ROBOT_TO_QUEST.inverse()).toPose2d();
  }
  
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
  // Check if Quest is connected
  public boolean isConnected() {
    return questNav.isConnected();
  }
<<<<<<< HEAD


=======
  
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
  // Check if Quest is tracking
  public boolean isTracking() {
    return questNav.isTracking();
  }
<<<<<<< HEAD
=======
  
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025

  @Override
  public void periodic() {
    questNav.commandPeriodic();

    PoseFrame[] poseFrames = questNav.getAllUnreadPoseFrames();
<<<<<<< HEAD

    if (poseFrames.length > 0 && poseFrames[poseFrames.length - 1].isTracking()) {

    if (poseFrames.length > 0 && poseFrames[poseFrames.length - 1].isTracking()) {
      currentQuestPose = poseFrames[poseFrames.length - 1].questPose3d();
      timestamp = poseFrames[poseFrames.length - 1].dataTimestamp();
      // Display Quest pose

      // the quest x & y

      SmartDashboard.putNumber("Quest/X", getRobotPose2d().getX());
      SmartDashboard.putNumber("Quest/Y", getRobotPose2d().getY());

      // the quest x & y

      SmartDashboard.putNumber("Quest/X", getRobotPose2d().getX());
      SmartDashboard.putNumber("Quest/Y", getRobotPose2d().getY());

      robotField.setRobotPose(currentQuestPose.transformBy(ROBOT_TO_QUEST3D.inverse()).toPose2d());
    }
  }

      robotField.setRobotPose(currentQuestPose.transformBy(ROBOT_TO_QUEST3D.inverse()).toPose2d());
    }
  

  // gives me the timestamp of the newst frame
  public double getTimestamp() {
    return timestamp;
  }


  public void questResetfromRobotToQuest(Rotation2d angle){
    setQuestPose(new Pose3d(getRobotPose2d().getX(),getRobotPose2d().getY(),currentQuestPose.getZ(),new Rotation3d(angle)));
=======
    
    if(poseFrames.length > 0 && poseFrames[poseFrames.length - 1].isTracking()){
      currentQuestPose = poseFrames[poseFrames.length - 1].questPose3d();
      timestamp = poseFrames[poseFrames.length - 1].dataTimestamp();
      // Display Quest pose
      SmartDashboard.putNumber("Quest X", currentQuestPose.getX());
      SmartDashboard.putNumber("Quest Y", currentQuestPose.getY());
      SmartDashboard.putNumber("Quest Rotation", currentQuestPose.getRotation().getZ());

      field.setRobotPose(currentQuestPose.toPose2d());
    }
    
    // Diagnostics - helpful for debugging!
    SmartDashboard.putBoolean("Quest Connected", questNav.isConnected());
    SmartDashboard.putBoolean("Quest Tracking", questNav.isTracking());
    SmartDashboard.putNumber("Quest Latency (ms)", questNav.getLatency());
    
    // Battery monitoring
    questNav.getBatteryPercent().ifPresent(
      battery -> SmartDashboard.putNumber("Quest Battery %", battery)
    );
  }
  // gives me the timestamp of the newst frame
  public double getTimestamp(){
    return timestamp;
  }
  
  public void questReset() {
    questNav.setPose(new Pose3d(new Pose2d(0, 0, Rotation2d.kZero)));
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
  }
}