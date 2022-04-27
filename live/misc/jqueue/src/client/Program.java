package client;

import java.util.Scanner;

import common.Settings;
import server.Server;

public class Program {

	public static void main(String[] args) {
		try (Client client = new Client(Settings.HOST, Settings.PORT, message -> {
			System.out.println(message);
		})) {
			System.out.println("Client connected, type 'exit' to stop it.");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					if (command == null || "exit".equalsIgnoreCase(command)) {
						break;
					} else {
						client.send(command);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			System.exit(0);
		}
	}

}
