import java.util.HashSet;

import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class Rook extends Piece {
	
	private boolean canCastle;
	
	public Rook(Shade clr, int[] startCoords, Chessboard cb){
		super(clr, startCoords, cb,"Rook");
		canCastle = true;
		
		if (clr == Shade.WHITE){
			try{
				setIcon(new ImageIcon("Images/whiteRook.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		} else {
			try{
				setIcon(new ImageIcon("Images/blackRook.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		}
	}
	
	public boolean validMove(Coord cd){
		if (cd == null || !(c.getSquare(cd).canMove(this.color))) return false;
		if (coords.vectorTo(cd).equals(new Coord(0,0))) return false;
		if (coords.vectorTo(cd).getFile() == 0){
			for (int x = (Math.min(coords.getRank(), cd.getRank()) + 1);
					x < Math.max(coords.getRank(), cd.getRank()); x++){
				if (c.getSquare(new Coord(coords.getFile(),x)).isOccupied())
					return false;
			}
			return true;
		} else if(coords.vectorTo(cd).getRank() == 0) {
			for (int x = (Math.min(coords.getFile(), cd.getFile()) + 1);
					x < Math.max(coords.getFile(), cd.getFile()); x++){
				if (c.getSquare(new Coord(x, coords.getRank())).isOccupied())
					return false;
			}
			return true;
			
		} else return false;
	}
	
	//A method to be called when the rook is actually moved, implying it can no longer be castled.
	public void moved(){
		canCastle = false;
	}
	
	public boolean canCastle(){
		return canCastle;
	}
	
	public HashSet<Coord> getAttack (){
		attack.clear();
		if (!(this.alive)) return attack;	
		for (int i = 1; i < 7; i++){
			if (!(inRange(this.coords.add(new Coord(0,i))))){
				break;
			} else {
				if (c.getSquare(this.coords.add(new Coord(0,i))).isOccupied()) {
					attack.add(this.coords.add(new Coord(0,i)));
					break;
				} else {
					attack.add(this.coords.add(new Coord(0,i)));
				}
			}
		}
		
		for (int i = 1; i < 7; i++){
			if (!(inRange(this.coords.add(new Coord(0,-i))))){
				break;
			} else {
				if (c.getSquare(this.coords.add(new Coord(0,-i))).isOccupied()) {
					attack.add(this.coords.add(new Coord(0,-i)));
					break;
				} else {
					attack.add(this.coords.add(new Coord(0,-i)));
				}
			}
		}
		
		for (int i = 1; i < 7; i++){
			if (!(inRange(this.coords.add(new Coord(i,0))))){
				break;
			} else {
				if (c.getSquare(this.coords.add(new Coord(i,0))).isOccupied()) {
					attack.add(this.coords.add(new Coord(i,0)));
					break;
				} else {
					attack.add(this.coords.add(new Coord(i,0)));
				}
			}
		}
		
		for (int i = 1; i < 7; i++){
			if (!(inRange(this.coords.add(new Coord(-i,0))))){
				break;
			} else {
				if (c.getSquare(this.coords.add(new Coord(-i,0))).isOccupied()) {
					attack.add(this.coords.add(new Coord(-i,0)));
					break;
				} else {
					attack.add(this.coords.add(new Coord(-i,0)));
				}
			}
		}
		
		return attack;
	}
	
	public String toString(){
		return (this.color + " Rook at " + c.getSquare(coords).toString());
	}
	
	//TODO: Implement squares attacked function

}
