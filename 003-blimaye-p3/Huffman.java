import java.io.*;

/**
 * Class represents a Huffman Tree implementation.
 * @author Brian Limaye
 */
class Huffman implements Serializable{
	// Note: We define this class (and a couple of other classes of 
	// this project) as Serializable in order to be able to save 
	// the Huffman Object into a file for encoding/decoding. 
	// (See main method below for details.)
	// You do not need to do anything special in your implementation 
	// for this. When a serializable object gets output into a file, 
	// "transient" members will be skipped.
	
	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------

	//default length used to create hashtables
	/**
	 * Default length of HashTable.
	 */
	public static final int DEFAULT_TABLE_LENGTH = 11;
	
	//original input string to encode
	/**
	 * Contents of the input string.
	 */
	private transient String inputContents = null;
	
	//hashtable used to count the frequencies of input characters
	/**
	 * HashTable of characters, along with its number of occurrences.
	 */
	private transient HashTable<Character,Integer> counts = 
		new HashTable<Character,Integer>(DEFAULT_TABLE_LENGTH);
		
	//priority queue used to build huffman tree
	/**
	 * Priority queue storing TreeNodes.
	 */
	private transient PriorityQueue<TreeNode> queue = new PriorityQueue<>();
	
	//huffman tree
	/**
	 * Huffman Tree representing a Binary Tree.
	 */
	private BinaryTree huffmanTree = new BinaryTree();
	
	//hashtable used to record the encoding for input characters
	/**
	 * Encodings used for validation purposes.
	 */ 
	private HashTable<Character,String> encodings = new HashTable<>(DEFAULT_TABLE_LENGTH);
		
	//setters and getters to help testing
	/**
	 * Sets the counts HashTable.
	 * @param counts The HashTable to be set.
	 */ 	
	public void setCounts(HashTable<Character, Integer> counts){
		this.counts = counts;
	}
	
	/**
	 * Gets the counts HashTable.
	 * @return Returns the counts HashTable.
	 */
	public HashTable<Character,Integer> getCounts(){
		return counts;
	}
	
	/**
	 * Sets the queue.
	 * @param queue The queue to be set.
	 */
	public void setQueue(PriorityQueue<TreeNode> queue){
		this.queue = queue;
	}

	/**
	 * Gets the queue.
	 * @return Returns the queue.
	 */
	public PriorityQueue<TreeNode> getQueue(){
		return queue;
	}

	/**
	 * Sets the tree.
	 * @param huffmanTree The tree to be set.
	 */
	public void setTree(BinaryTree huffmanTree){
		this.huffmanTree = huffmanTree;
	}

	/**
	 * Gets the tree.
	 * @return Returns the huffmanTree.
	 */
	public BinaryTree getTree(){
		return huffmanTree;
	}

	/**
	 * Gets the encodings.
	 * @return Returns the encodings.
	 */
	public HashTable<Character,String> getEncodings(){
		return encodings;
	}

	//provided methods for encoding
	//generate the encoding result from the huffman tree
	//if you have constructed a correct huffman tree, 
	// this would work...
	/**
	 * Computes the encodings used for validation purposes.
	 */
	public void computeEncodings(){
		computeEncodings(huffmanTree.root,"");
	}

	//recursive helper method for encoding
	/**
	 * Computes the encodings used for validation purposes, given a current location and encoding.
	 * @param currentLoc The current TreeNode.
	 * @param encoding The current encoding.
	 */
	private void computeEncodings(TreeNode currentLoc, String encoding){
		if(currentLoc.character != null){
			this.encodings.put(currentLoc.character, encoding);
		}
		else{
			computeEncodings(currentLoc.left, encoding+"0");
			computeEncodings(currentLoc.right, encoding+"1");
		}
	}

