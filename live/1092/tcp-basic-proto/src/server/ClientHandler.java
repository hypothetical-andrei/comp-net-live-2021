package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	private CounterProto proto;
	
	public ClientHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.reader =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintWriter(socket.getOutputStream());
		this.proto = new CounterProto();
	}

	@Override
	public void run() {
		while (!this.socket.isClosed()) {
			try {
				String command = reader.readLine();
				if ("exit".equals(command.strip())) {
					this.socket.close();
				} else {
					System.out.println(command);
					this.writer.println(this.process(command));
					this.writer.flush();
				}
			} catch (Exception e) {
				this.writer.println(e.getMessage());
				this.writer.flush();
			}
		}
	}
	
	public String process(String input) {
		return this.proto.process(input);
	}

}
