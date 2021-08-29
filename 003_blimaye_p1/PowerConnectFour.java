// TO DO: add your implementation and JavaDocs

public class PowerConnectFour {

	// DO NOT MODIFY INSTANCE VARIABLES PROVIDED BELOW
	
	/**
	 *  The grid to contain tokens. Cells can be empty.
	 */
	//underlying array of columns for storage -- you MUST use this for credit!
	//Do NOT change the name or type

	private Column<Token>[] grid;

	/**
	 *  The fixed number of columns the game grid should have.
	 */
	private static final int NUM_COLS = 7;

	/**
	 *  The minimum number of rows of the grid _for display_.
	 */
	private static final int MIN_ROWS = 6;

	/**
	 * The two players of the game.
	 * playerOne is always the first to make a move when the game starts.
	 */
	private static final Token playerOne = Token.RED;
	private static final Token playerTwo = Token.YELLOW;

	/**
	 * The character used to represent empty cells when the grid is displayed.
	 */
	private static final Character empty = Character.valueOf('-');
	
	/**
	 * When grid is displayed, the top row of the grid should always be empty.
	 */  
	private static final int MARGIN_ROWS = 1;
	
	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

	private Token currentPlayer;

	private void changeTurn() {

		currentPlayer = (currentPlayer == Token.RED) ? Token.YELLOW : Token.RED;
	}

	@SuppressWarnings("unchecked")
	private void initColumns() {

		for(int i = 0; i < NUM_COLS; i++) {

			grid[i] = (Column<Token>) new Column(MIN_ROWS);
		}
	}
	
		
	@SuppressWarnings("unchecked")
	public PowerConnectFour() {
		// Constructor with no arguments.
		
		// A grid with NUM_COLS columns should be created.  The initial capacity of 
		// each column should be DEFAULT_CAPACITY defined in our Column class. 
		// All columns are empty initially(size 0).

		grid = (Column<Token>[]) new Column[NUM_COLS];
		initColumns();
		
		// Remember to initialize game settings.

		currentPlayer = Token.RED;
	}


	public int sizeCol() { 
		// Return number of columns of the grid
		// Reminder: we set this to be a constant number.
		return NUM_COLS;
		// O(1)
	}
	
	public int sizeRow() { 
		// Return number of rows _for DISPLAY_ of the grid
		
		// Note: just because a method comes early in the code doesn't mean you should 
		// write it first. You need to read the rest of the code to understand how 
		// this will work.
		
		// Reminder: return of this method is used by GUI to set the appropriate display 
		//		range for the grid.
		//   Our rules for display: (check the project spec for details & examples)
		//    - always show at least MIN_ROWS for each column;
		//	  - if the number of pieces of any column reaches or grows beyond MIN_ROWS,
		//		make sure the display covers the "tallest" column and leaves one "margin"
		//		row at the top of the grid.

		int max = 0;

		for(int i = 0; i < NUM_COLS; i++) {

			if(grid[i].size() > max) {

				max = grid[i].size();
			}
		}

		return (max >= MIN_ROWS) ? max + 1 : MIN_ROWS;
		// O(1)	
	}
	
	public Character getEmptySymbol(){
		// Return the character defined for empty cells for display.
		// Reminder: we set this to be a constant.
		return empty;
		// O(1)
	}
		

