import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;

public class Connect4 extends JPanel implements MouseListener {

    // These control the size of the board.  The traditional Connect 4 board is 
    // seven spaces wide by six spaces high
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private static final int pieceSize = 50;     //size of the pieces in pixels
    private static final int spacing = 10;       //spacing between adjacent pieces in pixels;
    private static final int headerHeight = 50;  // height of header for messages and column numbers
    private String message = "";
    
    private JFrame myFrame;
    private Board myBoard;

    // this contains the ordered list of players in the game
    private ArrayList<Player> players;
    private int currentPlayerIndex = 0;
    
	private InputMap im;
	private ActionMap am;

   
     /** creates the connect four interface with the specified number of rows and colonms
       * @param rows int
       * @param cols int
       */   
    public Connect4(int rows, int cols ) {

        myFrame = new JFrame();
        this.myBoard = new Board(rows, cols);
        addMouseListener(this);
        // the hard numbers at the end are for the menubar at the top and side handles
        myFrame.setSize( myBoard.getCols() * (pieceSize + spacing) + spacing + 10,
                         myBoard.getRows() * (pieceSize + spacing) + spacing + headerHeight + 35);
        myFrame.add(this);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //myFrame.setResizable( false );
        myFrame.setTitle( "Connect Four" );
        myFrame.setVisible(true);
        
        

        // start a new game 
        newGame();
    }    
    
//    public Action quitGame() {
//    	System.out.println("Fire");
//		return null;
//    }

    
    
    //////  Gameplay
    //////
    // Note, the main loop in the gameplay (players alternating turns) happens between 
    //  the play() and takeTurn() methods.  These call each other, which is a type of
    //  recursion (called mutual recursion).  
    // Call newGame() to restart the game, or start it for the first time.
    
