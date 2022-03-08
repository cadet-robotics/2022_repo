package frc.robot.io;

import com.revrobotics.CANSparkMax;

public class MotorPair {
    private CANSparkMax m1, m2;

    public MotorPair(CANSparkMax motor1, CANSparkMax motor2) {
        m1 = motor1;
        m2 = motor2;
    }

    public void setValue(double val) {
        m1.set(val);
        m2.set(val);
    }

    public CANSparkMax getMotor1() { return m1; }
    public CANSparkMax getMotor2() { return m2; }
}
