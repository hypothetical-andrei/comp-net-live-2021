package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements AutoCloseable {

	private ServerSocket serverSocket;
	
	public Server(int port) throws IOException  {
		serverSocket = new ServerSocket(port);
		ExecutorService executorService = Executors.newFixedThreadPool(10 * Runtime.getRuntime().availableProcessors());
		executorService.execute(() -> {
			while (!serverSocket.isClosed()) {
				try {
					executorService.submit(new ClientHandler(serverSocket.accept()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void close() throws Exception {
		serverSocket.close();
	}

}
