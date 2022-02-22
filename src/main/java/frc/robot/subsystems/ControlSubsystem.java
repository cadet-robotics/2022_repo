package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.*;
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

    private JoystickButton magStop,     // only one ball in mag at a time
                           ballLift,    // lifts ball to shooter
                           shooterLift; // lifts shooter to angle

    private DoubleSolenoid airBoy; // this name was the doing of AJ Williams

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

        // initialize controls
        driverJoystick = new Joystick(conf.getControls().getInt("driver joystick"));

        magStop = new JoystickButton(driverJoystick, 5);


        airBoy = new DoubleSolenoid(
                PneumaticsModuleType.REVPH,
                conf.getPneumatics().getInt("airboy port 0"),
                conf.getPneumatics().getInt("airboy port 1")
        );
    }

    @Override
    public void periodic() {
        drivePeriodic();

        airBoy.set(magStop.get() ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);

        //motors.getDrive()[1].set(testSpinButton.get() ? 100 : 0);
    }

    /**
     * Handles driving from joystick input
     */
    void drivePeriodic() {
        double x = driverJoystick.getX(),
               y = driverJoystick.getY(),
               z = -driverJoystick.getZ();

        if (Math.abs(x) < Constants.JOYSTICK_THRESHOLD)
            x = 0;
        if (Math.abs(y) < Constants.JOYSTICK_THRESHOLD)
            y = 0;
        if (Math.abs(z) < Constants.JOYSTICK_THRESHOLD)
            z = 0;

        x *= Constants.DRIVE_MODIFIER;
        y *= Constants.DRIVE_MODIFIER;
        z *= Constants.DRIVE_MODIFIER;

        // TODO: fix cartesian drive so that yspeed xspeed zrotation is all correct coefficients and placements
        // NOTE: Field oriented drive is implemented by ahrs ", ahrs.getAngle()"
        drive.driveCartesian(x, y, z);
    }
}

