package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Transport;

public class Client implements AutoCloseable{

	private Socket socket;
	
	public interface ClientCallback {
		void onTalk(String message);
	}
	
	public Client(String host, int port, ClientCallback callback) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		new Thread(() -> {
			while (socket != null && !socket.isClosed()) {
				try {
					callback.onTalk(Transport.receive(socket));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void send(String message) throws IOException {
		Transport.send(message, socket);
	}
	
	@Override
	public void close() throws Exception {
		socket.close();
	}

}
