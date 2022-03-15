package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.ControlSubsystem;

public class SingleShootCommand extends CommandBase {
    private ControlSubsystem cs;
    private Timer t;
    private int pet; // periodic execute time

    public SingleShootCommand(ControlSubsystem cs) {
        this.cs = cs;
        t = new Timer();
        pet = cs.periodicCounter;
    }

    @Override
    public void initialize() {
        //cs.magLock.setPermission(2);
        cs.shooter.setPermission(2);
        cs.liftSolenoid.setPermission(2);

        t.start();
    }

    @Override
    public void execute() {
        //cs.magLock.getVal(2).set(DoubleSolenoid.Value.kReverse);
        cs.shooter.getVal(2).setValue(Constants.SHOOTER_MODIFIER);

        if (t.get() > 1 && t.get() < 3.5) {
            cs.liftSolenoid.getVal(2).set(DoubleSolenoid.Value.kReverse);
        } else if (t.get() > 3.5 && t.get() < 4.5) {
            cs.liftSolenoid.getVal(2).set(DoubleSolenoid.Value.kForward);
        }
        //else if (t.get() > 4.5) {
        //    cs.magLock.getVal(2).set();
        //}
    }

    @Override
    public boolean isFinished() {
        if (t.get() > 4.6)
            return true;

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        //cs.magLock.getVal(2).set(DoubleSolenoid.Value.kForward);
        cs.shooter.getVal(2).setValue(0);

        //cs.magLock.setDefaultPermission();
        cs.shooter.setDefaultPermission();
        cs.liftSolenoid.setDefaultPermission();
    }

    public int getPet() {
        return pet;
    }
}