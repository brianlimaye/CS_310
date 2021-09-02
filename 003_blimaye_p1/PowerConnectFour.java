// TO DO: add your implementation and JavaDocs

import java.util.Scanner;

/**
 * This class represents and controls the functionality of the various facets of the PowerConnectFour game.
 * This includes all the various moves, the current player, the collection of columns, and the display sizes.
 *
 * @author Brian Limaye
 */
public class PowerConnectFour {

    // DO NOT MODIFY INSTANCE VARIABLES PROVIDED BELOW

    /**
     * The grid to contain tokens. Cells can be empty.
     */
    //underlying array of columns for storage -- you MUST use this for credit!
    //Do NOT change the name or type

    private Column<Token>[] grid;

    /**
     * The fixed number of columns the game grid should have.
     */
    private static final int NUM_COLS = 7;

    /**
     * The minimum number of rows of the grid _for display_.
     */
    private static final int MIN_ROWS = 6;

    /**
     * The first player of the game.
     * playerOne is always the first to make a move when the game starts.
     */
    private static final Token playerOne = Token.RED;

    /**
     * The second player of the game.
     * playerTwo is always the second to make a move, following playerOne.
     */
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

    /**
     * The current player at the start of a turn.
     */
    private Token currentPlayer;

    /**
     * Toggles the current player, following a successful move.
     */
    private void changeTurn() {

        currentPlayer = (currentPlayer == Token.RED) ? Token.YELLOW : Token.RED;
    }

    /**
     * Initializes the columns of the display grid.
     */
    @SuppressWarnings("unchecked")
    private void initColumns() {

        for (int i = 0; i < NUM_COLS; i++) {

            grid[i] = (Column<Token>) new Column(MIN_ROWS);
        }
    }

    /**
     * Obtains the start index of a major diagonal, given a point(col, row) to start from. A key is also given for either a column or row.
     *
     * @param col    The original column index.
     * @param row    The original row index.
     * @param maxRow The maximum row indices that are currently being displayed.
     * @param key    Denotes whether a starting column or row should be returned for the particular major diagonal.
     * @return Returns the starting row or column, that is relative to the point(col, row) on its major diagonal.
     */
    private int getMajorStartIndex(int col, int row, int maxRow, char key) {

        Token temp;

        for (int i = 0; i < NUM_COLS; i++) {

            try {
                //Attempts to get the previous Token, going in the up-left direction.
                temp = grid[col - 1].get(row + 1);

                //Shifts the position to the previous Token.
                col--;
                row++;

            } catch (IndexOutOfBoundsException iobe) {

                //Returns the previous column/row, dependant on the key, prior to going out of bounds.
                return (key == 'r') ? row : col;
            }
        }

        return (key == 'r') ? row : col;
    }

    /**
     * Obtains the end index of a major diagonal, given a point(col, row) to start from. A key is also given for either a column or row.
     *
     * @param col    The original column index.
     * @param row    The original row index.
     * @param maxRow The maximum row indices that are currently being displayed.
     * @param key    Denotes whether an ending column or row should be returned for the particular major diagonal.
     * @return Returns the ending row or column, that is relative to the point(col, row) on its major diagonal.
     */
    private int getMajorEndIndex(int col, int row, int maxRow, char key) {

        Token temp;

        for (int i = 0; i < NUM_COLS; i++) {

            try {
                //Attempts to get the next Token, going in the down-right direction.
                temp = grid[col + 1].get(row - 1);

                //Shifts the position to the next Token.
                col++;
                row--;

            } catch (IndexOutOfBoundsException iobe) {

                //Returns the previous column/row, dependant on the key, prior to going out of bounds.
                return (key == 'r') ? row : col;
            }
        }

        return (key == 'r') ? row : col;
    }

    /**
     * Obtains the start index of a minor diagonal, given a point(col, row) to start from. A key is also given for either a column or row.
     *
     * @param col    The original column index.
     * @param row    The original row index.
     * @param maxRow The maximum row indices that are currently being displayed.
     * @param key    Denotes whether a starting column or row should be returned for the particular minor diagonal.
     * @return Returns the starting row or column, that is relative to the point(col, row) on its minor diagonal.
     */
    private int getMinorStartIndex(int col, int row, int maxRow, char key) {

        Token temp;

        for (int i = 0; i < NUM_COLS; i++) {

            try {
                //Attempts to get the previous Token, going in the up-left direction.
                temp = grid[col - i].get(row - i);

                //Shifts the position to the previous Token.
                col--;
                row--;
            } catch (IndexOutOfBoundsException iobe) {

                //Returns the previous column/row, dependant on the key, prior to going out of bounds.
                return (key == 'r') ? row : col;
            }
        }

        return (key == 'r') ? row : col;
    }

