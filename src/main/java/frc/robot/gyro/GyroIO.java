package frc.robot.gyro;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;

public interface GyroIO extends Sendable {
	public void setYaw(double yaw_deg);

	/* in range [0, 360) */
	public double getYaw_deg();

	default public boolean isConnected() {
		return true;
	}

	@Override
	default public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("Gyro");
		builder.addDoubleProperty("Value", () -> this.getYaw_deg() - 180, null);
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
