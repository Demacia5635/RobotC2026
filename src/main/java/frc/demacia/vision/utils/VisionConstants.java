// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.demacia.vision.utils;

<<<<<<< HEAD
import org.ejml.simple.SimpleMatrix;

import edu.wpi.first.math.Matrix;
=======
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
<<<<<<< HEAD
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import frc.demacia.vision.TagPose;
=======
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025

/**
 * Constants and configuration values for AprilTag vision processing.
 * This class contains the physical layout and properties of AprilTags on the
 * field,
<<<<<<< HEAD
 * as well as camera mounting parameters for the 2026 REEFSCAPE season.
 */
public class VisionConstants {

        // Heights of different AprilTag groups from the ground (in meters)
        private static final double OUTPOST_TAG_HEIGHT = inchToMeter(35.0); // Outpost tags (human player stations)
        private static final double HUB_TAG_HEIGHT = inchToMeter(44.25); // Hub scoring location tags
        private static final double DEPOT_TAG_HEIGHT = inchToMeter(21.75); // Depot perimeter tags
=======
 * as well as camera mounting parameters.
 */
public class VisionConstants {

        // Heights of different AprilTag groups from the ground
        private static double BARGE_TAG_HIGHT = inchToMeter(73.54);
        private static double REEF_TAG_HIGHT = inchToMeter(12.13);
        private static double STATION_TAG_HIGHT = inchToMeter(58.50);
        private static double SIDE_TAG_HIGHT = inchToMeter(51.25);
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025

        public static final double BEST_RELIABLE_DISTANCE = 1; // meters - high confidence range
        public static final double WORST_RELIABLE_DISTANCE = 4; // meters - Low confidence range

        public static final double BEST_RELIABLE_SPEED = 1; // meter a second - high confidence range
        public static final double WORST_RELIABLE_SPEED = 3; // meter a second - Low confidence range

        /**
         * Array of AprilTag positions relative to field origin (0,0).
         * Each Translation2d represents X,Y coordinates in meters.
         * Index corresponds to AprilTag ID (0 is unused).
         * Coordinates are converted from inches to meters using inchToMeter().
<<<<<<< HEAD
         * 
         * Updated for 2026 REEFSCAPE season.
         */
        public static final Translation2d[] O_TO_TAG = {
                        null, // 0 - unused
                        new Translation2d(inchToMeter(467.637), inchToMeter(292.314)), // 1
                        new Translation2d(inchToMeter(469.111), inchToMeter(182.6)), // 2
                        new Translation2d(inchToMeter(445.349), inchToMeter(172.844)), // 3
                        new Translation2d(inchToMeter(445.349), inchToMeter(158.844)), // 4
                        new Translation2d(inchToMeter(469.111), inchToMeter(135.088)), // 5
                        new Translation2d(inchToMeter(467.637), inchToMeter(25.374)), // 6
                        new Translation2d(inchToMeter(470.586), inchToMeter(25.374)), // 7
                        new Translation2d(inchToMeter(483.111), inchToMeter(135.088)), // 8
                        new Translation2d(inchToMeter(492.881), inchToMeter(144.844)), // 9
                        new Translation2d(inchToMeter(492.881), inchToMeter(158.844)), // 10
                        new Translation2d(inchToMeter(483.111), inchToMeter(182.6)), // 11
                        new Translation2d(inchToMeter(470.586), inchToMeter(292.314)), // 12
                        new Translation2d(inchToMeter(650.918), inchToMeter(291.469)), // 13
                        new Translation2d(inchToMeter(650.918), inchToMeter(274.469)), // 14
                        new Translation2d(inchToMeter(650.904), inchToMeter(170.219)), // 15
                        new Translation2d(inchToMeter(650.904), inchToMeter(153.219)), // 16
                        new Translation2d(inchToMeter(183.586), inchToMeter(25.374)), // 17
                        new Translation2d(inchToMeter(182.111), inchToMeter(135.088)), // 18
                        new Translation2d(inchToMeter(205.873), inchToMeter(144.844)), // 19
                        new Translation2d(inchToMeter(205.873), inchToMeter(158.844)), // 20
                        new Translation2d(inchToMeter(182.111), inchToMeter(182.6)), // 21
                        new Translation2d(inchToMeter(183.586), inchToMeter(292.314)), // 22
                        new Translation2d(inchToMeter(180.637), inchToMeter(292.314)), // 23
                        new Translation2d(inchToMeter(168.111), inchToMeter(182.6)), // 24
                        new Translation2d(inchToMeter(158.341), inchToMeter(172.844)), // 25
                        new Translation2d(inchToMeter(158.341), inchToMeter(158.844)), // 26
                        new Translation2d(inchToMeter(168.111), inchToMeter(135.088)), // 27
                        new Translation2d(inchToMeter(180.637), inchToMeter(25.374)), // 28
                        new Translation2d(inchToMeter(0.305), inchToMeter(26.219)), // 29
                        new Translation2d(inchToMeter(0.305), inchToMeter(43.219)), // 30
                        new Translation2d(inchToMeter(0.318), inchToMeter(147.469)), // 31
                        new Translation2d(inchToMeter(0.318), inchToMeter(164.469)) // 32
        };

