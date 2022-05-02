package sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class MulticastSender {
	
	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket();
		InetAddress group = InetAddress.getByName("230.0.0.0");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				byte[] buffer = command.getBytes();
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 8080);
				socket.send(packet);
				if ("exit".equals(command.strip())) {
					break;
				}
			}
		} 
		socket.close();
		
	}

}
