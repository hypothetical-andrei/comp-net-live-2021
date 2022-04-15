package client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;

import common.Contract;
import common.Settings;
import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.Service;

public class Client {
	public static void main(String[] args) throws MalformedURLException {
		String endpointURI = String.format("http://%s:%d/greetings", Settings.HOST, Settings.PORT);
		Contract proxy = Service.create(new URL(endpointURI + "?wsdl"), new QName("http://localhost", "Service")).getPort(new QName("http://localhost", "ServicePort"), Contract.class);
		System.out.println("Server started. Type 'exit' to close.");
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String command = scanner.nextLine();
				if (command == null || "exit".equals(command)) {
					break;
				} else {
					System.out.println(proxy.sayHello(command));
				}
			}
		} finally {
			System.exit(0);
		}
	}
}
