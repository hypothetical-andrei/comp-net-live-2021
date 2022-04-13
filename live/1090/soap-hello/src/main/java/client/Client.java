package client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.xml.namespace.QName;

import common.Contract;
import common.Settings;
import jakarta.xml.ws.Service;

public class Client {

	public static void main(String[] args) throws MalformedURLException {
		Contract proxy = Service.create(new URL(String.format("http://%s:%d/greetings?wsdl", Settings.HOST, Settings.PORT)), new QName("http://localhost", "Service")).getPort(new QName("http://localhost", "ServicePort"), Contract.class);
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				System.out.println("Enter you name or type 'exit' to quit");
				String input = scanner.nextLine();
				if ("exit".equals(input)) {
					break;
				} else {
					System.out.println(proxy.sayHello(input));
				}				
			}
		} finally {
			System.exit(0);
		}
	}

}
