package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(8080);
		byte[] buffer = new byte[512];
		while (socket != null && !socket.isClosed()) {
			DatagramPacket request = new DatagramPacket(buffer, buffer.length);
			socket.receive(request);
			InetAddress clientAddress = request.getAddress();
			int clientPort = request.getPort();
			String decoded = new String(buffer, 0, request.getLength());
			System.out.println("received " + decoded + " from " + clientAddress + " on port " + clientPort);
		}
	}

}
