package common;

import java.io.Serializable;
import java.net.InetSocketAddress;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;

	public static final int SUBSCRIBE = 1;
	public static final int UNSUBSCRIBE = 2;
	public static final int TALK = 3;
	
	private int type;
	private String text;
	
	private transient InetSocketAddress address;	
	
	public Message(int type, String text) {
		super();
		this.type = type;
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}
	
	public static Message subscribe() {
		return new Message(SUBSCRIBE, null);
	}
	
	public static Message unsubscribe() {
		return new Message(UNSUBSCRIBE, null);
	}
	
	public static Message talk(String text) {
		return new Message(TALK, text);
	}
}
