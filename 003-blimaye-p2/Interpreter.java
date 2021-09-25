import java.io.IOException;
import java.io.File;
import java.util.Scanner;

/**
 * A class that simulates a JVM interpreter that reads certain instructions, producing the desired output.
 *
 * @author Brian Limaye
 */
public class Interpreter {

    /**
     * A Stack used for the entirety of Phase1, pushing/popping values on/from the Stack.
     */
    protected Stack<Integer> intStack = new Stack<>();
    /**
     * A HashMap used in Phase2 to store values internally with their respective keys for lookup/updating.
     */
    protected HashMap<Integer, Integer> indexMap = new HashMap<>();
    /**
     * A HashMap also used in Phase2 to match the offset of an instruction with its corresponding Instruction, stored as a value in a Node.
     */
    protected HashMap<Integer, Node<Instruction>> instMap = new HashMap<>();

    /**
     * Helper method responsible for filling the instruction map, after reading the inputted file at runtime.
     *
     * @param list The newly created Linked List of Instructions used to fill the HashMap with its corresponding offset as a key.
     */
    protected void fillInstMap(final LList<Instruction> list) {

        Node<Instruction> curr = list.getFirst();

        while (curr != null) {    //Iterates through all Instruction nodes for the HashMap.
            final Instruction instr = curr.getValue();
            instMap.put(instr.getOffset(), curr);    //Stores the Instruction node into the HashMap, using its offset as the key.
            curr = curr.getNext();
        }
    }

