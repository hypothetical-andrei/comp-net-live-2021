package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server implements AutoCloseable{

	private DatagramSocket socket;
	
	public Server(int port) throws IOException {
		socket = new DatagramSocket(port);
		while (socket != null && !socket.isClosed()) {
			byte[] buffer = new byte[512];
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			socket.receive(request);
			InetAddress clientAddress = request.getAddress();
			int clientPort = request.getPort();
			
			String decoded = new String(buffer, 0, request.getLength());
			
			System.out.println(decoded);
			
			buffer = decoded.toUpperCase().getBytes();
			
			DatagramPacket response = new DatagramPacket(buffer, buffer.length, clientAddress, clientPort);
			socket.send(response);
		}
	}
	
	@Override
	public void close() throws Exception {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

}
