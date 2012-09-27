import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;


@SuppressWarnings("unused")
public class ChessTest {
	
	@Test public void chessBoardSetupTest(){
		Chessboard c = new Chessboard();
		assertTrue("An error occurred in the setup process",true);
	}
	
	@Test public void playerInitializationTest(){
		Chessboard c = new Chessboard();
		Player p1 = new Player(Shade.WHITE,c);
		Player p2 = new Player(Shade.BLACK,c);
		try {
			Player p3 = new Player(Shade.WHITE,c);
			fail("Shouldn't be here, third player should fail");
		} catch (Exception e) {
			if (e instanceof IllegalArgumentException) assertTrue(true);
		}
	}
	
	@Test public void constructorTests(){
		Chessboard c = new Chessboard();
		Pawn p = new Pawn(Shade.BLACK,new int[]{0,0},c);
		Bishop b = new Bishop(Shade.BLACK,new int[]{1,0},c);
		Knight n = new Knight(Shade.BLACK,new int[]{2,0},c);
		Rook r = new Rook(Shade.BLACK,new int[]{3,0},c);
		King k = new King(Shade.BLACK,new int[]{4,0},c);
		Queen q = new Queen(Shade.WHITE,new int[]{0,4},c);
		assertTrue("Just trying to set up some pieces is all",true);
	}
	
	@Test public void squareMethodTests(){
		Chessboard c = new Chessboard();
		assertFalse("See if empty square is empty",c.getSquare(new Coord(0,0)).isOccupied());
		assertFalse(c.getSquare(new Coord(0,0)).isAttacked(Shade.WHITE));
		assertFalse(c.getSquare(new Coord(0,0)).isAttacked(Shade.BLACK));
		assertEquals("Occupant color should be null",null,c.getSquare(new Coord(0,0)).occupantShade());
		assertTrue("CanMove on empty square should return true",c.getSquare(new Coord(0,0)).canMove(Shade.WHITE));
		assertTrue("CanMove on empty square should return true",c.getSquare(new Coord(0,0)).canMove(Shade.BLACK));
		Pawn p = new Pawn(Shade.BLACK,new int[]{0,0},c);
		assertTrue("CanMove on empty square should return true",c.getSquare(new Coord(0,0)).canMove(Shade.WHITE));
		assertFalse("CanMove on empty square should return true",c.getSquare(new Coord(0,0)).canMove(Shade.BLACK));
		assertTrue("See if pawn square is occupied",c.getSquare(new Coord(0,0)).isOccupied());
		assertEquals("See if get color is correct",Shade.BLACK,c.getSquare(new Coord(0,0)).occupantShade());
		assertEquals("See if square knows its color",Shade.BLACK,c.getSquare(new Coord(0,0)).getShade());
		c.getSquare(new Coord(0,0)).clear();
		assertFalse("See if empty square is empty: PART DEUX",c.getSquare(new Coord(0,0)).isOccupied());
		c.getSquare(new Coord(0,0)).setBlackAttack(true);
		c.getSquare(new Coord(0,0)).setWhiteAttack(true);
		assertTrue(c.getSquare(new Coord(0,0)).isAttacked(Shade.BLACK));
		assertTrue(c.getSquare(new Coord(0,0)).isAttacked(Shade.WHITE));
	}
	
	@Test public void pieceMethodTests(){
		Chessboard c = new Chessboard();
		Pawn p = new Pawn(Shade.WHITE,new int[]{0,0},c);
		assertEquals(Shade.WHITE,p.getShade());
		assertEquals(new Coord(0,0),p.getCoord());
		assertTrue(p.isAlive());
		p.put(new Coord(0,1));
		assertEquals(new Coord(0,1),p.getCoord());
		p.kill();
		assertFalse(p.isAlive());
	}
	
	@Test public void whitePawnMoveTests(){
		Chessboard c = new Chessboard();
		Pawn p = new Pawn(Shade.WHITE,new int[]{4,1},c); //New pawn on e2
		assertFalse("Very wrong move",p.validMove(new Coord(0,0)));
		assertFalse("Backwards move",p.validMove(new Coord(4,0)));
		assertFalse("Sideways move",p.validMove(new Coord(3,1)));
		Knight n = new Knight(Shade.BLACK,new int[]{4,2},c);
		assertFalse("Single square forward",p.validMove(new Coord(4,2)));
		assertFalse("Two squares forward",p.validMove(new Coord(4,3)));
		c.getSquare(new Coord(4,2)).clear();
		n.kill();
		assertTrue("Single square forward",p.validMove(new Coord(4,2)));
		assertTrue("Two squares forward",p.validMove(new Coord(4,3)));
		Queen q = new Queen(Shade.BLACK,new int[]{3,2},c); //Make a new Queen
		assertTrue("One square forward still valid",p.validMove(new Coord(4,2)));
		assertTrue("TAKE THE QUEEN",p.validMove(new Coord(3,2)));
		p.put(new Coord(3,2)); //Move the pawn
		assertFalse("Can't move forward two squares anymore",p.validMove(new Coord(3,4)));
		assertTrue("Can still move forward one square",p.validMove(new Coord(3,3)));
		assertFalse(p.validMove(p.getCoord()));
	}
	
