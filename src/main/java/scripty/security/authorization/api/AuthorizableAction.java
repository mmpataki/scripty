package scripty.security.authorization.api;

public enum AuthorizableAction {
	READ(4),
	WRITE(2),
	UPDATE(1);
	
	int action;
	private AuthorizableAction(int action) {
		this.action = action;
	}
	
	public int get() {
		return action;
	}
}
