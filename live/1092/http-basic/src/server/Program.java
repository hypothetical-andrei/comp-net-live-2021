package server;

import java.util.Scanner;

public class Program {

	public static void main(String[] args) {
		try (WebServer server = new WebServer()) {
			server.open(Settings.PORT);
			System.out.println("Server is running. Type 'exit' to close");
			try (Scanner scanner = new Scanner(System.in)) {
				while(true) {
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
