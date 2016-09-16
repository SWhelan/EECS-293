package eecs293.uxb.devices;

import eecs293.uxb.Connector;

/**
 * A device that is required to have at least one 
 * Connector.Type.COMPUTER and one Connector.Type.PERIPHERAL.
 * 
 * @author Sarah Whelan
 */
public class Hub extends AbstractDevice<Hub.Builder> {
	
	public static class Builder extends AbstractDevice.Builder<Builder> {
		public static final String MISSING_COMPUTER_CONNECTOR_MESSAGE = "This hub does not have a computer connector.";
		public static final String MISSING_PERIPHERAL_CONNECTOR_MESSAGE = "This hub does not have a peripheral connector.";

		public Builder(Integer version) {
			super(version);
		}
		
		public Hub build() throws IllegalStateException {
			this.validate();
			return new Hub(this);
		}
		
		protected Builder getThis() {
			return this;
		}
		
		/**
		 * Requires a non-null version, at least one COMPUTER type connector, 
		 * and at least one PERIPHERAL type connector.
		 * 
		 * @throws IllegalStateException if any of the requirements are not met
		 */
		protected void validate() throws IllegalStateException {
			super.validate();
		
			if (!super.getConnectors().contains(Connector.Type.COMPUTER)) {
				throw new IllegalStateException(MISSING_COMPUTER_CONNECTOR_MESSAGE);
			}
			
			if (!super.getConnectors().contains(Connector.Type.PERIPHERAL)) {
				throw new IllegalStateException(MISSING_PERIPHERAL_CONNECTOR_MESSAGE);
			}
		}
		
	}

	private Hub(Builder builder) {
		super(builder);
	}

	@Override
	public DeviceClass getDeviceClass() {
		return DeviceClass.HUB;
	}

}
