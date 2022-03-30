package processors;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import server.WebProcessor;
import server.WebRequest;
import server.WebResponse;
import java.util.Arrays;

public class FilesProcessor implements WebProcessor {

	@Override
	public void process(WebRequest request, WebResponse response) throws Exception {
		switch (request.getMethod().toUpperCase()) {
		case "GET":
			if (request.getQuery().containsKey("name")) {
				Path path = Paths.get("files", request.getQuery().get("name"));
				if (path.toFile().exists()) {
					if (path.toFile().isFile()) {
						byte[] buffer = Files.readAllBytes(path);
						response.getStream().write(buffer, 0, buffer.length);
					}
				} else {
					response.setStatus(404);
				}
			} else {
				response.getWriter().print("<html><ul>");
				Arrays.stream(Paths.get("files").toFile().listFiles(file -> file.isFile())).forEach(file -> response.getWriter().printf("<li>%s</li>", file.getName()));
				response.getWriter().print("</ul></html>");
				response.setContentType("text/html");
			}
			break;
		case "POST":

			break;
		case "DELETE":

			break;

		default:
			break;
		}
	}

}