package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server implements AutoCloseable{

	public DatagramSocket socket;
	
	public Server(int port) throws IOException {
		socket = new DatagramSocket(port);
		byte[] buffer = new byte[512];
		while (socket !=null && !socket.isClosed()) {
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
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

}