	public Token get(int col, int row){
		// Return token at the given column and row of the grid.

		// For an invalid row/col index (out of the range of current display), 
		// throw an IndexOutOfBoundsException.
		// Use this code to produce the correct error message for
		// the exception (do not use a different message):
		//	  "Col " + col + ", Row "+ row + " out of bounds!"

		// Return null if the cell at the given col and row is empty

		int maxRows = sizeRow();

		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRows)) {
			throw new IndexOutOfBoundsException("Col " + col + ", Row "+ row + " out of bounds!");
		}

		if(row == maxRows - 1) {
			return null;
		}

		return grid[col].get(row);
		// O(1)		
	}

	
	public Column<Token> getColumn(int col){
		// Return column at the given index
		
		// For an invalid column index, throw an IndexOutOfBoundsException.
		// Use this code to produce the correct error message for
		// the exception (do not use a different message):
		//	  "Col " + col + " out of bounds!"

		if((col < 0) || (col >= NUM_COLS)) {
			throw new IndexOutOfBoundsException("Col " + col + " out of bounds!");
		}

		return grid[col];
		// O(1)
	}
	

	public Token currentPlayer(){
		// Return the player that can make the next move.
		return currentPlayer;
		// O(1)
	}


	public boolean drop(int col){
		// Current player drop a token at the given column.
		// Return true if the move can be made; return false 
		// if the move cannot be made for any reason.  Switch 
		// to the other player only if the move can be made successfully.
		
		// Reminder: when a column grows, the display settings may need to be changed.

		if((col < 0) || (col >= NUM_COLS)) {
			return false;
		}

		grid[col].add(currentPlayer);
		changeTurn();
		return true;
		// Amortized O(1)		
	}
	


	public boolean powerDrop(int col, int row){
		// Current player drop/insert a token at the given column and row.
		// Note: no floating tokens allowed.
		
		// Return true if the move can be made; return false  if the move 
		// cannot be made for any reason.  Switch  to the other player only 
		// if the move is made successfully.

		// Reminder: when a column grows, the display settings may need to be changed.

		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= sizeRow())) {
			return false;
		}

		try {

			grid[col].add(row, currentPlayer());
		}
		catch(IndexOutOfBoundsException iobe) {
			
			return false;
		}

		changeTurn();
		return true;
		// O(N) where N is the number of tokens in the involved column
	}
	

	public boolean pop(int col){
		// Current player pop a token from the given column.
		// Return true if the move can be made; return false 
		// if the move cannot be made for any reason.  Switch 
		// to the other player only if the move is made successfully.
		
		// Reminder: when a column shrinks, the display settings may need to be changed.

		if((col < 0) || (col >= NUM_COLS)) {
			return false;
		}

		if(grid[col].get(0) == currentPlayer) {
			grid[col].delete(0);
			changeTurn();
			return true;
		}
		return false;
		// O(N) where N is the number of tokens in the involved column
	}

	
	public boolean powerPop(int col, int row){
		// Current player pop/remove a token from the given column and row.

		// Note: tokens above the removed one need to be shifted to make sure
		// there are no floating tokens in grid.		
		// Return true if the move can be made; return false  if the move 
		// cannot be made for any reason.  Switch  to the other player only 
		// if the move is made successfully.

		// Reminder: when a column shrinks, the display settings may need to be changed.
		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= sizeRow())) {
			return false;
		}

		if(grid[col].get(row) == currentPlayer) {
			grid[col].delete(row);
			changeTurn();
			return true;
		}
		return false;
		// O(N) where N is the number of tokens in the involved column
	}
	

	public int countRow(int col, int row, Token player){
		// Count and return the number of consecutive tokens for the given player
		// in a row such that one of those tokens is at the given row and col 
		// of the grid.
		
		// Return 0 if out of bounds

		int consecutives = 0;

		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= sizeRow())) {
			return 0;
		}

		if(grid[col].get(row) != player) {
			return 0;
		}

		for(int i = -3; i < 4; i++) {

			if((col + i >= 0) && (col + i < NUM_COLS) && (grid[col + i].get(row) == player)) {

				++consecutives;
				continue;
			}
			else if(i > 0) {
				break;
			}
		}

		return consecutives;
		// O(1)
	}
		

	//To-Do: Fix Big O.
	public int countCol(int col, int row, Token player){
		// Count and return the number of consecutive tokens for the given player
		// in a column such that one of those tokens is at the given row and col 
		// of the grid.
		
		// Return 0 if out of bounds

		int consecutives = 0;

		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= sizeRow())) {
			return 0;
		}

		if(grid[col].get(row) != player) {
			return 0;
		}

		for(int i = -3; i < 4; i++) {

			if((row + i >= 0) && (row + i < NUM_COLS) && (grid[col].get(row + i) == player)) {

				++consecutives;
				continue;
			}
			else if(i > 0) {
				break;
			}
		}

		return consecutives;		
		// O(N) where N is the number of tokens in the involved column		
	}
	
	
	public int countMajorDiagonal(int col, int row, Token player){
		// Count and return the number of consecutive tokens for the given player
		// in a major diagonal such that one of those tokens is at the given row and col 
		// of the grid.  A major diagonal line covering (col, row) can extend diagonally 
		// down and to the right as well as up and to the left from the given 
		// location (col, row).

		int consecutives = 0;
		int maxRow = sizeRow();

		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRow)) {
			return 0;
		}

		if(grid[col].get(row) != player) {
			return 0;
		}

		for(int i = -3; i < 4 ; i++) {

			if((row - i >= 0) && (row - i < maxRow - 1) && (col + i >= 0) && (col + i < NUM_COLS)
		      && (grid[col + i].get(row - i) == player)) {

				++consecutives;
				continue;
			}
			else if(i > 0) {
				break;
			}
		}
		System.out.println("Diagonals are " + consecutives);
		return consecutives;
		
		// Return 0 if out of bounds
		// O(1)		
	}

	public int countMinorDiagonal(int col, int row, Token player){
		// Count and return the number of consecutive tokens for the given player
		// in a minor diagonal such that one of those tokens is at the given row and col 
		// of the grid.  A minor diagonal line covering (col, row) can extend diagonally 
		// up and to the right as well as down and to the left from the given 
		// location (col, row).

		int consecutives = 0;
		int maxRow = sizeRow();

		if((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRow)) {
			return 0;
		}

		if(grid[col].get(row) != player) {
			return 0;
		}

		for(int i = -3; i < 4 ; i++) {

			System.out.println("Row: " + (row + i) + " Col: " + (col + i));

			if((row + i >= 0) && (row + i < maxRow - 1) && (col + i >= 0) && (col + i < NUM_COLS)
		      && (grid[col + i].get(row + i) == player)) {

				++consecutives;
				continue;
			}
			else if(i > 0) {
				break;
			}
		}
		return consecutives;  
		
		// Return 0 if out of bounds
		// O(1)
	}

	//******************************************************
	//*******  DO NOT EDIT ANYTHING IN THIS SECTION  *******
	//*******        But do read this section!       *******
	//******************************************************
	
	/**
	 * The method that checks whether the specified player has four connected tokens
	 * horizontally, vertically, or diagonally.  It relies on the methods of countRow(),
	 * countCol(), countMajorDiagonal(), and countMinorDiagonal() to work correctly.
	 *
	 * @param player the token to be checked
	 * @return whether the given player has four tokens connected
	 */
	public boolean hasFourConnected(Token player){
		// Check whether the specified player has four tokens either in a row,
		// in a column, or in a diagonal line (major or minor). Return true if 
		// so; return false otherwise.	
		
		for (int j = 0; j<sizeCol(); j++){
			for (int i = 0; i<sizeRow(); i++){
				if (countRow(j, i, player)>=4 || countCol(j, i, player)>=4
					|| countMajorDiagonal(j, i, player)>=4 
					|| countMinorDiagonal(j, i, player)>=4 )
					return true;
			}
		}
		return false;
		
	}

	//******************************************************
	//*******     BELOW THIS LINE IS TESTING CODE    *******
	//*******      Edit it as much as you'd like!    *******
	//*******		Remember to add JavaDoc			 *******
	//******************************************************
	
	public static void main(String[] args) {
	
		// init with an empty grid
		PowerConnectFour myGame = new PowerConnectFour();	

		if (myGame.sizeCol() == NUM_COLS && myGame.sizeRow() == MIN_ROWS 
			&& myGame.getColumn(2).size() == 0 && myGame.currentPlayer() == Token.RED
			&& myGame.get(0,0) == null) {
			System.out.println("Yay 1!");		
		}
		
		// drop
		if (!myGame.drop(10) && myGame.drop(2) && myGame.getColumn(2).size() == 1 && 
			myGame.get(2,0) == Token.RED && myGame.currentPlayer() == Token.YELLOW ){
			System.out.println("Yay 2!");					
		}
		
		// drop, pop, column growing/shrinking, board display changed
		boolean ok = true;
		for (int i=0; i<5; i++){
			ok = ok && myGame.drop(2); 	//take turns to drop to column 2 for five times
		}
		//System.out.println("===Current Grid===");		
		//PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display
		if (ok && myGame.getColumn(2).size() == 6 && myGame.sizeRow() == 7
			&& myGame.pop(2) && myGame.sizeRow() == 6) { //&& myGame.get(2,1) == Token.RED){
			System.out.println("Yay 3!");							
		}
		//PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display
		
		// power drop
		if (!myGame.powerDrop(3,1) && myGame.powerDrop(3,0) && myGame.powerDrop(2,2)
			&& myGame.getColumn(2).size() == 6 && myGame.get(2,2) == Token.RED
			&& myGame.get(2,3) == Token.YELLOW && myGame.getColumn(3).size() == 1){
			System.out.println("Yay 4!");							
		}
		//PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display
		
		//power pop
		if (!myGame.powerPop(2,1) && myGame.powerPop(2,3) 
			&& myGame.getColumn(2).size() == 5 && myGame.get(2,3).getSymbol()=='R'){
			System.out.println("Yay 5!");									
		}
		PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display
		PowerConnectFourGUI.reportcurrentPlayer(myGame);
		// expected display:
		//|   || 0 || 1 || 2 || 3 || 4 || 5 || 6 |
		//| 5 || - || - || - || - || - || - || - |
		//| 4 || - || - || Y || - || - || - || - |
		//| 3 || - || - || R || - || - || - || - |
		//| 2 || - || - || R || - || - || - || - |
		//| 1 || - || - || R || - || - || - || - |
		//| 0 || - || - || Y || Y || - || - || - |
		//Player R's turn

		
		System.out.println(myGame.countRow(3, 0, Token.YELLOW));
		//counting
		if (myGame.countRow(3,0,Token.YELLOW) == 2 && myGame.countRow(3,0,Token.RED) == 0
			&& myGame.countCol(2,3,Token.RED) == 3 && myGame.drop(3) /*one more R*/
			&& myGame.countMajorDiagonal(3,1,Token.RED) == 2 /* (3,1) and (2,2) */
			&& myGame.countMinorDiagonal(2,0,Token.YELLOW) == 1) {
			System.out.println("Yay 6!");												
		}
	}
}