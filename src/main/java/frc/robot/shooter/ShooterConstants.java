package frc.robot.shooter;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Field;

public class ShooterConstants {
    public static final Translation2d SHOOTER_OFFSET = new Translation2d(-0.115, 0);
    public static final double FUTURE_TIME = 0.04;

    public static final double SAFE_MARGIN_Y = 0.8;
    public static final Translation2d DELIVERY_RED_LEFT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_RED_X + Field.Zones.CENTER_LINE_X * 0.25,
            SAFE_MARGIN_Y
        );

    public static final Translation2d DELIVERY_RED_RIGHT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_RED_X + Field.Zones.CENTER_LINE_X * 0.25,
            Field.FieldDimensions.WIDTH - SAFE_MARGIN_Y
        );

    public static final Translation2d DELIVERY_BLUE_LEFT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_BLUE_X - Field.Zones.CENTER_LINE_X * 0.25,
            SAFE_MARGIN_Y
        );

    public static final Translation2d DELIVERY_BLUE_RIGHT =
        new Translation2d(
            Field.Zones.ROBOT_STARTING_LINE_BLUE_X - Field.Zones.CENTER_LINE_X * 0.25,
            Field.FieldDimensions.WIDTH - SAFE_MARGIN_Y
        );
}
