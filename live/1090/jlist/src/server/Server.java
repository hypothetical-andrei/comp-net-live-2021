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

import java.util.List;
import java.util.Map;

public class Server implements AutoCloseable{

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	private Map<String, List<String>> storage = Collections.synchronizedMap(new HashMap<>());
	
	@Override
	public void close() throws Exception {
		stop();
	}

	public void start(int port) throws IOException {
		stop();
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
		final List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>());
		executorService.execute(() -> {
			while (serverSocket != null && !serverSocket.isClosed()) {
				try {
					Socket socket = serverSocket.accept();
					executorService.execute(() -> {
						try {							
							clients.add(socket);
							ClientState state = new ClientState();
							while (socket != null && !socket.isClosed()) {
								String command = Transport.receive(socket);
								String response = processCommand(command, state);
								System.out.println(command + " -> " + response);
								Transport.send(response, socket);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally  {
							clients.remove(socket);
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stop() throws IOException {
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

}
