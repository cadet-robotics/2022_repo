package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.commands.DumpToShooterCommand;
import frc.robot.subsystems.ControlSubsystem;

public class AutoCommand extends CommandBase {
    private ControlSubsystem controlSubsystem;
    private Timer timer;

    public AutoCommand(ControlSubsystem cs) {
        controlSubsystem = cs;
        timer = new Timer();
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        timer.start();

        controlSubsystem.drive.setPermission(2);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (timer.get() < 1) {
            if (controlSubsystem.drive.hasPermission(2))
                controlSubsystem.drive.getVal(2).driveCartesian(0, 0.35, 0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        if (controlSubsystem.drive.hasPermission(2)) {
            controlSubsystem.drive.getVal(2).driveCartesian(0, 0, 0);
            controlSubsystem.drive.setDefaultPermission();

            controlSubsystem.autoShootCmd = new DumpToShooterCommand(controlSubsystem, controlSubsystem.sensors);
            controlSubsystem.autoShootCmd.schedule();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (!controlSubsystem.drive.hasPermission(2))
            return true;
        if (timer.get() > 1) {
            return true;
        }

        return false;
    }
}
