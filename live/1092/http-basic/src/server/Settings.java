package server;

import java.util.ResourceBundle;

public class Settings {
	public static final int PORT;
	
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("settings");
		PORT = Integer.parseInt(bundle.getString("port"));
	}
}
