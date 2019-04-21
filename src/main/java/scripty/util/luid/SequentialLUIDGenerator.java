package scripty.util.luid;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class SequentialLUIDGenerator implements LUIDService, LUIDRenewer {

	public static final int DEFAULT_BLOCK_SIZE = 100;
	static ConcurrentHashMap<String, SeqToken> registry;

	public SequentialLUIDGenerator() {
		registry = new ConcurrentHashMap<>();
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
			throw new Exception("Token already exists. " + name);
		}
		return tok;
	}
	
	@Override
	public void unregister(String name) throws Exception {
		registry.remove(name);
	}

	@Override
	public void renew(RenewableToken tok) throws Exception {
		/* for now let's have a dummy implementation */
		tok.update(tok.current() + tok.blockSize());
	}
	
	static class SeqToken implements Token, RenewableToken {
		
		private LUIDRenewer renewer;
		private int blockSize;
		private String name;
		private volatile AtomicLong current;
		private volatile long rangeEnd;
		
		public SeqToken(String name, int blockSize, LUIDRenewer renewer) {
			this.name = name;
			this.blockSize = blockSize;
			this.renewer = renewer;
			this.current = new AtomicLong(0);
			this.rangeEnd = 0;
		}
		
		@Override
		public String getName() {
			return name;
		}
		
		@Override
		public String next() throws Exception {
			if(current.get() == rangeEnd) {
				updateRange();
			}
			return current.getAndIncrement() + "";
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
			return (this == obj || name.equals(((SeqToken)obj).name));
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
	}

}
