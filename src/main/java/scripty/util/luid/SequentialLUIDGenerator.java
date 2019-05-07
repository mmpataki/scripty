package scripty.util.luid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class SequentialLUIDGenerator implements LUIDService, LUIDRenewer {

	private String persistFile = "seqgen.db";
	public static final int DEFAULT_BLOCK_SIZE = 100;
	static ConcurrentHashMap<String, SeqToken> registry;

	Logger logger = Logger.getLogger(SequentialLUIDGenerator.class);
	
	@SuppressWarnings("unchecked")
	public SequentialLUIDGenerator() {
		registry = (ConcurrentHashMap<String, SeqToken>) read(persistFile);
		if(registry == null) {
			registry = new ConcurrentHashMap<String, SeqToken>();
		} else {
			for (SeqToken old : registry.values()) {
				SeqToken tok = new SeqToken(old.getName(), old.getBlockSize(), this);
				if(tok != null) {
					registry.put(old.getName(), tok);
				}
			}
		}
	}
	
	@Override
	public Token getToken(String name) throws Exception {
		return registry.get(name);
	}

	@Override
	public Token register(String name, int blockSize) throws Exception {
		blockSize = blockSize == 0 ? DEFAULT_BLOCK_SIZE : blockSize;
		SeqToken tok = new SeqToken(name, blockSize, this);
		if(registry.putIfAbsent(name, tok) != null) {
			return registry.get(name);
		}
		write(persistFile, registry);
		return tok;
	}
	
	@Override
	public void unregister(String name) throws Exception {
		registry.remove(name);
		write(persistFile, registry);
	}

	@Override
	public void renew(RenewableToken tok) throws Exception {
		/* for now let's have a dummy implementation */
		tok.update(tok.current() + tok.blockSize());
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
