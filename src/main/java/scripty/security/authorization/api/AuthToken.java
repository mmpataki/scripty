package scripty.security.authorization.api;

import java.io.Serializable;

public abstract class AuthToken implements Serializable {

	private static final long serialVersionUID = -7787314039420001613L;

	public byte[] getBytes() {
		throw new UnsupportedOperationException("subclasses must implement this");
	}
	
	public AuthToken() {
		
	}
	
	public AuthToken(byte[] bytes) {
		throw new UnsupportedOperationException("subclasses must implement this");
	}
}
