package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {

	public static void main(String[] args) throws IOException {
		MulticastSocket socket = new MulticastSocket(8080);
		byte[] buffer = new byte[512];
		InetAddress group = InetAddress.getByName("230.0.0.0");
		socket.joinGroup(group);
		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println(received);
			if ("exit".strip().equals(received)) {
				break;
			}
		}
		socket.leaveGroup(group);
		socket.close();
	}

}
