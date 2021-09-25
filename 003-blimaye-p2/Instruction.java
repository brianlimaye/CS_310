import java.util.*;

/**
 *A class that represents all data associated with a particular Instruction.
 *@author Brian Limaye
 */
public class Instruction
{
	/**
	 *Represents the "offset" or the instruction number of a particular Instruction instance.
	 */
	private int offset;
	/**
	 *Represents the separated Instruction name to be examined by the Interpreter.
	 */
	private String opcode;
	/**
	 *Represents a collection of parameters held by a particular Instruction instance.
	 */
	private ArrayList<Integer> parameters;  

	/**
	 *One-argument constructor that creates a new Intruction instance from an inputted literal.
	 *@param s An unparsed literal representing the miscellaneous details of the Instruction instance.
	 */
	public Instruction(String s) {  

		s = s.trim();
		parameters = new ArrayList<Integer>();

		String[] tokens = s.split("[:|,|\\s]+");
		int count = 1;

	    for (String token : tokens) {
    	
	    	String item = token.trim();
		    if (item.length() == 0) {
		    	System.out.println("blank item");
				continue;
		    }
		    if (count == 1) {
				offset = Integer.valueOf(item);  	
		    } else if (count == 2) { 
				opcode = item; 						
		    } else if (count == 3) {			
				parameters.add(Integer.valueOf(item));    
		    } else if (count == 4) {
				parameters.add(Integer.valueOf(item));  
		    } else {
				throw new RuntimeException("Illegal format: " + item);
		    }
		    count++;
	    }
	}

	/**
	 *Gets the human interpreted form representing the current Instruction instance.
	 *@return Returns the human interpreted form for the current Instruction.
	 */
	public String toString() {
		String s = offset + ": " + opcode + " ";
		if (parameters != null) {    	//Appends the parameters to the String, if present.
			for (int param : parameters) {
				s += param + " ";
			}
		}
		return s;
	}

	/**
	 *Gets the instruction number/offset of the current Instruction instance.
	 *@return Returns the instruction number/offset for the Instruction.
	 */
	public int getOffset() { return offset; }
	
	/**
	 *Gets the instruction name/opcode of the current Instruction instance.
	 *@return Returns the instruction name/opcode of the current Instruction instance.
	 */
	public String getOpcode() { return opcode; }

	/**
	 *Gets the number of parameters for the current Instruction instance.
	 *@return Returns the number of parameters present for the Instruction.
	 */
	public int getNumParameters() { return parameters.size(); }
	
	/**
	 *Gets the first parameter for the current Instruction instance, if possible.
	 *@return Returns the first parameter present for the Instruction.
	 */
	public int getParam1() { 
		if (getNumParameters() < 1) {
			throw new RuntimeException("instruction takes zero parameters");
		}
		return parameters.get(0); 
	}

	/**
	 *Gets the second parameter for the current Instruction instance, if possible.
	 *@return Returns the second parameter present for the Instruction.
	 */
	public int getParam2() { 
		if (getNumParameters() < 2) {
			throw new RuntimeException("instruction takes zero or one parameters");
		}
		return parameters.get(1); 
	}

	/**
	 *The main method solely responsible for testing the functionality of the Instruction class.
	 *@param args Command-line arguments used for testing on the fly at run-time.
	 */
	public static void main(String args[]) {

		Instruction ins = new Instruction("0: iconst_2");
		if ( (ins.getOffset() == 0) && 
				(ins.getOpcode().equals("iconst_2")) && 
					(ins.getNumParameters() == 0)) {
			System.out.println("Yay1");
		}

		ins = new Instruction("21 : bipush         6");
		if ( (ins.getOffset() == 21) && 
				(ins.getOpcode().equals("bipush")) && 
					(ins.getNumParameters() == 1) &&
						(ins.getParam1() == 6) )
		{
			System.out.println("Yay2");
		}

		ins = new Instruction("40:iinc 4, 1");
		if ( (ins.getOffset() == 40) && 
				(ins.getOpcode().equals("iinc")) && 
					(ins.getNumParameters() == 2) &&
						(ins.getParam1() == 4) && (ins.getParam2() == 1)) {
			System.out.println("Yay3");
		}
	}
		
}