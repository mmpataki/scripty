package scripty.test.consistency;

import static org.testng.Assert.fail;

import org.testng.annotations.Test;

import scripty.util.luid.SequentialLUIDGenerator;
import scripty.util.luid.Token;

public class LUIDGeneration {

	public static final int SIZE = 20000;
	SequentialLUIDGenerator sqgen;
	Token tok;

	public LUIDGeneration() throws Exception {
		sqgen = new SequentialLUIDGenerator();
		tok = sqgen.register("dummy", 100);
	}

	class Unit extends Thread {
		String[] gens = new String[SIZE];

		@Override
		public void run() {
			for (int i = 0; i < SIZE; i++) {
				try {
					gens[i] = tok.next();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public int verify(Unit u1, Unit u2) {
		int wrongCount = 0;
		String s1[] = u1.gens;
		String s2[] = u2.gens;

		int i1 = 0, i2 = 0;
		try {
			while (i1 < SIZE && i2 < SIZE) {
				long l1 = Long.parseLong(s1[i1]);
				long l2 = Long.parseLong(s2[i2]);

				if (l1 < l2) {
					i1++;
				} else if (l2 < l1) {
					i2++;
				} else {
					wrongCount++;
				}
			}
		} catch (Exception ex) {
			System.out.println("s1=" + s1[i1] + ", s2=" + s2[i2] + ",  i1=" + i1 + ", i2=" + i2);
			throw ex;
		}
		return wrongCount;
	}

	@Test(invocationCount = 100)
	public void f() {

		Unit u1 = new Unit();
		Unit u2 = new Unit();

		u1.start();
		u2.start();

		xwait(u1);
		xwait(u2);

		int wc;
		if ((wc = verify(u1, u2)) != 0)
			fail("SequentialLUIDGenerator consistency test failed : " + wc + " similar records found");
	}

	private void xwait(Unit u1) {
		while (u1.isAlive()) {
			try {
				u1.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
