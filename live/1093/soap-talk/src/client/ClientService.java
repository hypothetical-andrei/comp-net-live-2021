package client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import common.ClientContract;
import common.ServerContract;
import common.Settings;
import jakarta.jws.WebService;
import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.soap.AddressingFeature;

import com.sun.xml.ws.api.addressing.OneWayFeature;
import com.sun.xml.ws.api.addressing.WSEndpointReference;

@Addressing(required= true)
@WebService(serviceName = "Service", 
portName = "ServicePort", 
targetNamespace = "http://ase.ro", 
endpointInterface = "common.ClientContract")
public class ClientService implements ClientContract, AutoCloseable{

	private ServerContract proxy;
	private ClientContract callback;
	
	public ClientService(ClientContract callback) throws MalformedURLException {
		this.callback = callback;
		int port = Settings.PORT + 1;
		EndpointReference reference = null;
		while (reference == null && port < Short.MAX_VALUE) {
			try {
				reference = Endpoint.publish(String.format("http://%s:%d/talk", Settings.HOST, port), this).getEndpointReference();				
			} catch (Exception e) {
				port++;
			}
		}
		if (reference != null) {
			URL url = new URL(String.format("http://%s:%d/talk", Settings.HOST, Settings.PORT));
			this.proxy = Service.create(url, new QName("http://ase.ro", "Service"))
					.getPort(new QName("http://ase.ro", "ServicePort"), ServerContract.class, new AddressingFeature(), new OneWayFeature(true, new WSEndpointReference(reference)));
			this.proxy.subscribe();
		}
	}
	
	@Override
	public void close() throws Exception {
		if (proxy != null) {
			proxy.unsubscribe();
		}
	}

	@Override
	public void onReceive(String message) {
		callback.onReceive(message);
	}
	
	public void send(String message) {
		if (proxy != null) {
			proxy.send(message);
		}
	}

}
