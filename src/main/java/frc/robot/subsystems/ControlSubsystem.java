package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Config;
import frc.robot.Constants;
import frc.robot.commands.DumpToShooterCommand;
import frc.robot.commands.ZStepCommand;
import frc.robot.io.MotorPair;
import frc.robot.io.Motors;
import frc.robot.io.Sensors;
import frc.robot.permissions.PermissiveHolder;
import frc.robot.vision.PixyHandler;
import io.github.pseudoresonance.pixy2api.Pixy2;

public class ControlSubsystem extends SubsystemBase {
    private Config conf = Config.getInstance();

    private int periodicCounter = 0;

    private Motors motors;
    public Sensors sensors;

    public PermissiveHolder<MecanumDrive> drive; // 1-manual, 2-autonomous, 3-zStepper
    public PermissiveHolder<MotorPair> shooter; // 1-manual, 2-shoot cmd

    private Joystick driverJoystick;
    private Joystick codriverJoystick;

    private Compressor airCompressor = new Compressor(conf.getCan().getInt("pneumatic hub"), PneumaticsModuleType.REVPH);

    private JoystickButton magLockButton,     // only one ball in mag at a time
                           ballLiftButton,    // lifts ball to shooter
                           runShooterButton, // runs the shooter
                           shooterLimitButton, // sets shooter limit
                           intakeLimitButton, // sets max intake speed
                           throttleLimitButton, // sets max drive speed
                           teamBallButton,
                           overrideButton,
                           autoShootButton,
                           zStepToZeroButton; // runs zStepper cmd to bring heading to 0

    // this used to be airBoy, named by AJ Williams. Rip airBoy.
    public PermissiveHolder<DoubleSolenoid> liftSolenoid; // 1-manual, 2-shoot cmd
    public PermissiveHolder<DoubleSolenoid> magLock; // 1-manual, 2-shoot cmd

    private AHRS ahrs;

    private PixyHandler pixy;
    private int ballSignature = 1; // 1 = red, 2 = blue

    private ZStepCommand zStepper;
    public DumpToShooterCommand autoShootCmd;

    private static double sliderToPercent(double sliderVal) {
        return 1 - ((sliderVal + 1) / 2);
    }
    private double sliderToPercent() {
        return sliderToPercent(driverJoystick.getRawAxis(conf.getDriverControls().getInt("dj slider")));
    }

