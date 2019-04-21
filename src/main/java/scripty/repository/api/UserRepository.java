package scripty.repository.api;

import scripty.models.User;

public interface UserRepository extends Repository<String, User> {

	public User getByEmail(String email) throws Exception;
	
	public boolean emailExists(String email) throws Exception;

}
