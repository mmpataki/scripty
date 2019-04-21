package scripty.repository.filebackedimpl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import scripty.models.User;
import scripty.repository.api.UserRepository;

@Service
public class UserRepositoryImpl implements UserRepository {

	String fileName = "./users.db";
	Logger logger = Logger.getLogger(UserRepositoryImpl.class);

	/* stored */
	Map<String, User> userId2User;

	/* constructed */
	Map<String, User> emailId2User;

	@Override
	public void put(User user) throws Exception {
		emailId2User.put(user.getEmailId(), user);
		userId2User.put(user.getId(), user);
		fsync();
	}

	@Override
	public User get(String id) throws Exception {
		return userId2User.get(id);
	}

	@Override
	public User getByEmail(String email) throws Exception {
		return emailId2User.get(email);
	}

	@Override
	public boolean has(String key) throws Exception {
		return userId2User.containsKey(key);
	}

	@Override
	public boolean emailExists(String email) throws Exception {
		return emailId2User.containsKey(email);
	}

	@Override
	public User remove(String uId) throws Exception {
		User user = userId2User.remove(uId);
		emailId2User.remove(user.getEmailId());
		return user;
	}

	@Override
	public Set<User> search(List<String> searchTerms) {
		Set<User> searchResults = new HashSet<>();
		for (String searchTerm : searchTerms) {
			if (emailId2User.containsKey(searchTerm)) {
				searchResults.add(emailId2User.get(searchTerm));
			}
		}
		return searchResults;
	}

	/*
	 * TODO: Learn about this warning.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() throws Exception {
		logger.info("Initializing UserManagementService");

		emailId2User = new HashMap<>();
		userId2User = new HashMap<>();

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(fileName);
		} catch (Exception ex) {
			fsync();
			return;
		}

		userId2User = (HashMap<String, User>) (new ObjectInputStream(fis)).readObject();
		for (User user : userId2User.values()) {
			emailId2User.put(user.getEmailId(), user);
		}

		fis.close();
		logger.info("UserManagementService initialization done");
	}

	private void fsync() throws Exception {
		FileOutputStream fos = new FileOutputStream(fileName);
		(new ObjectOutputStream(fos)).writeObject(userId2User);
		fos.close();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void applyUpdate(String uId, UpdateFunction<User> uFunc) throws Exception {
		User user = get(uId);
		Exception thrown = null;
		/* lock */
		try {
			uFunc.update(user);
		} catch (Exception e) {
			thrown = e;
		}
		/* unlock */
		if (thrown != null)
			throw thrown;
	}

}
