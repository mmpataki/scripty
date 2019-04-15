package scripty.services.api;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import scripty.models.User;

@Service
public interface UserManagementService {

	public void addUser(User user) throws Exception;
	
	public void removeUser(String id) throws Exception;
	
	public User updateUser(User user) throws Exception;
	
	public Set<User> searchUser(List<String> searchTerms) throws Exception;
	
}
