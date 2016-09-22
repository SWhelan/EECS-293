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
public abstract class AbstractPrinter extends AbstractPeripheral {

	public abstract static class Builder extends AbstractPeripheral.Builder {

		public Builder(Integer version) {
			super(version);
		}

	}

	protected AbstractPrinter(Builder builder) {
		super(builder);
	}
	
	@Override
	public DeviceClass getDeviceClass() {
		return DeviceClass.PRINTER;
	}

}
