package server;

import java.io.File;
import java.util.Scanner;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

public class FtpServerConsole {

	public static void main(String[] args) {
		int port;
		String userFile;
		if (args.length == 2) {
			port = Integer.parseInt(args[0]);
			userFile = args[1];
			FtpServerFactory serverFactory = new FtpServerFactory();
			ListenerFactory listenerFactory = new ListenerFactory();
			listenerFactory.setPort(port);
			serverFactory.addListener("default", listenerFactory.createListener());
			PropertiesUserManagerFactory userManagerFactory =new PropertiesUserManagerFactory();
			userManagerFactory.setFile(new File(userFile));
			userManagerFactory.setPasswordEncryptor(new ClearTextPasswordEncryptor());
			UserManager userManager = userManagerFactory.createUserManager();
			serverFactory.setUserManager(userManager);
			FtpServer server = serverFactory.createServer();
			try {
				server.start();
			} catch (FtpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try (Scanner scanner = new Scanner(System.in)){
				while (true) {
					String command = scanner.nextLine();
					if (command == null || "exit".equals(command)) {
						break;
					}
				}
			} 
		} else {
			System.out.println("Not enough options");
		}
	}

}