    public ControlSubsystem(Motors motors, Sensors sensors) {
        this.motors = motors;
        this.sensors = sensors;

        // initialize mecanum drive
        CANSparkMax[] driveMotors = motors.getDrive();
        drive = new PermissiveHolder<>(new MecanumDrive(
                driveMotors[0],
                driveMotors[1],
                driveMotors[2],
                driveMotors[3]
        ), 1);

        // initialize shooter motors
        CANSparkMax[] shooters = motors.getShooter();
        shooter = new PermissiveHolder<>(new MotorPair(shooters[0], shooters[1]), 1);

        // initialize hardware
        ahrs = new AHRS(SPI.Port.kMXP);

        pixy = new PixyHandler(Pixy2.createInstance(Pixy2.LinkType.SPI));

        liftSolenoid = new PermissiveHolder<>(new DoubleSolenoid(
                conf.getCan().getInt("pneumatic hub"),
                PneumaticsModuleType.REVPH,
                conf.getPneumatics().getInt("lift down"),
                conf.getPneumatics().getInt("lift up")
        ), 1);

        magLock = new PermissiveHolder<>(new DoubleSolenoid(
                conf.getCan().getInt("pneumatic hub"),
                PneumaticsModuleType.REVPH,
                conf.getPneumatics().getInt("maglock 0"),
                conf.getPneumatics().getInt("maglock 1")
        ), 1);

        // initialize controls
        driverJoystick = new Joystick(0);
        codriverJoystick = new Joystick(1);

        magLockButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("mag lock"));
        ballLiftButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("ball lift"));

        runShooterButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("run shooter"));

        // sets max throttle limit for shooter
        shooterLimitButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("shooter limit set"));
        shooterLimitButton.whenPressed(() -> {
            Constants.SHOOTER_MODIFIER = sliderToPercent();
        });

        // Sets max throttle limit for drive
        throttleLimitButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("drive limit set"));
        throttleLimitButton.whenPressed(() -> {
            Constants.DRIVE_MODIFIER = sliderToPercent();
        });

        // Sets the color of our alliance's ball
        teamBallButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("team ball color"));
        teamBallButton.whenPressed(() -> {
            if (ballSignature == 1) {
                ballSignature = 2;
            } else {
                ballSignature = 1;
            }
        });

        // Sets intake speed limit
        intakeLimitButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("intake limit set"));
        intakeLimitButton.whenPressed(() -> {
            Constants.INTAKE_MODIFIER = sliderToPercent();
        });

        overrideButton = new JoystickButton(driverJoystick, conf.getDriverControls().getInt("dj override"));
        overrideButton.whenPressed(() -> {
            if (zStepper != null) {
                zStepper.cancel();
                zStepper = null;
            }
            if (autoShootCmd != null) {
                autoShootCmd.cancel();
                autoShootCmd = null;
            }
        });

        autoShootButton = new JoystickButton(codriverJoystick, conf.getCoDriverControls().getInt("auto shoot"));
        autoShootButton.whenPressed(() -> {
            if (!sensors.ballOnLift.get()) {
                autoShootCmd = new DumpToShooterCommand(this, sensors);
                autoShootCmd.schedule();
            }
        });

        // steps the bot to z rotation = 0 degrees
        //zStepToZeroButton = new JoystickButton(driverJoystick, 6);
        //zStepToZeroButton.whenPressed(() -> {
        //    if (zStepper != null) {
        //        zStepper.cancel();
        //    }
        //    zStepper = new ZStepCommand(0, ahrs, drive);
        //    zStepper.schedule();
        //});

        // Setup camera
        CameraServer.startAutomaticCapture();

        // Enable air compressor
        airCompressor.enableDigital();
    }

    @Override
    public void periodic() {
        // Outputs useful stuff to shuffleboard :)
        // once per ~.25 seconds
        periodicCounter++;
        if (periodicCounter % (50/4) == 0) {
            SmartDashboard.putString("Alliance Color", ballSignature == 1 ? "Red" : "Blue");
            SmartDashboard.putNumber("Slider Percent", sliderToPercent());

            SmartDashboard.putNumber("Intake Modifier", Constants.INTAKE_MODIFIER);
            SmartDashboard.putNumber("Shooter Modifier", Constants.SHOOTER_MODIFIER);
            SmartDashboard.putNumber("Drive Modifier", Constants.DRIVE_MODIFIER);

            SmartDashboard.putNumber("Compressor Pressure", airCompressor.getPressure());
            SmartDashboard.putNumber("Intake RPM", motors.getIntake().get());
            SmartDashboard.putNumberArray("Shooter RPM", new double[] {
                    motors.getShooter()[0].get(),
                    motors.getShooter()[1].get()
            });

            SmartDashboard.putBoolean("Pixy Triggered", pixy.getBallAvailable(ballSignature));
            SmartDashboard.putNumber("Pixy FPS", pixy.getPixy().getFPS());

            SmartDashboard.putBoolean("Lift Loaded", !sensors.ballOnLift.get());

            // Turns off annoying limelight green light, disables built-in limelight vision code
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(1);
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(1);
        }

        // manual mecanum drive controller
        drivePeriodic();

        // solenoid control
        if (liftSolenoid.hasPermission(1))
            liftSolenoid.getVal(1).set(
                    ballLiftButton.get() ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward
            );
        if (magLock.hasPermission(1))
            magLock.getVal(1).set(
                    magLockButton.get() ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward
            );

        // intake
        if (driverJoystick.getRawButton(conf.getDriverControls().getInt("dj intake manual")) &&
                (overrideButton.get() || pixy.getBallAvailable(ballSignature))) {
            motors.getIntake().set(Constants.INTAKE_MODIFIER);
        } else {
            motors.getIntake().set(0);
        }

        // shooter
        if (shooter.hasPermission(1)) {
            shooter.getVal(1).setValue(
                    runShooterButton.get() ? Constants.SHOOTER_MODIFIER : 0
            );
        }
    }

    /**
     * Handles driving from joystick input
     */
    private void drivePeriodic() {
        if (!drive.hasPermission(1))
            return;

        double x = -driverJoystick.getX(),
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

        x *= Constants.DRIVE_X_MODIFIER;
        y *= Constants.DRIVE_Y_MODIFIER;
        z *= Constants.DRIVE_Z_MODIFIER;

        // TODO: fix cartesian drive so that yspeed xspeed zrotation is all correct coefficients and placements
        // NOTE: Field oriented drive is implemented by ahrs ", ahrs.getAngle()"
        drive.getVal(1).driveCartesian(x, y, z);
    }
}