	// Use the encoding hashtable to generate a string of 
	// 0's and 1's as the encoding of the input.
	// The input might have multiple characters.
	/**
	 * Encodes an inputted String with 1s and 0s.
	 * @param input The input String to be encoded.
	 * @return Returns the encoded String.
	 */
	public String encode(String input){

		StringBuffer output = new StringBuffer();
		
		for (char ch : input.toCharArray()){
			output.append(this.encodings.get(ch));
		}
		
		return output.toString();	
	
	}

	//After encodings are computed, encode inputContents
	/**
	 * Encodes the inputContents String literal with 1s and 0s.
	 * @return Returns an encoded String based on inputContents.
	 */
	public String encode(){

		StringBuffer output = new StringBuffer();
		
		for (char ch : inputContents.toCharArray()){
			output.append(this.encodings.get(ch));
		}
		
		return output.toString();	

	}

	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

	/**
	 * Default constructor to initialize a new Huffman instance given an input String.
	 * @param input The input String to be associated with the Huffman tree.
	 */
	public Huffman(String input){
		this.inputContents = input;
	}
	
	/**
	 * Step 1 of Huffman's algorithm to count the occurrences of each characters in inputContents.
	 */
	public void createCounts(){
		
		//Check for a non-set/empty inputContents.
		if((inputContents == null) || (inputContents.length() == 0)) {
			return;
		}

		for(int i = 0; i < inputContents.length(); i++) {

			char currentChar = inputContents.charAt(i);
			Integer numOccurrences = counts.get(currentChar);

			//Case where a character has not been yet set into the HashTable.
			if(numOccurrences == null) {
				numOccurrences = 0;
			}

			//Case where a character has been found already.
			counts.put(currentChar, ++numOccurrences);
		}
	}
	
	/**
	 * Step 2 of Huffman's algorithm to fill a queue of unique TreeNodes associated with the HashTable.
	 */
	public void initQueue(){
		
		//Check for a non-set/empty inputContents.
		if((inputContents == null) || (inputContents.length() == 0)) {
			return;
		}

		for(int i = 0; i < inputContents.length(); i++) {

			char currentChar = inputContents.charAt(i);
			Integer numOccurrences = counts.get(currentChar);

			//Prematurely intializes a TreeNode prior to enqueuing.
			TreeNode currElement = new TreeNode(numOccurrences, currentChar);

			//If not a duplicate, the TreeNode element is added to the queue.
			if((numOccurrences != null) && (!queue.contains(currElement))) {
				queue.add(currElement);
			}
		}	
	}
	
	/**
	 * Step 3 of Huffman's algorithm to create a Huffman Tree from the queue of TreeNodes by merging children.
	 */	
	public void buildTree(){
	
		while(queue.size() > 1) {

			TreeNode left = queue.remove();
			TreeNode right = queue.remove();

			//Merges properties of the left and right children nodes to create a parent node.
			TreeNode mergedParent = new TreeNode(left.count + right.count, null);
			mergedParent.setLeft(left);
			mergedParent.setRight(right);

			queue.add(mergedParent);
		}

		//Sets the huffman tree's root to be the final element in the queue.
		huffmanTree.setRoot(queue.element());
	}
	
	/**
	 * Step 4 of Huffman's algorithm to decode an input String with a complete Huffman Tree.
	 * @param input The input String of 1s and 0s to be decoded.
	 * @return Returns the decoded result.
	 */
	public String decode(String input){
	
		//Check for an invalid input String.
		if((input == null) || (input.length() == 0)) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		TreeNode curr = huffmanTree.root;
		char[] charSeq = input.toCharArray();

		for(char c : charSeq) {

			//Moves to the right child node if a 1.
			if(c == '1') {
				curr = curr.right;
			}

			//Moves to the left child node if a 0.
			if(c == '0') {
				curr = curr.left;
			}

			//Once a leaf is found, the current node is reset to the root while appending the character at that leaf.
			if((curr.right == null) && (curr.left == null)) {
				
				sb.append(curr.character);
				curr = huffmanTree.root;
			}
		}

		return sb.toString();
	}

