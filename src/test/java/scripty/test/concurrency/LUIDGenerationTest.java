package scripty.test.concurrency;

import org.testng.annotations.Test;

import scripty.util.luid.LUIDService;
import scripty.util.luid.SequentialLUIDGenerator;
import scripty.util.luid.Token;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;

public class LUIDGenerationTest {

	final int INVOCATIONS = 10000;
	String TOK_NAME = "dummy";
	LUIDService service;
	Token token;
	
	long start, end;

	@Test(threadPoolSize = 3, invocationCount = INVOCATIONS / 100)
	public void f() throws Exception {
		for (int i = 0; i < 100; i++)
			System.out.println(token.next());
	}

	@BeforeTest
	public void beforeTest() throws Exception {
		start = System.nanoTime();
		service = new SequentialLUIDGenerator();
		token = service.register(TOK_NAME, 100);
	}

	@AfterTest
	public void afterTest() throws Exception {
		service.unregister(TOK_NAME);
		end = System.nanoTime();
		System.out.println("operations/sec = " + ( (((double)INVOCATIONS) * 1e9) / ((double)(end-start)) ));
	}

}
