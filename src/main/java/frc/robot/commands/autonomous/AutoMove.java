package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.permissions.PermissiveHolder;

public class AutoMove extends CommandBase {
    private PermissiveHolder<MecanumDrive> drive;
    private double moveTime;
    private Timer timer;

    private double x, y, z;

    public AutoMove(PermissiveHolder<MecanumDrive> drive, double moveTime, double x, double y, double z) {
        this.drive = drive;
        this.moveTime = moveTime;
        this.x = x;
        this.y = y;
        this.z = z;

        timer = new Timer();
    }

    @Override
    public void initialize() {
        drive.setPermission(2);
        timer.start();
    }

    @Override
    public void execute() {
        if (drive.hasPermission(2))
            drive.getVal(2).driveCartesian(x, y, z);
    }

    @Override
    public void end(boolean interrupted) {
        drive.setDefaultPermission();
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= moveTime;
    }
}
