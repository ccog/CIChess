import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


//Squares are objects that contain 

@SuppressWarnings("serial")
public class Square extends JPanel{
	
	private Piece occupant;
	private Shade clr;
	private Coord location;
	private boolean underBlackAttack;
	private boolean underWhiteAttack;
	
	/* The constructor verifies there is no piece occupying the square, sets the
	 * color of the square, and initializes the square as not under attack, 
	 * though this will likely change when the pieces are placed.
	 */ 
	public Square(Shade shade, Coord place){
		location = place;
		occupant = null;
		clr = shade;
		underBlackAttack = false;
		underWhiteAttack = false;
		
		if(clr == Shade.BLACK) setBackground(Color.gray);
		else setBackground(Color.white);
		setPreferredSize(new Dimension(75,75));
	}
	
	//Method that returns the color of the square for the purpose of drawing
	public Shade getShade(){
		return clr;
	} 
	
	/* Returns true if the square is empty, or if there is an occupant of the 
	 * square not of the same color as the inquiring piece, allowing for a take
	 */
	public boolean canMove(Shade inquirer){
		if (occupant == null || occupant.getShade() != inquirer) return true;
		else return false;
	}
	
	/* A method that determines whether or not a square is occupied, preventing
	 * rooks, bishops, queens, and pawns from hopping over squares with opposing
	 * pieces.
	 */
	public boolean isOccupied(){
		if (occupant instanceof PassantShadow) return false;
		else if (!(occupant == null)) return true;
		else return false;
	}
	
	public boolean hasPassant(){
		if(occupant == null) return false;
		else if (occupant instanceof PassantShadow) return true;
		else return false;
	}
	
	public Piece getOccupant(){
		return occupant;
	}
	
	public Coord getCoords(){
		return location;
	}
	
	/* Attempts to move a piece to the square, but fails if the square is 
	 * occupied by any piece. The main event loop of the program will clear
	 * the square before a piece is placed. This method involves the change
	 * of state of both the piece and the square.
	 */
	public boolean place(Piece p){
		if (occupant == null || occupant instanceof PassantShadow){
			occupant = p;
			p.put(this.location);
			return true;
		}
		else return false;
	}
	
	/* The method used to clear a square after a piece is moved from it or
	 * before a piece is moved to it once the piece occupying the square has 
	 * been taken.
	 */
	public void clear(){
		occupant = null;
	}
	
	/* Returns the color of the occupying piece, unsure if needed yet.
	 */
	public Shade occupantShade(){
		if (occupant == null) return null;
		else return occupant.getShade();
	}
	
	//A pair of methods for updating the state, to be used in conjunction with a yet-to-
	//be-written function evaluating which will determine whether a square is under attack
	public void setBlackAttack(boolean value){
		underBlackAttack = value;
	}
	
	public void setWhiteAttack(boolean value){
		underWhiteAttack = value;
	}
	
	/* A method that, given the color of the inquiring king, will return whether 
	 *or not the opposite color is attacking the square. Necessary for 
	 *determining the validity of king moves and castling.
	 */
	public boolean isAttacked(Shade king){
		if (king == Shade.BLACK) return underWhiteAttack;
		else return underBlackAttack;
	}
	
	public String toString(){
		String returnable = "(" + location.getFile() + "," + location.getRank() + ")";
		return returnable;
	}

}
