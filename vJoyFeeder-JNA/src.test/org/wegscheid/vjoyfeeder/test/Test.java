package org.wegscheid.vjoyfeeder.test;

import org.wegscheid.vjoyfeeder.jna.VJoyFeeder;
import org.wegscheid.vjoyfeeder.jna.VJoyFeeder.Axis;

public class Test {

	public static void main(String[] args) throws InterruptedException {

		System.out.println(VJoyFeeder.getVJoyProductString());
		System.out.println(VJoyFeeder.getVJoyManufacturerString());
		System.out.println(VJoyFeeder.getVJoySerialNumberString());
		System.out.println("we have " + VJoyFeeder.getNumberOfVJoyDevices() + " devices");

		try (VJoyFeeder vjoyAccessor = new VJoyFeeder(1)) {
			System.out.println(vjoyAccessor.getAxes());
			
			int xmax = (int) vjoyAccessor.getAxisMax(Axis.X);
			int xmin = (int) vjoyAccessor.getAxisMin(Axis.X);
			
			System.out.println("max/min = " + xmax + " " + xmin);

			while (true) {
				vjoyAccessor.push(1);
				vjoyAccessor.setAxis(Axis.X, xmax);
				Thread.sleep(1000);
				vjoyAccessor.release(1);
				vjoyAccessor.setAxis(Axis.X, xmin);
				Thread.sleep(1000);
			}
		}
	}

}