	@Test public void blackPawnMoveTests(){
		Chessboard c = new Chessboard();
		Pawn p = new Pawn(Shade.BLACK,new int[]{4,6},c); //New pawn on e2
		assertFalse("Very wrong move",p.validMove(new Coord(0,7)));
		assertFalse("Backwards move",p.validMove(new Coord(4,7)));
		assertFalse("Sideways move",p.validMove(new Coord(3,7)));
		Knight n = new Knight(Shade.BLACK,new int[]{4,5},c);
		assertFalse("Single square forward",p.validMove(new Coord(4,5)));
		assertFalse("Two squares forward",p.validMove(new Coord(4,4)));
		n.kill();
		assertTrue("Single square forward",p.validMove(new Coord(4,5)));
		assertTrue("Two squares forward",p.validMove(new Coord(4,4)));
		Queen q = new Queen(Shade.WHITE,new int[]{3,5},c); //Make a new Queen
		assertTrue("One square forward still valid",p.validMove(new Coord(4,5)));
		assertTrue("TAKE THE QUEEN",p.validMove(new Coord(3,5)));
		p.put(new Coord(3,5)); //Move the pawn
		assertFalse("Can't move forward two squares anymore",p.validMove(new Coord(3,3)));
		assertTrue("Can still move forward one square",p.validMove(new Coord(3,4)));
		assertFalse(p.validMove(p.getCoord()));
	}
	
	@Test public void rookMoveTests(){
		Chessboard c = new Chessboard();
		Rook r = new Rook(Shade.BLACK,new int[]{0,0},c);
		assertTrue(r.validMove(new Coord(0,7)));
		assertTrue(r.validMove(new Coord(0,5)));
		assertTrue(r.validMove(new Coord(0,3)));
		assertTrue(r.validMove(new Coord(0,1)));
		assertTrue(r.validMove(new Coord(1,0)));
		assertTrue(r.validMove(new Coord(3,0)));
		assertTrue(r.validMove(new Coord(5,0)));
		assertTrue(r.validMove(new Coord(7,0)));
		assertFalse(r.validMove(new Coord(1,1)));
		assertFalse(r.validMove(new Coord(3,1)));
		assertFalse(r.validMove(new Coord(1,3)));
		Knight n = new Knight(Shade.WHITE,new int[]{3,0},c);
		assertFalse(r.validMove(new Coord(4,0)));
		assertTrue(r.validMove(new Coord(3,0)));
		assertTrue(r.validMove(new Coord(2,0)));
		Bishop b = new Bishop(Shade.BLACK,new int[]{0,3},c);
		assertFalse(r.validMove(new Coord(0,4)));
		assertFalse(r.validMove(new Coord(0,3)));
		assertTrue(r.validMove(new Coord(0,2)));
		assertFalse(r.validMove(new Coord(4,0)));
		assertTrue(r.validMove(new Coord(3,0)));
		assertFalse(r.validMove(new Coord(2,1)));
		c.getSquare(new Coord(0,0)).clear();
		c.getSquare(new Coord(3,0)).clear();
		c.getSquare(new Coord(0,3)).clear();
		Rook r2 = new Rook(Shade.WHITE,new int[]{7,7},c);
		assertTrue(r2.validMove(new Coord(7,6)));
		assertTrue(r2.validMove(new Coord(7,4)));
		assertTrue(r2.validMove(new Coord(7,3)));
		assertTrue(r2.validMove(new Coord(7,1)));
		assertTrue(r2.validMove(new Coord(1,7)));
		assertTrue(r2.validMove(new Coord(3,7)));
		assertTrue(r2.validMove(new Coord(6,7)));
		assertFalse(r2.validMove(new Coord(1,1)));
		assertFalse(r2.validMove(new Coord(3,1)));
		assertFalse(r2.validMove(new Coord(1,3)));
		Knight n2 = new Knight(Shade.WHITE,new int[]{7,3},c);
		Bishop b2 = new Bishop(Shade.BLACK,new int[]{5,7},c);
		assertTrue(r2.validMove(new Coord(6,7)));
		assertTrue(r2.validMove(new Coord(5,7)));
		assertFalse(r2.validMove(new Coord(4,7)));
		assertTrue(r2.validMove(new Coord(7,4)));
		assertFalse(r2.validMove(new Coord(7,3)));
		assertFalse(r2.validMove(new Coord(7,2)));
		assertFalse(r2.validMove(r2.getCoord()));
	}
	
