package scripty.util.luid;

import org.springframework.stereotype.Service;

@Service
public interface LUIDService {
	
	/**
	 * Registers a LUID generator and gets a token for that. If some generator
	 * is already registered with same name, will throw an exception
	 * @param name : unique name for the LUIDGenerator
	 * @param blockSize : Size of the block used for extending the range. 
	 * 	default 100 will be used if 0 is passed.   
	 * @return token from which LUIDs can be generated.
	 */
	Token register(String name, int blockSize) throws Exception;
	
	/**
	 * Get a token with this name
	 * @param name : Name of the token 
	 * @return : token with name=`name`
	 * @throws Exception when there is no such token.
	 */
	Token getToken(String name) throws Exception;
	
	/**
	 * Unregister your LUID generator
	 * @param name
	 * @throws Exception
	 */
	void unregister(String name) throws Exception;
	
}
