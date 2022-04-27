package receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver {

    public static void main(String[] args) throws IOException {
    	MulticastSocket socket = new MulticastSocket(4446);
    	byte[] buf = new byte[256];
        InetAddress group = InetAddress.getByName("230.0.0.0");
        socket.joinGroup(group);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String received = new String(
              packet.getData(), 0, packet.getLength());
            System.out.println(received);
            if ("end".equals(received)) {
                break;
            }
        }
        socket.leaveGroup(group);
        socket.close();
	}
    
   
}