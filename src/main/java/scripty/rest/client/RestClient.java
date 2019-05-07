package scripty.rest.client;

import java.util.Base64;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import scripty.models.Comment;
import scripty.models.ScriptEntity;
import scripty.models.User;
import scripty.rest.server.controllers.RESTResponse;
import scripty.rest.server.controllers.ScriptsController;
import scripty.rest.server.controllers.UserController;


public class RestClient {
	
	RestTemplate template;
	
	ParameterizedTypeReference<RESTResponse<User>> userParamArg = new ParameterizedTypeReference<RESTResponse<User>>() {};
	ParameterizedTypeReference<RESTResponse<ScriptEntity>> scriptParamArg = new ParameterizedTypeReference<RESTResponse<ScriptEntity>>() {};
	ParameterizedTypeReference<RESTResponse<List<User>>> userListParamArg = new ParameterizedTypeReference<RESTResponse<List<User>>>() {};
	ParameterizedTypeReference<RESTResponse<List<ScriptEntity>>> scriptListParamArg = new ParameterizedTypeReference<RESTResponse<List<ScriptEntity>>>() {};
	
	public RestClient() {
		template = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		
	}

	private String getBaseUri() {
		return "http://localhost:8080";
	}
	
	public <InputType, OutputType> OutputType exec(HttpMethod method, String path, InputType o, ParameterizedTypeReference<RESTResponse<OutputType>> ptype) throws RESTException {
		String notEncoded = "testuser:passwd";
	    String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes());
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Authorization", encodedAuth);
		RESTResponse<OutputType> resp = template.exchange(getBaseUri() + path, method, new HttpEntity<InputType>(o, headers), ptype).getBody();
		if(resp.isSuccess()) {
			return resp.getItem();
		} else {
			throw new RESTException(resp.getError());
		}
	}
	
	public <T> T get(String path, ParameterizedTypeReference<RESTResponse<T>> gtype) throws RESTException {
		return exec(HttpMethod.GET, path, null, gtype);
	}
	
	public <T> T post(String path, Object o, ParameterizedTypeReference<RESTResponse<T>> gtype) throws RESTException {
		return exec(HttpMethod.POST, path, o, gtype);
	}
	
	public <T> void delete(String path, ParameterizedTypeReference<RESTResponse<T>> gtype) throws RESTException {
		exec(HttpMethod.DELETE, path, null, gtype);
	}
	
	private <T> void patch(String path, Object o, ParameterizedTypeReference<RESTResponse<T>> ptype) throws RESTException {
		exec(HttpMethod.PATCH, path, o, ptype);
	}
	
	public User createUser(User user) throws RESTException {
		return (User) post(UserController.USERS_BASE, user, userParamArg);
	}
	
	public User getUser(String uId) throws RESTException {
		return (User) get(UserController.USERS_BASE + "/" + uId, userParamArg);
	}

	public void deleteUser(String uId) throws RESTException {
		delete(UserController.USERS_BASE + "/" + uId, userParamArg);
	}

	public List<User> searchUser(List<String> searchTerms) throws RESTException {
		throw new RESTException("currently unsupported");
	}
	
	public ScriptEntity createScript(ScriptEntity script) throws RESTException {
		return (ScriptEntity) post(ScriptsController.SCRIPT_BASE, script, scriptParamArg);
	}

	public ScriptEntity getScript(String sId) throws RESTException {
		return (ScriptEntity) get(ScriptsController.SCRIPT_BASE + "/" + sId, scriptParamArg);
	}

	public void deleteScript(String sId) throws RESTException {
		delete(ScriptsController.SCRIPT_BASE + "/" + sId, scriptParamArg);
	}
	
	public void upVoteScript(String sId) throws RESTException {
		patch(ScriptsController.SCRIPT_BASE + "/" + sId + "/upvote", null, scriptParamArg);
	}

	public void downVoteScript(String sId) throws RESTException {
		patch(ScriptsController.SCRIPT_BASE + "/" + sId + "/downvote", null, scriptParamArg);
	}

	public void commentOnScript(String sId, Comment comment) throws RESTException {
		patch(ScriptsController.SCRIPT_BASE + "/" + sId + "/comment", comment, scriptParamArg);
	}
	
}