	@Test public void knightMoveTests(){
		Chessboard c = new Chessboard();
		Knight n = new Knight(Shade.BLACK,new int[]{3,3},c);
		assertTrue(n.validMove(new Coord(4,5)));
		assertTrue(n.validMove(new Coord(5,4)));
		assertTrue(n.validMove(new Coord(5,2)));
		assertTrue(n.validMove(new Coord(4,1)));
		assertTrue(n.validMove(new Coord(2,1)));
		assertTrue(n.validMove(new Coord(1,2)));
		assertTrue(n.validMove(new Coord(1,4)));
		assertTrue(n.validMove(new Coord(2,5)));
		assertFalse(n.validMove(new Coord(2,0)));
		Bishop b = new Bishop(Shade.BLACK,new int[]{4,5},c);
		Pawn p = new Pawn(Shade.WHITE,new int[]{2,5},c);
		assertFalse(n.validMove(new Coord(4,5)));
		assertTrue(n.validMove(new Coord(2,5)));
		assertFalse(n.validMove(n.getCoord()));
	}
	
	@Test public void bishopMoveTests(){
		Chessboard c = new Chessboard();
		Bishop b = new Bishop(Shade.WHITE,new int[]{3,3},c);
		//Up to the right
		assertTrue(b.validMove(new Coord(4,4)));
		assertTrue(b.validMove(new Coord(5,5)));
		assertTrue(b.validMove(new Coord(6,6)));
		assertTrue(b.validMove(new Coord(7,7)));
		//Down to the left
		assertTrue(b.validMove(new Coord(2,2)));
		assertTrue(b.validMove(new Coord(1,1)));
		assertTrue(b.validMove(new Coord(0,0)));
		//Down to the right
		assertTrue(b.validMove(new Coord(4,2)));
		assertTrue(b.validMove(new Coord(5,1)));
		assertTrue(b.validMove(new Coord(6,0)));
		//Up to the left
		assertTrue(b.validMove(new Coord(2,4)));
		assertTrue(b.validMove(new Coord(1,5)));
		assertTrue(b.validMove(new Coord(0,6)));
		Knight n = new Knight(Shade.WHITE,new int[]{1,5},c);
		Rook r = new Rook(Shade.BLACK,new int[]{5,5},c);
		assertTrue(b.validMove(new Coord(4,4)));
		assertTrue(b.validMove(new Coord(5,5)));
		assertFalse(b.validMove(new Coord(6,6)));
		assertTrue(b.validMove(new Coord(2,4)));
		assertFalse(b.validMove(new Coord(1,5)));
		assertFalse(b.validMove(new Coord(0,6)));		
		b.kill();	n.kill();
		Bishop b2 = new Bishop(Shade.BLACK,new int[]{6,6},c);
		assertTrue(b2.validMove(new Coord(7,7)));
		assertTrue(b2.validMove(new Coord(5,7)));
		assertTrue(b2.validMove(new Coord(7,5)));
		assertFalse(b2.validMove(new Coord(4,4)));
		assertFalse(b2.validMove(new Coord(5,5)));
		assertFalse(b2.validMove(b2.getCoord()));
	}
	
