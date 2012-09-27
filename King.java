import java.util.HashSet;

import javax.swing.ImageIcon;


@SuppressWarnings("serial")
public class King extends Piece {
	
	//private boolean inCheck;
	private boolean canCastle;
	
	public King(Shade clr, int[] startCoords, Chessboard cb){
		super(clr, startCoords, cb,"KING");
		//inCheck = false;
		canCastle = true;
		if (clr == Shade.WHITE){
			try{
				setIcon(new ImageIcon("Images/whiteKing.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		} else {
			try{
				setIcon(new ImageIcon("Images/blackKing.png"));
				setText("");
			} catch (Exception e){
				//Keep label as-is if image not found
			}
		}
	}
	
	public boolean validMove(Coord cd){
		if (cd == null || !(c.getSquare(cd).canMove(this.color))) return false;
		if (coords.vectorTo(cd).equals(new Coord(0,0))) return false;
		if (c.getSquare(cd).isAttacked(this.color)) return false;
		if (coords.vectorTo(cd).equals(new Coord(2,0))){
			if (this.color == Shade.BLACK){
				if ((c.getSquare(new Coord (7,7)).getOccupant() instanceof Rook)){
					if (this.canCastle
							&& ((Rook) c.getSquare(new Coord(7,7)).getOccupant()).canCastle()){
						if (!(c.getSquare(new Coord(5,7)).isOccupied()
								|| c.getSquare(new Coord(6,7)).isOccupied())){
							if(!(c.getSquare(new Coord(5,7)).isAttacked(this.color)
									|| c.getSquare(new Coord(6,7)).isAttacked(this.color)
									|| c.getSquare(this.coords).isAttacked(this.color))){
								return true;
							} else return false;
						} else return false;
					} else return false;
				} else return false;
			} else {
				if ((c.getSquare(new Coord (7,0)).getOccupant() instanceof Rook)){
					if (this.canCastle
							&& ((Rook) c.getSquare(new Coord(7,0)).getOccupant()).canCastle()){
						if (!(c.getSquare(new Coord(5,0)).isOccupied()
								|| c.getSquare(new Coord(6,0)).isOccupied())){
							if(!(c.getSquare(new Coord(5,0)).isAttacked(this.color)
									|| c.getSquare(new Coord(6,0)).isAttacked(this.color)
									|| c.getSquare(this.coords).isAttacked(this.color))){
								return true;
							} else return false;
						} else return false;
					} else return false;
				} else return false;
			}
		} else if (coords.vectorTo(cd).equals(new Coord(-2,0))){
			if (this.color == Shade.BLACK){
				if ((c.getSquare(new Coord (0,7)).getOccupant() instanceof Rook)){
					if (this.canCastle &&
							((Rook) c.getSquare(new Coord(0,7)).getOccupant()).canCastle()){
						if (!(c.getSquare(new Coord(1,7)).isOccupied() ||
								c.getSquare(new Coord(2,7)).isOccupied()
								|| c.getSquare(new Coord(3,7)).isOccupied())){
							if(!(c.getSquare(new Coord(1,7)).isAttacked(this.color)
									|| c.getSquare(new Coord(2,7)).isAttacked(this.color)
									|| c.getSquare(new Coord(3,7)).isAttacked(this.color)
									|| c.getSquare(this.coords).isAttacked(this.color))){
								return true;
							} else return false;
						} else return false;
					} else return false;
				} else return false;
			} else {
				if ((c.getSquare(new Coord (0,0)).getOccupant() instanceof Rook)){
					if (this.canCastle &&
							((Rook) c.getSquare(new Coord(0,0)).getOccupant()).canCastle()){
						if (!(c.getSquare(new Coord(1,0)).isOccupied() ||
								c.getSquare(new Coord(2,0)).isOccupied()
								|| c.getSquare(new Coord(3,0)).isOccupied())){
							if(!(c.getSquare(new Coord(1,0)).isAttacked(this.color)
									|| c.getSquare(new Coord(2,0)).isAttacked(this.color)
									|| c.getSquare(new Coord(3,0)).isAttacked(this.color)
									|| c.getSquare(this.coords).isAttacked(this.color))){
								return true;
							} else return false;
						} else return false;
					} else return false;
				} else return false;
			}
		}
		if (Math.abs(coords.vectorTo(cd).getFile()) > 1 ||
				Math.abs(coords.vectorTo(cd).getRank()) > 1) return false;
		return true;
	}
	
	public void moved(){
		canCastle = false;
	}
	
	public HashSet<Coord> getAttack (){
		attack.clear();
		if (!(this.alive)) return attack;	
		if (inRange(this.coords.add(new Coord(0,1)))) 
			attack.add(coords.add(new Coord(0,1)));
		if (inRange(this.coords.add(new Coord(1,1)))) 
			attack.add(coords.add(new Coord(1,1)));
		if (inRange(this.coords.add(new Coord(1,0)))) 
			attack.add(coords.add(new Coord(1,0)));
		if (inRange(this.coords.add(new Coord(1,-1)))) 
			attack.add(coords.add(new Coord(1,-1)));
		if (inRange(this.coords.add(new Coord(0,-1)))) 
			attack.add(coords.add(new Coord(0,-1)));
		if (inRange(this.coords.add(new Coord(-1,-1)))) 
			attack.add(coords.add(new Coord(-1,-1)));
		if (inRange(this.coords.add(new Coord(-1,0)))) 
			attack.add(coords.add(new Coord(-1,0)));
		if (inRange(this.coords.add(new Coord(-1,1)))) 
			attack.add(coords.add(new Coord(-1,1)));
		
		return attack;
	}
	
	public boolean canCastle(){
		return canCastle;
	}
	
	public String toString(){
		return (this.color + " King at " + c.getSquare(coords).toString());
	}
	
}
