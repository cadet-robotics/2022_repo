package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SingleShootCommand;
import frc.robot.subsystems.ControlSubsystem;

public class BasicAutoPath extends SequentialCommandGroup {
    public BasicAutoPath(ControlSubsystem controlSubsystem) {
        addCommands(
                new AutoMove(controlSubsystem.drive, 1, 0, .35, 0),
                new SingleShootCommand(controlSubsystem),
                new AutoMove(controlSubsystem.drive, 2, 0, .25, 0)
        );
    }
}