	@Test public void queenMoveTests(){
		Chessboard c = new Chessboard();
		Queen q = new Queen(Shade.WHITE,new int[]{3,3},c);
		//Up to the right
		assertTrue(q.validMove(new Coord(4,4)));
		assertTrue(q.validMove(new Coord(5,5)));
		assertTrue(q.validMove(new Coord(6,6)));
		assertTrue(q.validMove(new Coord(7,7)));
		//Down to the left
		assertTrue(q.validMove(new Coord(2,2)));
		assertTrue(q.validMove(new Coord(1,1)));
		assertTrue(q.validMove(new Coord(0,0)));
		//Down to the right
		assertTrue(q.validMove(new Coord(4,2)));
		assertTrue(q.validMove(new Coord(5,1)));
		assertTrue(q.validMove(new Coord(6,0)));
		//Up to the left
		assertTrue(q.validMove(new Coord(2,4)));
		assertTrue(q.validMove(new Coord(1,5)));
		assertTrue(q.validMove(new Coord(0,6)));
		Knight n = new Knight(Shade.WHITE,new int[]{1,5},c);
		Rook r = new Rook(Shade.BLACK,new int[]{5,5},c);
		assertTrue(q.validMove(new Coord(4,4)));
		assertTrue(q.validMove(new Coord(5,5)));
		assertFalse(q.validMove(new Coord(6,6)));
		assertTrue(q.validMove(new Coord(2,4)));
		assertFalse(q.validMove(new Coord(1,5)));
		assertFalse(q.validMove(new Coord(0,6)));		
		q.kill();	n.kill();
		Queen q2 = new Queen(Shade.BLACK,new int[]{6,6},c);
		assertTrue(q2.validMove(new Coord(7,7)));
		assertTrue(q2.validMove(new Coord(5,7)));
		assertTrue(q2.validMove(new Coord(7,5)));
		assertFalse(q2.validMove(new Coord(4,4)));
		assertFalse(q2.validMove(new Coord(5,5)));
		q2.kill(); r.kill();
		Queen q3 = new Queen(Shade.BLACK,new int[]{0,0},c);
		assertTrue(q3.validMove(new Coord(0,7)));
		assertTrue(q3.validMove(new Coord(0,5)));
		assertTrue(q3.validMove(new Coord(0,3)));
		assertTrue(q3.validMove(new Coord(0,1)));
		assertTrue(q3.validMove(new Coord(1,0)));
		assertTrue(q3.validMove(new Coord(3,0)));
		assertTrue(q3.validMove(new Coord(5,0)));
		assertTrue(q3.validMove(new Coord(7,0)));
		assertFalse(q3.validMove(new Coord(3,1)));
		assertFalse(q3.validMove(new Coord(1,3)));
		Knight n1 = new Knight(Shade.WHITE,new int[]{3,0},c);
		assertFalse(q3.validMove(new Coord(4,0)));
		assertTrue(q3.validMove(new Coord(3,0)));
		assertTrue(q3.validMove(new Coord(2,0)));
		Bishop b = new Bishop(Shade.BLACK,new int[]{0,3},c);
		assertFalse(q3.validMove(new Coord(0,4)));
		assertFalse(q3.validMove(new Coord(0,3)));
		assertTrue(q3.validMove(new Coord(0,2)));
		assertFalse(q3.validMove(new Coord(4,0)));
		assertTrue(q3.validMove(new Coord(3,0)));
		assertFalse(q3.validMove(new Coord(2,1)));
		q3.kill();	b.kill();	n1.kill();
		Queen q4 = new Queen(Shade.WHITE,new int[]{7,7},c);
		assertTrue(q4.validMove(new Coord(7,6)));
		assertTrue(q4.validMove(new Coord(7,4)));
		assertTrue(q4.validMove(new Coord(7,3)));
		assertTrue(q4.validMove(new Coord(7,1)));
		assertTrue(q4.validMove(new Coord(1,7)));
		assertTrue(q4.validMove(new Coord(3,7)));
		assertTrue(q4.validMove(new Coord(6,7)));
		assertTrue(q4.validMove(new Coord(1,1)));
		assertFalse(q4.validMove(new Coord(3,1)));
		assertFalse(q4.validMove(new Coord(1,3)));
		Knight n2 = new Knight(Shade.WHITE,new int[]{7,3},c);
		Bishop b2 = new Bishop(Shade.BLACK,new int[]{5,7},c);
		assertTrue(q4.validMove(new Coord(6,7)));
		assertTrue(q4.validMove(new Coord(5,7)));
		assertFalse(q4.validMove(new Coord(4,7)));
		assertTrue(q4.validMove(new Coord(7,4)));
		assertFalse(q4.validMove(new Coord(7,3)));
		assertFalse(q4.validMove(new Coord(7,2)));
		assertFalse(q4.validMove(q4.getCoord()));
	}

	@Test public void kingMoveTests(){
		Chessboard c = new Chessboard();
		King k = new King(Shade.WHITE,new int[]{3,3},c);
		assertTrue(k.validMove(new Coord(3,4)));
		assertTrue(k.validMove(new Coord(4,4)));
		assertTrue(k.validMove(new Coord(4,3)));
		assertTrue(k.validMove(new Coord(4,2)));
		assertTrue(k.validMove(new Coord(3,2)));
		assertTrue(k.validMove(new Coord(2,2)));
		assertTrue(k.validMove(new Coord(2,3)));
		assertTrue(k.validMove(new Coord(2,4)));
		assertFalse(k.validMove(new Coord(3,5)));
		assertFalse(k.validMove(new Coord(5,3)));
		assertFalse(k.validMove(new Coord(5,5)));
		assertFalse(k.validMove(new Coord(5,1)));
		assertFalse(k.validMove(new Coord(3,1)));
		assertFalse(k.validMove(new Coord(1,1)));
		assertFalse(k.validMove(new Coord(1,3)));
		assertFalse(k.validMove(new Coord(5,3)));
		Pawn p = new Pawn(Shade.WHITE,new int[]{4,3},c);
		Rook r = new Rook(Shade.BLACK,new int[]{4,4},c);
		assertTrue(k.validMove(new Coord(4,4)));
		assertFalse(k.validMove(new Coord(4,3)));
		c.getSquare(new Coord(3,4)).setBlackAttack(true);
		c.getSquare(new Coord(2,4)).setBlackAttack(true);
		assertFalse(k.validMove(new Coord(3,4)));
		assertFalse(k.validMove(new Coord(2,4)));
		c.getSquare(new Coord(2,4)).setBlackAttack(false);
		c.getSquare(new Coord(2,4)).setWhiteAttack(true);
		assertTrue(k.validMove(new Coord(2,4)));
	}

