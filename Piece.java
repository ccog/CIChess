import java.util.HashSet;

import javax.swing.JLabel;


//Piece is the superclass of each respective piece on the chess board, and is never itself instantiated
@SuppressWarnings("serial")
public class Piece extends JLabel{
	
	protected Shade color;
	protected Coord coords;
	//A set containing the possible move vectors along which a piece can legally travel
	protected boolean alive = true;
	protected Chessboard c;
	protected HashSet<Coord> attack;
	
	//A method that returns the color of the piece in question
	public Shade getShade(){
		return color;
	}
	
	/* A method that returns the present numerical coordinates of the piece on
	 * the board. The return type is a Coord object.
	 */
	public Coord getCoord(){
		return coords;
	}
	
	//A method for updating the coordinates of a piece
	public void put(Coord cd){
		coords = cd;
	}
	
	//A method that returns whether or not the piece is alive and still in play.
	public boolean isAlive(){
		return alive;
	}
	
	/* A method that, given a new coordinate for the piece, checks to see whether
	 * said new coordinate is a valid move. If the move is valid, the method
	 * returns true. Otherwise, it will return false.
	 * 
	 * Valid moves involve placing a piece on a square not occupied by a piece
	 * of the same color. For rooks, queens, bishops, and sometimes pawns, this
	 * also means that no intervening squares between the initial and final 
	 * squares may be occupied. For kings, the square in question must also 
	 * not be under attack by any opposing pieces.
	 */	
	public boolean validMove(Coord c){
		//Stub to be overridden
		return false;
	}
	
	//A method that kills the piece in question
	public void kill(){
		alive = false;
		c.getSquare(coords).clear();
	}
	
	//A method for reviving pieces after the checks that occur in the main event loop
	public void revive(){
		alive = true;
		c.getSquare(coords).place(this);
	}
	
	/*Constructor assigns color, start coordinates, and the chessboard context.
	 *It also attempts to place the piece, and raises an exception if the piece
	 *cannot be placed
	 */
	public Piece(Shade clr, int[] startCoords, Chessboard cb,String label) throws IllegalArgumentException{
		super(clr + label);
		color = clr;
		coords = new Coord(startCoords[0],startCoords[1]);
		c = cb;
		if(!(c.getSquare(this.coords).place(this)))
			throw new IllegalArgumentException("Piece " + this.toString() + " could not be placed.");
		attack = new HashSet<Coord>();
		/*try {
		    img = ImageIO.read(new File("strawberry.jpg"));
		} catch (IOException e) {
		}*/
	}
	
	//A method that returns a hashset containing all of the squares that a given piece is attacking
	public HashSet<Coord> getAttack(){
		return new HashSet<Coord>();
	}
	
	//A redundant method only in place to be overridden by the Pawn, whose moves are different than the squares it attacks
	public HashSet<Coord> getMoves(){
		return this.getAttack();
	}
	
	//A helper method for determining whether or not a given piece trajectory has left the board
	protected boolean inRange(Coord cd){
		if (cd.getFile() < 8 && cd.getFile() >= 0 &&
				cd.getRank() < 8 && cd.getRank() >= 0) return true;
		else return false;
	}

}