	//-------------------------------------------------------------
	// PROVIDED TESTING CODE: FEEL FREE TO EDIT
	//-------------------------------------------------------------
	/**
	 * Method primarily used for testing the implementation of Huffman.
	 */
	public static void testMain(){
	
		Huffman huff = new Huffman("cabbeadcdcdcdbbd");
		
		//step 1: count frequency 
		huff.createCounts();
		HashTable<Character,Integer> counts = huff.getCounts();
		System.out.println(counts);
		//System.out.println(counts);
		//System.out.println(counts.toStringDebug());

		if (counts.size() == 5 && counts.get('a') == 2 && counts.get('e')==1
			&& counts.toString().equals("c:4\nd:5\ne:1\na:2\nb:4")){
			System.out.println("Yay 1");
		}
		
		//step 2: initialize priority queue with leaf nodes
		huff.initQueue();
		PriorityQueue<TreeNode> queue = huff.getQueue();
		//System.out.println(queue);
		if (queue.size() == 5 && queue.element().character=='e' && queue.element().count==1){
			System.out.println("Yay 2");		
		}
		
		if (queue.toString().equals("<e,1> <a,2> <b,4> <c,4> <d,5>")){
			System.out.println("Yay 3");				
		}
		
		//step 3: build huffman tree with the help of priority queue
		huff.buildTree();
		BinaryTree tree= huff.getTree();
		if (tree.root.count == 16 && tree.root.left.count == 7 & tree.root.right.count == 9){
			System.out.println("Yay 4");					
		}
		
		//System.out.println(tree.toStringPreOrder());
		if (tree.toStringPreOrder().equals("<null,16><null,7><null,3><e,1><a,2><b,4><null,9><c,4><d,5>")){
			System.out.println("Yay 5");					
		}
		
		//step 4: encoding and decoding
		huff.computeEncodings();
		if (huff.decode("1000101").equals("cab") && huff.encode("cab").equals("1000101")){
			System.out.println("Yay 6");							
		}
		
	}	
	//-------------------------------------------------------------
	// END OF TESTING CODE
	//-------------------------------------------------------------


	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	//--------------------------------------------------------------------------------
	// How to run:
	// - To run testMain: java Huffman
	// - To encode:  java Huffman -e fileToEncode encodedOutputFile HuffmanObjectOutputFile
	// - To decode:  java Huffman -d fileToDecode decodedOutputFile HuffmanObjectInputFile
	//--------------------------------------------------------------------------------

	/**
	 * Main method used for testing purposes.
	 * @param args Command-line arguments used for testing functionality at run-time.
	 */
	public static void main(String[] args)
	{
		// no command-line args: provided testing of Huffman's algorithm
		if (args.length==0){
			testMain();
			return;
		}
		
		// with command-line args: file I/O for encoding/decoding
		if(args[0].equals("-e") && (args.length < 4 || args.length > 4)){
			System.out.println("Usage: java Huffman -e fileToEncode encodedOutputFile HuffmanObjectOutputFile");
			return;
		}
		else if(args[0].equals("-d") && (args.length < 4 || args.length > 4)){
			System.out.println("Usage: java Huffman -d fileToDecode decodedOutputFile HuffmanObjectInputFile");
			return;
		}
		else if(!args[0].equals("-d") && !args[0].equals("-e")){
			System.out.println("Usage: java Huffman -[e|d]");
			return;
		}
		
		String fileAsString;
		Huffman huff;
		try{
			switch(args[0]){
				case "-e": //encoding
				
					//read in fileToEncode
					fileAsString = getFileContents(args[1]);
					//System.out.println(fileAsString);
					
					//Huffman's algorithm 
					huff = new Huffman(fileAsString);
					huff.createCounts(); //step 1
					//System.out.println(hTree.counts);
					huff.initQueue(); //step 2
					//System.out.println(hTree.queue);
					huff.buildTree(); //step 3
					
					huff.computeEncodings();
					
					//encoding
					String encoding = huff.encode();
					//System.out.println("Encoded: " + encoding);
					
					//output encoded contents as a sequence of bits into file
					writeEncodedMessage(encoding, args[2]);
					
					//output Huffman object into file
					writeEncodedObject(huff, args[3]);
					break;
					
				case "-d": //decoding
				
					//read in from file and construct Huffman object
					huff = getEncodedObject(args[3]);
					
					//read in from file the encoded bits and
					//convert into a string (with only characters '0' and '1')
					fileAsString = getFileBinaryContents(args[1]);
					//System.out.println(fileAsString);
					
					//decoding
					String decodedMessage = huff.decode(fileAsString);
					
					//output decoded contents into file
					writeDecodedMessage(decodedMessage, args[2]);
					break;
			}
		}
		catch(IOException e){
			System.out.println("Problem reading or writing to specified file");
			System.out.println(e.toString());
		}
		
	}
	
