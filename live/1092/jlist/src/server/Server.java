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
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	public void start(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
		List<Socket> clients = Collections.synchronizedList(new ArrayList<>());
		executorService.execute(() -> {
			while (serverSocket != null && !serverSocket.isClosed()) {
				try {
					Socket client = serverSocket.accept();
					executorService.execute(() -> {
						try {
							clients.add(client);
							ClientState state = new ClientState();
							while (client != null && !client.isClosed()) {
								String request = Transport.receive(client);
								String response = processCommand(request, state);
								System.out.println(request + " -> " + response);
								Transport.send(response, client);
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							clients.remove(client);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private String processCommand(String request, ClientState state) {
		String items[] = request.strip().split("\\s");
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
					return "not enough arguments";					
				}
			case "get":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						return storage.get(items[1]).stream().reduce("", (a, e) -> a + e);
					} else {
						return "no such list";
					}
				} else {
					return "not enough arguments";										
				}
			case "delete":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						storage.remove(items[1]);
					} else {
						return "no such list";						
					}
				} else {
					return "not enough arguments";															
				}
			case "lists":
				return storage.keySet().stream().reduce("", (a, e) -> a + e);
			default:
				return "not a valid command";
			}
		} else {
			if (items[0].equals("auth")) {
				if (items.length == 2) {
					if (items[1].equals("supersecret")) {
						state.isAuthenticated = true;
						return "welcome in";
					} else {
						return "that was not the secret";
					}
				} else {
					return "not enough arguments";
				}
			} else {
				return "tell me a secret";
			}
		}
	}

}
