package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Transport;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket;
	private ExecutorService executorService;
	
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
					final Socket client = serverSocket.accept();
					executorService.submit(() -> {
						clients.add(client);
						try {
							while (serverSocket != null && !serverSocket.isClosed()) {
								String message = Transport.receive(client);
								clients.forEach(c -> {
									try {
										Transport.send(message, c);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								});
							}
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							clients.remove(client);
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
