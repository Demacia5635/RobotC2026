package frc.robot;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import frc.demacia.utils.leds.LedConstants;
import frc.demacia.utils.leds.LedManager;
import frc.demacia.utils.leds.LedStrip;
import frc.robot.RobotCommon.Shifts;
import frc.robot.intack.subsystems.Intack;
import frc.robot.shooter.subsystems.Shooter;
import frc.robot.turret.subsystems.Turret;

public class RobotCLedStrip extends LedStrip{
    private final double TIME_TO_BLINK = 7;
    
    public RobotCLedStrip(LedManager ledManager) {
        super("Robot C Strip",  LedConstants.LENGTH, ledManager);
        
        SmartDashboard.putData("Main Leds", this);
    }

    @Override
    public void periodic() {
        if (RobotState.isDisabled()) {
            boolean isCalibrated = Shooter.getInstance().getIsCalibrationAll() &&
                Turret.getInstance().getIsCalibrationAll() &&
                Intack.getInstance().getIsCalibrationAll(); 
            setColor(isCalibrated ? Color.kGreen : Color.kWhite);
            return;
        }

        Color currentColor = determineAllianceColor();
        double timeLeft = StateManger.getInstance().getShiftTimeLeft();
        
        double shiftMaxTime = shiftMaxTime();
        

        double clampedTime = Math.max(0, Math.min(timeLeft, shiftMaxTime));

        int activeLeds = (int) Math.ceil(((clampedTime / shiftMaxTime) * (size-2)));

        Color[] ledBuffer = new Color[size];
        ledBuffer[0] = Color.kPurple;
        ledBuffer[size-1] = Color.kPurple;
        for (int i = 1; i < size-1; i++) {
            if (i-1 < activeLeds) {
                ledBuffer[i] = currentColor;
            } else {
                ledBuffer[i] = Color.kWhite;
            }
        }

        if (timeLeft <= TIME_TO_BLINK && timeLeft > 0) {
            setBlink(ledBuffer);
        } else {
            setColor(ledBuffer);
        }
    }

    private Color determineAllianceColor() {
        Shifts currentShift = RobotCommon.getShift();
        
        if (currentShift == Shifts.Transition || currentShift == Shifts.Endgame) {
            return Color.kPurple;
        }
        
        return StateManger.getInstance().isRedHub() ? Color.kRed : Color.kBlue;
    }

    private double shiftMaxTime() {
        switch (StateManger.getInstance().getShiftNum()) {
            case 0:
                return 20;
            case 1:
                return 10;
            case 6:
                return 30;
            default:
                return 25;

        }
    }
}
