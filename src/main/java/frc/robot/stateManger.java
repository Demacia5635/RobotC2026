package frc.robot;

import frc.robot.intake.IntakeConstants.IntakeState;
import frc.robot.shooter.ShooterConstants.ShooterStates;
import frc.robot.turret.TurretConstants.TurretStates;

public class stateManger {

    public boolean isWork;
    
    private boolean isTranch(){
        if(RobotCommon.getRobotFucerPose().getX() > Field.TrenchBlueAudience.X_BACK - 0.5 && RobotCommon.getRobotFucerPose().getX() < Field.TrenchBlueAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getX() > Field.TrenchBlueScoring.X_BACK - 0.5 && RobotCommon.getRobotFucerPose().getX() < Field.TrenchBlueAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getY() > Field.TrenchBlueAudience.Y_BACK - 0.5 && RobotCommon.getRobotFucerPose().getY() < Field.TrenchBlueAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getY() > Field.TrenchBlueScoring.Y_BACK - 0.5 && RobotCommon.getRobotFucerPose().getY() < Field.TrenchBlueAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getX() > Field.TrenchRedAudience.X_BACK - 0.5 && RobotCommon.getRobotFucerPose().getX() < Field.TrenchRedAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getX() > Field.TrenchRedScoring.X_BACK - 0.5 && RobotCommon.getRobotFucerPose().getX() < Field.TrenchRedAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getY() > Field.TrenchRedAudience.Y_BACK - 0.5 && RobotCommon.getRobotFucerPose().getY() < Field.TrenchRedAudience.X_FRONT + 0.5){
            return true;
        }
        else if(RobotCommon.getRobotFucerPose().getY() > Field.TrenchRedScoring.Y_BACK - 0.5 && RobotCommon.getRobotFucerPose().getY() < Field.TrenchRedAudience.X_FRONT + 0.5){
            return true;
        }
        else{
            return false;

        }
    }


    private boolean isHub(){
        if(RobotCommon.currentRobotPose.getX() > Field.HubBlue.CENTER.getX() - 0.5 && RobotCommon.currentRobotPose.getX() < Field.HubBlue.CENTER.getX() + 0.5){
            return true;
        }
        else if(RobotCommon.currentRobotPose.getY() > Field.HubBlue.CENTER.getY() - 0.5 && RobotCommon.currentRobotPose.getY() < Field.HubBlue.CENTER.getY() + 0.5){
            return true;
        }
        else if(RobotCommon.currentRobotPose.getX() > Field.HubRed.CENTER.getX() - 0.5 && RobotCommon.currentRobotPose.getX() < Field.HubRed.CENTER.getX() + 0.5){
            return true;
        }
        else if(RobotCommon.currentRobotPose.getY() > Field.HubRed.CENTER.getY() - 0.5 && RobotCommon.currentRobotPose.getY() < Field.HubRed.CENTER.getY() + 0.5){
            return true;
        }
        else{
            return false;
        }
    }


    private boolean isDeleveryAndIntakeBlue(){
        if(RobotCommon.currentRobotPose.getX() > Field.HubBlue.CENTER.getX() - 0.5 && RobotCommon.currentRobotPose.getX() < Field.HubBlue.CENTER.getX() + 0.5){
            return true;
        }
        else if(RobotCommon.currentRobotPose.getY() > Field.HubBlue.CENTER.getY() - 0.5 && RobotCommon.currentRobotPose.getY() < Field.HubBlue.CENTER.getY() + 0.5){
            return true;
        } 
        else if(RobotCommon.currentRobotPose.getX() < Field.HubBlue.CENTER.getX() - 0.5 && RobotCommon.currentRobotPose.getX() > Field.HubBlue.CENTER.getX() + 0.5){
            return true;
        }
        else if(RobotCommon.currentRobotPose.getY() < Field.HubBlue.CENTER.getY() - 0.5 && RobotCommon.currentRobotPose.getY() > Field.HubBlue.CENTER.getY() + 0.5){
            return true;
        }
        else{
            return false;
        }
    }

    public void update(){   
        if(isWork){
            if(!isTranch()){
                if(isHub()){
                    RobotContainer.shooter.setShooterState(ShooterStates.SHOOTER);
                    RobotContainer.turret.setState(TurretStates.SHOOTING);
                    RobotContainer.intake.setState(IntakeState.DEPLOYED);
                }
                else if(isDeleveryAndIntakeBlue()){
                    RobotContainer.shooter.setShooterState(ShooterStates.DELIVERY);
                    RobotContainer.turret.setState(TurretStates.DELIVERY);
                    RobotContainer.intake.setState(IntakeState.INTAKING);
                }
        }
    }  
}
}