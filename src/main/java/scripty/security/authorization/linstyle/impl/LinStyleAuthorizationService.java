package scripty.security.authorization.linstyle.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import scripty.security.authorization.api.AuthContext;
import scripty.security.authorization.api.AuthToken;
import scripty.security.authorization.api.AuthorizableAction;
import scripty.security.authorization.api.AuthorizationService;


@Service
@Scope("prototype")
public class LinStyleAuthorizationService implements AuthorizationService {

	private int mask;
	
	
	@Override
	public AuthToken getToken(String oId, AuthContext ac) {
		LinAuthContext lac = (LinAuthContext) ac;
		return new LinAuthToken(lac.getUser());
	}
	
	@Override
	public boolean isAuthorized(AuthToken tok, AuthContext context, AuthorizableAction action) {
		LinAuthContext lac = (LinAuthContext)context;
		LinAuthToken lat = (LinAuthToken)tok;
		int shift = lac.getUser().equals(lat.getUser()) ? 6 : 0;
		return ((action.get() << shift) & mask) != 0;
	}

	@Override
	public void setAuthParam(Object authParam) {
		mask = (Integer)authParam;
	}

}
