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

	public void start(int port) throws SocketException {
		stop();
		final Set<InetSocketAddress> addresses = new HashSet<InetSocketAddress>();
		socket = new DatagramSocket(port);
		socket.setSendBufferSize(10 * 1024);
		new Thread(() -> {
			while (socket != null && !socket.isClosed()) {
				Message message;
				try {
					message = Transport.receive(socket);
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
								e.printStackTrace();
							}
						});
						break;
					default:
						break;
					}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public void stop() {
		if (socket != null && !socket.isClosed()) {
			socket.close();
			socket = null;
		}
	}

	@Override
	public void close() throws Exception {
		stop();
	}

}
