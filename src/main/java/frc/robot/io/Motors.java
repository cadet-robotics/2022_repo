package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.robot.Config;

public class Motors {
    private CANSparkMax frontLeft, frontRight, backLeft, backRight; // drive motors
    private CANSparkMax intake, intakeTop;
    private CANSparkMax shooter1, shooter2;
    private CANSparkMax climb;

    private Config conf = Config.getInstance();

    public Motors() {
        // initialize drive motors
        frontLeft = new CANSparkMax(conf.getCan().getInt("fl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        frontLeft.enableVoltageCompensation(11);
        frontRight = new CANSparkMax(conf.getCan().getInt("fr drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        frontRight.enableVoltageCompensation(11);
        backLeft = new CANSparkMax(conf.getCan().getInt("bl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft.enableVoltageCompensation(11);
        backRight = new CANSparkMax(conf.getCan().getInt("br drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight.enableVoltageCompensation(11);

        intake = new CANSparkMax(conf.getCan().getInt("intake"), CANSparkMaxLowLevel.MotorType.kBrushed);
        intake.enableVoltageCompensation(10);
        intakeTop = new CANSparkMax(conf.getCan().getInt("intake top"), CANSparkMaxLowLevel.MotorType.kBrushed);
        intakeTop.enableVoltageCompensation(10);

        shooter1 = new CANSparkMax(conf.getCan().getInt("flywheel 0"), CANSparkMaxLowLevel.MotorType.kBrushless);
        shooter2 = new CANSparkMax(conf.getCan().getInt("flywheel 1"), CANSparkMaxLowLevel.MotorType.kBrushless);
        shooter1.enableVoltageCompensation(11);
        shooter2.enableVoltageCompensation(11);

        climb = new CANSparkMax(conf.getCan().getInt("climb"), CANSparkMaxLowLevel.MotorType.kBrushless);
        climb.enableVoltageCompensation(11);

        // can motor setup stuff :)
        frontLeft.setInverted(true);
        shooter1.setInverted(true);
        intake.setInverted(true);
        intakeTop.setInverted(false);
    }

    /**
     * @return The drive motors, in order FL,FR,BL,BR
     */
    public CANSparkMax[] getDrive() {
        return new CANSparkMax[] { frontLeft, frontRight, backLeft, backRight };
    }
    public MotorPair getIntake() { return new MotorPair(intake, intakeTop); }
    public CANSparkMax[] getShooter() {return new CANSparkMax[] { shooter1, shooter2 }; }
    public CANSparkMax getClimb() { return climb; }
}