package scripty.security.authorization.linstyle.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import scripty.security.authorization.api.AuthContext;
import scripty.security.authorization.api.AuthUtil;

@Component
public class LinStyleAuthUtil implements AuthUtil {

	@Override
	public AuthContext getAC() {
		return new LinAuthContext("dummy");
	}
	
	public AuthContext getAc() {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		return new LinAuthContext(authentication.getName());
	}

}
