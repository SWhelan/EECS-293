package eecs293.uxb.devices.peripherals;

import eecs293.uxb.Connector;
import eecs293.uxb.devices.AbstractDevice;

/**
 * Peripherals require that all of their connectors
 * are peripherals but otherwise act like devices.
 * 
 * @author Sarah Whelan
 */
public abstract class AbstractPeripheral extends AbstractDevice<AbstractPeripheral.Builder> {
	
	public static abstract class Builder extends AbstractDevice.Builder<AbstractPeripheral.Builder> {
		private static final String NOT_ALL_CONNECTORS_ARE_PERIPHERALS = "Not all connectors are of type peripheral.";
	
		public Builder(Integer version) {
			super(version);
		}
	
		@Override
		public void validate() throws IllegalStateException, NullPointerException {
			super.validate();
			if (!this.getConnectors().stream().allMatch(type -> type.equals(Connector.Type.PERIPHERAL))) {
				throw new IllegalStateException(NOT_ALL_CONNECTORS_ARE_PERIPHERALS);
			}
		}
	}
	
	protected AbstractPeripheral(Builder builder) {
		super(builder);
	}
}
