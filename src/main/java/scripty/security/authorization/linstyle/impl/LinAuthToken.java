package scripty.security.authorization.linstyle.impl;

import scripty.security.authorization.api.AuthToken;

public class LinAuthToken extends AuthToken {

	private static final long serialVersionUID = 1379601145576369597L;
	
	boolean authorized = false;
	String user;
	
	public LinAuthToken(byte[] bytes) {
		user = new String(bytes);
	}
	
	public LinAuthToken(String user) {
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setAuthorized() {
		authorized = true;
	}
	
	@Override
	public byte[] getBytes() {
		return user.getBytes();
	}
	
}
