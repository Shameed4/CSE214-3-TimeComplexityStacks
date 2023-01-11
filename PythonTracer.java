
/**
 * The PythonTracer class takes a Python file and prints out its time complexity
 * 
 * @author Sean Erfan
 */

import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileNotFoundException;

public class PythonTracer {
	public static final int SPACES_COUNT = 4;
	public static Scanner fileReader;

	public static Stack<CodeBlock> codeStack;
	public static Stack<Integer> intStack;
	public static int stackSize;
	public static int stackLevel;

	/**
	 * Prompts the user for a file name and prints out the complexity.
	 */
	public static void main(String[] args) {
		String input;
		Scanner inputReader = new Scanner(System.in);
		while (true) {
			System.out.print("Please enter a file name (or 'quit' to quit): ");
			input = inputReader.nextLine().trim();
			if (input.toLowerCase().equals("quit")) {
				System.out.println("Program terminating successfully...");
				break;
			}
			try {
				System.out.println("\nOverall complexity of "
				        + input.substring(0, input.indexOf('.')) + ": "
				        + traceFile(input));
			}
			catch (FileNotFoundException e) {
				System.out.println("File not found");
			}
			System.out.println();
		}
		inputReader.close();
	}

	/**
	 * Traces a file and finds its complexity
	 * 
	 * @param fileName The file to use
	 * @return The time complexity
	 * @throws FileNotFoundException The file was not found
	 */
	public static Complexity traceFile(String fileName)
	        throws FileNotFoundException {
		stackSize = 0;
		codeStack = new Stack<CodeBlock>();
		intStack = new Stack<Integer>();
		stackLevel = 0;

		try {
			File myFile = new File(fileName);
			fileReader = new Scanner(myFile);

		}
		catch (FileNotFoundException e) {
			throw e;
		}

		while (fileReader.hasNextLine()) {
			String line = fileReader.nextLine();
			if (!line.trim().equals("") && line.trim().charAt(0) != '#') {
				int spaceCount = 0;
				for (int i = 0; i < line.length()
				        && line.charAt(i) == ' '; i++) {
					spaceCount++;
				}
				int indents = spaceCount / SPACES_COUNT;
				while (indents < stackSize) {
					if (indents == 0) {
						fileReader.close();
						while (stackSize > 1) {
							CodeBlock oldTop = popStacks();
							System.out.print(
							        "\n    Leaving block " + oldTop.getName());
							Complexity oldTopComplexity = oldTop
							        .totalComplexity();
							if (getGreaterComplexity(oldTopComplexity, codeStack
							        .peek()
							        .getHighestSubComplexity()) == oldTopComplexity) {
								codeStack.peek().setHighestSubComplexity(
								        oldTopComplexity);
								System.out.println(", updating block "
								        + codeStack.peek().getName() + ":");
							}
							else {
								System.out.println(", nothing to update.");
							}
						}
						System.out.println("    Leaving block 1.");
						return codeStack.peek().totalComplexity();
					}
					else {
						CodeBlock oldTop = popStacks();
						System.out.print(
						        "\n    Leaving block " + oldTop.getName());
						Complexity oldTopComplexity = oldTop.totalComplexity();
						if (getGreaterComplexity(oldTopComplexity, codeStack
						        .peek()
						        .getHighestSubComplexity()) == oldTopComplexity) {
							codeStack.peek()
							        .setHighestSubComplexity(oldTopComplexity);
							System.out.println(", updating block "
							        + codeStack.peek().getName() + ":");
						}
						else {
							System.out.println(", nothing to update.");
						}
					}
					printBlockInfo(codeStack.peek());

				}

				int keyWordIndex = -1;
				if (line.indexOf("def") == 0)
					keyWordIndex = 0;
				if (line.trim().indexOf("else:") != -1)
					keyWordIndex = CodeBlock.ELSE;
				for (int i = 1; i < CodeBlock.BLOCK_TYPES.length; i++) {
					if (line.indexOf(
					        " " + CodeBlock.BLOCK_TYPES[i] + " ") != -1) {
						keyWordIndex = i;
						break;
					}
				}

				if (keyWordIndex != -1) {
					CodeBlock block;
					if (keyWordIndex == CodeBlock.FOR) {
						Complexity c;
						if (line.indexOf("log_N:") != -1)
							c = new Complexity(0, 1);
						else
							c = new Complexity(1, 0);
						block = new CodeBlock(c);
					}

					else if (keyWordIndex == CodeBlock.WHILE) {
						String loopVariable = line
						        .substring(line.indexOf(" while "));
						loopVariable = loopVariable.substring(7);
						loopVariable = loopVariable.substring(0,
						        loopVariable.indexOf(" "));
						Complexity c = new Complexity(0, 0);
						block = new CodeBlock(c, loopVariable);
					}

					else
						block = new CodeBlock(new Complexity(0, 0));
					pushStacks(block, keyWordIndex);
					printBlockInfo(block);
				}

				else if (stackSize != 0
				        && codeStack.peek().getLoopVariable() != null) {
					String loopVariable = codeStack.peek().getLoopVariable();
					Complexity temp = codeStack.peek().getBlockComplexity();
					if (line.trim().contains(loopVariable + " -=")) {
						temp.setnPower(temp.getnPower() + 1);
						System.out.println(
						        "\n    Found update statement, updating block "
						                + readIntStack() + ":");
						printBlockInfo(codeStack.peek());
					}
					else if (line.trim().contains(loopVariable + " /=")) {
						temp.setLogPower(temp.getLogPower() + 1);
						System.out.println(
						        "\n    Found update statement, updating block "
						                + readIntStack() + ":");
						printBlockInfo(codeStack.peek());
					}
				}
			}
		}
		while (stackSize > 1) {
			CodeBlock oldTop = popStacks();
			System.out.print("\n    Leaving block " + oldTop.getName());
			Complexity oldTopComplexity = oldTop.totalComplexity();
			if (getGreaterComplexity(oldTopComplexity, codeStack.peek()
			        .getHighestSubComplexity()) == oldTopComplexity) {
				codeStack.peek().setHighestSubComplexity(oldTopComplexity);
				System.out.println(
				        ", updating block " + codeStack.peek().getName() + ":");
				printBlockInfo(codeStack.peek());
			}
			else {
				System.out.println(", nothing to update.");
			}
		}
		System.out.println(
		        "\n    Leaving block " + codeStack.peek().getName() + ".");
		fileReader.close();
		return popStacks().totalComplexity();
	}

