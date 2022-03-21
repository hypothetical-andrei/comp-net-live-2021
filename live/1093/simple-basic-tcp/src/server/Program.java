package server;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Program {

	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(ResourceBundle.getBundle("resources").getString("port"));

		try (Server server = new Server(port)) {
			System.out.println(String.format("Server started on port %d. Type 'exit' to close", port));
			try (Scanner scanner = new Scanner(System.in)) {
				String command = scanner.nextLine();
				if (command == null || "exit".equals(command)) {
					server.close();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.exit(0);
		}
	}

}
