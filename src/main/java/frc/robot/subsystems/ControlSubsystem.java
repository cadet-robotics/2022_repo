package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Config;
import frc.robot.Constants;
import frc.robot.io.Motors;

public class ControlSubsystem extends SubsystemBase {
    private Config conf = Config.getInstance();

    private Motors motors;
    private MecanumDrive drive;

    private Joystick driverJoystick;

    private JoystickButton testSpinButton;

    private AHRS ahrs;

    public ControlSubsystem(Motors motors) {
        this.motors = motors;

        // initialize mecanum drive
        CANSparkMax[] driveMotors = motors.getDrive();
        drive = new MecanumDrive(
                driveMotors[0],
                driveMotors[1],
                driveMotors[2],
                driveMotors[3]
        );

        ahrs = new AHRS(SPI.Port.kMXP);

        // initialize controls and buttons
        driverJoystick = new Joystick(conf.getControls().getInt("driver joystick"));

        testSpinButton = new JoystickButton(driverJoystick, 5);

        // JoystickButton example declaration
        JoystickButton jb = new JoystickButton(driverJoystick, 0);

    }

    @Override
    public void periodic() {
        // TODO: fix cartesian drive so that yspeed xspeed zrotation is all correct coefficients and placements
        drive.driveCartesian(
                driverJoystick.getX() * Constants.DRIVE_MODIFIER,
                driverJoystick.getY() * Constants.DRIVE_MODIFIER,
                driverJoystick.getTwist() * Constants.DRIVE_MODIFIER,
                ahrs.getAngle()
        );

        motors.getDrive()[1].set(testSpinButton.get() ? 100 : 0);
    }
}
