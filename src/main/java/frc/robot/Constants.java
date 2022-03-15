// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final String CONFIG_FILE = "config.json";

    public static final double JOYSTICK_THRESHOLD = 0.2;

    public static final double DRIVE_X_MODIFIER = 1,
                               DRIVE_Y_MODIFIER = 1,
                               DRIVE_Z_MODIFIER = 0.6;

    public static final double DRIVE_DEFAULT = 1,
                               INTAKE_DEFAULT = 0.35,
                               SHOOTER_DEFAULT = 0.75,
                               CLIMB_SPEED = 0.65;

    public static double DRIVE_MODIFIER = DRIVE_DEFAULT;
    public static double INTAKE_MODIFIER = INTAKE_DEFAULT;
    public static double SHOOTER_MODIFIER = 0.9;

    public static final double Z_STEPPER_SPEED = 0.1,
                               Z_STEPPER_TOLERANCE = 2;
}