	@Test public void movePieceBasicTest(){
		Chessboard c = new Chessboard();
		Knight n = new Knight(Shade.BLACK,new int[]{3,3},c);
		assertFalse(c.movePiece(n, new Coord(3,3)));
		assertFalse(c.movePiece(n, new Coord(0,0)));
		assertTrue(c.movePiece(n, new Coord(5,4)));
		assertFalse(c.getSquare(new Coord(3,3)).isOccupied());
		assertTrue(c.getSquare(new Coord(5,4)).isOccupied());
		assertEquals(new Coord(5,4),n.getCoord());
	}
	
	@Test public void movePieceTakePieceTest(){
		Chessboard c = new Chessboard();
		Queen q = new Queen(Shade.BLACK,new int[]{3,3},c);
		Knight n = new Knight(Shade.WHITE, new int[]{4,4},c);
		assertFalse(c.movePiece(q, new Coord(3,3)));
		assertFalse(c.movePiece(q, new Coord(5,5)));
		assertTrue(c.movePiece(q, new Coord(0,0)));
		assertTrue(c.movePiece(q, new Coord(4,4)));
		assertFalse(c.getSquare(new Coord(3,3)).isOccupied());
		assertFalse(c.getSquare(new Coord(0,0)).isOccupied());
		assertTrue(c.getSquare(new Coord(4,4)).isOccupied());
		assertEquals(Shade.BLACK,c.getSquare(new Coord(4,4)).occupantShade());
		assertEquals(new Coord(4,4),q.getCoord());
	}

	@Test public void rookAttackTest(){
		Chessboard c = new Chessboard();
		Rook r = new Rook(Shade.BLACK,new int[]{3,3},c);
		HashSet<Coord> test = new HashSet<Coord>();
		test.addAll(r.getAttack());
		assertTrue(test.contains(new Coord(3,0)));
		assertTrue(test.contains(new Coord(3,2)));
		assertTrue(test.contains(new Coord(3,4)));
		assertTrue(test.contains(new Coord(3,6)));
		assertTrue(test.contains(new Coord(3,7)));
		assertTrue(test.contains(new Coord(0,3)));
		assertTrue(test.contains(new Coord(2,3)));
		assertFalse(test.contains(new Coord(3,3)));
		assertTrue(test.contains(new Coord(4,3)));
		assertTrue(test.contains(new Coord(6,3)));
		Knight n = new Knight(Shade.WHITE,new int[]{3,2},c);
		Bishop b = new Bishop(Shade.BLACK,new int[]{5,3},c);
		test.clear();
		test.addAll(r.getAttack());
		assertFalse(test.contains(new Coord(3,0)));
		assertFalse(test.contains(new Coord(3,1)));
		assertTrue(test.contains(new Coord(3,2)));
		assertTrue(test.contains(new Coord(3,4)));
		assertTrue(test.contains(new Coord(3,6)));
		assertTrue(test.contains(new Coord(3,7)));
		assertTrue(test.contains(new Coord(0,3)));
		assertTrue(test.contains(new Coord(2,3)));
		assertFalse(test.contains(new Coord(3,3)));
		assertTrue(test.contains(new Coord(4,3)));
		assertTrue(test.contains(new Coord(5,3)));
		assertFalse(test.contains(new Coord(6,3)));
		assertFalse(test.contains(new Coord(7,3)));
		assertFalse(test.contains(r.getCoord()));
		
		c.movePiece(r, new Coord(3,2)); //Take the knight
		assertFalse(n.isAlive());
		test.clear();
		test.addAll(r.getAttack());
		assertTrue(test.contains(new Coord(7,2)));
		assertTrue(test.contains(new Coord(0,2)));
		assertFalse(test.contains(new Coord(0,3)));
		assertFalse(test.contains(new Coord(2,3)));
		assertFalse(test.contains(new Coord(3,2)));
		assertFalse(test.contains(new Coord(4,3)));
		assertFalse(test.contains(new Coord(5,3)));
		assertFalse(test.contains(new Coord(6,3)));
		assertFalse(test.contains(new Coord(7,3)));
	}

