package eecs293.uxb.devices.peripherals.printers;

import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.peripherals.AbstractPeripheral;

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
