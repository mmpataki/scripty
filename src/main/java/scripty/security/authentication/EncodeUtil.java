package scripty.security.authentication;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class EncodeUtil {

	public static final String SALT = "verysalty";

	public static String encodePassword(String password) {
		return new Md5PasswordEncoder().encodePassword(password, SALT);
	}
	
}