    /**
     * Obtains the end index of a minor diagonal, given a point(col, row) to start from. A key is also given for either a column or row.
     *
     * @param col    The original column index.
     * @param row    The original row index.
     * @param maxRow The maximum row indices that are currently being displayed.
     * @param key    Denotes whether an ending column or row should be returned for the particular minor diagonal.
     * @return Returns the ending row or column, that is relative to the point(col, row) on its minor diagonal.
     */
    private int getMinorEndIndex(int col, int row, int maxRow, char key) {

        Token temp;

        for (int i = 0; i < NUM_COLS; i++) {

            try {
                //Attempts to get the next Token, going in the down-right direction.
                temp = grid[col + i].get(row + i);

                //Shifts the position to the next Token.
                col++;
                row++;
            } catch (IndexOutOfBoundsException iobe) {

                //Returns the previous column/row, dependant on the key, prior to going out of bounds.
                return (key == 'r') ? row : col;
            }
        }

        return (key == 'r') ? row : col;
    }


    /**
     * Initializes a new PowerConnectFour instance, starting the game.
     */
    @SuppressWarnings("unchecked")
    public PowerConnectFour() {

        grid = (Column<Token>[]) new Column[NUM_COLS];

        //The individual columns are instantiated each.
        initColumns();

        //Default current player.
        currentPlayer = Token.RED;
    }

    /**
     * Gets the number of columns in the display grid.
     *
     * @return Returns the number of columns of the grid.
     */
    public int sizeCol() {
        return NUM_COLS;
        // O(1)
    }

    /**
     * Gets the maximum number of rows to display, in order to fit all pieces.
     *
     * @return Returns the number of rows to display on the board.
     */
    public int sizeRow() {

        int max = 0;

        for (int i = 0; i < NUM_COLS; i++) {
            /*
             *Finds the maximum number of rows, amongst all Columns in the grid.
             */
            if (grid[i].size() > max) {

                max = grid[i].size();
            }
        }

        //If the maximum number of rows exceeds MIN_ROWS, an extra row is displayed. If not, MIN_ROWS is returned...
        return (max >= MIN_ROWS) ? max + 1 : MIN_ROWS;
        // O(1)
    }

    /**
     * Gets the Character representation of an empty cell.
     *
     * @return Returns an empty cell Character.
     */
    public Character getEmptySymbol() {
        return empty;
        // O(1)
    }

    /**
     * Gets a Token at a given column and row.
     *
     * @param col The column of the grid to search, if possible.
     * @param row The row of the grid to search, if possible.
     * @return Returns the Token at a particular column and row, if applicable.
     */
    public Token get(int col, int row) {

        //Obtains the maximum number of rows displayed.
        int maxRows = sizeRow();

        //Check for an invalid column and/or row.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRows)) {
            throw new IndexOutOfBoundsException("Col " + col + ", Row " + row + " out of bounds!");
        }

        //Last row should be empty entirely.
        //Returns empty when a column does not contain any token, or while it has a smaller capacity than the element being accessed.
        if ((row == maxRows - 1) || (row >= grid[col].capacity())) {
            return null;
        }

