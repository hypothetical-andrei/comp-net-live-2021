package server;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		WebRequest request = new WebRequest(exchange);

	}

}
