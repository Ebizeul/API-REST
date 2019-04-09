package getInfoFromAPI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

public class AppelClient {
	
	public static void main(String[] args) {
		Client client = ClientBuilder.newBuilder().newClient();
		WebTarget target = client.target("http://localhost:8184");
		target = target.path("/test");
		
		Invocation.Builder builder = target.request();
		Response response 
		  = builder.get();
		
		System.out.println("response : " + response);
		
	}

}
