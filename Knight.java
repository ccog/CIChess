import java.util.HashSet;

import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class Knight extends Piece {
	
	/* The knight's move pattern is unique (read: simple) because it can only
	 * move in one of eight specific pathways, and these pathways exist whether
	 * or not there are pieces between the knight and its destination. Thus, 
	 * these movement vectors can be represented as a constant HashSet of Coord
	 * objects.
	 */
	private HashSet<Coord> moveSet = new HashSet<Coord>();
	
	public Knight(Shade clr, int[] startCoords, Chessboard cb){
		super(clr, startCoords, cb,"Knight");
		
		moveSet.add(new Coord(2,-1));
		moveSet.add(new Coord(2,1));
		moveSet.add(new Coord(-2,-1));
		moveSet.add(new Coord(-2,1));
		moveSet.add(new Coord(1,-2));
		moveSet.add(new Coord(1,2));
		moveSet.add(new Coord(-1,-2));
		moveSet.add(new Coord(-1,2));
		
		if (clr == Shade.WHITE){
			try{
				setIcon(new ImageIcon("Images/whiteKnight.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		} else {
			try{
				setIcon(new ImageIcon("Images/blackKnight.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		}
	}
	
	public boolean validMove(Coord cd){
		if (cd == null || !(c.getSquare(cd).canMove(this.color))) return false;
		if (coords.vectorTo(cd).equals(new Coord(0,0))) return false;
		if (moveSet.contains(coords.vectorTo(cd)))
			return true;
		else return false;
	}
	
	public HashSet<Coord> getAttack (){
		attack.clear();
		if (!(this.alive)) return attack;	
		if (inRange(this.coords.add(new Coord(2,-1)))) 
			attack.add(coords.add(new Coord(2,-1)));
		if (inRange(this.coords.add(new Coord(2,1)))) 
			attack.add(coords.add(new Coord(2,1)));
		if (inRange(this.coords.add(new Coord(-2,-1)))) 
			attack.add(coords.add(new Coord(-2,-1)));
		if (inRange(this.coords.add(new Coord(-2,1)))) 
			attack.add(coords.add(new Coord(-2,1)));
		if (inRange(this.coords.add(new Coord(1,-2)))) 
			attack.add(coords.add(new Coord(1,-2)));
		if (inRange(this.coords.add(new Coord(-1,-2)))) 
			attack.add(coords.add(new Coord(-1,-2)));
		if (inRange(this.coords.add(new Coord(1,2)))) 
			attack.add(coords.add(new Coord(1,2)));
		if (inRange(this.coords.add(new Coord(-1,2)))) 
			attack.add(coords.add(new Coord(-1,2)));
		
		return attack;
	}
	
	public String toString(){
		return (this.color + " Knight at " + c.getSquare(coords).toString());
	}

	//TODO: Implement squares attacked function

}
