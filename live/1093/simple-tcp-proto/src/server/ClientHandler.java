package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

	private Socket clientSocket;
	private BufferedReader reader;
	private PrintWriter writer;
	private CounterProtocol protocol;
	
	public ClientHandler(Socket socket) throws IOException {
		this.clientSocket = socket;
		this.reader= new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		this.writer = new PrintWriter(this.clientSocket.getOutputStream());
		this.protocol = new CounterProtocol();
	}
	
	@Override
	public void run() {
		while (!clientSocket.isClosed()) {
			try {
				String command = reader.readLine();
				if ("exit".equals(command)) {
					clientSocket.close();
				} else {
					String response = this.process(command);
					writer.println(response);
					writer.flush();					
				}					
			} catch (Exception e) {
				writer.println(e.getMessage());
				writer.flush();
			}
		}		
	}
	
	public String process(String command) {
		return protocol.process(command);
	}

}
