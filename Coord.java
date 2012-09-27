
public class Coord {
	
	//To avoid any accidental slips in array notation and to preserve the 
	//invariant that coordinates on the board are int*int tuples, the pieces'
	//location on the board will be represented with this Coord type rather
	//than an int[] type.
	
	private int[] coord;
	
	//A constructor that takes in the desired start position
	public Coord (int x, int y){
		coord = new int[2];
		coord[0] = x;
		coord[1] = y;
	}
	
	//A method for setting the piece's location after it's been moved
	public void setCoords(Coord newPoint){
		coord[0] = newPoint.getFile();
		coord[1] = newPoint.getRank();
	}
	
	//A method for getting the file (column) of the piece, indexed 0-7 instead
	//of the standard a-h notation.
	public int getFile(){
		return coord[0];
	}
	
	//A method for getting the rank (row) of the piece, indexed 0-7 instead
	//of the standard 1-8 notation.
	public int getRank(){
		return coord[1];
	}
	
	//A method for determining the vector connecting another point to this one
	public Coord vectorTo(Coord newPoint){
		int x = newPoint.getFile() - this.getFile();
		int y = newPoint.getRank() - this.getRank();
		return new Coord(x,y);
	}
	
	/*A method that performs vector addition, vital to the function of 
	 * evaluating whether or not a square is attacked, and by which pieces.
	 */
	public Coord add(Coord move){
		int newx = coord[0] + move.getFile();
		int newy = coord[1] + move.getRank();
		return new Coord(newx, newy);
	}
	
	public int hashCode(){
		return (coord[0] * 100 + coord[1]);
	}
	
	public boolean equals (Object o){
		if (o instanceof Coord) {
			Coord c = (Coord) o;
			if (c.getFile() == this.getFile() &&
					c.getRank() == this.getRank())
				return true;
			else return false;
		} else {return false;}
	}

}
