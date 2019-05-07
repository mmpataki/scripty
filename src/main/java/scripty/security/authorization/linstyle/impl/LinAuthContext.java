package scripty.security.authorization.linstyle.impl;

import scripty.security.authorization.api.AuthContext;

public class LinAuthContext extends AuthContext {

	private String user;

	public LinAuthContext(String user) {
		super();
		this.user = user;
	}
	
	public String getUser() {
		return user;
	}
}
