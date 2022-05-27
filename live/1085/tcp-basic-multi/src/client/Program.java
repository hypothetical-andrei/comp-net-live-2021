package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {
	
	public static void main(String[] args) {
//		int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
		int port = 4444;
		String hostname = ResourceBundle.getBundle("settings").getString("host");
		try (Socket socket = new Socket(hostname, port)) {
			System.out.println("Connected to server");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			try(Scanner scanner = new Scanner(System.in)) {
				while(true) {
					String command = scanner.nextLine();
					writer.println(command);
					writer.flush();
					String response = reader.readLine();
					System.out.println(response);	
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.exit(0);
		}
	}

}
