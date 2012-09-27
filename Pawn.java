import java.util.HashSet;

import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class Pawn extends Piece {
	
	private HashSet<Coord> temp = new HashSet<Coord>();
	
	public Pawn(Shade clr, int[] startCoords, Chessboard cb){
		super(clr, startCoords, cb,"pawn");
		
		if (clr == Shade.WHITE){
			try{
				setIcon(new ImageIcon("Images/whitePawn.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		} else {
			try{
				setIcon(new ImageIcon("Images/blackPawn.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		}
	}
	
	public boolean validMove(Coord cd){
		if (cd == null || !(c.getSquare(cd).canMove(this.color))) return false;
		if (coords.vectorTo(cd).equals(new Coord(0,0))) return false;
		if (this.color == Shade.BLACK){
			if (coords.vectorTo(cd).equals(new Coord(0,-1))){
				if (c.getSquare(cd).isOccupied()) return false;
			} else if (coords.vectorTo(cd).equals(new Coord(0,-2))){
				if(!(this.coords.getRank() == 6)) return false;
				if (c.getSquare(cd).isOccupied()) return false;
				if (c.getSquare(new Coord(this.coords.getFile(),5)).isOccupied()) return false;
			} else if (coords.vectorTo(cd).equals(new Coord(1,-1))){
				if (!(c.getSquare(cd).isOccupied() || c.getSquare(cd).hasPassant())) return false;
				if (c.getSquare(cd).occupantShade() == this.color) return false;
			} else if (coords.vectorTo(cd).equals(new Coord(-1,-1))){
				if (!(c.getSquare(cd).isOccupied() || c.getSquare(cd).hasPassant())) return false;
				if (c.getSquare(cd).occupantShade() == this.color) return false;
			} else return false;
			
			return true;
			
		} else {
			if (coords.vectorTo(cd).equals(new Coord(0,1))){
				if (c.getSquare(cd).isOccupied()) return false;
			} else if (coords.vectorTo(cd).equals(new Coord(0,2))){
				if(!(this.coords.getRank() == 1)) return false;
				if (c.getSquare(cd).isOccupied()) return false;
				if (c.getSquare(new Coord(this.coords.getFile(),2)).isOccupied()) return false;
			} else if (coords.vectorTo(cd).equals(new Coord(1,1))){
				if (!(c.getSquare(cd).isOccupied() || c.getSquare(cd).hasPassant())) return false;
				if (c.getSquare(cd).occupantShade() == this.color) return false;				
			} else if (coords.vectorTo(cd).equals(new Coord(-1,1))){
				if (!(c.getSquare(cd).isOccupied() || c.getSquare(cd).hasPassant())) return false;
				if (c.getSquare(cd).occupantShade() == this.color) return false;				
			} else return false;
			
			return true;
		}
		
	}
	
	public HashSet<Coord> getAttack (){
		attack.clear();
		if (!(this.alive)) return attack;	
		if (color == Shade.BLACK){
			if (inRange(coords.add(new Coord(1,-1)))) 
				attack.add(coords.add(new Coord(1,-1)));
			if (inRange(coords.add(new Coord(-1,-1))))
				attack.add(coords.add(new Coord(-1,-1)));
		} else {
			if (inRange(coords.add(new Coord(1,1)))) 
				attack.add(coords.add(new Coord(1,1)));
			if (inRange(coords.add(new Coord(-1,1))))
				attack.add(coords.add(new Coord(-1,1)));
		}
		return attack;
	}
	
	@Override
	public HashSet<Coord> getMoves(){
		temp.clear();
		temp.addAll(this.getAttack());
		if(color == Shade.BLACK){
			if(inRange(coords.add(new Coord(0,-1))))
				temp.add(coords.add(new Coord(0,-1)));
			if(this.coords.getRank() == 6)
				temp.add(coords.add(new Coord(0,-2)));
		} else {
			if(inRange(coords.add(new Coord(0,1))))
				temp.add(coords.add(new Coord(0,1)));
			if(this.coords.getRank() == 1)
				temp.add(coords.add(new Coord(0,2)));
		}
		return temp;
	}
		
	public String toString(){
		return (this.color + " Pawn at " + c.getSquare(coords).toString());
	}
}
