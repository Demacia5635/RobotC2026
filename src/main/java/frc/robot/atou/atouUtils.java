package frc.robot.atou;

import frc.robot.Field;
import frc.robot.RobotCommon;

public class atouUtils {
 public static double addWithRedOrBlue(double xBlue){
    return RobotCommon.isRed() ?  Field.FieldDimensions.LENGTH - xBlue: xBlue;
  }   
}
