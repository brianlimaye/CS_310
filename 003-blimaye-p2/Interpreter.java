// TO DO: add your implementation and JavaDocintStack.
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class Interpreter {

// We will test your implementation by running "java Interpreter [inputFile]" and checking your output
// which should be one or more integer values separated by a single space (the outcome(s) of the "print" instruction)
// (see the "Testing" section in the project description document)


// Below are hints that you might want to follow

	// Phase1:
	// create an attribure of type Stack<Integer>
	private Stack<Integer> intStack = new Stack<>();

	// Phase2:
	// create an attribute of type HashMap<Integer, Integer> to map the index of a variable to its value
	// create an attribute of type HashMap<Integer, Node<Instruction>> to map the offset of an instruction to its corresponding node in the LList
	private HashMap<Integer, Integer> indexMap = new HashMap<>();
	private HashMap<Integer, Node<Instruction>> instMap = new HashMap<>();

	private void fillInstMap(LList<Instruction> list) {

		Node<Instruction> curr = list.getFirst();

		while(curr != null) {

			Instruction instr = curr.getValue();
			instMap.put(instr.getOffset(), curr);
			curr = curr.getNext();
		}

	}

	private int evaluateResult(Instruction inst, String opCode, int first, int second) {

		int result = 0;

		switch(opCode) {

			case "iadd":
				result = second + first;
				break;
			case "imul":
				result = second * first;
				break;
			case "idiv":
				result = second / first;
				break;
			case "isub":
				result = second - first;
				break;
			case "irem":
				result = second % first;
				break;
			default:
				break;
		}

		return result;
	}

	private int parseValue(String opCode) {

		int value = -1;
		int index = opCode.indexOf("_");

		if(index < opCode.length() - 1) {
			
			try {
				value = Integer.parseInt(opCode.substring(index + 1));
			}
			catch(NumberFormatException mfe) {

			}
		}

		return ((value >= 0) && (value <= 5)) ? value : -1;
	}

	private boolean evaluatePhaseOne(Instruction inst, String opCode) {

		if(opCode == null) {
			return false;
		}

		int result;
		int value = 0;

		if(opCode.contains("_")) {

			value = parseValue(opCode);
			opCode = opCode.substring(0, opCode.indexOf("_"));
		}

		if(value == -1) {
			return false;
		}
		
		switch(opCode) {

			case "iconst":
				intStack.push(value);
				break;
			case "bipush":
				intStack.push(inst.getParam1());
				break;
			case "iadd":
			case "imul":
			case "idiv":
			case "isub":
			case "irem":
				//if(intStack.getSize() < 2) { break; }
				int firstVal = intStack.pop();
				int secondVal = intStack.pop();
				result = evaluateResult(inst, opCode, firstVal, secondVal);
				intStack.push(result);
				break;
			case "print":
				System.out.println(intStack.pop());
				break;
			case "return":
				break;
			default:
				return false;
		}

		return true;
	}

	private int evaluatePhaseTwo(Instruction inst, String opCode) {

		if(opCode == null) {
			return -1;
		}

		int varIndex = -1;
		int offsetNo = -1;
		int numParams = inst.getNumParameters();

		if((opCode.contains("_")) && (!opCode.startsWith("if"))) {

			varIndex = parseValue(opCode);
			opCode = opCode.substring(0, opCode.indexOf("_"));
		}

		switch(opCode) {

			case "iload":
				if((varIndex == -1) && (numParams == 1)) {
					varIndex = inst.getParam1();
				}

				Integer val = indexMap.get(varIndex);
				intStack.push(val);
				break;
			case "istore":
				Integer value = intStack.pop();

				if((varIndex == -1) && (numParams == 1)) {
					varIndex = inst.getParam1();
				}
				indexMap.put(varIndex, value);
				break; 
			case "goto":
				offsetNo = inst.getParam1();
				break;
			case "iinc":
				indexMap.put(inst.getParam1(), indexMap.get(inst.getParam1()) + 1);
				break;
			case "if_icmpeq":
				offsetNo = (intStack.pop() == intStack.pop()) ? inst.getParam1() : offsetNo;
				break;
			case "if_icmpne":
				offsetNo = (intStack.pop() != intStack.pop()) ? inst.getParam1() : offsetNo;
				break;
			case "if_icmpge":
				offsetNo = (intStack.pop() <= intStack.pop()) ? inst.getParam1() : offsetNo;
				break;
			case "if_icmpgt":
				//System.out.println("Comparing....");
				offsetNo = (intStack.pop() < intStack.pop()) ? inst.getParam1() : offsetNo;
				break;
			case "if_icmple":
				offsetNo = (intStack.pop() >= intStack.pop()) ? inst.getParam1() : offsetNo;
				break;
			case "if_icmplt":
				offsetNo = (intStack.pop() > intStack.pop()) ? inst.getParam1() : offsetNo;
				break;
			case "ifne":
				offsetNo = (intStack.pop() != 0) ? inst.getParam1() : offsetNo;
				break;
			default:
				return -1;
		}

		//System.out.println("OffsetNo: " + offsetNo);
		return offsetNo;
	}
		

	// public static LList<Instruction> readFile(String filename) throws IOException
	//
	// parse the instructions in fileName and store them in a LList<Instruction> (each as a Node<Instruction>) 
	// return the resulting LList<Instruction>
	// In the list, the instructions must follow the same order as in the input file
	//
	// (Note: Do not use "dummy nodes" since this is Java and not C)
	public static LList<Instruction> readFile(String filename) throws IOException {

		LList<Instruction> instr = new LList<>();

		File file = new File(filename);

		if((!file.isFile()) || (!file.exists())) {

			throw new IOException("File not Found...");
		}

		Scanner sc = new Scanner(file);

		while(sc.hasNext()) {

			Instruction instruction = new Instruction(sc.nextLine());
			instr.insertLast(new Node<Instruction>(instruction));
		}

		return instr;
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

		if(list == null) {
			return;
		}

		int offsetNo = -1;
		int cout = 0;
		Node<Instruction> curr = list.getFirst();
		fillInstMap(list);

		while(curr != null) {

			String opCode = curr.getValue().getOpcode();

			//System.out.println("Opcode: " + opCode);

			if(opCode != null) {

				if(!evaluatePhaseOne(curr.getValue(), opCode)) {

					offsetNo = evaluatePhaseTwo(curr.getValue(), opCode);
				}

				//System.out.println("Stack: " + intStack.toString());
			}

			curr = (offsetNo != -1) ? instMap.get(offsetNo) : curr.getNext();
			offsetNo = -1;
		}
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