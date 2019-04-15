package scripty.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import scripty.models.User;
import scripty.services.api.UserManagementService;

@RestController
public class UserController {

	@Autowired
	UserManagementService UMS;
	
	@RequestMapping(method = RequestMethod.POST, value = "/users")
	public void addUser(@RequestBody User user) throws Exception {
		UMS.addUser(user);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/users/{uId}")
	public void removeUser(@PathVariable String uId) throws Exception {
		UMS.removeUser(uId);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/users")
	public void updateUser(User user) throws Exception {
		UMS.updateUser(user);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/users/search")
	public Set<User> searchUser(@RequestBody List<String> searchTerms) throws Exception {
		return UMS.searchUser(searchTerms);
	}
}
