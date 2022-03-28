package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class WebServer implements AutoCloseable{

	private HttpServer server;
	
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void open(int port) throws IOException {
		InetSocketAddress address = new InetSocketAddress(port);
		server = HttpServer.create(address, 0);
		server.createContext("/", new WebHandler());
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
	}

}
