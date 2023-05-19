package frc.robot.gyro;

public interface GyroIO {
	public void setYaw(double yaw_deg);

	/* in range [0, 360) */
	public double getYaw_deg();

	default public boolean isConnected() {
		return true;
	}

	/** dummy gyro that does nothing */
	static class NoGyro implements GyroIO {
		public void setYaw(double yaw_deg) {}

		public double getYaw_deg() {
			return 0;
		}

		public boolean isConnected() {
			return false;
		}
	}
}
