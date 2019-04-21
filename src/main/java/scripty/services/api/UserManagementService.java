package scripty.services.api;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import scripty.models.User;

@Service
public interface UserManagementService {

	/* create a user and return it */
	public User addUser(User user) throws Exception;
	
	public void removeUser(String id) throws Exception;
	
	public User updateUser(User user) throws Exception;
	
	public Set<User> searchUser(List<String> searchTerms) throws Exception;

	public User getUser(String uId) throws Exception;

	public Iterable<User> getUsers(String fromUID);
	
}
