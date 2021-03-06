package client;

import java.util.Scanner;

import common.Settings;

public class Program {

	public static void main(String[] args) {
		try (Client client = new Client(Settings.HOST, Settings.PORT, message -> {
			System.out.println(message);
		})) {
			System.out.println("Client started. Type 'exit' to close.");
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					client.send(command);
					if (command == null || "exit".equals(command)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
