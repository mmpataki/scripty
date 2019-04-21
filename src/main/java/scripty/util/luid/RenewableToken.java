package scripty.util.luid;

/**
 * This interface is for internal purposes only.
 * @author mpataki
 *
 */
interface RenewableToken {
	void update(long rangeEnd);
	long current();
	int blockSize();
}