        /**
         * Array of AprilTag rotation angles (yaw).
         * Each Rotation2d represents the angle the tag faces.
         * Index corresponds to AprilTag ID (0 is unused).
         * 
         * Updated for 2026 REEFSCAPE season.
         */
        public static final Rotation2d[] TAG_ANGLE = {
                        null, // 0 - unused
                        Rotation2d.fromDegrees(180), // 1
                        Rotation2d.fromDegrees(90), // 2
                        Rotation2d.fromDegrees(180), // 3
                        Rotation2d.fromDegrees(180), // 4
                        Rotation2d.fromDegrees(270), // 5
                        Rotation2d.fromDegrees(180), // 6
                        Rotation2d.fromDegrees(0), // 7
                        Rotation2d.fromDegrees(270), // 8
                        Rotation2d.fromDegrees(0), // 9
                        Rotation2d.fromDegrees(0), // 10
                        Rotation2d.fromDegrees(90), // 11
                        Rotation2d.fromDegrees(0), // 12
                        Rotation2d.fromDegrees(180), // 13
                        Rotation2d.fromDegrees(180), // 14
                        Rotation2d.fromDegrees(180), // 15
                        Rotation2d.fromDegrees(180), // 16
                        Rotation2d.fromDegrees(0), // 17
                        Rotation2d.fromDegrees(270), // 18
                        Rotation2d.fromDegrees(0), // 19
                        Rotation2d.fromDegrees(0), // 20
                        Rotation2d.fromDegrees(90), // 21
                        Rotation2d.fromDegrees(0), // 22
                        Rotation2d.fromDegrees(180), // 23
                        Rotation2d.fromDegrees(90), // 24
                        Rotation2d.fromDegrees(180), // 25
                        Rotation2d.fromDegrees(180), // 26
                        Rotation2d.fromDegrees(270), // 27
                        Rotation2d.fromDegrees(180), // 28
                        Rotation2d.fromDegrees(0), // 29
                        Rotation2d.fromDegrees(0), // 30
                        Rotation2d.fromDegrees(0), // 31
                        Rotation2d.fromDegrees(0) // 32
=======
         */
        public static Translation2d[] O_TO_TAG = { null, // 0
                        new Translation2d(inchToMeter(657.37), inchToMeter(25.80)), // 1
                        new Translation2d(inchToMeter(657.37), inchToMeter(291.20)), // 2
                        new Translation2d(inchToMeter(455.15), inchToMeter(317.15)), // 3
                        new Translation2d(inchToMeter(365.20), inchToMeter(241.64)), // 4
                        new Translation2d(inchToMeter(365.20), inchToMeter(75.39)), // 5
                        new Translation2d(inchToMeter(530.49), inchToMeter(130.17)), // 6
                        new Translation2d(inchToMeter(546.87), inchToMeter(158.50)), // 7
                        new Translation2d(inchToMeter(530.49), inchToMeter(186.83)), // 8
                        new Translation2d(inchToMeter(497.77), inchToMeter(186.83)), // 9
                        new Translation2d(inchToMeter(481.39), inchToMeter(158.50)), // 10
                        new Translation2d(inchToMeter(497.77), inchToMeter(130.17)), // 11
                        new Translation2d(inchToMeter(33.51), inchToMeter(25.80)), // 12
                        new Translation2d(inchToMeter(33.51), inchToMeter(291.20)), // 13
                        new Translation2d(inchToMeter(325.68), inchToMeter(241.64)), // 14
                        new Translation2d(inchToMeter(325.68), inchToMeter(75.39)), // 15
                        new Translation2d(inchToMeter(235.73), inchToMeter(-0.15)), // 16
                        new Translation2d(inchToMeter(160.39), inchToMeter(130.17)), // 17
                        new Translation2d(inchToMeter(144.00), inchToMeter(158.50)), // 18
                        new Translation2d(inchToMeter(160.39), inchToMeter(186.83)), // 19
                        new Translation2d(inchToMeter(193.10), inchToMeter(186.83)), // 20
                        new Translation2d(inchToMeter(209.49), inchToMeter(158.50)), // 21
                        new Translation2d(inchToMeter(193.10), inchToMeter(130.17)),// 22

        };
        public static Rotation2d[] TAG_ANGLE = { null, // 0
                        Rotation2d.fromDegrees(126), // 1
                        Rotation2d.fromDegrees(234), // 2
                        Rotation2d.fromDegrees(270), // 3
                        Rotation2d.fromDegrees(0), // 4
                        Rotation2d.fromDegrees(0), // 5
                        Rotation2d.fromDegrees(300), // 6
                        Rotation2d.fromDegrees(0), // 7
                        Rotation2d.fromDegrees(60), // 8
                        Rotation2d.fromDegrees(120), // 9
                        Rotation2d.fromDegrees(180), // 10
                        Rotation2d.fromDegrees(240), // 11
                        Rotation2d.fromDegrees(54), // 12
                        Rotation2d.fromDegrees(306), // 13
                        Rotation2d.fromDegrees(180), // 14
                        Rotation2d.fromDegrees(180), // 15
                        Rotation2d.fromDegrees(90), // 16
                        Rotation2d.fromDegrees(240), // 17
                        Rotation2d.fromDegrees(180), // 18
                        Rotation2d.fromDegrees(120), // 19
                        Rotation2d.fromDegrees(60), // 20
                        Rotation2d.fromDegrees(0), // 21
                        Rotation2d.fromDegrees(300),// 22
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
        };

        /**
         * Array of AprilTag heights from the ground.
<<<<<<< HEAD
         * Each value corresponds to the Z-coordinate from the CSV.
         * Index corresponds to AprilTag ID (0 is unused).
         * 
         * Updated for 2026 REEFSCAPE season.
         */
        public static final double[] TAG_HEIGHT = {
                        0, // 0 - unused
                        OUTPOST_TAG_HEIGHT, // 1 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 2 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 3 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 4 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 5 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 6 - 35" (Outpost)
                        OUTPOST_TAG_HEIGHT, // 7 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 8 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 9 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 10 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 11 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 12 - 35" (Outpost)
                        DEPOT_TAG_HEIGHT, // 13 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 14 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 15 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 16 - 21.75" (Depot)
                        OUTPOST_TAG_HEIGHT, // 17 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 18 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 19 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 20 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 21 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 22 - 35" (Outpost)
                        OUTPOST_TAG_HEIGHT, // 23 - 35" (Outpost)
                        HUB_TAG_HEIGHT, // 24 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 25 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 26 - 44.25" (Hub)
                        HUB_TAG_HEIGHT, // 27 - 44.25" (Hub)
                        OUTPOST_TAG_HEIGHT, // 28 - 35" (Outpost)
                        DEPOT_TAG_HEIGHT, // 29 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 30 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT, // 31 - 21.75" (Depot)
                        DEPOT_TAG_HEIGHT // 32 - 21.75" (Depot)
        };

        // Scoring position offsets TODO: Update these based on 2026 Hub/Tower/Outpost
        // positions
        public static final Translation2d HUB_TAG_TO_RIGHT_SCORING = new Translation2d(-0.55, -0.160);
        public static final Translation2d HUB_TAG_TO_LEFT_SCORING = new Translation2d(-0.55, 0.160);
        public static final Translation2d INTAKE_TAG_TO_SCORING = new Translation2d(-0.85, 0.0);
=======
         * Each value corresponds to either LOW, MID, or HIGH mounting position.
         * Index corresponds to AprilTag ID (0 is unused).
         */
        public static double[] TAG_HEIGHT = { 0, // 0
                        STATION_TAG_HIGHT, // 1
                        STATION_TAG_HIGHT, // 2
                        SIDE_TAG_HIGHT, // 3
                        BARGE_TAG_HIGHT, // 4
                        BARGE_TAG_HIGHT, // 5
                        REEF_TAG_HIGHT, // 6
                        REEF_TAG_HIGHT, // 7
                        REEF_TAG_HIGHT, // 8
                        REEF_TAG_HIGHT, // 9
                        REEF_TAG_HIGHT, // 10
                        REEF_TAG_HIGHT, // 11
                        STATION_TAG_HIGHT, // 12
                        STATION_TAG_HIGHT, // 13
                        BARGE_TAG_HIGHT, // 14
                        BARGE_TAG_HIGHT, // 15
                        SIDE_TAG_HIGHT, // 16
                        REEF_TAG_HIGHT, // 17
                        REEF_TAG_HIGHT, // 18
                        REEF_TAG_HIGHT, // 19
                        REEF_TAG_HIGHT, // 20
                        REEF_TAG_HIGHT, // 21
                        REEF_TAG_HIGHT,// 22
        };

        public static final Translation2d REEF_TAG_TO_RIGHT_SCORING = new Translation2d(-0.55, -0.160);
        public static final Translation2d REEF_TAG_TO_LEFT_SCORING = new Translation2d(-0.55, 0.160);
        public static final Translation2d INTAKE_TAG_TO_LEFT_SCORING = new Translation2d(-0.85, 0.0);
>>>>>>> 5876208a703990faca365c1e3e5f7933601df025

        /**
         * Converts a measurement from inches to meters
         * 
         * @param inch Value in inches
         * @return Value in meters
         */
        public static double inchToMeter(double inch) {
                return inch * 0.0254;
        }
<<<<<<< HEAD
        /**
         * Converts a measurement from inches to meters
         * 
         * @param inch Value in inches
         * @return Value in meters
         */
        ;

        // Vision processing constants
=======

        // 0 is LEFT TAG(ll2), 1 is RIGHT TAG, 2 is SIDE TAG, 3 is NOTE.

        // note for faniel and tomer in 14/2/25 -
        // the vision code suled work for the maunt noga printed if somthing
        // dose not work reaf the next coments

        // if the dist calc do not work, chaneg to 90-the number(only for id 1 and 2) in
        // line 141

        // if the robot tihnk it is behined the tag swap id 1 with id 2 in line 150
        // TAG Camera mounting configuration

>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
        public static final double MIN_CROP = 0.5;
        public static final double MAX_CROP = 0.6;
        public static final double CROP_CONSTAT = 0.35;
        public static final double PREDICT_X = -0.1;
        public static final double PREDICT_Y = 0.15;
        public static final double PREDICT_OMEGA = 0.2;

<<<<<<< HEAD
        // Quest VR headset mounting configuration
        // TODO: Find the right offsets for your robot's Quest mounting position
        public static final Rotation3d YAW_OFFSET_QUEST = new Rotation3d(0, 0, 0);
        public static final double Y_OFFSET_QUEST = -0.14018022;
        public static final double X_OFFSET_QUEST = 0.3026234;
        public static final double Z_OFFSET_QUEST = 0.460;


        /**
         * Transform from the center of the robot to Quest headset position in 3d
         */
        public static final Transform3d ROBOT_TO_QUEST3D = new Transform3d(
                        X_OFFSET_QUEST,
                        Y_OFFSET_QUEST,
                        Z_OFFSET_QUEST,
                        YAW_OFFSET_QUEST);

        // public static final TagPose FUEL = new TagPose(new Camera("fuel", new
        // Translation3d(0.329, -0.24325, 0.3195), -24, 0, false, true));

        // public static final TagPose LIMELIGHT4 = new TagPose(new Camera("hub", new
        // Translation3d(-0.133, 0.19, 0.35345), 30.0, 0.0, false, Rotation2d.kZero, new
        // Translation3d(0.11307,0.14305,0.18386) ));
        public static final Matrix<N3, N1> QUEST_STD = new Matrix<>(
                        new SimpleMatrix(
                                        new double[] { 0.05, 0.05, 0.1 }));


        
        public static class Tags {
                public static final TagPose[] TAGS_ARRAY = {};

        }
=======

        //TODO:find the right offsets
        public static final Rotation3d YAW_OFFSET_QUEST = new Rotation3d(Rotation2d.fromDegrees(180));
        public static final double X_OFFSET_QUEST = 0.1;
        public static final double Y_OFFSET_QUEST = 0.3;
        public static final double Z_OFFSET_QUEST = 0.38;
        public static final Transform3d ROBOT_TO_QUEST = new Transform3d(X_OFFSET_QUEST,Y_OFFSET_QUEST,Z_OFFSET_QUEST,YAW_OFFSET_QUEST);


>>>>>>> 5876208a703990faca365c1e3e5f7933601df025
}