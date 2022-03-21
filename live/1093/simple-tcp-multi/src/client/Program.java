package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		int port = Integer.parseInt(ResourceBundle.getBundle("resources").getString("port"));
		String host = ResourceBundle.getBundle("resources").getString("host");
		try (Socket clientSocket = new Socket(host, port)) {
			System.out.println("Connected to server!");
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					if ("exit".equals(command.strip())) {
						break;
					} else {
						writer.println(command.strip());
						writer.flush();
						String response = reader.readLine();
						System.out.println(response);											
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.exit(0);
		}

	}

}
