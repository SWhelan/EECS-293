package eecs293.uxb.messages;

import eecs293.uxb.connectors.Connector;
import eecs293.uxb.devices.Device;

public final class StringMessage implements Message {
	
	private final String string;
	
	public StringMessage(String string) {
		this.string = string == null ? "" : string;
	}
	
	public String getString() {
		return new String(string);
	}
	
	public int length() {
		return this.string.length();
	}
	
	public char charAt(int index) {
		return this.string.charAt(index);
	}
	
	public boolean contains(CharSequence s) {
		return this.string.contains(s);
	}
	
	public boolean endsWith(String suffix) {
		return this.string.endsWith(suffix);
	}
	
	public boolean startsWith(String prefix) {
		return this.string.startsWith(prefix);
	}
	
	public int indexOf(int ch) {
		return this.string.indexOf(ch);
	}
	
	public int indexOf(int ch, int fromIndex) {
		return this.string.indexOf(ch, fromIndex);
	}
	
	public int indexOf(String str) {
		return this.string.indexOf(str);
	}
	
	public int indexOf(String str, int fromIndex) {
		return this.string.indexOf(str, fromIndex);
	}
	
	public int lastIndexOf(int ch) {
		return this.string.lastIndexOf(ch);
	}
	
	public int lastIndexOf(int ch, int fromIndex) {
		return this.string.lastIndexOf(ch, fromIndex);
	}
	
	public int lastIndexOf(String str) {
		return this.string.lastIndexOf(str);
	}
	
	public int lastIndexOf(String str, int fromIndex) {
		return this.string.lastIndexOf(str, fromIndex);
	}
	
	public boolean isEmpty() {
		return this.string.isEmpty();
	}
	
	@Override
	public int hashCode() {
		return this.string.hashCode();
	}
	
	@Override
	public void reach(Device device, Connector connector) {
		device.recv(this, connector);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringMessage other = (StringMessage) obj;
		if (string == null) {
			if (other.string != null)
				return false;
		} else if (!string.equals(other.string))
			return false;
		return true;
	}
	
}
