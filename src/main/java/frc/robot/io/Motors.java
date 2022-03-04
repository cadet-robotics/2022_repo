package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.robot.Config;

public class Motors {
    private CANSparkMax frontLeft, frontRight, backLeft, backRight; // drive motors
    private CANSparkMax intake;
    private CANSparkMax shooter1, shooter2;

    private Config conf = Config.getInstance();

    public Motors() {
        // initialize drive motors
        frontLeft = new CANSparkMax(conf.getCan().getInt("fl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        frontRight = new CANSparkMax(conf.getCan().getInt("fr drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new CANSparkMax(conf.getCan().getInt("bl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new CANSparkMax(conf.getCan().getInt("br drive"), CANSparkMaxLowLevel.MotorType.kBrushless);

        intake = new CANSparkMax(conf.getCan().getInt("intake"), CANSparkMaxLowLevel.MotorType.kBrushed);
        intake.enableVoltageCompensation(10);

        shooter1 = new CANSparkMax(conf.getCan().getInt("flywheel 0"), CANSparkMaxLowLevel.MotorType.kBrushless);
        shooter2 = new CANSparkMax(conf.getCan().getInt("flywheel 1"), CANSparkMaxLowLevel.MotorType.kBrushless);
        shooter1.enableVoltageCompensation(11);
        shooter2.enableVoltageCompensation(11);

        // can motor setup stuff :)
        frontLeft.setInverted(true);
        shooter1.setInverted(true);
        intake.setInverted(true);
    }

    /**
     * @return The drive motors, in order FL,FR,BL,BR
     */
    public CANSparkMax[] getDrive() {
        return new CANSparkMax[] { frontLeft, frontRight, backLeft, backRight };
    }
    public CANSparkMax getIntake() { return intake; }
    public CANSparkMax[] getShooter() {return new CANSparkMax[] { shooter1, shooter2 }; }
}