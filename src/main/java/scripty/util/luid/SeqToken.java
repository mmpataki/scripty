package scripty.util.luid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;

class SeqToken implements Token, RenewableToken, Serializable {

	private static final long serialVersionUID = -5452872587405035374L;

	transient Logger logger = Logger.getLogger(SeqToken.class);

	transient private LUIDRenewer renewer;
	private int blockSize;
	private String name;
	private volatile AtomicLong current;
	private volatile long rangeEnd;

	public SeqToken(String name, int blockSize, LUIDRenewer renewer) {
		this.name = name;
		this.blockSize = blockSize;
		this.renewer = renewer;
		this.current = new AtomicLong(readOld());
		this.rangeEnd = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String next() throws Exception {
		if (current.get() == rangeEnd) {
			updateRange();
		}
		String ret = current.getAndIncrement() + "";
		persist(ret);
		return ret;
	}

	private long readOld() {
		String last = (String) read(name);
		if (last != null)
			return Long.parseLong(last);
		return 0;
	}

	private void persist(String ret) {
		write(name, ret);
	}

	private void updateRange() throws Exception {
		renewer.renew(this);
	}

	@Override
	public int getBlockSize() {
		return blockSize;
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj || name.equals(((SeqToken) obj).name));
	}

	/* shouldn't be available for the user. */
	@Override
	public void update(long rangeEnd) {
		this.rangeEnd = rangeEnd;
	}

	@Override
	public long current() {
		return current.get();
	}

	@Override
	public int blockSize() {
		return blockSize;
	}

	@Override
	public String last() {
		return current.get() + "";
	}

	public static void write(String file, Object o) {
		try (FileOutputStream fos = new FileOutputStream(file)) {
			new ObjectOutputStream(fos).writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object read(String file) {
		try (FileInputStream fos = new FileInputStream(file)) {
			return new ObjectInputStream(fos).readObject();
		} catch (IOException | ClassNotFoundException e) {
		}
		return null;
	}
}
