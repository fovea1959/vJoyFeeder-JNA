package org.wegscheid.vjoyfeeder.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

public interface VJoyInterface extends Library {
	VJoyInterface INSTANCE = (VJoyInterface) Native.loadLibrary("vJoyInterface", VJoyInterface.class);

	// General driver data
	
	// TODO SHORT getvJoyVersion
    int vJoyEnabled();
	Pointer GetvJoyProductString();
	Pointer GetvJoyManufacturerString();
	Pointer GetvJoySerialNumberString();
	// TODO DriverMatch
	// TODO getvJoyMaxDevices
	int GetNumberExistingVJD(IntByReference n);
	
	// vJoy Device Properties
    int GetVJDButtonNumber(int rID);
    int GetVJDDiscPovNumber(int rID);
    int GetVJDContPovNumber(int rID);
    int GetVJDAxisExist(int rID, int Axis);
    int GetVJDAxisMax(int rID, int Axis, NativeLongByReference max); // in .h, not in .pdf
    int GetVJDAxisMin(int rID, int Axis, NativeLongByReference min); // in .h, not in .pdf
    int GetVJDStatus(int rID);
    
    // Write access - basic
	static public final int VJD_STAT_OWN = 0;
	static public final int VJD_STAT_FREE = 1;
	static public final int VJD_STAT_BUSY = 2;
	static public final int VJD_STAT_MISS = 3;
	static public final int VJD_STAT_UNKN = 4;

    int AcquireVJD(int rID);
    void RelinquishVJD(int rID);
    // TODO UpdateVJD
    
    // Robust write access
	// HID Descriptor definitions - Axes
	public static final int HID_USAGE_X	=	0x30;
	public final static int HID_USAGE_Y	=	0x31;
	public final static int HID_USAGE_Z	=	0x32; 
	public final static int HID_USAGE_RX =	0x33;
	public final static int HID_USAGE_RY =	0x34;
	public final static int HID_USAGE_RZ =	0x35;
	public final static int HID_USAGE_SL0 =	0x36;
	public final static int HID_USAGE_SL1 =	0x37;
	public final static int HID_USAGE_WHL =	0x38;
	public final static int HID_USAGE_POV =	0x39;


    int ResetVJD(int rID);
    int ResetAll();
    // TODO ResetButtons
    // TODO ResetPOvs
    int SetAxis(int Value, int rID, int Axis);
    int SetBtn(int Value, int rID, int nBtn);
    int SetDiscPov(int Value, int rID, int nPov);
    int SetContPov(int Value, int rID, int nPov); // may need something besides int for Value

    

}