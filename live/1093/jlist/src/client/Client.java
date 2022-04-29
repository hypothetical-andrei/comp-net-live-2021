package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import common.Transport;

public class Client implements AutoCloseable{

	public interface ClientCallback {
		void onReceive(String message);
	}
	
	private Socket socket;
	
	public Client(String host, int port, ClientCallback callback) throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		new Thread(() -> {
			while (socket != null && !socket.isClosed()) {
				try {
					callback.onReceive(Transport.receive(socket));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	public void close() throws Exception {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

}
