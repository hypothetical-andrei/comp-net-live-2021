package server;

import java.util.Scanner;

import common.Settings;
import jakarta.xml.ws.Endpoint;

public class Server {

	public static void main(String[] args) {
		String endpoint = String.format("http://%s:%d/greetings", Settings.HOST, Settings.PORT);
		Endpoint.publish(endpoint, new Service());
		System.out.println("Server running, type 'exit' to close");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if (command == null || "exit".equals(command)) {
					break;
				}
			}
		} finally {
			System.exit(0);
		}
	}

}
