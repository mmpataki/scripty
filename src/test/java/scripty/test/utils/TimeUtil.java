package scripty.test.utils;

public class TimeUtil {

	public static double nanoDiffInSec(long end, long start) {
		return ((double)(end - start)) / 1e9;
	}
	
}
