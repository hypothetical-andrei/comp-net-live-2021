package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements AutoCloseable{

	private DatagramSocket socket;
	
	public Server(int port) throws IOException {
		socket = new DatagramSocket(port);
		while (!socket.isClosed()) {
			byte[] buffer = new byte[512];
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			socket.receive(request);
			InetAddress clientAddress = request.getAddress();
			int clientPort = request.getPort();
			String decoded = new String(buffer, 0, request.getLength());
			System.out.println("received " + decoded + " from " + clientAddress + " on " + clientPort);
		}
	}

	@Override
	public void close() throws Exception {
		if (socket != null) {
			socket.close();
		}
	}

}
