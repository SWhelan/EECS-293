package eecs293.uxb.devices.peripherals.printers;

import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.peripherals.AbstractPeripheral;

/**
 * Peripheral printer class - should be the
 * parent class of all printer peripherals.
 * 
 * @author Sarah Whelan
 *
 */
public abstract class AbstractPrinter<T extends AbstractPrinter.Builder<T>> extends AbstractPeripheral<T> {

	public abstract static class Builder<T> extends AbstractPeripheral.Builder<T> {

		public Builder(Integer version) {
			super(version);
		}

	}

	protected AbstractPrinter(Builder<T> builder) {
		super(builder);
	}
	
	@Override
	public DeviceClass getDeviceClass() {
		return DeviceClass.PRINTER;
	}

}
