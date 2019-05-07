package scripty.security.authentication;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import scripty.models.User;
import scripty.security.authorization.api.AuthContext;
import scripty.security.authorization.linstyle.impl.LinAuthContext;
import scripty.services.api.UserManagementService;

@Service
public class ScriptyAuthenticationManager implements AuthenticationProvider {
	
	@Autowired
	UserManagementService UMS;
	
	AuthContext iAC;
	final String USER = "auth-service";
	
	@PostConstruct
	public void init() {
		/* build a context for your self. */
		iAC = new LinAuthContext(USER);
	}
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String userName = authentication.getName();
		try {
			User u = UMS.getUser(userName);
			if(u.getPassword().equals(hash(authentication.getCredentials().toString())))
				return new UsernamePasswordAuthenticationToken(userName, "");
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	public String hash(String passwd) {
		return EncodeUtil.encodePassword(passwd);
	}
}
