package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.io.Sensors;
import frc.robot.subsystems.ControlSubsystem;

public class DumpToShooterCommand extends CommandBase {
    private ControlSubsystem controlSubsystem;
    private Sensors sensors;
    private Timer timer;

    public DumpToShooterCommand(ControlSubsystem cs, Sensors s) {
        timer = new Timer();
        controlSubsystem = cs;
        sensors = s;
    }

    @Override
    public void initialize() {
        controlSubsystem.shooter.setPermission(2);
        controlSubsystem.magLock.setPermission(2);
        controlSubsystem.liftSolenoid.setPermission(2);

        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        controlSubsystem.shooter.getVal(2).setValue(Constants.SHOOTER_MODIFIER);
        if (timer.get() > 2 && timer.get() < 3) {
            controlSubsystem.magLock.getVal(2).set(DoubleSolenoid.Value.kReverse);
            controlSubsystem.liftSolenoid.getVal(2).set(DoubleSolenoid.Value.kReverse);
        } else if (timer.get() > 3 && timer.get() < 5) {
            controlSubsystem.liftSolenoid.getVal(2).set(DoubleSolenoid.Value.kForward);
        } else if (timer.get() > 5 && timer.get() < 7) {
            controlSubsystem.magLock.getVal(2).set(DoubleSolenoid.Value.kForward);
        } else if ((timer.get() > 7 && timer.get() < 8)) {
            controlSubsystem.liftSolenoid.getVal(2).set(DoubleSolenoid.Value.kReverse);
        }
    }

    @Override
    public void end(boolean interrupted) {
        controlSubsystem.shooter.getVal(2).setValue(0);
        controlSubsystem.liftSolenoid.getVal(2).set(DoubleSolenoid.Value.kForward);
        controlSubsystem.magLock.getVal(2).set(DoubleSolenoid.Value.kForward);

        controlSubsystem.shooter.setPermission(1);
        controlSubsystem.magLock.setPermission(1);
        controlSubsystem.liftSolenoid.setPermission(1);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if (timer.get() > 9)
            return true;

        if (!controlSubsystem.shooter.hasPermission(2))
            return true;
        if (!controlSubsystem.liftSolenoid.hasPermission(2))
            return true;
        if (!controlSubsystem.magLock.hasPermission(2))
            return true;

        return false;
    }
}
