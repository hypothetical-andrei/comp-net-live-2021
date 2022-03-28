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
				if (path.toFile().isDirectory()) {
					exchange.sendResponseHeaders(403, 0);
				} else {
					byte[] bytes = Files.readAllBytes(path);
					exchange.sendResponseHeaders(200, bytes.length);
					exchange.getResponseBody().write(bytes, 0, bytes.length);
				}
			} else {
				exchange.sendResponseHeaders(404, 0);
			}
		} catch (IOException e) {
			e.printStackTrace();
			exchange.sendResponseHeaders(500, 0);
		} finally {
			exchange.close();
		}
	}

}
