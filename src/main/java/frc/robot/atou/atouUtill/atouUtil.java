package frc.robot.atou.atouUtill;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;

public class atouUtil {

    public static Supplier<Pose2d> pose2DtoSupplierPose2d(Pose2d pose){
        return () -> pose;
    }
    
}
