package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Transport;

public class Client implements AutoCloseable {

	private Socket socket;
	
	public interface ClientCallback {
		void onTalk(String message);
	}
	
	public Client(String host, int port, ClientCallback callback) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		new Thread(() -> {
			while (socket != null && !socket.isClosed()) {
				callback.onTalk(Transport.receive(socket));
			}
		}).start();
	}
	
	@Override
	public void close() throws IOException {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}
	
	private void send(String message) {
		try {
			Transport.send(message, socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
