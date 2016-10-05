package org.wegscheid.vjoyfeeder.jna;

import java.io.Closeable;
import java.util.EnumSet;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

public class VJoyFeeder implements AutoCloseable, Closeable {
	static VJoyInterface vjoyInterface;

	static VJoyInterface getVjoyInterface() {
		if (vjoyInterface == null) {
			vjoyInterface = VJoyInterface.INSTANCE;
		}
		return vjoyInterface;
	}

	public static int getNumberOfVJoyDevices() {
		IntByReference rv = new IntByReference();
		getVjoyInterface().GetNumberExistingVJD(rv);
		return rv.getValue();
	}

	public static String getVJoyProductString() {
		Pointer p = getVjoyInterface().GetvJoyProductString();
		return stringize(p);
	}

	public static String getVJoyManufacturerString() {
		Pointer p = getVjoyInterface().GetvJoyManufacturerString();
		return stringize(p);
	}

	public static String getVJoySerialNumberString() {
		Pointer p = getVjoyInterface().GetvJoySerialNumberString();
		return stringize(p);
	}

	int rID;
	boolean failOnMissingUIElement = false;

	public VJoyFeeder(int rID) {
		this.rID = rID;
		getVjoyInterface();
		if (!boolOk(vjoyInterface.vJoyEnabled())) {
			throw new VJoyFeederException("vJoy not enabled");
		}

		int status = vjoyInterface.GetVJDStatus(rID);

		if (status != VJoyInterface.VJD_STAT_FREE) {
			switch (status) {
			// this is recoverable, think about this one
			case VJoyInterface.VJD_STAT_OWN:
				throw new VJoyFeederException("vJoy device " + rID + " is already owned by this feeder");
			case VJoyInterface.VJD_STAT_BUSY:
				throw new VJoyFeederException("vJoy device " + rID + " is already owned by another feeder");
			case VJoyInterface.VJD_STAT_MISS:
				throw new VJoyFeederException("vJoy device " + rID + " is not installed or is disabled");
			default:
				throw new VJoyFeederException("vJoy device " + rID + " general error: " + status);
			}
		}

		if (!boolOk(vjoyInterface.AcquireVJD(rID))) {
			throw new VJoyFeederException("Failed to acquire vJoy device number " + rID);
		}
	}

	static EnumSet<Axis> allAxes = EnumSet.allOf(Axis.class);

	public EnumSet<Axis> getAxes() {
		EnumSet<Axis> rv = EnumSet.noneOf(Axis.class);
		for (Axis a : allAxes) {
			if (getAxisExist(a)) {
				rv.add(a);
			}
		}
		return rv;
	}

	public int getButtonNumber() {
		return vjoyInterface.GetVJDButtonNumber(rID);
	}

	public int getDiscPovNumber() {
		return vjoyInterface.GetVJDDiscPovNumber(rID);
	}

	public int getContPovNumber() {
		return vjoyInterface.GetVJDContPovNumber(rID);
	}

	public boolean getAxisExist(Axis axis) {
		int rv1 = vjoyInterface.GetVJDAxisExist(rID, axis.getAxisUint());
		return boolOk(rv1);
	}

	public int getAxisMax(Axis axis) {
		NativeLongByReference rv = new NativeLongByReference();
		vjoyInterface.GetVJDAxisMax(rID, axis.getAxisUint(), rv);
		return rv.getValue().intValue();
	}

	public int getAxisMin(Axis axis) {
		NativeLongByReference rv = new NativeLongByReference();
		vjoyInterface.GetVJDAxisMin(rID, axis.getAxisUint(), rv);
		return rv.getValue().intValue();
	}

	public void setAxis(Axis axis, int v) {
		int rv = vjoyInterface.SetAxis(v, rID, axis.getAxisUint());
		if (failOnMissingUIElement && !boolOk(rv))
			throw new VJoyFeederException("unable to setAxis");
	}

	public void push(int id) {
		int rv = vjoyInterface.SetBtn(1, this.rID, id);
		if (failOnMissingUIElement && !boolOk(rv))
			throw new VJoyFeederException("unable to push");
	}

	public void release(int id) {
		int rv = vjoyInterface.SetBtn(0, this.rID, id);
		if (failOnMissingUIElement && !boolOk(rv))
			throw new VJoyFeederException("unable to release");
	}

	boolean boolOk(int i) {
		return i > 0;
	}

	static String stringize(Pointer p) {
		return p.getWideString(0);
	}

	@Override
	public void close() {
		// TODO ????
		vjoyInterface.ResetVJD(rID);
		vjoyInterface.RelinquishVJD(rID);
	}

	public enum Axis {
		X(VJoyInterface.HID_USAGE_X), //
		Y(VJoyInterface.HID_USAGE_Y), //
		Z(VJoyInterface.HID_USAGE_Z), //
		RX(VJoyInterface.HID_USAGE_RX), //
		RY(VJoyInterface.HID_USAGE_RY), //
		RZ(VJoyInterface.HID_USAGE_RZ), //
		SL0(VJoyInterface.HID_USAGE_SL0), //
		SL1(VJoyInterface.HID_USAGE_SL1), //
		WHL(VJoyInterface.HID_USAGE_WHL), //
		POV(VJoyInterface.HID_USAGE_POV);
		
		private int axisUint;

		Axis(int _axisUint) {
			this.axisUint = _axisUint;
		}

		int getAxisUint() {
			return this.axisUint;
		}
	}

}