    /**
     * Helper method responsible for evaluating the arithmetic/geometric operation on two numbers, based on the instruction name.
     *
     * @param inst   The instruction instance used to determine the corresponding operation.
     * @param opCode The instruction name used to determine the corresponding operation.
     * @param first  The "second" operand of the expression, as the operation is flipped.
     * @param second The "first" operand of the expression, as the operation is flipped.
     * @return Returns the output from the corresponding operation, 0 if inconclusive.
     */
    private int evaluateResult(final Instruction inst, final String opCode, final int first, final int second) {

        int result = 0;

        switch (opCode) {

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

    /**
     * Helper function responsible for parsing the value for Phase1 Instructions including an underscore.
     *
     * @param opCode The Instruction name used for parsing the value, -1 if not found.
     * @return Returns the parsed value, -1 if unsuccessful.
     */
    protected int parseValue(final String opCode) {

        int value = -1;
        int index = opCode.indexOf("_");        //Locates the index where an _ is found, if possible.

        if (index < opCode.length() - 1) {    //A value is able to be parsed if the index is non-negative and NOT the last character of opCode.

            //A check to determine if the parsed value is a numerical value.
            try {
                value = Integer.parseInt(opCode.substring(index + 1));
            } catch (NumberFormatException mfe) {

            }
        }

        return ((value >= 0) && (value <= 5)) ? value : -1;    //Validation that the parsed value is between 0 and 5, inclusive.
    }

    /**
     * Helper function responsible for evaluating a Phase1 Instruction.
     *
     * @param inst   The next Instruction to be evaluated, while determining if it belongs to Phase1.
     * @param opCode The instruction name for the current Instruction being evaluated.
     * @return Returns true if successfully evaluated, false otherwise.
     */
    protected boolean evaluatePhaseOne(final Instruction inst, String opCode) {

        if (opCode == null) {
            return false;
        }

        int result;
        int value = 0;

        if (opCode.contains("_")) {

            //Parses the value following the _ , if possible.
            value = parseValue(opCode);
            opCode = opCode.substring(0, opCode.indexOf("_"));
        }

        //Case where the value cannot be parsed successfully.
        if (value == -1) {
            return false;
        }

        switch (opCode) {

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

    /**
     * Helper function responsible for evaluating a Phase2 Instruction.
     *
     * @param inst   The next Instruction to be evaluated, while determining if it belongs to Phase2.
     * @param opCode The Instruction name for the current Instruction being evaluated.
     * @return Returns the next offset when a non-conditional/conditional jump is performed, -1 to indicate moving to the next Instruction.
     */
    protected int evaluatePhaseTwo(final Instruction inst, String opCode) {

        if (opCode == null) {
            return -1;
        }

        /*
         *Default values for the varIndex and offsetNo.
         */
        int varIndex = -1;
        int offsetNo = -1;

        final int numParams = inst.getNumParameters();

        //Parses value following the _ , IF the instruction is NOT a conditional check.
        if ((opCode.contains("_")) && (!opCode.startsWith("if"))) {
            varIndex = parseValue(opCode);
            opCode = opCode.substring(0, opCode.indexOf("_"));
        }

        switch (opCode) {

            case "iload":
                if ((varIndex == -1) && (numParams == 1)) {
                    //Variation of iload where there is an index parameter, rather than an index following an underscore.
                    varIndex = inst.getParam1();
                }

                final Integer val = indexMap.get(varIndex);
                intStack.push(val);
                break;
            case "istore":
                final Integer value = intStack.pop();

                if ((varIndex == -1) && (numParams == 1)) {
                    //Variation of istore where there is an index parameter, rather than an index following an underscore.
                    varIndex = inst.getParam1();
                }
                indexMap.put(varIndex, value);
                break;
            case "goto":
                offsetNo = inst.getParam1();
                break;
            case "iinc":
                //Increments the current value at the indicated key by the second parameter.
                indexMap.put(inst.getParam1(), indexMap.get(inst.getParam1()) + inst.getParam2());
                break;
            case "if_icmpeq":
                //If value1 and value2 are equal, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() == intStack.pop()) ? inst.getParam1() : offsetNo;
                break;
            case "if_icmpne":
                //If value1 and value2 are NOT equal, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() != intStack.pop()) ? inst.getParam1() : offsetNo;
                break;
            case "if_icmpge":
                //If value1 <= value2, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() <= intStack.pop()) ? inst.getParam1() : offsetNo;
                break;
            case "if_icmpgt":
                //If value1 < value2, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() < intStack.pop()) ? inst.getParam1() : offsetNo;
                break;
            case "if_icmple":
                //If value1 >= value2, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() >= intStack.pop()) ? inst.getParam1() : offsetNo;
                break;
            case "if_icmplt":
                //If value1 > value2, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() > intStack.pop()) ? inst.getParam1() : offsetNo;
                break;
            case "ifne":
                //If value1 != 0, a conditional jump is performed at the first parameter of the Instruction.
                offsetNo = (intStack.pop() != 0) ? inst.getParam1() : offsetNo;
                break;
            default:
                return -1;
        }
        return offsetNo;
    }


    /**
     * Responsible for reading the inputted file, while creating a Linked List of Instruction nodes.
     *
     * @param filename The filename to be read from, if possible.
     * @return Returns the Linked List of Instruction nodes represented by the inputted file.
     * @throws IOException Thrown when the file is unable to be read/processed.
     */
    public static LList<Instruction> readFile(final String filename) throws IOException {

        final LList<Instruction> instr = new LList<>();

        final File file = new File(filename);

        //Validation that the file exists and/or is a legitimate file.
        if ((!file.isFile()) || (!file.exists())) {
            throw new IOException("File not Found...");
        }

        final Scanner sc = new Scanner(file, "UTF-8");

        //Reads the file line by line, until a newline is found.
        while (sc.hasNext()) {
            Instruction instruction = new Instruction(sc.nextLine());        //Creates a new Instruction based on the read line.
            instr.insertLast(new Node<Instruction>(instruction));            //Inserts the Instruction node into the Linked List.
        }

        sc.close();
        return instr;
    }

    /**
     * Responsible for evaluating ALL instructions from the Linked List, whether they are Phase1 or Phase2 instructions.
     *
     * @param list The Linked List of Instruction nodes containing all instructions to be evaluated.
     */
    public void evaluateInstructions(final LList<Instruction> list) {

        //Initial check for a null list.
        if (list == null) {
            return;
        }

        int offsetNo = -1;

        //Gets the head of the Linked List of Instruction nodes.
        Node<Instruction> curr = list.getFirst();
        fillInstMap(list);

        while (curr != null) {

            final String opCode = curr.getValue().getOpcode();

            if (opCode != null) {

                //Attempts to evaluate the Phase1 Instruction, if possible.
                if (!evaluatePhaseOne(curr.getValue(), opCode)) {

                    offsetNo = evaluatePhaseTwo(curr.getValue(), opCode);        //Attempts the Phase2 Instruction if the Phase1 Instruction had been unsuccessful.
                }
            }

            //Gets the next Instruction if offsetNo hasn't been changed, otherwise the conditional/unconditional jump is performed to that Instruction.
            curr = (offsetNo != -1) ? instMap.get(offsetNo) : curr.getNext();
            offsetNo = -1;
        }
    }

    /**
     * The main method used to scan the input files, as command-line arguments, for testing at runtime.
     *
     * @param args The command-line arguments containing the input file used for testing purposes.
     */
    public static void main(final String[] args) throws Throwable {


        if (args.length != 1) {
            System.out.println("Usage: java Interpreter [filename]");
            System.exit(0);
        }
		
		try {
			final LList<Instruction> input = readFile(args[0]);		
			new Interpreter().evaluateInstructions(input);		
		}
		catch(IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}	
		
		/*
        // to test the readFile() method, do something similar to below
        try {
            LList<Instruction> input1 = readFile(args[0]);
            System.out.println(input1.listToString());
            if (input1.listToString().equals("0: iconst_1  1: iconst_2  2: iadd  3: print  6: return")) {
                System.out.println("Yay1");
            }
        } catch (IOException e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        //System.out.println(-17 % 7);
        */

    }
}