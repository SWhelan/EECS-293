package eecs293.uxb.messages;

import eecs293.uxb.Connector;
import eecs293.uxb.devices.Device;

public interface Message {
	
	/**
	 * Signifies that the Message has reached the given device coming from the given connector. 
	 * 
	 * @param device
	 * @param connector
	 */
	public void reach(Device device, Connector connector);
	
}
