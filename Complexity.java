/**
 * The Complexity class represents the Big-Oh complexity of some block of code.
 * 
 * @author Sean Erfan
 *
 */
public class Complexity {
	private int nPower;
	private int logPower;

	/**
	 * Instantiates a Complexity object and initializes its variables
	 * 
	 * @param n
	 * 		The n power
	 * @param log
	 * 		The log(n) power
	 */
	public Complexity(int n, int log) {
		nPower = n;
		logPower = log;
	}

	/**
	 * @return the n power
	 */
	public int getnPower() {
		return nPower;
	}

	/**
	 * @param nPower
	 * 		the n power to set
	 */
	public void setnPower(int nPower) {
		this.nPower = nPower;
	}

	/**
	 * @return the log power
	 */
	public int getLogPower() {
		return logPower;
	}

	/**
	 * @param logPower
	 * 		the log power to set
	 */
	public void setLogPower(int logPower) {
		this.logPower = logPower;
	}

	/**
	 * @return the order of complexity
	 */
	public String toString() {
		if (nPower == 0) {
			if (logPower == 0)
				return "O(1)";
			if (logPower == 1)
				return "O(log(n))";
			return "O(log(n)^" + logPower + ")";
		}
		else if (nPower == 1) {
			if (logPower == 0)
				return "O(n)";
			if (logPower == 1)
				return "O(n * log(n))";
			return "O(n * log(n)^" + logPower + ")";
		}
		else {
			if (logPower == 0)
				return "O(n^" + nPower + ")";
			if (logPower == 1)
				return "O(n^" + nPower + " * log(n))";
			return "O(n^" + nPower + " * log(n)^" + logPower + ")";
			
		}
	}
}
