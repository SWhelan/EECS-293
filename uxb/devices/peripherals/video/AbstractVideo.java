package eecs293.uxb.devices.peripherals.video;

import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.peripherals.AbstractPeripheral;

public abstract class AbstractVideo extends AbstractPeripheral {

	public abstract static class Builder extends AbstractPeripheral.Builder {

		public Builder(Integer version) {
			super(version);
		}

	}
	
	protected AbstractVideo(Builder builder) {
		super(builder);
	}

	@Override
	public DeviceClass getDeviceClass() {
		return DeviceClass.VIDEO;
	}

}
