package eecs293.uxb.devices.peripherals.video;

import eecs293.uxb.devices.DeviceClass;
import eecs293.uxb.devices.peripherals.AbstractPeripheral;

/**
 * Peripheral video class - should be the
 * parent class of all video peripherals.
 * 
 * @author Sarah Whelan
 *
 */
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
