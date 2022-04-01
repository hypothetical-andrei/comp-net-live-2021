package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import common.Message;
import common.Transport;

public class Server implements AutoCloseable {

	private DatagramSocket socket;

	@Override
	public void close() throws Exception {
		stop();
	}

	public void start(int port) throws SocketException {
		stop();
		final Set<InetSocketAddress> addresses = new HashSet<>();
		socket = new DatagramSocket(port);
		socket.setSendBufferSize(10 * 1024);
		new Thread(() -> {
			Message message;
			try {
				message = Transport.receive(socket);
				System.out.println(message.getText());
				switch (message.getType()) {
				case Message.SUBSCRIBE:
					addresses.add(message.getAddress());
					break;
				case Message.UNSUBSCRIBE:
					addresses.remove(message.getAddress());
					break;
				case Message.TALK:
					addresses.forEach(address -> {
						try {
							Transport.send(message, socket, address);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
					break;
				default:
					break;
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void stop() {
		if (socket != null && !socket.isClosed()) {
			socket.close();
			socket = null;
		}
	}

}
