import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


/* A Chessboard is a map between 64 Coord objects and 64 Square objects, and 
 * gives the pieces a way to reference the Squares in play. The chessboard
 * object is meant to execute large-scale events like the full movement of a
 * piece using smaller methods local to the pieces and squares they refer to.
 * It is also the main means by which squares and pieces communicate with one
 * another, as pieces are endowed only with Coord objects as state, which serve
 * as keys to Square objects.
 */

public class Chessboard{
	
	private HashMap<Coord,Square> chessbord = new HashMap<Coord,Square>();
	
	public Chessboard(){
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				if ((i+j)%2 == 0){
					chessbord.put(new Coord(i,j), new Square(Shade.BLACK,new Coord(i,j)));
				} else {
					chessbord.put(new Coord(i,j), new Square(Shade.WHITE,new Coord(i,j)));
				}
			}
		}
	}
	
	//A standard fetching method for the squares on the board
	public Square getSquare(Coord c){
		return chessbord.get(c);
	}
	
	//A method for clearing the pieces from the board, square-by-square
	public void clearBoard(){
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++){
				chessbord.get(new Coord(i,j)).clear();
			}
		}
	}
	
	/* A method that actually moves pieces across the board. Returns true if move
	 * was successful and false otherwise. Functions by first ensuring that the
	 * piece in reference is not null, and then goes on to use the piece's local
	 * validMove function to determine whether or not a move is legal. If the 
	 * piece referenced is a king, it checks to see whether or not the king 
	 * castled. If so, the function is called recursively on the accompanying rook.
	 * Enemy pieces are killed, and king and rook states are adjusted accordingly.
	 */
	
	public boolean movePiece(Piece p, Coord cd){
		if (p == null) return false;
		if (p.validMove(cd)) {
			if (p instanceof King){
				if (p.getCoord().vectorTo(cd).getFile() == 2){
					if (p.getShade() == Shade.BLACK){
						movePiece(chessbord.get(new Coord(7,7)).getOccupant(),new Coord(5,7));
					} else {
						movePiece(chessbord.get(new Coord(7,0)).getOccupant(),new Coord(5,0));
					}
				} else if (p.getCoord().vectorTo(cd).getFile() == -2){
					if (p.getShade() == Shade.BLACK){
						movePiece(chessbord.get(new Coord(0,7)).getOccupant(),new Coord(3,7));
					} else {
						movePiece(chessbord.get(new Coord(0,0)).getOccupant(),new Coord(3,0));
					}
				}
			}
			if (chessbord.get(cd).isOccupied() || chessbord.get(cd).hasPassant())
				chessbord.get(cd).getOccupant().kill();
			chessbord.get(p.getCoord()).clear();
			if (!(chessbord.get(cd).place(p)))
				throw new IllegalArgumentException("Error occurred in placing piece (see Square.place)");
			if(p instanceof Rook) ((Rook) p).moved();
			if(p instanceof King) ((King) p).moved();
			return true;
		} else return false;
	}
	
	//A method for clearing the attacks on the board, performed after each turn.	
	public void clearAttacks(){
		for(Square s:chessbord.values()){
			s.setBlackAttack(false);
			s.setWhiteAttack(false);
		}
	}
	
	//A test method for applying attacks, only used for JUnit tests and not in-game.
	public void applyAttack(Piece p){
		HashSet<Coord> attacks = new HashSet<Coord>();
		attacks = p.getAttack();
		if (p.getShade() == Shade.BLACK){
			for(Coord c:attacks){
				chessbord.get(c).setBlackAttack(true);
			}
		} else {
			for(Coord c:attacks){
				chessbord.get(c).setWhiteAttack(true);
			}
		}
	}
	
	//A method for evaluating which squares a given player is attacking, 
	//necessary for determining valid moves for the king, and other pieces
	//if the king is in check.
	public void applyAttack(Player p){
		clearAttacks();
		HashSet<Coord> attacks = new HashSet<Coord>();
		attacks = p.getSquaresAttacked();
		if (p.getPlayerShade() == Shade.BLACK){
			for(Coord cd:attacks){
				chessbord.get(cd).setBlackAttack(true);
			}
		} else {
			for(Coord cd:attacks){
				chessbord.get(cd).setWhiteAttack(true);
			}
		}
		
	}
	
	//A method that returns the set of pieces on the board for purposes of iteration.
	public Collection<Square> getAllSquares(){
		return chessbord.values();
	}
	
}
