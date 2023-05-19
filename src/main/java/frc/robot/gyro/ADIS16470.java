package frc.robot.gyro;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.ADIS16470_IMU;

/*
 * from
 * https://github.com/Team2170/2023-Competition/blob/main/src/main/java/frc/robot/subsystems/IMU/ADIS16470Swerve.java
 */

public class ADIS16470 implements GyroIO {
	public ADIS16470_IMU gyro = new ADIS16470_IMU();

	public double yawOffset_deg = 0.0;

	public void setYaw(double yaw_deg) {
		yawOffset_deg = (yaw_deg % 360) + (gyro.getAngle() % 360);
	}

	public double getYaw_deg() {
		return (gyro.getAngle() % 360) - yawOffset_deg;
	}

	public double getPitch_deg() {
		return gyro.getXComplementaryAngle() % 360;
	}

	public double getRoll_deg() {
		return gyro.getYComplementaryAngle() % 360;
	}

	public Translation3d getAccel_mps2() {
		return new Translation3d(gyro.getAccelX(), gyro.getAccelY(), gyro.getAccelZ());
	}

	public boolean isConnected() {
		return gyro.isConnected();
	}
}
