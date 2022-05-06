package server;

import java.util.Scanner;

import common.Settings;
import jakarta.xml.ws.Endpoint;

public class ServerApplication {

	public static void main(String[] args) {
		String endpoint = String.format("http://%s:%d/talk", Settings.HOST, Settings.PORT);
		Endpoint.publish(endpoint, new ServerService());
		System.out.println("Server started. Type 'exit' to close.");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if (command == null || "exit".equals(command)) {
					break;
				}
			}
		}
	}

}
