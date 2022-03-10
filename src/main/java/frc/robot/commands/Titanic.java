package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ControlSubsystem;

// Name by John
public class Titanic extends CommandBase {
    private ControlSubsystem cs;

    public Titanic(ControlSubsystem cs) {
        this.cs = cs;
    }

    @Override
    public void initialize() {
        cs.drive.setPermission(2);
    }

    @Override
    public void execute() {

    }
}
