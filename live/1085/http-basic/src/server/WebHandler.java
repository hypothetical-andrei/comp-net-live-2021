package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		WebRequest request;
		try {
			request = new WebRequest(exchange);
			Path path = Paths.get("web", request.getPath());
			Optional<Class<?>> processorType = Settings.getProcessorType(request.getPath());
			if (processorType.isPresent()) {
				WebResponse response = new WebResponse();
				WebProcessor.class.cast(processorType.get().getConstructor().newInstance()).process(request, response);
				if (response.getContentType() != null) {
					exchange.getResponseHeaders().add("content-type", response.getContentType());
				}
				response.getStream().close();
				response.getWriter().close();
				byte[] buffer = response.getStream().toByteArray();
				exchange.sendResponseHeaders(response.getStatus(), buffer.length);
				if (buffer.length > 0) {
					exchange.getResponseBody().write(buffer, 0, buffer.length);
				}
			} else {
				if (path.toFile().exists()) {
					if (path.toFile().isDirectory()) {
						exchange.sendResponseHeaders(403, 0);
					} else {
						byte[] buffer = Files.readAllBytes(path);
						exchange.sendResponseHeaders(200, buffer.length);
						exchange.getResponseBody().write(buffer, 0, buffer.length);
					}
				} else {
					exchange.sendResponseHeaders(404, 0);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
			exchange.sendResponseHeaders(500, 0);
		} finally {
			exchange.close();
		}
	}

}
