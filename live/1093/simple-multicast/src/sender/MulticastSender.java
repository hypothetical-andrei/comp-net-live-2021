package sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSender {

	public static void main(String[] args) throws UnknownHostException, IOException {
		multicast("Hello all!", InetAddress.getByName("230.0.0.0"));
	}
	
	public static void multicast(String message, InetAddress address) throws IOException{
		DatagramSocket socket = new DatagramSocket();
		socket.setBroadcast(true);
		byte[] buffer = message.getBytes();
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 8080);
		socket.send(packet);
		socket.close();
	}

}
