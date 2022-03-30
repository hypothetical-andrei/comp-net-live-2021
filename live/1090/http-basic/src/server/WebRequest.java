package server;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class WebRequest {

	private String method;
	private String path;
	private String contentType;
	private Map<String, String> query;
	private byte[] body;
	
	public WebRequest(HttpExchange exchange) {
		method = exchange.getRequestMethod();
		path = exchange.getRequestURI().getPath().substring(1);
		query = new HashMap<>();
		if (exchange.getRequestURI().getQuery() != null) {
			for (String part: exchange.getRequestURI().getQuery().split("&")) {
				String[] items = part.split("=");
				if (items.length == 2) {
					query.put(items[0], items[1]);
				}
			}
		}
		if (exchange.getRequestHeaders().containsKey("content-type")) {
			contentType = exchange.getRequestHeaders().getFirst("content-type");
		}
		if (exchange.getRequestHeaders().containsKey("content-length")) {
			body = new byte[Integer.parseInt(exchange.getRequestHeaders().getFirst("content-length"))];
		} else {
			body = new byte[0];
		}
		
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getContentType() {
		return contentType;
	}

	public Map<String, String> getQuery() {
		return query;
	}

	public byte[] getBody() {
		return body;
	}

	
}
