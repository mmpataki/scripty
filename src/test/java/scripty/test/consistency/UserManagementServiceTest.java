package scripty.test.consistency;

import org.testng.annotations.Test;

import scripty.models.User;
import scripty.rest.client.RestClient;
import scripty.test.utils.TimeUtil;

import org.testng.annotations.BeforeTest;

import static org.testng.Assert.fail;

import org.testng.annotations.AfterTest;

public class UserManagementServiceTest {
	
	RestClient client;
	
	long start, end;
	final int SIZE = 10;
	User[] users = new User[SIZE]; 
	
	@Test
	public void createUser() {
		for (int i = 0; i < SIZE; i++) {
			String uname = "user" + i;
			users[i] = client.createUser(
							new User(
								uname,
								"user" + i + "@scripty.com",
								new char[] {'1', '2', '3', '4'},
								0L,
								0L,
								0L
							)
						);
			if(users[i] == null) {
				fail("returned null");
			} else if(!uname.equals(users[i].getUserName())) {
				fail("uname is not matching");
			}
		}
	}
	
	@Test(dependsOnMethods = "createUser")
	public void createDuplicateUser() {
		User u = new User(
				"u1duptest",
				"userduptest" + "@scripty.com",
				new char[] {'1', '2', '3', '4'},
				0L,
				0L,
				0L
			);
		User r1 = client.createUser(u);
		try {
			User r2 = client.createUser(u);
			if(r2 != null && r2.equals(r1)) {
				fail("creating duplicate objects successful");
			}
		} catch(Exception e) {
			/* pass */
		}
		client.deleteUser(r1.getId());
	}
	
	@Test(dependsOnMethods = "createDuplicateUser")
	public void getUser() {
		for (User user : users) {
			User u = client.getUser(user.getId());
			if(u == null) {
				fail("returned null");
			} else if(!u.equals(user)) {
				fail("returned object is not equal to requested object");
			}
		}
	}
	
	@Test(dependsOnMethods = "iterateUsers")
	public void deleteUser() {
		for (User user : users) {
			client.deleteUser(user.getId());
		}
		/* let's check whether they exist */
		for (User user : users) {
			try {
				client.getUser(user.getId());
				fail("user " + user.getId() + " was not deleted");
			} catch(Exception ex) {
			}
		}
	}
	
	@Test(dependsOnMethods = "createDuplicateUser")
	public void updateUser() {
		
	}
	
	@Test(dependsOnMethods = "createDuplicateUser")
	public void iterateUsers() {
		
	}

	@BeforeTest
	public void beforeTest() {
		start = System.nanoTime();
		client = new RestClient();
	}

	@AfterTest
	public void afterTest() {
		end = System.nanoTime();
		System.out.println("Time for UserManagementServiceTest " + TimeUtil.nanoDiffInSec(end, start));
	}

}