	// output a Huffman Object to a file 
	/**
	 * Writes a Huffman Object to a file.
	 * @param huff Huffman instance to use.
	 * @param filename File to write to.
	 * @throws IOException Thrown when the file cannot be written to/processed.
	 */
	public static void writeEncodedObject(Huffman huff, String filename) throws IOException{
		try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename))){
			output.writeObject(huff);
		}
	}

	// read from a file and create a Huffman Object based on the file contents
	/**
	 * Gets the encoded object from a file.
	 * @param filename File to read from.
	 * @return Returns the Huffman Object associated with the file.
	 * @throws IOException Thrown when the file cannot be read from/processed.
	 */
	public static Huffman getEncodedObject(String filename) throws IOException{
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename))){
			return (Huffman)input.readObject();
		}
		catch(ClassNotFoundException e){
			throw new IOException("Can not read class from provided file.");
		}
	}
	
	// read the encoding result (as a string of 0's and 1's) from a file
	/**
	 * Gets the binary contents of a specific file.
	 * @param filename File to read from.
	 * @return Returns the binary contents of the file.
	 * @throws IOException Thrown when the file cannot be read from/processed.
	 */
	public static String getFileBinaryContents(String filename) throws IOException{
		StringBuffer fileContents = new StringBuffer();
		try(BitInputStream bs = new BitInputStream(new FileInputStream(filename), true)){
			while(bs.hasNextBit()){
				fileContents.append(bs.readBit());
			}
		}
		return fileContents.toString();
	}

	// output the encoding result (a string of 0's and 1's) as a bit sequence into a file
	/**
	 * Writes the encoding result to a specified file.
	 * @param message The message to be output.
	 * @param filename The file to be written to.
	 * @throws IOException Thrown when the file cannot be written to/processed.
	 */
	public static void writeEncodedMessage(String message, String filename) throws IOException{
		try(BitOutputStream bs = new BitOutputStream(new FileOutputStream(filename), true)){
			bs.writeBits(message);
		}
	}

	//read from file and return file contents as a string
	/**
	 * Gets the file contents from a specified file.
	 * @param filename The file to be read from.
	 * @return Returns the contents from a specified file.
	 * @throws IOException Thrown when the file cannot be read from/processed.
	 */
	public static String getFileContents(String filename) throws IOException{
		StringBuffer fileContents = new StringBuffer();
		try(BufferedReader br = new BufferedReader(new FileReader(filename))){
			String input = br.readLine();
			fileContents.append(input);
			input = br.readLine();
			
			while(input != null){
				fileContents.append("\n" + input);
				input = br.readLine();
			}
		}
		
		return fileContents.toString();
	}

	// out put message as a sequence of bits to file
	/**
	 * Writes a decoded message to a specified file.
	 * @param message The message to be written.
	 * @param filename The file to be written to.
	 * @throws IOException Thrown when the file cannot be written to/processed.
	 */
	public static void writeDecodedMessage(String message, String filename) throws IOException{
		try(BufferedWriter br = new BufferedWriter(new FileWriter(filename))){
			br.write(message);
		}
	}
	
	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------
}