        return grid[col].get(row);
        // O(1)
    }


    /**
     * Gets a particular column of the grid, given an index.
     *
     * @param col The column index to obtain.
     * @return Returns the column at a given index, if possible.
     */
    public Column<Token> getColumn(int col) {

        //Check for an invalid column index.
        if ((col < 0) || (col >= NUM_COLS)) {
            throw new IndexOutOfBoundsException("Col " + col + " out of bounds!");
        }

        return grid[col];
        // O(1)
    }

    /**
     * Obtains the current player, at the start of a turn.
     *
     * @return Returns the current player, prior to their turn.
     */
    public Token currentPlayer() {
        // Return the player that can make the next move.
        return currentPlayer;
        // O(1)
    }


    /**
     * A move to drop a Token in a particular column, assuming the validity of the move is true.
     *
     * @param col The column, in which the Token is being dropped into.
     * @return Returns true if the move was successful, false otherwise.
     */
    public boolean drop(int col) {

        //Checks for an invalid column.
        if ((col < 0) || (col >= NUM_COLS)) {
            return false;
        }

        //The token is added if a valid column index is provided, thus making this a valid move.
        grid[col].add(currentPlayer);
        changeTurn();
        return true;
        // Amortized O(1)
    }

    /**
     * A move to power drop a Token in a particular column AND row, assuming the dropped Token is bound by at least one other Token.
     *
     * @param col The column, in which the Token is being dropped into.
     * @param row The row, in which the Token is being dropped into.
     * @return Returns true if the move was successful, false otherwise.
     */
    public boolean powerDrop(int col, int row) {

        //Checks for an invalid column and/or row that is out of bounds/floating.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= sizeRow())) {
            return false;
        }

        try {
            //Attempts to add the current player's token, while anticipating a thrown exception, if not possible.
            grid[col].add(row, currentPlayer());
        } catch (IndexOutOfBoundsException iobe) {

            return false;
        }

        //Changes turn, if the insertion is completed.
        changeTurn();
        return true;
        // O(N) where N is the number of tokens in the involved column
    }


    /**
     * A move to pop a Token from its corresponding column, assuming this Token belongs to the current player.
     *
     * @param col The column, in which the Token is attempted to be removed from.
     * @return Returns true if the move was successful, false otherwise.
     */
    public boolean pop(int col) {

        //A check for an invalid column index.
        if ((col < 0) || (col >= NUM_COLS)) {
            return false;
        }

        //First, a check is made that the popped token belongs to the current player.
        if (grid[col].get(0) == currentPlayer) {
            grid[col].delete(0);
            //The turn is changed once the verification is completed.
            changeTurn();
            return true;
        }
        return false;
        // O(N) where N is the number of tokens in the involved column
    }


    /**
     * A move to power pop a Token from its corresponding column and row is attempted, assuming this Token belongs to the player.
     *
     * @param col The column index, in which the Token is attempted to be removed from.
     * @param row The row index, in which the Token is attempted to be removed from.
     * @return Returns true if the move was successful, false otherwise.
     */
    public boolean powerPop(int col, int row) {

        //A check for an invalid column and/or row index.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= sizeRow())) {
            return false;
        }

        //A check is made that the popped token belongs to the current player.
        if (grid[col].get(row) == currentPlayer) {
            grid[col].delete(row);
            //The turn is changed once the verification is completed.
            changeTurn();
            return true;
        }
        return false;
        // O(N) where N is the number of tokens in the involved column
    }

    /**
     * Counts the number of contiguous tokens of a given player at the row level, where one token is supposedly placed at a given column and row.
     *
     * @param col    The column index, supposedly containing a token from the current player.
     * @param row    The row index, supposedly containing a token from the current player.
     * @param player The token that represents the current player.
     * @return Returns the number of consecutive tokens of a given player at a row, starting at a particular column and row.
     */
    public int countRow(int col, int row, Token player) {

        int consecutives = 0;
        int maxRow = sizeRow() - 1;

        //Check for any invalid column and/or row indices.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRow)) {
            return 0;
        }

        //Check for a row that exceeds the capacity of a given column.
        if (row >= grid[col].capacity()) {

            return 0;
        }

        //A check to ensure the token at the column and row belongs to the current player.
        if (grid[col].get(row) != player) {
            return 0;
        }

        //A constant loop that checks all columns located in the current row/col for contiguous tokens.
        for (int i = 0; i < NUM_COLS; i++) {

            //Checks to see if the shifted column is in bounds. If so, its contents are compared to the current player's token.
            if ((row < grid[i].capacity()) && (grid[i].get(row) == player)) {

                ++consecutives;
                continue;
            } else if (i > col) {   //Exits out of the loop once a non-player token is reached, following i = col.
                break;
            } else {
                consecutives = 0;    //When a non-player token is found before the column and row are reached, the count is reset.
            }
        }

        return consecutives;
        // O(1)
    }

    /**
     * Counts the number of contiguous tokens of a given player at the column level, where one token is supposedly placed at a given column and row.
     *
     * @param col    The column index, supposedly containing a token from the current player.
     * @param row    The row index, supposedly containing a token from the current player.
     * @param player The token that represents the current player.
     * @return Returns the number of consecutive tokens of a given player at a column, starting at a particular column and row.
     */
    public int countCol(int col, int row, Token player) {
        // Count and return the number of consecutive tokens for the given player
        // in a column such that one of those tokens is at the given row and col
        // of the grid.

        // Return 0 if out of bounds

        int consecutives = 0;
        int maxRow = sizeRow() - 1;

        //Check for any invalid column and/or row indices.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRow)) {
            return 0;
        }

        //Check for a row that exceeds the capacity of a given column.
        if (row >= grid[col].capacity()) {
            return 0;
        }

        //Ensures there is a token that belongs to the current player at the row and column.
        if (grid[col].get(row) != player) {
            return 0;
        }

        //A loop that checks the all rows in the current row/col for contiguous tokens.
        for (int i = 0; i < maxRow; i++) {

            //Checks to see if the shifted rows are in bounds. If so, its contents are compared to the current player's token.
            if ((i < grid[col].capacity()) && (grid[col].get(i) == player)) {

                ++consecutives;
                continue;
            }
            //Exits out of the loop once a non-player token is reached and the current row is passed.
            else if (i > row) {
                break;
            }
            //When a non-player token is found before the column and row are reached, count is reset.
            else {
                consecutives = 0;
            }
        }

        return consecutives;
        // O(N) where N is the number of tokens in the involved column
    }

    /**
     * Counts the number of contiguous tokens of a given player at the major diagonal level, up-left and down-right, where one token is supposedly placed at the column and row.
     *
     * @param col    The column index, supposedly containing a token from the current player.
     * @param row    The row index, supposedly containing a token from the current player.
     * @param player The token that represents the current player.
     * @return Returns the number of consecutive tokens of a given player, along the major diagonal, starting at a particular column and row.
     */
    public int countMajorDiagonal(int col, int row, Token player) {
        // Count and return the number of consecutive tokens for the given player
        // in a major diagonal such that one of those tokens is at the given row and col
        // of the grid.  A major diagonal line covering (col, row) can extend diagonally
        // down and to the right as well as up and to the left from the given
        // location (col, row).

        int consecutives = 0;
        int maxRow = sizeRow() - 1;

        int startRow = getMajorStartIndex(col, row, maxRow, 'r');
        int endRow = getMajorEndIndex(col, row, maxRow, 'r');

        int startCol = getMajorStartIndex(col, row, maxRow, 'c');
        int endCol = getMajorEndIndex(col, row, maxRow, 'c');

        int currentRow = startRow;
        int currentCol = startCol;

        //Check for any invalid column and/or row indices.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRow)) {
            return 0;
        }

        if (row >= grid[col].capacity()) {
            return 0;
        }

        //Ensures there is a token that belongs to the current player at the row and column.
        if (grid[col].get(row) != player) {
            return 0;
        }

        //A constant loop that checks the left and right portion of the major diagonal to the current column and row for contiguous tokens.
        for (int i = 0; i < NUM_COLS; i++) {

            //Checks to see if the main diagonal tokens are in bounds. If so, its contents are compared to the current player's token.

            if ((i >= startCol) && (i <= endCol) && (countRow(currentCol, currentRow, player) >= 1) && (grid[currentCol].get(currentRow) == player)) {
                ++consecutives;
            } else if (currentCol >= col) {
                break;
            } else {
                consecutives = 0;
            }

            currentCol++;
            currentRow--;
        }

        return consecutives;

        // Return 0 if out of bounds
        // O(1)
    }

    /**
     * Counts the number of contiguous tokens of a given player at the minor diagonal level, up-right and down-left, where one token is supposedly placed at the column and row.
     *
     * @param col    The column index, supposedly containing a token from the current player.
     * @param row    The row index, supposedly containing a token from the current player.
     * @param player The token that represents the current player.
     * @return Returns the number of consecutive tokens of a given player, along the minor diagonal, starting at a particular column and row.
     */
    public int countMinorDiagonal(int col, int row, Token player) {
        // Count and return the number of consecutive tokens for the given player
        // in a minor diagonal such that one of those tokens is at the given row and col
        // of the grid.  A minor diagonal line covering (col, row) can extend diagonally
        // up and to the right as well as down and to the left from the given
        // location (col, row).

        int consecutives = 0;
        int maxRow = sizeRow();

        int startRow = getMinorStartIndex(col, row, maxRow, 'r');
        int endRow = getMinorEndIndex(col, row, maxRow, 'r');

        int startCol = getMinorStartIndex(col, row, maxRow, 'c');
        int endCol = getMinorEndIndex(col, row, maxRow, 'c');

        int currentRow = startRow;
        int currentCol = startCol;

        //Check for any invalid column and/or row indices.
        if ((col < 0) || (col >= NUM_COLS) || (row < 0) || (row >= maxRow - 1)) {
            return 0;
        }

        if (row >= grid[col].capacity()) {
            return 0;
        }

        //Ensures there is a token that belongs to the current player at the row and column.
        if (grid[col].get(row) != player) {
            return 0;
        }

        //A constant loop that checks the left and right portion of the minor diagonal to the current column and row for contiguous tokens.
        for (int i = 0; i < NUM_COLS; i++) {

            //Checks to see if the minor diagonal tokens are in bounds. If so, its contents are compared to the current player's token.
            if ((i >= startCol) && (i <= endCol) && (countRow(currentCol, currentRow, player) >= 1) && (grid[currentCol].get(currentRow) == player)) {

                ++consecutives;
            } else if (currentCol >= col) {        //Exits out of the loop once a non-player token is reached, following i = 0;
                break;
            } else {
                consecutives = 0;   //When a non-player token is found before the column and row are reached, count is reset.
            }

            currentCol++;
            currentRow++;
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
    public boolean hasFourConnected(Token player) {
        // Check whether the specified player has four tokens either in a row,
        // in a column, or in a diagonal line (major or minor). Return true if
        // so; return false otherwise.

        for (int j = 0; j < sizeCol(); j++) {
            for (int i = 0; i < sizeRow(); i++) {
                if (countRow(j, i, player) >= 4 || countCol(j, i, player) >= 4
                        || countMajorDiagonal(j, i, player) >= 4
                        || countMinorDiagonal(j, i, player) >= 4)
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

    /**
     * Main method for briefly testing the functionality of the PowerConnectFour implementation.
     *
     * @param args The command-line arguments, primarily used for testing input values at run-time.
     */

    public static void main(String[] args) {

        // init with an empty grid
        PowerConnectFour myGame = new PowerConnectFour();

        if (myGame.sizeCol() == NUM_COLS && myGame.sizeRow() == MIN_ROWS
                && myGame.getColumn(2).size() == 0 && myGame.currentPlayer() == Token.RED
                && myGame.get(0, 0) == null) {
            System.out.println("Yay 1!");
        }

        // drop
        if (!myGame.drop(10) && myGame.drop(2) && myGame.getColumn(2).size() == 1 &&
                myGame.get(2, 0) == Token.RED && myGame.currentPlayer() == Token.YELLOW) {
            System.out.println("Yay 2!");
        }

        // drop, pop, column growing/shrinking, board display changed
        boolean ok = true;
        for (int i = 0; i < 5; i++) {
            ok = ok && myGame.drop(2);    //take turns to drop to column 2 for five times
        }
        //System.out.println("===Current Grid===");
        //PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display
        if (ok && myGame.getColumn(2).size() == 6 && myGame.sizeRow() == 7
                && myGame.pop(2) && myGame.sizeRow() == 6) { //&& myGame.get(2,1) == Token.RED){
            System.out.println("Yay 3!");
        }
        //PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display

        // power drop
        if (!myGame.powerDrop(3, 1) && myGame.powerDrop(3, 0) && myGame.powerDrop(2, 2)
                && myGame.getColumn(2).size() == 6 && myGame.get(2, 2) == Token.RED
                && myGame.get(2, 3) == Token.YELLOW && myGame.getColumn(3).size() == 1) {
            System.out.println("Yay 4!");
        }
        //PowerConnectFourGUI.displayGrid(myGame); //uncomment to check the grid display

        //power pop
        if (!myGame.powerPop(2, 1) && myGame.powerPop(2, 3)
                && myGame.getColumn(2).size() == 5 && myGame.get(2, 3).getSymbol() == 'R') {
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


        //counting
        if (myGame.countRow(3, 0, Token.YELLOW) == 2 && myGame.countRow(3, 0, Token.RED) == 0
                && myGame.countCol(2, 3, Token.RED) == 3 && myGame.drop(3) /*one more R*/
                && myGame.countMajorDiagonal(3, 1, Token.RED) == 2 /* (3,1) and (2,2) */
                && myGame.countMinorDiagonal(2, 0, Token.YELLOW) == 1) {
            System.out.println("Yay 6!");
        }
    }
}