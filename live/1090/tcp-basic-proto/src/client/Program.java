package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
		String host = ResourceBundle.getBundle("settings").getString("host");
		try (Socket socket = new Socket(host, 8090)) {
			System.out.println("Connected to server");
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					writer.println(command);
					writer.flush();
					StringBuilder response = new StringBuilder();
					String line = "";
					while ((line = reader.readLine()) != null) {
						if (line.isEmpty()) {
							break;
						}
						response.append(line);
					}
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
