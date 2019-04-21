package scripty.util.luid;

import org.springframework.stereotype.Service;

@Service
public interface Token {

	/**
	 * Next unique number for this token.
	 * @return
	 */
	public String next() throws Exception;
	
	/**
	 * Tokens are named.
	 * @return name of the token
	 */
	public String getName();
	
	/**
	 * Get blocksize of the token.
	 * @return
	 */
	public int getBlockSize();
}
