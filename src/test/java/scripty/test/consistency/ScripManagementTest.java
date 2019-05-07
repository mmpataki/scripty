package scripty.test.consistency;

import org.testng.annotations.Test;

import scripty.models.ScriptEntity;
import scripty.rest.client.RESTException;
import scripty.rest.client.RestClient;
import scripty.test.utils.TimeUtil;

import org.testng.annotations.BeforeTest;

import static org.testng.Assert.fail;

import java.util.Random;

import org.testng.annotations.AfterTest;

public class ScripManagementTest {
	
	long start, end;
	RestClient client;
	
	final int SIZE = 10;
	ScriptEntity scripts[] = new ScriptEntity[SIZE];
	
	@Test
	public void createScriptTest() throws RESTException {
		for (int i = 0; i < SIZE; i++) {
			ScriptEntity script = new ScriptEntity(
						"mkdirr" + System.currentTimeMillis(),
						"mkdir /tmp/" + i,
						"bash",
						"creates directories in /tmp",
						"./mkdirr"
					);
			scripts[i] = client.createScript(script);
		}
		Random rand = new Random();
		int i = (Math.abs(rand.nextInt()) % (SIZE-1)) + 1;
		ScriptEntity s0 = scripts[0], si = scripts[i];
		if(s0.equals(si))
			fail("Scripts with different content are same " + s0.getId() + "==" + si.getId());
	}
	
	@Test(dependsOnMethods = "createScriptTest")
	public void getScriptTest() throws RESTException {
		for (ScriptEntity s : scripts) {
			ScriptEntity se = null;
			se = client.getScript(s.getId());
			if(!s.equals(se))
				fail("getting a script failed! not the same object");
		}
	}
	
	@Test(dependsOnMethods = "downVoteTest")
	public void deleteScriptTest() {
		for (ScriptEntity se : scripts) {
			try {
				client.deleteScript(se.getId());
			} catch (RESTException e) {
				fail("Deletion of script with Id=" + se.getId() + " failed.", e);
			}
		}
	}

	@Test(dependsOnMethods = "getScriptTest")
	public void duplicateScriptTest() {
		ScriptEntity s = new ScriptEntity("mkdir" + System.currentTimeMillis(), "mkdir /tmp/" + System.nanoTime(), "bash", "creates directories", "mkdirr");
		for (int i = 0; i < 2; i++) {
			try {
				client.createScript(s);
				if(i == 1)
					fail("Duplicate scripts can be created");
			} catch(RESTException e) {
				if(i == 0)
					fail("1st Script creation failed in duplicateScript: " + e.getMessage());
			}
		}
	}
	
	@Test(dependsOnMethods = "duplicateScriptTest")
	public void upVoteTest() throws RESTException {
		String tid = scripts[0].getId();
		Long cvote = scripts[0].getUpVotes();
		client.upVoteScript(tid);
		ScriptEntity ns = client.getScript(tid);
		if(ns.getUpVotes() <= cvote)
			fail("failed to upvote" + tid);
	}

	@Test(dependsOnMethods = "upVoteTest")
	public void downVoteTest() throws RESTException {
		String tid = scripts[0].getId();
		Long cvote = scripts[0].getDownVotes();
		client.downVoteScript(tid);
		ScriptEntity ns = client.getScript(tid);
		if(ns.getDownVotes() <= cvote)
			fail("failed to downvote" + tid);
	}
	
	@Test(dependsOnMethods = "createScriptTest")
	public void commentOnScript() throws RESTException {
		
	}
	
	@BeforeTest
	public void beforeTest() {
		client = new RestClient();
		start = System.nanoTime();
	}

	@AfterTest
	public void afterTest() {
		end = System.nanoTime();
		System.out.println(getClass() + " took " + TimeUtil.nanoDiffInSec(end, start));
	}

}
