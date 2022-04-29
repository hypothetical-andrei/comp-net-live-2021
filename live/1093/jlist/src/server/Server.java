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
				try {
					Socket socket = serverSocket.accept();
					executorService.execute(() -> {
						try {
							clients.add(socket);
							ClientState state = new ClientState();
							while (socket != null && !socket.isClosed()) {
								try {
									String command = Transport.receive(socket);
									System.out.println(command);
									String response = processCommand(command, state);
									Transport.send(response, socket);
								} catch (ClassNotFoundException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}							
						} catch (Exception e) {
							e.printStackTrace();
						}
						finally {
						clients.remove(socket);
					}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
//				
			}
		});
	}
	
	private String processCommand(String command, ClientState state) {
		String[] items = command.strip().split("\\s");
		if (state.isAuthenticated) {
			switch (items[0]) {
			case "put":
				if (items.length == 3) {
					List<String> existing;
					if (storage.containsKey(items[1])) {
						existing = storage.get(items[1]);
					} else {
						existing = new ArrayList<>();
					}
					existing.add(items[2]);
					storage.put(items[1], existing);
					return "added to the list";
				} else {
					return "not enough parameters";					
				}
			case "get":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						return storage.get(items[1]).stream().reduce("", (a, e) -> a + "," + e);
					} else {
						return "no such list";
					}
				} else {					
					return "not enough parameters";					
				}
			case "delete":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						storage.remove(items[1]);
					} else {
						return "no such list";
					}
				} else {					
					return "not enough parameters";					
				}
			case "lists":
				return storage.keySet().stream().reduce("", (a, e) -> a + "," + e);
			default:
				return "unrecognized command";
			}
		} else {
			if (items[0].equals("auth")) {
				if (items.length == 2) {
					if (items[1].equals("supersecret")) {
						state.isAuthenticated = true;
						return "welcome in";
					} else {
						return "you shall not pass";
					}
				} else {
					return "not enough parameters";
				}
			} else {
				return "tell me a secret";
			}
		}
	}

}
