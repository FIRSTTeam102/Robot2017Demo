package frc.robot.gyro;

import edu.wpi.first.wpilibj.AnalogGyro;

/*
 * http://www.team358.org/files/programming/ControlSystem2015-2019/specs/Accelerometer-Gyro.pdf
 */

public class ADW22307 implements GyroIO {
	public ADW22307(int channel) {
		gyro = new AnalogGyro(channel);

		gyro.initGyro();
		gyro.reset();
		gyro.setSensitivity(0.007);
	}

	public AnalogGyro gyro;

	public double yawOffset_deg = 0.0;

	public void setYaw(double yaw_deg) {
		yawOffset_deg = (yaw_deg % 360) + (gyro.getAngle() % 360);
	}

	public double getYaw_deg() {
		return (gyro.getAngle() % 360) - yawOffset_deg;
	}
}
