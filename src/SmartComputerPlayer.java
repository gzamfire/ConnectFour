import java.awt.Color;
import java.util.Random;

/*
 * Class SmartComputerPlayer
 * This player generates its moves relatively intelligently
 */ 

public class SmartComputerPlayer extends ComputerPlayer {

    public SmartComputerPlayer(String name, Color color) {
        super(name, color);
    }
    
    private Random randGen = new Random();
    
    public Move getMove(Board board) {
    	
    	for (int col = 0; col < board.getCols(); col++) {
    		Board testBoard = new Board(board.getRows(), board.getCols());
    		for (int row = 0; row < board.getRows(); row++) {
    			for (int c = 0; c < board.getCols(); c++) {
    				testBoard.getGrid()[row][c] = board.getGrid()[row][c];
    			}
    		}
    		
    		Move tryMove = new Move(col, this);
    		
    		testBoard.addPiece(tryMove);
    		if (testBoard.winner(tryMove) instanceof Player) {
    			return tryMove;
    		}	
    	}
    	
        // right now, the computer just chooses randomly.
        return( new Move(randGen.nextInt(board.getCols()), this) );
    }
    

}