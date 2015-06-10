import java.awt.Color;


public class Board  {


    private int rows;
    private int cols;
    
    /** The grid of pieces */
    private Player[][] grid;
    
    

    public Board(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.grid = new Player[rows][cols];
        // set each cell of the board to null (empty).
        reset();

    }
    
    public void reset() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = null;
            }
        }
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public Player[][] getGrid() {
    	return this.grid;
    }
    
    
    /**
    * Returns the Player whose piece occupies the given location, 
    * @param row int
    * @param col int
    */
    public Player getCell(int row, int col ) throws IndexOutOfBoundsException{
        if( (row < 0) || (col < 0) || (row >= rows) || (col >= cols) ) {
            throw new IndexOutOfBoundsException();
        } else {
            return grid[row][col];
        }
    }
    
    // this method fills the board once a winner is crowned
    // that prevents the player or the computer from making additional moves without those moves being impossible
    public void fillBoard(Move move) {
    	
    	Player dummy = new Player("Dummy", Color.gray);
    	
    	for (int row = 0; row < rows; row++) {
    		
    		for (int col = 0; col < cols; col++) {
    			
    			if (!(grid[row][col] instanceof Player)) {
    				grid[row][col] = dummy;
    			}
    			
    		}
	
    	}
    }

    // Returns true if move is possible given board state.  
    public boolean possibleMove(Move move) {
        // TODO: write this.  Right now, it ignores filled columns, claiming any move is possible
    	
    	// the idea is that a move is impossible if a column is already filled
    	// this method need only return true or false, depending on whether the move is possible
    	// so we need to check a given column to see if it is filled
    		// we could check every element in that column
    		// or we could simply check the top element, because if that is filled then the column is full
    		// HOWEVER, because stacking does not yet work... 
    	
    	int intendedColumn = move.getColumn();
    	
    	if (grid[rows-1][intendedColumn] instanceof Player) {
    		
    		System.out.println("Impossible move");
    		return false;
    	} else {
    		return true;
    	}
    	
//    	for (int r = rows - 1; r >= 0; r--) {
//    		if (grid[r][intendedColumn] == null) {
//    			return true;
//    		}
//    	}
//        return false;
    }
    
    // Adds a piece to the board for a given Move
    public void addPiece(Move move) {
    	
    	if (move instanceof BombMove) {
    		
    		for (int col = 0; col < cols; col++) {
    			for (int row = 0; row < rows-1; row++) {
    				grid[row][col] = grid[row+1][col];
    			}
    		}
    		
    		for (int col = 0; col < cols; col++) {
    			grid[rows-1][col] = null;
    		}
    		
    	} else {
	    	    	
	        //TODO: this is a test stub, you need to rewrite this.
	    	
	    	int intendedColumn = move.getColumn();
	    	boolean stackedOn = false;
	    	
	    	// first, check to see if a move is possible (unsure if this is implemented previously or not!)
	    	if (possibleMove(move)) {
	    		
	    		// next, check to see which is the last inhabited row; start with the top row until you find a populated one
	    		for (int r = rows-1; r >= 0; r--) {
	    			if (grid[r][intendedColumn] != null) {
	    				stackedOn = true;
	    				grid[r+1][intendedColumn] = move.getPlayer();
	    				r = -1; // to force exit of the loop, since we cannot use break
	    			}		
	    		}
	    		
	    		// if there are no other plays in that column, we simply inhabit the bottom-most row
	    		if (!stackedOn) { grid[0][move.getColumn()] = move.getPlayer(); }
	    		
	    	}
    	
    	}

    }

    // if the board contains a winning position, returns the Player that wins.
    // Otherwise, returns null.  You could ignore lastMove.
    public Player winner(Move lastMove) {
    	
//    	if (columnWinner(lastMove) instanceof Player) {
//    		return columnWinner(lastMove);
//    	}
    	
//    	return columnWinner(lastMove) || rowWinner(lastMove) || ;
    	
        // TODO: write this.  Currently, there is never a winnder.
    	
    	int connectN = 4;
    	
    	// ROW WINNER CHECK
    	for (int row = 0; row < rows; row++) {
    		for (int col = 0; col < cols - connectN + 1; col++) {
    			boolean streak = true;
    			
    			if (grid[row][col] instanceof Player) {

    				Player potentialWinner = grid[row][col];
    				for (int n = 1; n < connectN; n++) {
        				if (grid[row][col+n] != potentialWinner) {
        					streak = false;
        					n = connectN; // exit the loop; the streak was broken        					
        				}
        			}
    				if (streak) {
    					return potentialWinner;
    				}	
    			}	  			
    		}
    	}
    	
    	// COLUMN WINNER CHECK
    	for (int col = 0; col < cols; col++) {
    		for (int row = 0; row < rows - connectN + 1; row++) {
    			boolean streak = true;
    			
    			if (grid[row][col] instanceof Player) {

    				Player potentialWinner = grid[row][col];
    				for (int n = 1; n < connectN; n++) {
        				if (grid[row+n][col] != potentialWinner) {
        					streak = false;
        					n = connectN; // exit the loop; the streak was broken        					
        				}
        			}
    				if (streak) {
    					return potentialWinner;
    				}	
    			}	  			
    		}
    	}
    	
    	// DIAGONAL (RISING) WINNER CHECK
    	for (int col = 0; col < cols - connectN + 1; col++) {
    		for (int row = 0; row < rows - connectN + 1; row++) {
    			boolean streak = true;
    			
    			if (grid[row][col] instanceof Player) {

    				Player potentialWinner = grid[row][col];
    				for (int n = 1; n < connectN; n++) {
        				if (grid[row+n][col+n] != potentialWinner) {
        					streak = false;
        					n = connectN; // exit the loop; the streak was broken        					
        				}
        			}
    				if (streak) {
    					return potentialWinner;
    				}	
    			}	  			
    		}
    	}
    	
    	// DIAGONAL (FALLING) WINNER CHECK
    	for (int col = 0; col < cols - connectN + 1; col++) {
    		for (int row = 0 + connectN - 1; row < rows; row++) {
    			boolean streak = true;
    			
    			if (grid[row][col] instanceof Player) {

    				Player potentialWinner = grid[row][col];
    				for (int n = 1; n < connectN; n++) {
        				if (grid[row-n][col+n] != potentialWinner) {
        					streak = false;
        					n = connectN; // exit the loop; the streak was broken        					
        				}
        			}
    				if (streak) {
    					return potentialWinner;
    				}	
    			}	  			
    		}
    	}
    	
        return null;
    }
    
    
} // end Board class