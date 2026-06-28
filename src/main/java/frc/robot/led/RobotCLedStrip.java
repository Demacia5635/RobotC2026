package frc.robot.led;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import frc.demacia.utils.leds.LedConstants;
import frc.demacia.utils.leds.LedManager;
import frc.demacia.utils.leds.LedStrip;

import frc.robot.RobotCommon;
import frc.robot.RobotContainer;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.stateManger.StateManger;
import frc.robot.stateManger.StateManger.Shifts;
import frc.robot.stateManger.utils.stateMangerUtils;

public class RobotCLedStrip extends LedStrip {

    public RobotCLedStrip(LedManager ledManager) {
        super("Robot C Strip", LedConstants.LENGTH, ledManager);
        this.transitionTimer = new Timer();
        this.ourShiftTimer = new Timer();
        this.theirShiftTimer = new Timer();
        this.endGameTimer = new Timer();
        this.disableTimer = new Timer();

        this.userButtonTimer = new Timer();
        this.shiftEndedTimer = new Timer();

        SmartDashboard.putData("Main Leds", this);
        SmartDashboard.putData("Quest Connected", new InstantCommand(() -> isQuestDisconnected = false).ignoringDisable(true));
    }

    Timer transitionTimer;
    Timer ourShiftTimer;
    Timer theirShiftTimer;
    Timer endGameTimer;
    Timer disableTimer;
    Timer shiftEndedTimer;

    Timer userButtonTimer;

    public void startUserButton() {
        userButtonTimer.start();
    }

    private void startTransition() {
        transitionTimer.reset();
        transitionTimer.start();
    }

    private void startOurShift() {
        ourShiftTimer.reset();
        ourShiftTimer.start();
    }

    private void startTheirShift() {
        theirShiftTimer.reset();
        theirShiftTimer.start();
    }

    private void startEndGame() {
        endGameTimer.reset();
        endGameTimer.start();
    }

    private void startDisable() {
        disableTimer.reset();
        disableTimer.start();
    }

    public void startShift() {
        switch (StateManger.getCurrenTShifts()) {
            case Transition:
                if (!transitionTimer.isRunning())
                    startTransition();
                break;

            case Active:
                if (!ourShiftTimer.isRunning())
                    startOurShift();
                break;

            case Inactive:
                if (!theirShiftTimer.isRunning())
                    startTheirShift();
                break;

            case Endgame:
                if (!endGameTimer.isRunning())
                    startEndGame();
                break;

            case Disable:
                if (!disableTimer.isRunning())
                    startDisable();
                break;

            default:
                break;
        }
    }

    public boolean isQuestDisconnected = false;

    public boolean isShiftEnded = false;

    private final double TIME_TO_BLINK = 10;

    @Override
    public void periodic() {
        super.periodic();

        // Default color based on current shift
        switch (StateManger.getCurrenTShifts()) {
            case Active,Disable,Endgame,Transition:
                setColor(Color.kPurple); // Our hub is active → Purple
                break;
            case Inactive:
                setColor(Color.kRed); // Their shift → Orange
                break;
            default:
                setColor(Color.kBlack); // Off otherwise
                break;
        }

        // Transition blink: purple blink 5 seconds before our shift starts
        if (transitionTimer.isRunning()) {
            setBlink(Color.kPurple);
        }

        // if(RobotState.isDisabled() && IntakeSubsystem.getInstance().isCalibrated()&& !IntakeSubsystem.getInstance().isIntakeDeployClosed()){
        //     setBlink(Color.kGreen);
        // }
        
        if(RobotState.isDisabled() && IntakeSubsystem.getInstance().isCalibrated()){
            setColor(Color.kGreen);
        }

        if (transitionTimer.hasElapsed(TIME_TO_BLINK / 2)) {
            transitionTimer.stop();
            transitionTimer.reset();
        }

        // Our shift starting soon: yellow blink 5 seconds before switching to their shift
        if (ourShiftTimer.isRunning()) {
            setBlink(Color.kYellow);
        }

        if (ourShiftTimer.hasElapsed(TIME_TO_BLINK / 2)) {
            ourShiftTimer.stop();
            ourShiftTimer.reset();
        }

        // Their shift starting soon: already handled by default Orange above
        if (theirShiftTimer.isRunning()) {
            setBlink(Color.kOrange);
        }

        if (theirShiftTimer.hasElapsed(TIME_TO_BLINK / 2)) {
            theirShiftTimer.stop();
            theirShiftTimer.reset();
        }

        if (endGameTimer.isRunning()) {
            setBlink(Color.kWhite);
        }

        if (endGameTimer.hasElapsed(TIME_TO_BLINK / 2)) {
            endGameTimer.stop();
            endGameTimer.reset();
        }

        if (disableTimer.isRunning()) {
            setBlink(Color.kWhite);
        }

        if (RobotState.isDisabled()) {
            disableTimer.stop();
            disableTimer.reset();
        }

        if (isShiftEnded || shiftEndedTimer.isRunning()) {
            setColor(Color.kWhite);
            shiftEndedTimer.start();
            isShiftEnded = false;
        }

        if (shiftEndedTimer.hasElapsed(1)) {
            shiftEndedTimer.stop();
            shiftEndedTimer.reset();
        }

        if (userButtonTimer.isRunning()) {
            setBlink(Color.kOrange);
        }

        if (userButtonTimer.hasElapsed(1)) {
            userButtonTimer.stop();
            userButtonTimer.reset();
        }

        if (isQuestDisconnected) {
            // setBlink(Color.kDarkRed);
        }
    }
}