	@Test public void pawnAttackTest(){
		Chessboard c = new Chessboard();
		Pawn p1 = new Pawn(Shade.BLACK,new int[]{3,3},c);
		Pawn p2 = new Pawn(Shade.WHITE,new int[]{4,4},c);
		HashSet<Coord> test1 = new HashSet<Coord>();
		HashSet<Coord> test2 = new HashSet<Coord>();
		test1.addAll(p1.getAttack());
		test2.addAll(p2.getAttack());
		assertTrue(test1.contains(new Coord(2,2)));
		assertTrue(test1.contains(new Coord(4,2)));
		assertTrue(test2.contains(new Coord(5,5)));
		assertTrue(test2.contains(new Coord(3,5)));
		assertFalse(test1.contains(new Coord(3,2)));
		assertFalse(test1.contains(new Coord(4,5)));
		assertFalse(test1.contains(p1.getCoord()));
		assertFalse(test2.contains(p2.getCoord()));
		test1.clear();	test2.clear();	p1.kill();	p2.kill();
		Pawn p = new Pawn(Shade.WHITE,new int[]{0,1},c);
		test1.addAll(p.getAttack());
		assertTrue(test1.contains(new Coord(1,2)));
	}
	
	@Test public void kingAttackTest(){
		Chessboard c = new Chessboard();
		King k = new King(Shade.BLACK,new int[]{3,3},c);
		HashSet<Coord> test = new HashSet<Coord>();
		test.addAll(k.getAttack());
		assertTrue(test.contains(new Coord(3,4)));
		assertTrue(test.contains(new Coord(4,4)));
		assertTrue(test.contains(new Coord(4,3)));
		assertTrue(test.contains(new Coord(4,2)));
		assertTrue(test.contains(new Coord(3,2)));
		assertTrue(test.contains(new Coord(2,2)));
		assertTrue(test.contains(new Coord(2,3)));
		assertTrue(test.contains(new Coord(2,4)));
		assertFalse(test.contains(k.getCoord()));
		King k2 = new King(Shade.WHITE,new int[]{7,7},c);
		test.clear();	test.addAll(k2.getAttack());
		assertTrue(test.contains(new Coord(6,6)));
		assertTrue(test.contains(new Coord(6,7)));
		assertTrue(test.contains(new Coord(7,6)));
		assertFalse(test.contains(k2.getCoord()));
		
	}
	
	@Test public void knightAttackTest(){
		Chessboard c = new Chessboard();
		Knight n = new Knight(Shade.WHITE,new int[]{3,3},c);
		HashSet<Coord> test	= new HashSet<Coord>();
		test.addAll(n.getAttack());
		assertTrue(test.contains(new Coord(5,4)));
		assertTrue(test.contains(new Coord(4,5)));
		assertTrue(test.contains(new Coord(5,4)));
		assertTrue(test.contains(new Coord(5,2)));
		assertTrue(test.contains(new Coord(4,1)));
		assertTrue(test.contains(new Coord(2,1)));
		assertTrue(test.contains(new Coord(1,2)));
		assertTrue(test.contains(new Coord(1,4)));
		assertTrue(test.contains(new Coord(2,5)));
		assertFalse(test.contains(n.getCoord()));
	}
	
	@Test public void bishopAttackTest(){
		Chessboard c = new Chessboard();
		Bishop b = new Bishop(Shade.BLACK,new int[]{3,3},c);
		HashSet<Coord> test = new HashSet<Coord>();
		test.addAll(b.getAttack());
		assertTrue(test.contains(new Coord(4,4)));
		assertTrue(test.contains(new Coord(6,6)));
		assertTrue(test.contains(new Coord(7,7)));
		assertTrue(test.contains(new Coord(2,2)));
		assertTrue(test.contains(new Coord(0,0)));
		assertTrue(test.contains(new Coord(2,4)));
		assertTrue(test.contains(new Coord(0,6)));
		assertTrue(test.contains(new Coord(4,2)));
		assertTrue(test.contains(new Coord(6,0)));
		assertFalse(test.contains(b.getCoord()));
		test.clear();
		Bishop b2 = new Bishop(Shade.WHITE,new int[]{5,5},c);
		Knight n = new Knight(Shade.BLACK,new int[]{4,2},c);
		test.addAll(b.getAttack());
		assertTrue(test.contains(new Coord(4,4)));
		assertTrue(test.contains(new Coord(5,5)));
		assertFalse(test.contains(new Coord(6,6)));
		assertFalse(test.contains(new Coord(7,7)));
		assertTrue(test.contains(new Coord(2,2)));
		assertTrue(test.contains(new Coord(0,0)));
		assertTrue(test.contains(new Coord(2,4)));
		assertTrue(test.contains(new Coord(0,6)));
		assertTrue(test.contains(new Coord(4,2)));
		assertFalse(test.contains(new Coord(6,0)));
	}

