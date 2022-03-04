package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.permissions.PermissiveHolder;

/**
 * Command that automatically aligns the robot's Z axis in 90 degree steps
 */
public class ZStepCommand extends CommandBase {
    private double headingGoal;
    private AHRS ahrs;
    private PermissiveHolder<MecanumDrive> drive;

    //
    public ZStepCommand(double heading, AHRS ahrs, PermissiveHolder<MecanumDrive> drive) {
       super();
       this.headingGoal = heading;
       this.ahrs = ahrs;
       this.drive = drive;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        drive.setPermission(3);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (ahrs.getAngleAdjustment() > headingGoal) {
            drive.getVal(3).driveCartesian(0, 0, Constants.Z_STEPPER_SPEED);
        } else {
            drive.getVal(3).driveCartesian(0, 0, -1 * Constants.Z_STEPPER_SPEED);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        drive.setDefaultPermission();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return ahrs.getAngleAdjustment() < Constants.Z_STEPPER_TOLERANCE &&
                ahrs.getAngleAdjustment() > -1 * Constants.Z_STEPPER_TOLERANCE;
    }
}
