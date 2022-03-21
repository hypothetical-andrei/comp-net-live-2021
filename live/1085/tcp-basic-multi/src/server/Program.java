package server;

import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		int port = Integer.parseInt(ResourceBundle.getBundle("settings").getString("port"));
		try (Server server = new Server(port)) {
			System.out.println(String.format("Server running on port %d, type 'exit' to close", port));
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					String command = scanner.nextLine();
					if (command == null || "exit".equals(command)) {
						break;
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
