package server;

import java.util.ArrayList;
import java.util.Collections;

import common.ServerContract;
import jakarta.annotation.Resource;
import jakarta.jws.WebService;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.soap.Addressing;
import jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import common.ClientContract;
import java.util.List;

import javax.xml.namespace.QName;

import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.addressing.WsaPropertyBag;

@Addressing(required= true)
@WebService(serviceName = "Service", portName = "ServicePort", targetNamespace = "http://ase.ro", endpointInterface = "common.ServerContract")
public class ServerService implements ServerContract {

	private static final List<ClientContract> clients = Collections.synchronizedList(new ArrayList<>());
	
	@Resource
	private WebServiceContext context;
	
	@Override
	public void subscribe() {
		clients.add(getClient());
	}

	@Override
	public void unsubscribe() {
		clients.remove(getClient());
	}

	@Override
	public void send(String message) {
		clients.forEach(client -> {
			client.onReceive(message);
		});
	}
	
	private ClientContract getClient() {
		WSEndpointReference reference = (WSEndpointReference) context.getMessageContext().get(WsaPropertyBag.WSA_REPLYTO_FROM_REQUEST);
		return new W3CEndpointReferenceBuilder()
				.serviceName(new QName("http://ase.ro", "Service"))
				.address(reference.getAddress())
				.wsdlDocumentLocation(reference.getAddress() + "?wsdl")
				.build()
				.getPort(ClientContract.class);
	}

}