	@Test public void queenRookAttackTest(){
		Chessboard c = new Chessboard();
		Queen q = new Queen(Shade.BLACK,new int[]{3,3},c);
		HashSet<Coord> test = new HashSet<Coord>();
		test.addAll(q.getAttack());
		assertTrue(test.contains(new Coord(3,0)));
		assertTrue(test.contains(new Coord(3,2)));
		assertTrue(test.contains(new Coord(3,4)));
		assertTrue(test.contains(new Coord(3,6)));
		assertTrue(test.contains(new Coord(3,7)));
		assertTrue(test.contains(new Coord(0,3)));
		assertTrue(test.contains(new Coord(2,3)));
		assertFalse(test.contains(new Coord(3,3)));
		assertTrue(test.contains(new Coord(4,3)));
		assertTrue(test.contains(new Coord(6,3)));
		Knight n = new Knight(Shade.WHITE,new int[]{3,2},c);
		Bishop b = new Bishop(Shade.BLACK,new int[]{5,3},c);
		test.clear();
		test.addAll(q.getAttack());
		assertFalse(test.contains(new Coord(3,0)));
		assertFalse(test.contains(new Coord(3,1)));
		assertTrue(test.contains(new Coord(3,2)));
		assertTrue(test.contains(new Coord(3,4)));
		assertTrue(test.contains(new Coord(3,6)));
		assertTrue(test.contains(new Coord(3,7)));
		assertTrue(test.contains(new Coord(0,3)));
		assertTrue(test.contains(new Coord(2,3)));
		assertFalse(test.contains(new Coord(3,3)));
		assertTrue(test.contains(new Coord(4,3)));
		assertTrue(test.contains(new Coord(5,3)));
		assertFalse(test.contains(new Coord(6,3)));
		assertFalse(test.contains(new Coord(7,3)));
		assertFalse(test.contains(q.getCoord()));
	}
	
	@Test public void queenBishopAttackTest(){
		Chessboard c = new Chessboard();
		Queen q = new Queen(Shade.BLACK,new int[]{3,3},c);
		HashSet<Coord> test = new HashSet<Coord>();
		test.addAll(q.getAttack());
		assertTrue(test.contains(new Coord(4,4)));
		assertTrue(test.contains(new Coord(6,6)));
		assertTrue(test.contains(new Coord(7,7)));
		assertTrue(test.contains(new Coord(2,2)));
		assertTrue(test.contains(new Coord(0,0)));
		assertTrue(test.contains(new Coord(2,4)));
		assertTrue(test.contains(new Coord(0,6)));
		assertTrue(test.contains(new Coord(4,2)));
		assertTrue(test.contains(new Coord(6,0)));
		assertFalse(test.contains(q.getCoord()));
		test.clear();
		Bishop b2 = new Bishop(Shade.WHITE,new int[]{5,5},c);
		Knight n = new Knight(Shade.BLACK,new int[]{4,2},c);
		test.addAll(q.getAttack());
		assertTrue(test.contains(new Coord(4,4)));
		assertTrue(test.contains(new Coord(5,5)));
		assertFalse(test.contains(new Coord(6,6)));
		assertFalse(test.contains(new Coord(7,7)));
		assertTrue(test.contains(new Coord(2,2)));
		assertTrue(test.contains(new Coord(0,0)));
		assertTrue(test.contains(new Coord(2,4)));
		assertTrue(test.contains(new Coord(0,6)));
		assertTrue(test.contains(new Coord(4,2)));
		assertFalse(test.contains(new Coord(6,0)));
	}

	@Test public void squaresAttackedTest(){
		Chessboard c = new Chessboard();
		Player white = new Player(Shade.WHITE,c);
		HashSet<Coord> test = new HashSet<Coord>();
		test.addAll(white.getSquaresAttacked());
		assertTrue(test.contains(new Coord(0,2)));
		assertTrue(test.contains(new Coord(1,2)));
		assertTrue(test.contains(new Coord(2,2)));
		assertTrue(test.contains(new Coord(3,2)));
		assertTrue(test.contains(new Coord(4,2)));
		assertTrue(test.contains(new Coord(5,2)));
		assertTrue(test.contains(new Coord(6,2)));
		assertTrue(test.contains(new Coord(7,2)));
	}

