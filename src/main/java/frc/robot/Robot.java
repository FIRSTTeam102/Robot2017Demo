package frc.robot;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.robot.gyro.GyroIO;
import frc.robot.gyro.GyroIO.NoGyro;

public class Robot extends TimedRobot {
	XboxController controller = new XboxController(0);

	WPI_TalonSRX frontLeft = new WPI_TalonSRX(0);
	WPI_TalonSRX backLeft = new WPI_TalonSRX(1);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(8);
	WPI_TalonSRX backRight = new WPI_TalonSRX(7);

	MecanumDrive mecanumDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

	GyroIO gyro;

	private double maxSpeed = 0.2; // FOR DEMO
	private GenericEntry maxSpeedDash;

	private boolean fieldOriented = false;
	private GenericEntry fieldOrientedDash;

	@Override
	public void robotInit() {
		gyro = new NoGyro(); // new ADW22307(1); // new ADIS16470();

		frontRight.setInverted(true);
		backRight.setInverted(true);

		frontLeft.setNeutralMode(NeutralMode.Brake);
		backLeft.setNeutralMode(NeutralMode.Brake);
		frontRight.setNeutralMode(NeutralMode.Brake);
		backRight.setNeutralMode(NeutralMode.Brake);

		var driveTab = Shuffleboard.getTab("drive");
		maxSpeedDash = driveTab
			.addPersistent("max speed", maxSpeed)
			.withWidget(BuiltInWidgets.kNumberSlider)
			.withProperties(Map.of("min", 0.0, "max", 1.0, "block increment", 0.1))
			.withPosition(0, 0)
			.withSize(4, 2)
			.getEntry();
		fieldOrientedDash = driveTab
			.add("field oriented", fieldOriented)
			.withWidget(BuiltInWidgets.kBooleanBox)
			.withPosition(4, 0)
			.withSize(2, 2)
			.getEntry();

		LiveWindow.enableAllTelemetry();
	}

	static double deadband = 0.1;

	@Override
	public void teleopPeriodic() {
		maxSpeed = maxSpeedDash.getDouble(maxSpeed);

		if (controller.getAButtonPressed()) {
			fieldOriented = !fieldOriented;
			fieldOrientedDash.setBoolean(fieldOriented);
		}

		if (controller.getYButtonPressed()) {
			gyro.setYaw(0);
		}

		mecanumDrive.driveCartesian(
			MathUtil.applyDeadband(-controller.getLeftY(), deadband) * maxSpeed,
			MathUtil.applyDeadband(controller.getLeftX(), deadband) * (maxSpeed + 0.1), // strafe is slower
			MathUtil.applyDeadband(controller.getRightX(), deadband) * maxSpeed,
			Rotation2d.fromDegrees(fieldOriented && gyro.isConnected()
				? gyro.getYaw_deg()
				: 0));

		// todo: implement shooter?
	}
}
