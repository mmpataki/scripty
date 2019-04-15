package scripty.services.impl.filebacked;

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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import scripty.models.User;
import scripty.services.api.UserManagementException;
import scripty.services.api.UserManagementService;

@Service
public class FBUserManagementService implements UserManagementService {

	String fileName = "./users.db";
	Logger logger = Logger.getLogger(FBUserManagementService.class);

	/* stored */
	Map<String, User> userId2User;

	/* constructed */
	Map<String, User> emailId2User;

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
		} catch(Exception ex) {
			fsync();
			return;
		}
		
		userId2User = (HashMap<String, User>)(new ObjectInputStream(fis)).readObject();
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

	/* service methods */
	public void addUser(User user) throws Exception {

		Assert.notNull(user.getEmailId(), "User emailId can't be null");

		if (emailId2User.containsKey(user.getEmailId())) {
			throw new UserManagementException("User " + user.getEmailId() + " exists");
		}
		
		String uId = userId2User.size() + 1 + "";
		user.setId(uId);
		
		userId2User.put(uId, user);
		emailId2User.put(user.getEmailId(), user);
		fsync();
	}

	public void removeUser(String uId) throws Exception {
	
		User user = userId2User.get(uId);
		
		if(user == null) {
			throw new UserManagementException("User " + uId + " doesn't exist");
		}
		
		emailId2User.remove(user.getEmailId());
		userId2User.remove(uId);
		fsync();
	}

	public User updateUser(User user) throws Exception {
		
		Assert.notNull(user, "`user` shouldn't be null");
		
		String oldEmail = userId2User.get(user.getId()).getEmailId();
		emailId2User.remove(oldEmail);
		
		userId2User.put(user.getId(), user);
		emailId2User.put(oldEmail, user);
		
		return user;
	}

	public Set<User> searchUser(List<String> searchTerms) {
		
		Assert.notEmpty(searchTerms, "`searchTerms` shouldn't be empty");
		
		Set<User> searchResults = new HashSet<>();
		for (String searchTerm : searchTerms) {
			if(emailId2User.containsKey(searchTerm)) {
				searchResults.add(emailId2User.get(searchTerm));
			}
		}
		return searchResults;
	}

}
