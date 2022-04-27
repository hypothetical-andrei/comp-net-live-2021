package sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastSender {

	public static void main(String[] args) throws IOException {
		DatagramSocket socket;
		InetAddress group;
		byte[] buf;
		String multicastMessage = "hello";
		socket = new DatagramSocket();
		group = InetAddress.getByName("230.0.0.0");
		buf = multicastMessage.getBytes();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
		socket.send(packet);
		socket.close();
	}

}
