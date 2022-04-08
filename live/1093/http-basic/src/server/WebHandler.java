package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		WebRequest request;
		try {
			request = new WebRequest(exchange);
			Path path = Paths.get("web", request.getPath());
			if (path.toFile().exists()) {
				if (path.toFile().isFile()) {
					byte[] buffer = Files.readAllBytes(path);
					exchange.sendResponseHeaders(200, buffer.length);
					exchange.getResponseBody().write(buffer, 0, buffer.length);
				} else {
					exchange.sendResponseHeaders(403, 0);
				}
			} else {
				exchange.sendResponseHeaders(404, 0);
			}
		} catch (Exception e) {
			exchange.sendResponseHeaders(500, 0);
		} finally {
			exchange.close();
		}
	}

}
