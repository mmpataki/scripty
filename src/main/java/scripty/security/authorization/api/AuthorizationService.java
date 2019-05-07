package scripty.security.authorization.api;

import org.springframework.context.annotation.Scope;

@Scope("prototype")
public interface AuthorizationService {
	
	public AuthToken getToken(String oId, AuthContext ac);
	
	public boolean isAuthorized(AuthToken auth, AuthContext context, AuthorizableAction action);
	
	public void setAuthParam(Object authParam);
}
