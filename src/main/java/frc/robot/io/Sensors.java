package frc.robot.io;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Config;

public class Sensors {
    public DigitalInput ballOnLift;
    public AnalogInput liftHeight;

    private Config conf = Config.getInstance();

    public Sensors() {
        ballOnLift = new DigitalInput(conf.getDigitalIn().getInt("lift sensor"));
        liftHeight = new AnalogInput(conf.getAnalogIn().getInt("lift distance"));
    }
}
