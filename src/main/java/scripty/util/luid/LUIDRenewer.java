package scripty.util.luid;

public interface LUIDRenewer {

	/**
	 * Renews a token.
	 * @param tok
	 * @throws Exception
	 */
	void renew(RenewableToken tok) throws Exception;
	
}
