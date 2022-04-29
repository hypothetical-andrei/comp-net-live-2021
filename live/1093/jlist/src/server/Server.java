package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Transport;

import java.util.Map;
import java.util.List;

public class Server implements AutoCloseable{

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private Map<String, List<String>> storage = Collections.synchronizedMap(new HashMap<>());
	
	
	@Override
	public void close() throws Exception {
		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
	}


	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(50);
		List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>());
		executorService.execute(() -> {
			while (serverSocket != null && !serverSocket.isClosed()) {
				Socket socket;
				try {
					socket = serverSocket.accept();
					executorService.execute(() -> {
						clients.add(socket);
						ClientState state = new ClientState();
						while (socket != null && !socket.isClosed()) {
							try {
								String command = Transport.receive(socket);
								String response = processCommand(command, state);
								Transport.send(response, socket);
							} catch (ClassNotFoundException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					clients.remove(socket);
				}
			}
		});
	}

}
