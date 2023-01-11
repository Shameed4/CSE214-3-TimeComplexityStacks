/**
 * Describes a nested block of code
 * 
 * @author Sean Erfan
 *
 */
public class CodeBlock {
	public static final String[] BLOCK_TYPES = { "def", "for", "while", "if",
	        "elif", "else" };
	public static final int DEF = 0, FOR = 1, WHILE = 2, IF = 3, ELIF = 4,
	        ELSE = 5;

	private Complexity blockComplexity;
	private Complexity highestSubComplexity;

	private String name;
	private String loopVariable;

	/**
	 * Instantiates a CodeBlock and initializes its complexity
	 * 
	 * @param blockComplexity The complexity to set
	 */
	public CodeBlock(Complexity blockComplexity) {
		this.blockComplexity = blockComplexity;
		highestSubComplexity = new Complexity(0, 0);
	}

	/**
	 * @param blockComplexity The complexity to set
	 * @param loopVariable    The loop variable
	 */
	public CodeBlock(Complexity blockComplexity, String loopVariable) {
		this.blockComplexity = blockComplexity;
		this.loopVariable = loopVariable;
		highestSubComplexity = new Complexity(0, 0);
	}

	/**
	 * @return the blockComplexity
	 */
	public Complexity getBlockComplexity() {
		return blockComplexity;
	}

	/**
	 * @param blockComplexity the blockComplexity to set
	 */
	public void setBlockComplexity(Complexity blockComplexity) {
		this.blockComplexity = blockComplexity;
	}

	/**
	 * @return the highestSubComplexity
	 */
	public Complexity getHighestSubComplexity() {
		return highestSubComplexity;
	}

	/**
	 * @param highestSubComplexity the highestSubComplexity to set
	 */
	public void setHighestSubComplexity(Complexity highestSubComplexity) {
		this.highestSubComplexity = highestSubComplexity;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the loopVariable
	 */
	public String getLoopVariable() {
		return loopVariable;
	}

	/**
	 * @param loopVariable the loopVariable to set
	 */
	public void setLoopVariable(String loopVariable) {
		this.loopVariable = loopVariable;
	}

	/**
	 * @return the sum of the blockComplexity and highestSubComplexity.
	 */
	public Complexity totalComplexity() {
		return new Complexity(
		        blockComplexity.getnPower() + highestSubComplexity.getnPower(),
		        blockComplexity.getLogPower()
		                + highestSubComplexity.getLogPower());
	}
}
