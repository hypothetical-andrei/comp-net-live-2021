package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {

	public static void main(String[] args) throws IOException {
		MulticastSocket socket = new MulticastSocket(8080);
		byte[] buffer = new byte[512];
		InetAddress address = InetAddress.getByName("230.0.0.0");
		socket.joinGroup(address);
		while (true) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			socket.receive(packet);
			String decoded = new String(buffer, 0, buffer.length);
			System.out.println(decoded);
			if ("exit".equals(decoded.strip())) {
				break;
			}
		}
		socket.leaveGroup(address);
		socket.close();
	}

}
