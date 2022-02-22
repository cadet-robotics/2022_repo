package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import frc.robot.Config;

public class Motors {
    private CANSparkMax frontLeft, frontRight, backLeft, backRight; // drive motors
    private CANSparkMax intake;

    private Config conf = Config.getInstance();

    public Motors() {
        // initialize drive motors
        frontLeft = new CANSparkMax(conf.getCan().getInt("fl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        frontRight = new CANSparkMax(conf.getCan().getInt("fr drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new CANSparkMax(conf.getCan().getInt("bl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new CANSparkMax(conf.getCan().getInt("br drive"), CANSparkMaxLowLevel.MotorType.kBrushless);

        intake = new CANSparkMax(conf.getCan().getInt("intake"), CANSparkMaxLowLevel.MotorType.kBrushless);

        // can motor setup stuff :)
        frontLeft.setInverted(true);
    }

    /**
     * @return The drive motors, in order FL,FR,BL,BR
     */
    public CANSparkMax[] getDrive() {
        return new CANSparkMax[] { frontLeft, frontRight, backLeft, backRight };
    }
    public CANSparkMax getIntake() { return intake; }
}