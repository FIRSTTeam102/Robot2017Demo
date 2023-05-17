package frc.robot;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

public class Robot extends TimedRobot {
	XboxController controller = new XboxController(0);

	WPI_TalonSRX frontLeft = new WPI_TalonSRX(0);
	WPI_TalonSRX backLeft = new WPI_TalonSRX(1);
	WPI_TalonSRX frontRight = new WPI_TalonSRX(8);
	WPI_TalonSRX backRight = new WPI_TalonSRX(7);

	MecanumDrive mecanumDrive = new MecanumDrive(frontLeft, backLeft, frontRight, backRight);

	private double maxSpeed = 0.2; // FOR DEMO
	private GenericEntry maxSpeedDash;

	@Override
	public void robotInit() {
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
	}

	static double deadband = 0.1;

	@Override
	public void teleopPeriodic() {
		maxSpeed = maxSpeedDash.getDouble(maxSpeed);
		mecanumDrive.driveCartesian(
			MathUtil.applyDeadband(-controller.getLeftY(), deadband) * maxSpeed,
			MathUtil.applyDeadband(controller.getLeftX(), deadband) * Math.min(maxSpeed + 0.1, 1), // strafe is slower
			MathUtil.applyDeadband(controller.getRightX(), deadband) * maxSpeed);

		// todo: implement shooter?
	}
}
