package frc.demacia.utils.dashboard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.net.WebServer;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.mechanisms.BaseMechanism;
import frc.demacia.utils.mechanisms.StateBaseMechanism;
import frc.demacia.utils.motors.MotorInterface;
import frc.demacia.utils.sensors.SensorInterface;
import frc.demacia.utils.sensors.Cancoder;
import frc.demacia.utils.sensors.Pigeon;

public class ElasticGenerator {
    private static ElasticGenerator instance;

    private List<MotorInterface> allMotors = new ArrayList<>();
    private List<SensorInterface> allSensors = new ArrayList<>();
    private List<BaseMechanism> mechanisms = new ArrayList<>();

    private Pigeon chassisGyro;
    private Cancoder[] chassisCancoders = new Cancoder[4];

    private ElasticGenerator() {
        SmartDashboard.putData("Elastic/Generate Layout", new InstantCommand(this::generateAndPublishLayout).ignoringDisable(true));
        
        try {
            WebServer.start(5800, Filesystem.getDeployDirectory().getPath());
        } catch (Exception e) {
            frc.demacia.utils.log.LogManager.log("Failed to start WebServer for Elastic: " + e.getMessage());
        }
    }

    public static ElasticGenerator getInstance() {
        if (instance == null) {
            instance = new ElasticGenerator();
        }
        return instance;
    }

    public void registerMotor(MotorInterface motor) {
        if (!allMotors.contains(motor)) {
            allMotors.add(motor);
        }
    }

    public void registerSensor(SensorInterface sensor) {
        if (!allSensors.contains(sensor)) {
            allSensors.add(sensor);
        }
    }

    public void registerMechanism(BaseMechanism mech) {
        if (!mechanisms.contains(mech)) {
            mechanisms.add(mech);
        }
    }

    public void registerChassisGyro(Pigeon gyro) {
        this.chassisGyro = gyro;
    }

    public void registerChassisCancoders(Cancoder[] cancoders) {
        this.chassisCancoders = cancoders;
    }

