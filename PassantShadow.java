/* A PassantShadow is simply a phantom piece that exists only for one turn
 * that allows pawns to capture via en passant.
 */

@SuppressWarnings("serial")
public class PassantShadow extends Piece{

	//The pawn to which the shadow is tied
	Pawn pass;
	
	public PassantShadow(Shade clr, int[] startCoords, Chessboard cb,
			Pawn p) throws IllegalArgumentException {
		super(clr, startCoords, cb, "");
		pass = p;
	}
	
	//A method killing this shadow as well as the it shadows
	public void kill(){
		alive = false;
		c.getSquare(coords).clear();
		pass.kill();
	}
	 
	//A method killing this shadow as a means of tying loose ends
	public void remove(){
		alive = false;
	}
	
	//A special override of the main revive function to revive the pawn to which it is tied, as well.
	public void revive(){
		alive = true;
		c.getSquare(coords).place(this);
		pass.revive();
	}

}
