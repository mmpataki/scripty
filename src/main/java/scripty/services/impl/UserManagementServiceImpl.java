package scripty.services.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import scripty.models.User;
import scripty.repository.api.UserRepository;
import scripty.services.api.UserManagementException;
import scripty.services.api.UserManagementService;
import scripty.util.luid.LUIDService;
import scripty.util.luid.Token;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	@Autowired
	private UserRepository URS;

	@Autowired
	private LUIDService luidService;

	AtomicReference<String> lastUID;
	Token LUG;
	Logger logger = Logger.getLogger(UserManagementServiceImpl.class);

	@PostConstruct
	void init() throws Exception {
		LUG = luidService.register("UUM", 1000);
		lastUID = new AtomicReference<>("0");
	}

	/* service methods */
	@Override
	public User addUser(User user) throws Exception {

		Assert.notNull(user.getEmailId(), "User emailId can't be null");

		if (URS.emailExists(user.getEmailId())) {
			throw new UserManagementException("User " + user.getEmailId() + " exists");
		}

		user.setId(LUG.next());
		lastUID.set(user.getId());
		
		URS.put(user);
		return user;
	}

	@Override
	public void removeUser(String uId) throws Exception {
		if (!URS.has(uId)) {
			throw new UserManagementException("User " + uId + " doesn't exist");
		}
		URS.remove(uId);
	}

	@Override
	public User updateUser(User user) throws Exception {

		Assert.notNull(user, "`user` shouldn't be null");
		Assert.notNull(user.getId(), "`userId` shouldn't be null.");

		URS.remove(user.getId());
		URS.put(user);

		return user;
	}

	@Override
	public Set<User> searchUser(List<String> searchTerms) throws Exception {
		Assert.notEmpty(searchTerms, "`searchTerms` shouldn't be empty");
		return URS.search(searchTerms);
	}

	@Override
	public User getUser(String uId) throws Exception {
		return URS.get(uId);
	}

	@Override
	public Iterable<User> getUsers(String fromUID) {
		return new UserIterator(fromUID);
	}

	public class UserIterator implements Iterable<User> {

		String currentUID;
		
		public UserIterator(String fromUID) {
			this.currentUID = fromUID;
		}

		@Override
		public Iterator<User> iterator() {
			return new Iterator<User>() {

				@Override
				public User next() {
					try {
						User u = URS.get(currentUID);
						currentUID = (Long.parseLong(currentUID) + 1) + "";
						return u;
					} catch (Exception e) {
						return null;
					}
				}

				@Override
				public boolean hasNext() {
					return !(currentUID.equals(lastUID.get()));
				}
			};
		}
	}
}