	/**
	 * Returns the complexity that is greater
	 * 
	 * @param c1 the first complexity to compare
	 * @param c2 the second complexity to compare
	 * @return the greater complexity
	 */
	public static Complexity getGreaterComplexity(Complexity c1,
	        Complexity c2) {
		if (c1.getnPower() > c2.getnPower()) {
			return c1;
		}
		if (c1.getnPower() < c2.getnPower()) {
			return c2;
		}
		if (c1.getLogPower() > c2.getLogPower()) {
			return c1;
		}
		return c2;

	}

	/**
	 * Pops the code and int stacks and updates stackSize
	 * 
	 * @return The CodeBlock that was popped
	 */
	public static CodeBlock popStacks() {
		stackSize--;
		stackLevel = intStack.pop();
		return codeStack.pop();
	}

	/**
	 * Pushes the code and int stacks, updates their size, and prints a relevant
	 * message
	 * 
	 * @param block   The block being pushed into the stack
	 * @param keyWord The type of block being pushed (for, while, def, etc)
	 */
	public static void pushStacks(CodeBlock block, int keyWord) {
		codeStack.push(block);
		intStack.push(++stackLevel);
		stackSize++;
		block.setName(readIntStack());
		System.out.println();
		System.out.println(
		        String.format("%4s", "") + "Entering block " + block.getName()
		                + " '" + CodeBlock.BLOCK_TYPES[keyWord] + "':");
		stackLevel = 0;
	}

	/**
	 * @return The block number (e.g. 1.1)
	 */
	public static String readIntStack() {
		String txt = "";
		Stack<Integer> temp = new Stack<Integer>();
		for (int i = 0; i < stackSize; i++) {
			temp.push(intStack.pop());
		}
		for (int i = 0; i < stackSize - 1; i++) {
			int num = temp.pop();
			txt += num + ".";
			intStack.push(num);
		}
		int num = temp.pop();
		txt += num;
		intStack.push(num);

		return txt;
	}

	/**
	 * Prints out information about the block on the top of the stack, including
	 * its complexity and highest sub complexity
	 * 
	 * @param block
	 */
	public static void printBlockInfo(CodeBlock block) {
		String txt = "\t" + String.format("%4s%-15s%-30s%-1s", "",
		        "BLOCK " + block.getName() + ":",
		        "block complexity = " + block.getBlockComplexity(),
		        "highest sub-complexity = " + block.getHighestSubComplexity());
		System.out.println(txt);
	}
}
