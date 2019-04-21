package scripty.rest.server.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;
import scripty.models.User;
import scripty.services.api.UserManagementService;

@RestController
public class UserController {

	@Autowired
	UserManagementService UMS;
	
	
	@RequestMapping(method = RequestMethod.POST, value = USERS_BASE)
	public User addUser(@RequestBody User user) throws Exception {
		return UMS.addUser(user);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = USERS_BASE + "/{uId}")
	public void removeUser(@PathVariable String uId) throws Exception {
		UMS.removeUser(uId);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = USERS_BASE)
	public void updateUser(User user) throws Exception {
		UMS.updateUser(user);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = USERS_SEARCH)
	public Set<User> searchUser(@ApiParam(value = "comma separated search terms", required = true, allowMultiple = true) @RequestParam String searchTerms) throws Exception {
		return UMS.searchUser(Arrays.asList(searchTerms.split(",")));
	}
	
	@RequestMapping(method = RequestMethod.GET, value = USERS_BASE + "/{uId}")
	public User getUser(@RequestParam(name = "uId") String uId) throws Exception {
		return UMS.getUser(uId);
	}
	
	/* simple stupid implementation, non-prod, just for testing */
	@RequestMapping(method = RequestMethod.GET, value = USERS_BASE)
	public List<User> getUsers(
			@RequestParam(defaultValue = "0") String fromUID, 
			@RequestParam(defaultValue = "100") int pageSize) throws Exception {
		List<User> uList = new ArrayList<>();
		Iterator<User> userIter = UMS.getUsers(fromUID).iterator();
		for (int i = 0; i < pageSize && userIter.hasNext(); i++) {
			uList.add(userIter.next());
		}
		return uList;
	}
	
	/* static final constants */
	public static final String USERS_BASE = "/users";
	public static final String USERS_SEARCH = USERS_BASE + "/search";
	
}
