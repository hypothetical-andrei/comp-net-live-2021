package client;

import java.io.IOException;
import java.net.Socket;

import common.Transport;

public class Client implements AutoCloseable{

	public interface ClientCallback {
		void onReceive(String message);
	}
	
	private Socket socket;
	
	public Client(String host, int port, ClientCallback callback){
		try {
			socket = new Socket(host, port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
	
	public void send(String message) throws IOException {
		Transport.send(message, socket);
	}
	
	@Override
	public void close() throws Exception {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

}