	@Test public void kingDangerZoneTest(){
		Chessboard c = new Chessboard();
		King k = new King(Shade.BLACK,new int[]{3,3},c);
		Pawn p = new Pawn(Shade.WHITE,new int[]{1,1},c);
		Knight n = new Knight(Shade.WHITE,new int[]{1,5},c);
		Bishop b1 = new Bishop(Shade.WHITE,new int[]{2,5},c);
		Bishop b2 = new Bishop(Shade.WHITE,new int[]{3,5},c);
		Rook r1 = new Rook(Shade.WHITE,new int[]{5,2},c);
		Rook r2 = new Rook(Shade.WHITE,new int[]{4,5},c);
		c.applyAttack(k);	c.applyAttack(p);	c.applyAttack(n);
		c.applyAttack(b1);	c.applyAttack(r1);	c.applyAttack(r2);
		c.applyAttack(b2);
		assertFalse(k.validMove(new Coord(3,4)));
		assertFalse(k.validMove(new Coord(4,4)));
		assertFalse(k.validMove(new Coord(4,3)));
		assertFalse(k.validMove(new Coord(4,2)));
		assertFalse(k.validMove(new Coord(3,2)));
		assertFalse(k.validMove(new Coord(2,2)));
		assertFalse(k.validMove(new Coord(2,3)));
		assertFalse(k.validMove(new Coord(2,4)));
	}
	
	@Test public void kingCastleTest(){
		Chessboard c = new Chessboard();
		King k = new King(Shade.WHITE,new int[]{4,0},c);
		Rook r1 = new Rook(Shade.WHITE,new int[]{0,0},c);
		Rook r2 = new Rook(Shade.WHITE,new int[]{7,0},c);
		assertTrue(k.validMove(new Coord(2,0)));
		assertTrue(k.validMove(new Coord(6,0)));
		Bishop b = new Bishop(Shade.BLACK,new int[]{3,2},c);
		c.applyAttack(b);
		assertFalse(k.validMove(new Coord(2,0)));
		assertFalse(k.validMove(new Coord(6,0)));
		b.kill();	c.clearAttacks();
		assertTrue(k.validMove(new Coord(2,0)));
		assertTrue(k.validMove(new Coord(6,0)));
		Queen q = new Queen(Shade.WHITE,new int[]{3,0},c);
		Knight n = new Knight(Shade.WHITE,new int[]{6,0},c);
		assertFalse(k.validMove(new Coord(2,0)));
		assertFalse(k.validMove(new Coord(6,0)));
		q.kill();	n.kill();
		assertTrue(k.validMove(new Coord(2,0)));
		assertTrue(k.validMove(new Coord(6,0)));
		Rook r3 = new Rook(Shade.BLACK, new int[]{4,4},c);
		c.applyAttack(r3);
		assertFalse(k.validMove(new Coord(2,0)));
		assertFalse(k.validMove(new Coord(6,0)));
		r3.kill();	c.clearAttacks();
		Pawn p = new Pawn(Shade.BLACK, new int[]{2,1},c);
		c.applyAttack(p);
		assertFalse(k.validMove(new Coord(2,0)));
		assertTrue(k.validMove(new Coord(6,0)));
		c.movePiece(p, new Coord(2,0));
		c.clearAttacks();	c.applyAttack(p);
		assertFalse(k.validMove(new Coord(2,0)));
		assertTrue(k.validMove(new Coord(6,0)));
		c.movePiece(r2, new Coord(7,1));
		p.kill();
		assertTrue(k.validMove(new Coord(2,0)));
		assertFalse(k.validMove(new Coord(6,0)));
		assertTrue(c.movePiece(k, new Coord(2,0)));
		assertEquals(new Coord(2,0),k.getCoord());
		assertEquals(new Coord(3,0),r1.getCoord());
		assertTrue(c.getSquare(new Coord(2,0)).isOccupied());
		assertTrue(c.getSquare(new Coord(3,0)).isOccupied());
		assertFalse(c.getSquare(new Coord(0,0)).isOccupied());
		assertFalse(c.getSquare(new Coord(4,0)).isOccupied());
		assertFalse(k.canCastle() || r1.canCastle() || r2.canCastle());
	}
	
	@Test public void enPassantTests(){
		Chessboard c = new Chessboard();
		Pawn p1 = new Pawn(Shade.WHITE,new int[]{0,1},c);
		Pawn p2 = new Pawn(Shade.BLACK,new int[]{1,3},c);
		c.movePiece(p1, new Coord(0,3));
		PassantShadow ps = new PassantShadow(Shade.WHITE,new int[]{0,2},c,p1);
		assertFalse(c.getSquare(new Coord(0,2)).isOccupied());
		assertTrue(c.getSquare(new Coord(0,2)).hasPassant());
		assertTrue(p2.validMove(new Coord(0,2)));
		assertTrue(c.movePiece(p2, new Coord(0,2)));
		assertFalse(c.getSquare(new Coord(0,3)).isOccupied());
		assertFalse(c.getSquare(new Coord(0,2)).hasPassant());
		assertTrue(c.getSquare(new Coord(0,2)).isOccupied());
	}

}
