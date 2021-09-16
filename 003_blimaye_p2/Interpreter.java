// TO DO: add your implementation and JavaDocs.
import java.io.IOException;


public class Interpreter {

// We will test your implementation by running "java Interpreter [inputFile]" and checking your output
// which should be one or more integer values separated by a single space (the outcome(s) of the "print" instruction)
// (see the "Testing" section in the project description document)


// Below are hints that you might want to follow

	// Phase1:
	// create an attribure of type Stack<Integer>

	// Phase2:
	// create an attribute of type HashMap<Integer, Integer> to map the index of a variable to its value
	// create an attribute of type HashMap<Integer, Node<Instruction>> to map the offset of an instruction to its corresponding node in the LList
		

	// public static LList<Instruction> readFile(String filename) throws IOException
	//
	// parse the instructions in fileName and store them in a LList<Instruction> (each as a Node<Instruction>) 
	// return the resulting LList<Instruction>
	// In the list, the instructions must follow the same order as in the input file
	//
	// (Note: Do not use "dummy nodes" since this is Java and not C)
	public static LList<Instruction> readFile(String filename) throws IOException {

		return null;
			 
		
	}
	
	// public void evaluateInstructions(LList<Instruction> list)
	//
	// traverse and evaluate the list of instructions 
	//
	// Hints: 
	// depending on the instruction at hand you might need to
	// a) push a value on the stack 
	// b) fetch the value from HashMap<Integer, Integer> then push it on the stack
	// c) pop a value off the stack and store it in HashMap<Integer, Integer>
	// d) to pop the stack and store the value in HashMap<Integer, Integer>
	// e) etc.
	// 
	// HashMap<Integer, Node<Instruction>> is only needed when evaluating the following instructions:
	// goto location, if_icmpeq location, if_icmpne location, if_icmpge location, if_icmpgt location, 
	// if_icmple location, if_icmplt location, ifne location
	
	public void evaluateInstructions(LList<Instruction> list) {

	}
	
	
	public static void main(String[] args) {
	
		if(args.length != 1) {
			System.out.println("Usage: java Interpreter [filename]");
			System.exit(0);
		}
		
		try {
			LList<Instruction> input = readFile(args[0]);		
			new Interpreter().evaluateInstructions(input);		
		}
		catch(IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		/*
		// to test the readFile() method, do something similar to below
		try {			
			LList<Instruction> input1 = readFile("..\\Inputs_part1\\Input0.txt");
			if (input1.listToString().equals("0: iconst_1  1: iconst_2  2: iadd  3: print  6: return")) {
				System.out.println("Yay1");
			}
		}
		catch(IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		*/

	}
}