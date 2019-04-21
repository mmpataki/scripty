package scripty.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import scripty.models.User;
import scripty.rest.server.controllers.UserController;

public class RestClient {

	Client client;
	WebTarget target;
	
	public RestClient() {
		client = ClientBuilder.newClient();
		target = client.target(getBaseUri());
	}

	private String getBaseUri() {
		return "http://localhost:8080";
	}
	
	
	public User createUser(User user) {
		Response resp = 
				target.path(UserController.USERS_BASE)
					.request()
						.post(Entity.entity(user, MediaType.APPLICATION_JSON));
		return (User) resp.readEntity(User.class);
	}
	
	public User getUser(String uId) {
		return target
				.path(UserController.USERS_BASE)
				.path(uId)
					.request()
						.accept(MediaType.APPLICATION_JSON)
							.get(User.class);
	}

	public void deleteUser(String uId) {
		target
			.path(UserController.USERS_BASE)
				.path(uId)
					.request()
						.delete();
	}
}
