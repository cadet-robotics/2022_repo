package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ControlSubsystem;

public class AutoShoot extends CommandBase {
    private ControlSubsystem controlSubsystem;
    private Timer t;

    public AutoShoot(ControlSubsystem cs) {
        controlSubsystem = cs;
        t = new Timer();
    }

    @Override
    public void initialize() {
        controlSubsystem.shooter.setPermission(2);
        controlSubsystem.magLock.setPermission(2);
        controlSubsystem.liftSolenoid.setPermission(2);
        t.start();
    }

    @Override
    public void execute() {
        //controlSubsystem.magLock.getVal(2).set();
        controlSubsystem.shooter.getVal(2).setValue(Constants.SHOOTER_MODIFIER);

    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
