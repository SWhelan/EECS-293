package eecs293.uxb.devices;

import eecs293.uxb.Connector;

public class Hub extends AbstractDevice<Hub.Builder> {
	
	public static class Builder extends AbstractDevice.Builder<Hub> {

		public Builder(Integer version) {
			super(version);
		}
		
		public Hub build() throws IllegalStateException {
			this.validate();
			return new Hub(this);
		}
		
		protected Hub getThis() {
			return this;
		}
		
		protected void validate() throws IllegalStateException {
			try {
				super.validate();
			} catch (NullPointerException e) {
				throw new IllegalStateException("The version number was null.");
			}
			
			if (!super.getConnectors().contains(Connector.Type.COMPUTER)) {
				throw new IllegalStateException("This hub does not have a computer connector.");
			}
			
			if (!super.getConnectors().contains(Connector.Type.PERIPHERAL)) {
				throw new IllegalStateException("This hub does not have a peripheral connector.");
			}
		}
		
	}

	private Hub(Builder builder) {
		AbstractDevice<Hub.Builder>();
	}

	@Override
	public DeviceClass getDeviceClass() {
		return DeviceClass.HUB;
	}

}
