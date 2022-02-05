package frc.robot.io;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Config;
import org.json.JSONObject;

public class Motors {
    CANSparkMax frontLeft, frontRight, backLeft, backRight; // drive motors

    Config conf = Config.getInstance();

    public Motors() {
        // initialize drive motors
        frontLeft = new CANSparkMax(conf.getMotors().getInt("fl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        frontRight = new CANSparkMax(conf.getMotors().getInt("fr drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backLeft = new CANSparkMax(conf.getMotors().getInt("bl drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
        backRight = new CANSparkMax(conf.getMotors().getInt("br drive"), CANSparkMaxLowLevel.MotorType.kBrushless);
    }

    /**
     * @return The drive motors, in order FL,FR,BL,BR
     */
    public CANSparkMax[] getDrive() {
        return new CANSparkMax[] { frontLeft, frontRight, backLeft, backRight };
    }
}
