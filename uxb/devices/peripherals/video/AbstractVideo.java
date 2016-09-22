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
public abstract class AbstractVideo<T extends AbstractVideo.Builder<T>> extends AbstractPeripheral<T> {

	public abstract static class Builder<T> extends AbstractPeripheral.Builder<T> {

		public Builder(Integer version) {
			super(version);
		}

	}
	
	protected AbstractVideo(Builder<T> builder) {
		super(builder);
	}

	@Override
	public DeviceClass getDeviceClass() {
		return DeviceClass.VIDEO;
	}

}
