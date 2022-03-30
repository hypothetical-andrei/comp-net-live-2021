package server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		WebRequest request;
		
		request = new WebRequest(exchange);
		
		Path path = Paths.get("web", request.getPath());
	}

}
