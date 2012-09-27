import java.util.HashSet;

/* A player is a collection of 8 pawns, 2 rooks, 2 bishops, 2 knights a king,
 * and a queen, all of the same color. They allow the Square objects to 
 * determine whether or not they are under attack, which thereby lets the 
 * program know whether or not the king can move to that square.
 */


public class Player {
	
	private HashSet<Piece> pieces = new HashSet<Piece>();
	private HashSet<Coord> squaresAttacked = new HashSet<Coord>();
	private Shade clr;
	private King k;
	
	public Player(Shade c, Chessboard cb){
		if (c == Shade.BLACK) {
			pieces.add(new Pawn(Shade.BLACK, new int[]{0,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{1,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{2,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{3,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{4,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{5,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{6,6},cb));
			pieces.add(new Pawn(Shade.BLACK, new int[]{7,6},cb));
			pieces.add(new Rook(Shade.BLACK, new int[]{0,7},cb));
			pieces.add(new Rook(Shade.BLACK, new int[]{7,7},cb));
			pieces.add(new Knight(Shade.BLACK, new int[]{1,7},cb));
			pieces.add(new Knight(Shade.BLACK, new int[]{6,7},cb));
			pieces.add(new Bishop(Shade.BLACK, new int[]{2,7},cb));
			pieces.add(new Bishop(Shade.BLACK, new int[]{5,7},cb));
			pieces.add(new Queen(Shade.BLACK, new int[]{3,7},cb));
			k = new King(Shade.BLACK, new int[]{4,7},cb);
			pieces.add(k);
		} else {
			pieces.add(new Pawn(Shade.WHITE, new int[]{0,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{1,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{2,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{3,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{4,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{5,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{6,1},cb));
			pieces.add(new Pawn(Shade.WHITE, new int[]{7,1},cb));
			pieces.add(new Rook(Shade.WHITE, new int[]{0,0},cb));
			pieces.add(new Rook(Shade.WHITE, new int[]{7,0},cb));
			pieces.add(new Knight(Shade.WHITE, new int[]{1,0},cb));
			pieces.add(new Knight(Shade.WHITE, new int[]{6,0},cb));
			pieces.add(new Bishop(Shade.WHITE, new int[]{2,0},cb));
			pieces.add(new Bishop(Shade.WHITE, new int[]{5,0},cb));
			pieces.add(new Queen(Shade.WHITE, new int[]{3,0},cb));
			k = new King(Shade.WHITE, new int[]{4,0},cb);
			pieces.add(k);
		}
		
		clr = c;
		
	}
	
	public Shade getPlayerShade(){
		return clr;
	}
	
	public HashSet<Coord> getSquaresAttacked(){
		squaresAttacked.clear();
		for (Piece p: pieces){
			squaresAttacked.addAll(p.getAttack());
		}
		return squaresAttacked;
	}
	
	public Coord getKingCoord(){
		return k.getCoord();
	}
	
	public void addPromotedPiece(Piece p){
		pieces.add(p);
	}
	
	public HashSet<Piece> getPieces(){
		return pieces;
	}
	
	public void clearPassant(){
		for(Piece pc: pieces){
			if (pc instanceof PassantShadow) ((PassantShadow) pc).remove();
		}
		return;
	}

}