    public void newGame() {
        myBoard.reset();
        players = new ArrayList<Player>();
        Player p1 = new Player("Jane", Color.black);
        SmartComputerPlayer p2 = new SmartComputerPlayer("Robot Joe", Color.red);
        players.add(p1);
        players.add(p2);
        
        message = "Let's play!  " + getCurrentPlayer().getName() + " goes first.  ";
        repaint();
        play();
    }
    
    
    // start the recursion to play the game.  
    private void play() {
    	
    	im = getInputMap();
        am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "Quit");
        am.put("Quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
        
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "Bomb");
        am.put("Bomb", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	takeTurn( new BombMove(0, getCurrentPlayer()) );
            }
        });
    	
        if ( (getCurrentPlayer() instanceof ComputerPlayer ) ) {
            // the player is a ComputerPlayer, so can calculate its own move
            //delay(1000);  // hm, this doesn't work
            // only ComputerPlayers have a getMove method, so we have to cast getCurrentPlayer into one.
            ComputerPlayer computer = (ComputerPlayer) getCurrentPlayer();
            takeTurn(computer.getMove(myBoard));
        }
        // otherwise, the player isn't a ComputerPlayer, so we stop, and let the listener invoke takeTurn()
    }
    
    
    private void takeTurn(Move move) {
    	
    	if (move instanceof BombMove) {
    		
    		myBoard.addPiece(move);
    		message = getCurrentPlayer().getName() + " just dropped the BOMB!";
    		repaint();
    		advanceToNextPlayer();
    		play();
    		
    	} else {
    	
	    	if (myBoard.possibleMove(move)) {
	    		// if a move is possible, the move is added to the board
		        myBoard.addPiece(move);
		        
		        // the message then declares what column the move took place in
		        message = getCurrentPlayer().getName() + " goes in column " + move.getColumn() + ".  ";
		        
		        if ( myBoard.winner(move) != null ) {
		        	// this means there is a winner
		        	
		        	repaint();
		            message = getCurrentPlayer().getName() + " wins!  ";
		            message += "Press 'Q' to restart.";
		            repaint();
		            
		            // we fill the board so that no more moves can be made
		            myBoard.fillBoard(move);
	//	            advanceToNextPlayer();
		            
		        } else {
		        	// this means there was no winner
		        	
		        	// we advance to the next player
		            advanceToNextPlayer();
		            // we declare whose turn it is
		            message += "It is now " + getCurrentPlayer().getName() + "'s turn.  ";
		            repaint();
		            play();
		        }
	    	} else {
	    		message = "Game over. Press 'Q' to restart.";
	//    		message = "Impossible move by " + getCurrentPlayer().getName() + ". Please move again.  ";
	    		repaint();
	    		play();
	    	}
    	}
    }
    
    
    // returns the current player
    private Player getCurrentPlayer () {
        return players.get(currentPlayerIndex);
    }
    
    // advance the next player in line
    private void advanceToNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) {
            currentPlayerIndex = 0;
        }
    }

    
    //////Listener
    //////
    public void mouseClicked(MouseEvent e) {
    	System.out.println("Mousevent.");
        // only process the click if the current player isn't a computer player
        if ( ! (getCurrentPlayer() instanceof ComputerPlayer)) {
            // find out which column was clicked on...
            int c = myBoard.getCols();
            while ( (e.getX() < horizontalPos(c)) && (c > 0) ) {
                c--;
            }
            // and restart the gameplay recursion
            takeTurn( new Move(c, getCurrentPlayer()) );
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
//    
//    public void keyPressed(KeyEvent e) {
//        int keyCode = e.getKeyCode();
//        switch( keyCode ) { 
//            case KeyEvent.VK_UP:
//            	System.out.println("UP");
//                // handle up 
//                break;
//            case KeyEvent.VK_DOWN:
//            	System.out.println("DOWN");
//                // handle down 
//                break;
//            case KeyEvent.VK_LEFT:
//            	System.out.println("LEFT");
//                // handle left
//                break;
//            case KeyEvent.VK_RIGHT :
//            	System.out.println("RIGHT");
//                // handle right
//                break;
//         }
//    } 
    
//    public void keyTyped(KeyEvent e) {
//    }
//
//    public void keyPressed(KeyEvent e) {
//    }
//
//    public void keyReleased(KeyEvent e) {
//    	System.out.println(" key Released");
//        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//            System.out.println("Right key Released");
//        }
//        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//            System.out.println("Left key Released");
//        }
//    }

    
    
    ///// GRAPHICS
    /////
    public void paint( Graphics g ) {
        g.setColor( Color.BLUE );
        g.fillRect( 0, 0, myFrame.getWidth(), myFrame.getHeight() );
        Player cell;
        
        // draw column header (numbers and message)
        g.setColor(Color.white);
        g.drawString(message, pieceSize, headerHeight - 25);
        for (int c = 0; c < myBoard.getCols(); c++) {
            g.drawString(Integer.toString(c), horizontalPos(c) + pieceSize/2 - 4, headerHeight);
        }
        
        // draw pieces
        for(int r = 0; r < myBoard.getRows(); r++ ) {
            for(int c = 0; c < myBoard.getCols(); c++ ) {
                cell = myBoard.getCell(r, c);
                if (cell != null) {
                    drawPiece( g, r, c, cell.getColor() );
                } else {
                    drawPiece( g, r, c, Color.gray );
                }
            }
        }
    }
    

    
    //shows a piece at location row r and col c for given color
    private void drawPiece(Graphics g, int r, int c, Color color ) {
        //System.out.println(c.toString());
        g.setColor( color );
        g.fillOval( horizontalPos(c), verticalPos(r), pieceSize, pieceSize );

        g.setColor( Color.white );
        g.drawOval( horizontalPos(c) - 1, verticalPos(r) - 1, pieceSize + 1, pieceSize + 1 );

        //g.setColor( new Color( 128, 128, 0 ) );
        //g.drawOval( horizontalPos(c), verticalPos(r), pieceSize, pieceSize );
    }  
    
    // returns the horizontal pixel position of a given 0-based column index   
    private int horizontalPos(int c) {
        return (spacing +  c * (pieceSize + spacing));
    }
    
    // returns the vertical pixel position of a given 0-based row index   
    private int verticalPos(int r) {
        return (spacing + headerHeight + (myBoard.getRows() - r - 1) * (pieceSize + spacing)); 
    }
        
    

    // This method causes your program to pause for a certain number of milliseconds
    // Don't worry about the details of the class Thread, we won't cover it in this course.
    private void delay(int ms) {
        try {
            Thread.sleep (ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
    ////// Main
    //////
    public static void main( String[] args ){
        Connect4 connect4 = new Connect4(ROWS, COLS);
    }

}
