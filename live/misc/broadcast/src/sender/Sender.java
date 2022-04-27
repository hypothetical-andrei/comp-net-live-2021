package sender;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class Sender {

	private static DatagramSocket socket = null;

    public static void main(String[] args) throws IOException {
//        broadcast("Hello", InetAddress.getByName("255.255.255.255"));
    	List<InetAddress> broadcastAddresses = listAllBroadcastAddresses();
    	broadcastAddresses.stream().forEach(address -> {
    		System.out.println(address.getHostAddress());
    		try {
				broadcast("Hello", address);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	});
    }

    public static void broadcast(
      String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet 
          = new DatagramPacket(buffer, buffer.length, address, 8080);
        socket.send(packet);
        socket.close();
    }
    
	public static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
	    List<InetAddress> broadcastList = new ArrayList<>();
	    Enumeration<NetworkInterface> interfaces 
	      = NetworkInterface.getNetworkInterfaces();
	    while (interfaces.hasMoreElements()) {
	        NetworkInterface networkInterface = interfaces.nextElement();

	        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
	            continue;
	        }

	        networkInterface.getInterfaceAddresses().stream() 
	          .map(a -> a.getBroadcast())
	          .filter(Objects::nonNull)
	          .forEach(broadcastList::add);
	    }
	    return broadcastList;
	}
}