    public void generateAndPublishLayout() {
        StringBuilder json = new StringBuilder();
        
        json.append("{\n");
        json.append("  \"version\": 1.0,\n");
        json.append("  \"grid_size\": 128,\n");
        json.append("  \"tabs\": [\n");

        json.append(buildTunerTabs());
        json.append(",\n");
        json.append(buildChassisTab());
        json.append(buildMechanismTabs());

        json.append("\n  ]\n");
        json.append("}\n");

        File dir = Filesystem.getDeployDirectory();
        dir.mkdirs(); 
        File file = new File(dir, "Generated_Elastic_Layout.json");

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
            
            SmartDashboard.putString("Elastic/Raw_JSON_Output", json.toString());
            
            SmartDashboard.putString("Elastic/Status", "Saved at: " + Timer.getFPGATimestamp() + " in " + file.getAbsolutePath());
            LogManager.log("Elastic layout saved to " + file.getAbsolutePath());
            
        } catch (IOException e) {
            LogManager.log("Failed to save Elastic layout: " + e.getMessage());
            SmartDashboard.putString("Elastic/Status", "Failed to save: " + e.getMessage());
        }
    }

    private String buildTunerTabs() {
        StringBuilder sb = new StringBuilder();
        int maxMotorsPerTab = 12; 
        int tabCount = (int) Math.ceil((double) allMotors.size() / maxMotorsPerTab);
        if (tabCount == 0) tabCount = 1;

        for (int t = 0; t < tabCount; t++) {
            if (t > 0) sb.append(",\n");
            
            String tabName = tabCount == 1 ? "Tuner" : "Tuner " + (t + 1);
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(tabName).append("\",\n");
            sb.append("      \"grid_layout\": {\n        \"layouts\": [],\n        \"containers\": [\n");
            
            int row = 0;
            int col = 0;
            boolean first = true;
            
            int startIndex = t * maxMotorsPerTab;
            int endIndex = Math.min(startIndex + maxMotorsPerTab, allMotors.size());
            
            for (int i = startIndex; i < endIndex; i++) {
                MotorInterface motor = allMotors.get(i);
                if (!first) sb.append(",\n");
                first = false;
                
                String motorPath = "/SmartDashboard/motors/" + motor.getName();
                double xOffset = col * 5.0;
                
                sb.append(createWidget("Boolean Box", motor.getName(), xOffset, row, 1, 1, 
                    motorPath + "/Is" + motor.getName() + "Connected", "\"data_type\": \"boolean\""));
                sb.append(",\n");
                
                sb.append(createWidget("Number Slider", "Power", xOffset + 1, row, 2, 1, 
                    motorPath + "/test Power", "\"data_type\": \"double\""));
                sb.append(",\n");
                
                sb.append(createWidget("Command", "Run", xOffset + 3.0, row, 2, 1, 
                    motorPath + "/test power command", "\"show_type\": true"));
                
                col++;
                if (col >= 2) { 
                    col = 0;
                    row++;
                }
            }
            
            if (allMotors.isEmpty()) {
                sb.append(createWidget("Text Display", "Status", 0, 0, 4, 1, "", "\"data_type\": \"string\""));
            }
            
            sb.append("\n        ]\n      }\n    }");
        }
        return sb.toString();
    }

    private String buildChassisTab() {
        StringBuilder sb = new StringBuilder();
        sb.append("    {\n");
        sb.append("      \"name\": \"Chassis Base\",\n");
        sb.append("      \"grid_layout\": {\n        \"layouts\": [],\n        \"containers\": [\n");
        
        sb.append(createWidget("Field", "Field", 0, 0, 2, 4, "/SmartDashboard/chassis/field", "\"field_rotation\": 90.0"));
        
        if (chassisGyro != null) {
            sb.append(",\n");
            sb.append(createWidget("Large Text Display", chassisGyro.getName(), 2, 0, 2, 2, "/SmartDashboard/sensors/" + chassisGyro.getName() + "/yaw Degree", "\"data_type\": \"double\""));
        }

        sb.append(",\n");
        sb.append(createWidget("Command", "Reset Gyro", 4, 0, 2, 1, "/SmartDashboard/chassis/reset gyro", "\"show_type\": true"));
        sb.append(",\n");
        sb.append(createWidget("Command", "Reset 180", 4, 1, 2, 1, "/SmartDashboard/chassis/reset gyro 180", "\"show_type\": true"));

        sb.append(",\n");
        sb.append(createWidget("Command", "Reset Odometry", 8, 0, 2, 1, "/SmartDashboard/chassis/reset odmetry", "\"show_type\": true"));

        if (chassisCancoders != null && chassisCancoders.length == 4) {
            if (chassisCancoders[0] != null) {
                sb.append(",\n");
                sb.append(createWidget("Text Display", "Front Left Abs", 2, 2, 1, 1, "/SmartDashboard/sensors/" + chassisCancoders[0].getName() + "/Abs Position", "\"data_type\": \"double\""));
            }
            if (chassisCancoders[1] != null) {
                sb.append(",\n");
                sb.append(createWidget("Text Display", "Front Right Abs", 3, 2, 1, 1, "/SmartDashboard/sensors/" + chassisCancoders[1].getName() + "/Abs Position", "\"data_type\": \"double\""));
            }
            if (chassisCancoders[2] != null) {
                sb.append(",\n");
                sb.append(createWidget("Text Display", "Back Left Abs", 2, 3, 1, 1, "/SmartDashboard/sensors/" + chassisCancoders[2].getName() + "/Abs Position", "\"data_type\": \"double\""));
            }
            if (chassisCancoders[3] != null) {
                sb.append(",\n");
                sb.append(createWidget("Text Display", "Back Right Abs", 3, 3, 1, 1, "/SmartDashboard/sensors/" + chassisCancoders[3].getName() + "/Abs Position", "\"data_type\": \"double\""));
            }
        }

        sb.append(",\n");
        sb.append(createWidget("Command", "Coast Chassis", 4, 2, 2, 1, "/SmartDashboard/chassis/set coast", "\"show_type\": true"));
        sb.append(",\n");
        sb.append(createWidget("Command", "Brake Chassis", 4, 3, 2, 1, "/SmartDashboard/chassis/set brake", "\"show_type\": true"));

        sb.append("\n        ]\n      }\n    }");
        return sb.toString();
    }

    private String buildMechanismTabs() {
        if (mechanisms.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < mechanisms.size(); i++) {
            BaseMechanism mech = mechanisms.get(i);
            sb.append(",\n");
            
            sb.append("    {\n");
            sb.append("      \"name\": \"").append(mech.getName()).append("\",\n");
            sb.append("      \"grid_layout\": {\n        \"layouts\": [],\n        \"containers\": [\n");
            
            String mechPath = "/SmartDashboard/" + mech.getName();
            List<String> widgets = new ArrayList<>();
            int xOffset = 0;
            int yBase = 0;
            
            boolean isStateMech = mech instanceof StateBaseMechanism;
            
            if (isStateMech) {
                widgets.add(createWidget("ComboBox Chooser", mech.getName() + " State Chooser", xOffset, yBase, 1, 1, mechPath + "/" + mech.getName() + " State Chooser", "\"sort_options\": false"));
                widgets.add(createWidget("Text Display", "State", xOffset + 1, yBase, 1, 1, mechPath + "/" + mech.getName() + " State", "\"data_type\": \"string\", \"show_submit_button\": false"));
                widgets.add(createWidget("Text Display", "Test Values", xOffset, yBase + 1, 2, 1, mechPath + "/" + mech.getName() + " Test Values", "\"data_type\": \"double[]\", \"show_submit_button\": true"));
                xOffset += 2;
            }
            
            MotorInterface[] motors = mech.getMotors();
            int startXForMotors = xOffset;
            
            for (MotorInterface motor : motors) {
                String motorName = motor.getName();
                String baseTopic = mechPath + "/" + motorName + "/";
                
                widgets.add(createWidget("Text Display", motorName + " wanted", xOffset, yBase, 1, 1, baseTopic + motorName + " wanted value", "\"data_type\": \"double\", \"show_submit_button\": false"));
                widgets.add(createWidget("Text Display", motorName + " current", xOffset + 1, yBase, 1, 1, baseTopic + motorName + " currnte Value", "\"data_type\": \"double\", \"show_submit_button\": false"));
                widgets.add(createWidget("Command", "Coast " + motorName, xOffset, yBase + 1, 2, 1, baseTopic + "set coast " + motorName, "\"show_type\": true"));
                widgets.add(createWidget("Command", "Brake " + motorName, xOffset, yBase + 2, 2, 1, baseTopic + "set brake " + motorName, "\"show_type\": true"));
                widgets.add(createWidget("Command", "Power " + motorName, xOffset, yBase + 3, 2, 1, baseTopic + "set power command " + motorName, "\"show_type\": true"));
                widgets.add(createWidget("Command", "Reset " + motorName, xOffset, yBase + 4, 2, 1, baseTopic + motorName + " manual reset", "\"show_type\": true"));
                widgets.add(createWidget("Boolean Box", "Calibrated", xOffset, yBase + 5, 2, 1, baseTopic + motorName + " has Calibrated", "\"data_type\": \"boolean\", \"true_color\": 4283215696, \"false_color\": 4294198070"));
                
                xOffset += 2;
                
                if (xOffset >= 24) {
                    xOffset = startXForMotors;
                    yBase += 6;
                }
            }
            
            sb.append(String.join(",\n", widgets));
            sb.append("\n        ]\n      }\n    }");
        }
        return sb.toString();
    }

    private String createWidget(String type, String title, double gridX, double gridY, double gridWidth, double gridHeight, String topic, String extraProps) {
        double x = gridX * 128.0;
        double y = gridY * 128.0;
        double width = gridWidth * 128.0;
        double height = gridHeight * 128.0;

        StringBuilder w = new StringBuilder();
        w.append("          {\n");
        w.append("            \"type\": \"").append(type).append("\",\n");
        w.append("            \"title\": \"").append(title).append("\",\n");
        w.append("            \"x\": ").append(x).append(",\n");
        w.append("            \"y\": ").append(y).append(",\n");
        w.append("            \"width\": ").append(width).append(",\n");
        w.append("            \"height\": ").append(height).append(",\n");
        w.append("            \"properties\": {\n");
        w.append("              \"topic\": \"").append(topic).append("\",\n");
        w.append("              \"period\": 0.06");
        if (extraProps != null && !extraProps.isEmpty()) {
            w.append(",\n              ").append(extraProps);
        }
        w.append("\n            }\n");
        w.append("          }");
        return w.toString();
    }
}