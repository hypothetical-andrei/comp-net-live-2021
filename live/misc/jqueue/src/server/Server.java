package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Transport;

public class Server implements AutoCloseable {
	
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
		executorService = Executors.newFixedThreadPool(50 * Runtime.getRuntime().availableProcessors());
		final List<Socket> clients = Collections.synchronizedList(new ArrayList<Socket>());
		executorService.execute(() -> {
			while (serverSocket != null && !serverSocket.isClosed()) {
				try {
					final Socket socket = serverSocket.accept();
					executorService.execute(() -> {
						try {
							clients.add(socket);
							ClientState state = new ClientState();
							while (socket != null && !socket.isClosed()) {
								try {
									String command = Transport.receive(socket);
									String response = processCommand(command, state);
									System.out.println(state.isAuthenticated);
									System.out.println(command + " -> " + response);
									Transport.send(response, socket);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							clients.remove(socket);
						}
					});
				} catch (Exception e) {
				}
			}
		});
	}

	private String processCommand(String command, ClientState state) {
		String[] items = command.strip().split("\\s");
		if (state.isAuthenticated) {			
			switch (items[0]) {
			case "put":
				if (items.length == 3) {
					if (storage.containsKey(items[1])) {
						List<String> existing = storage.get(items[1]);
						existing.add(items[2]);
						storage.put(items[1], existing);
						return "added to list";
					} else {
						List<String> existing = new ArrayList<>();
						existing.add(items[2]);
						storage.put(items[1], existing);
						return "added to new list";					
					}
				} else {
					return "not enough params";
				}
			case "get":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						return storage.get(items[1]).stream().reduce("", (a, e) -> a + e);
					} else {
						return "no such list";
					}
				} else {
					return "not enough params";
				}
			case "delete":
				if (items.length == 2) {
					if (storage.containsKey(items[1])) {
						return "list deleted";
					} else {
						return "no such list";
					}
				} else {
					return "not enough params";
				}
			case "lists":
				if (items.length == 1) {
					return storage.keySet().stream().reduce("", (a, e) -> a + e);				
				} else {
					return "too many params";
				}
			default:
				return "unknown command";	
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
					return "not enough params";
				}
			} else {
				return "tell me a secret";				
			}
		}
	}

	public void stop() throws IOException {
		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
		if (serverSocket != null) {
			serverSocket.close();
			serverSocket = null;
		}
 	}

}
