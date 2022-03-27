package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import static common.Message.talk;
import static common.Message.subscribe;
import static common.Message.unsubscribe;

import common.Message;
import common.Transport;

public class Client implements AutoCloseable {

	private DatagramSocket socket;
	private InetSocketAddress address;
	
	public interface ClientCallback {
		void onTalk(Message message);
	}
	
	public Client(String host, int port, ClientCallback callback) throws IOException {
		socket = new DatagramSocket();
		socket.setSendBufferSize(10 * 1024);
		address = new InetSocketAddress(host, port);
		Transport.send(subscribe(), socket, address);
		new Thread(() -> {
			while (socket != null && !socket.isClosed()) {
				try {
					callback.onTalk(Transport.receive(socket));
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void send(String text) {
		try {
			Transport.send(talk(text), socket, address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() throws Exception {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

}
