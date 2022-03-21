package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket;
	
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		while (!serverSocket.isClosed()) {
			Socket clientSocket = serverSocket.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
			while (!clientSocket.isClosed()) {
				try {
					String command = reader.readLine();
					if ("exit".equals(command.strip())) {
						clientSocket.close();
					} else {
						System.out.println(command);
						writer.println(command.toUpperCase());
						writer.flush();
					}	
				} catch (Exception e) {
					writer.println(e.getMessage());
					writer.flush();
				}
				
			}
		}
	}

	@Override
	public void close() throws Exception {
		serverSocket.close();	
	}

}
