import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Game implements Runnable{

	private boolean gameOver = false;

	/* Local elements of state. The above determines whether or not the game is
	 * still in play. A value of false disables all buttons, and occurs 
	 * concurrently with the termination of all pieces from state. StartSquare
	 * and endSquare serve self-evident purposes in executing moves, as does 
	 * chosenPiece. Because moves are judged to be legal in regards to check
	 * retroactively, another element of state, limboPiece, is necessary to 
	 * ensure that any piece taken during an illegal move can still be 
	 * replaced when the move is reset. The chessboard is initialized here for
	 * convenience, and the toMove variable ensures that white starts the game.
	 */
	
	private Square startSquare;
	private Square endSquare;
	private Piece chosenPiece;
	private Piece limboPiece;
	private Chessboard board = new Chessboard();
	private Shade toMove = Shade.WHITE;
	
	//The array defined here is used later in the prompt for pawn promotion.
	private Object[] options = {"Knight","Bishop","Rook","Queen"};
	
	
	/* The below method checks for checkmate or stalemate by determining 
	 * whether or not the color whose turn it is to move has any valid 
	 * moves remaining. It functions using the same algorithm as the movement
	 * protocol in the mouse listener added to each square. Whether or not the
	 * mate is a checkmate or a stalemate is determined later on. 
	 */
	private boolean mateCheck(Player p1,Player p2){
		//A temporary holding cell for the coordinates of a piece before it is
		//moved, so that it may be moved back in the event a move is invalid.
		Coord temp;
		int n = 1;
		if(toMove == Shade.BLACK){
			for(Piece pc: p2.getPieces()){
				if (pc instanceof PassantShadow) continue;
				temp = pc.getCoord();
				for(Coord cd:pc.getMoves()){
					if(board.getSquare(cd).isOccupied()) limboPiece = board.getSquare(cd).getOccupant();
					if(board.movePiece(pc, cd)){
						if(!(p1.getSquaresAttacked().contains(p2.getKingCoord()))){
							System.out.print("Found move for " + pc.toString() + " from " + board.getSquare(temp).toString() + "\n");
							board.getSquare(temp).place(pc);
							if(limboPiece != null) limboPiece.revive();
							return false;
						}
						board.getSquare(temp).place(pc);
						if(limboPiece != null) limboPiece.revive();
						System.out.print(n + " moves evaluated\n");
						n++;
					}
					limboPiece = null;
				}
			}
			return true;
		} else {
			for(Piece pc: p1.getPieces()){
				if (pc instanceof PassantShadow) continue;
				temp = pc.getCoord();
				for(Coord cd:pc.getMoves()){
					if(board.getSquare(cd).isOccupied()) limboPiece = board.getSquare(cd).getOccupant();
					if(board.movePiece(pc, cd)){
						if(!(p2.getSquaresAttacked().contains(p1.getKingCoord()))){
							System.out.print("Found move for " + pc.toString() + " at " + board.getSquare(cd).toString());
							board.getSquare(temp).place(pc);
							if(limboPiece != null) limboPiece.revive();
							return false;
						}
						board.getSquare(temp).place(pc);//movePiece(pc, temp);
						if(limboPiece != null) limboPiece.revive();
					}
					limboPiece = null;
				}
			}
			return true;
		}
	}
	
	public void toggleMove(){
		if (toMove == Shade.BLACK) toMove = Shade.WHITE;
		else toMove = Shade.BLACK;
	}
	
	//Method is passed the shade of the losing player and both players, so that it can terminate play.
	private void victory(Shade sh, Player p1, Player p2){
		for(Piece p:p1.getPieces()){
			p.kill();
		}
		
		for(Piece p:p2.getPieces()){
			p.kill();
		}
		
		for(Square sqr:board.getAllSquares()){
			if(sh == Shade.WHITE) sqr.setBackground(Color.BLACK);
			else sqr.setBackground(Color.WHITE);
		}
		
	}
	
	//Method is passed both players, allowing it to terminate play.
	public void draw(Player p1, Player p2){
		for(Piece p:p1.getPieces()){
			p.kill();
		}
		
		for(Piece p:p2.getPieces()){
			p.kill();
		}
		
		for(Square sqr:board.getAllSquares()){
			sqr.setBackground(Color.CYAN);
		}
		
	}
	
	@Override
	public void run() {
		JFrame frame = new JFrame("CIChess: A Chess Program by Cam Cogan");
		
		//White (p1) and black (p2) are initialized
		final Player p1 = new Player(Shade.WHITE,board);
		final Player p2 = new Player(Shade.BLACK,board);
		
		//The main GUI components are initialized, including the board and button panel at right.
		final JPanel grid = new JPanel();
		final JPanel buttonPanel = new JPanel();
		final JButton declareMate = new JButton("Declare Mate");
		final JButton offerDraw = new JButton("Offer Draw");
		final JButton resignButton = new JButton("Resign");
		final JButton partyButton = new JButton("Party Colors");
		
		//Configuring the button panel.
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.add(declareMate);
		buttonPanel.add(offerDraw);
		buttonPanel.add(resignButton);
		buttonPanel.add(partyButton);
		buttonPanel.setBorder(BorderFactory.createEtchedBorder());
		
		//Displaying the square objects in the correct order on the grid requires
		//the unorthodox indexing loop seen below, as the squares are added to
		//the grid from left to right, top row to bottom row.
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				final Square sq = board.getSquare(new Coord(j,7-i));
				grid.add(sq);
				if(sq.isOccupied()) sq.add(sq.getOccupant());
				else sq.removeAll();
			}
		}
		
		//This method uses the mateCheck method, then declares victory for the
		//proper player, declares a stalemate, or returns to play, letting the
		//players know it was unsuccessful in finding a mate.
		declareMate.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(!gameOver){
					if(mateCheck(p1,p2)){
						if(toMove == Shade.BLACK){
							if(p1.getSquaresAttacked().contains(p2.getKingCoord())){
								victory(toMove,p1,p2);
								toggleMove();
								JOptionPane.showMessageDialog(grid, toMove + " WINS BY CHECKMATE!");
								gameOver = true;
							} else {
								draw(p1, p2);
								JOptionPane.showMessageDialog(grid, "Stalemate!");
								gameOver = true;
							}
						} else {
							if(p2.getSquaresAttacked().contains(p1.getKingCoord())){
								victory(toMove,p1,p2);
								toggleMove();
								JOptionPane.showMessageDialog(grid, toMove + " WINS BY CHECKMATE!");
								gameOver = true;
							} else {
								draw(p1, p2);
								JOptionPane.showMessageDialog(grid, "Stalemate!");
								gameOver = true;
							}
						}
					} else {
						JOptionPane.showMessageDialog(grid, "No mates detected.");
					}
				}
			}
		});
		
		//A simple button that terminates play if a draw is agreed upon.
		offerDraw.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(!gameOver){
					toggleMove();
					int n = JOptionPane.showConfirmDialog(grid, toMove + ": Do you accept the draw?",
							"Draw Offered",JOptionPane.YES_NO_OPTION);
					if(n == 0){
						draw(p1, p2);
						JOptionPane.showMessageDialog(grid, "Draw declared!");
						gameOver = true;
					} else {
						toggleMove();
					}
				}
			}
		});
		
		//A button functionally identical to the one above, but which declares victory rather than a draw.
		resignButton.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(!gameOver){
					int n = JOptionPane.showConfirmDialog(grid, toMove + ": Are you sure you wish to resign?",
							"Resign?",JOptionPane.YES_NO_OPTION);
					if(n == 0){
						victory(toMove,p1,p2);
						toggleMove();
						JOptionPane.showMessageDialog(grid, toMove + " WINS BY RESIGNATION!");
						gameOver = true;
					}
				}
			}
		});
		
		//A button which iterates over the squares on the board and changes the color scheme to
		//magenta-green instead of black-white. A boolean field helps it keep track of its current
		//mode.
		partyButton.addMouseListener(new MouseAdapter(){
			private boolean state = false;
			Square sqare;
			
			public void mouseClicked(MouseEvent e){
				if(!gameOver){
					for(int i = 0; i < 8; i++){
						for(int j = 0; j < 8; j++){
							sqare = board.getSquare(new Coord(j,7-i));
							if(!state){
								if ((i+j)%2 ==0) sqare.setBackground(Color.GREEN);
								else sqare.setBackground(Color.MAGENTA);	
							} else {
								if ((i+j)%2 ==0) sqare.setBackground(Color.white);
								else sqare.setBackground(Color.gray);
							}
						}
					}
				}
				state = !(state);
				grid.repaint();
			}
			
		});
		
		/** The main mouse listener for the game, added to the grid to allow the
		 * existence of one listener for the board rather than 64. The main event
		 * loop of the entire game is contained within this listener.
		 * 
		 * A mousePressed event assigns the correct state to the startSquare and
		 * chosenPiece fields. The search for the correct square is dependent on
		 * the invariant that each square is 75 pixels by 75 pixels.
		 * 
		 * A mouseReleased event assigns the endSquare and limboPiece fields
		 * using the same algorithm as above. The program then performs a series
		 * of checks:
		 * -Is the start square or chosen piece selected null? If so, the action is discarded.
		 * -Can the chosenPiece piece be moved to the square endSquare from its present location? If not, the action is discarded.
		 * -If so, will this move leave the piece's king in check? If so, the action is discarded.
		 * -If the piece is a king, is it castling? If so, the graphical representation of the rook is also adjusted.
		 * -If the piece is a pawn, is it being promoted? If so, the piece is replaced with a piece of the user's choice.
		 * -If the piece is a pawn, is it is moving two spaces? If so, leave a PassantShadow object in its wake on the board.
		 * -If the piece is a pawn, is it capturing en passant? If so, remove the graphical representation of the pawn captured.
		 * 
		 * Following this series of evaluations, the program then performs a set of events regardless of the color of the piece,
		 * though the protocol below is technically stemmed by color:
		 * -The color to move next has its current attacks cleared from the board, and the color that has just moved
		 *     has its attacks applied to the board.
		 * -The toMove variable is toggled so that the next player may move.
		 * -startSquare and endSquare are cleared of any graphical representations of pieces.
		 * -The graphical representation of chosenPiece is added to endSquare.
		 * -PassantShadows are cleared for the color about to move
		 * -The board is repainted.
		 * -The console prints helper messages.
		 * -The state of the variables startSquare, endSquare, chosenPiece, and limboPiece are all reset to null.
		 * -PassantShadows are cleared from players' rosters.
		 */
		grid.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				Point start = e.getPoint();
				int x = start.x;
				int y = start.y;
				x /= 75;
				y = 7 - (y/75);
				startSquare = board.getSquare(new Coord(x,y));
				if(!startSquare.isOccupied()) chosenPiece = null;
				if(startSquare.occupantShade() == toMove){
					chosenPiece = startSquare.getOccupant();
				} else chosenPiece = null;
				System.out.print("Start square: " + startSquare.toString() + "\n");
				if(chosenPiece == null) System.out.print("Piece was not selected; startSquare.isOccupied returned false.\n");
				else System.out.print("Chosen piece: " + chosenPiece.toString() + "\n");
			}
			
			public void mouseReleased(MouseEvent e){
				Point end = e.getPoint();
				int x = end.x;
				int y = end.y;
				x /= 75;
				y = 7 - (y/75);
				endSquare = board.getSquare(new Coord(x,y));
				if (endSquare.isOccupied() || endSquare.hasPassant()) limboPiece = endSquare.getOccupant();
				if(!(startSquare == null || chosenPiece == null)){
					if(board.movePiece(chosenPiece, endSquare.getCoords())){
						if(toMove == Shade.WHITE){
							if(!(p2.getSquaresAttacked().contains(p1.getKingCoord()))){
								if (chosenPiece instanceof King){
									if (startSquare.getCoords().vectorTo(endSquare.getCoords()).equals(new Coord(2,0))){
										Square rookSquare = board.getSquare(new Coord(5,0));
										rookSquare.removeAll();
										rookSquare.add(rookSquare.getOccupant());
									} else if (startSquare.getCoords().vectorTo(endSquare.getCoords()).equals(new Coord(-2,0))){
										Square rookSquare = board.getSquare(new Coord(3,0));
										rookSquare.removeAll();
										rookSquare.add(rookSquare.getOccupant());
									}
									
								}
								if (chosenPiece instanceof Pawn){
									if (endSquare.getCoords().getRank() == 0 || endSquare.getCoords().getRank() == 7){
										int n = JOptionPane.showOptionDialog(grid,"To what would you like to promote that pawn?",
												"Pawn Promotion",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
											    null,options,options[3]);
										chosenPiece.kill();
										endSquare.removeAll();
										switch (n){
										case 0:
											Knight nite = new Knight(Shade.WHITE,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p1.addPromotedPiece(nite);
											chosenPiece = nite;
											break;
										case 1:
											Bishop b = new Bishop(Shade.WHITE,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p1.addPromotedPiece(b);
											chosenPiece = b;
											break;
										case 2:
											Rook r = new Rook(Shade.WHITE,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p1.addPromotedPiece(r);
											chosenPiece = r;
											break;
										case 3:
											Queen q = new Queen(Shade.WHITE,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p1.addPromotedPiece(q);
											chosenPiece = q;
											break;
										}
									} else if (startSquare.getCoords().vectorTo(endSquare.getCoords()).getRank() == 2){
										PassantShadow ps =
												new PassantShadow(Shade.WHITE,
														new int[]{endSquare.getCoords().getFile(),endSquare.getCoords().getRank()-1},
														board,(Pawn) chosenPiece);
										board.movePiece(ps, ps.getCoord());
										p1.getPieces().add(ps);
									} else if (limboPiece instanceof PassantShadow){
										board.getSquare(new Coord(endSquare.getCoords().getFile(),endSquare.getCoords().getRank()-1)).removeAll();
									}
								} 
								board.applyAttack(p1);
								toggleMove();
								startSquare.removeAll();
								endSquare.removeAll();
								endSquare.add(chosenPiece);
								endSquare.repaint();
								for(Square sqwr: board.getAllSquares()){
									if(sqwr.hasPassant() && sqwr.occupantShade() == Shade.BLACK) sqwr.clear();
								}
								grid.repaint();
								System.out.print("End square: " + endSquare.toString() + "\n");
								System.out.print("To move: " + toMove + "\n");
								startSquare = null;
								endSquare = null;
								chosenPiece = null;
								limboPiece = null;
								p2.clearPassant();
							} else {
								if(!startSquare.place(chosenPiece)) System.out.print("Problem placing chosenPiece");//movePiece(chosenPiece, startSquare.getCoords());
								endSquare.clear();
								if(limboPiece != null) limboPiece.revive();
								limboPiece = null;
								System.out.print("Invalid move, King under attack.\n");
							}
							
						} else {
							if(!(p1.getSquaresAttacked().contains(p2.getKingCoord()))){
								if (chosenPiece instanceof King){
									if (startSquare.getCoords().vectorTo(endSquare.getCoords()).equals(new Coord(2,0))){
										Square rookSquare = board.getSquare(new Coord(5,7));
										rookSquare.removeAll();
										rookSquare.add(rookSquare.getOccupant());
									} else if (startSquare.getCoords().vectorTo(endSquare.getCoords()).equals(new Coord(-2,0))){
										Square rookSquare = board.getSquare(new Coord(3,7));
										rookSquare.removeAll();
										rookSquare.add(rookSquare.getOccupant());
									}
									
								}
								if (chosenPiece instanceof Pawn){
									if (endSquare.getCoords().getRank() == 0 || endSquare.getCoords().getRank() == 7){
										int n = JOptionPane.showOptionDialog(grid,"To what would you like to promote that pawn?",
												"Pawn Promotion",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,
											    null,options,options[3]);
										chosenPiece.kill();
										endSquare.removeAll();
										switch (n){
										case 0:
											Knight nite = new Knight(Shade.BLACK,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p2.addPromotedPiece(nite);
											chosenPiece = nite;
											break;
										case 1:
											Bishop b = new Bishop(Shade.BLACK,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p2.addPromotedPiece(b);
											chosenPiece = b;
											break;
										case 2:
											Rook r = new Rook(Shade.BLACK,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p2.addPromotedPiece(r);
											chosenPiece = r;
											break;
										case 3:
											Queen q = new Queen(Shade.BLACK,new int[]{endSquare.getCoords().getFile(),
													endSquare.getCoords().getRank()},board);
											p2.addPromotedPiece(q);
											chosenPiece = q;
											break;
										}
									} else if (startSquare.getCoords().vectorTo(endSquare.getCoords()).getRank() == -2){
										PassantShadow ps =
												new PassantShadow(Shade.BLACK,
														new int[]{endSquare.getCoords().getFile(),endSquare.getCoords().getRank()+1},
														board,(Pawn) chosenPiece);
										board.movePiece(ps, ps.getCoord());
										p2.getPieces().add(ps);
									} else if (limboPiece instanceof PassantShadow){
										board.getSquare(new Coord(endSquare.getCoords().getFile(),endSquare.getCoords().getRank()+1)).removeAll();
									}
								}
								board.applyAttack(p2);
								toggleMove();
								startSquare.removeAll();
								endSquare.removeAll();
								endSquare.add(chosenPiece);
								endSquare.repaint();
								for(Square sqwr: board.getAllSquares()){
									if(sqwr.hasPassant() && sqwr.occupantShade() == Shade.WHITE) sqwr.clear();
								}
								grid.repaint();
								System.out.print("End square: " + endSquare.toString() + "\n");
								System.out.print("To move: " + toMove + "\n");
								startSquare = null;
								endSquare = null;
								chosenPiece = null;
								limboPiece = null;
								p1.clearPassant();
							} else {
								if(!startSquare.place(chosenPiece)) System.out.print("Problem placing chosenPiece");
								endSquare.clear();
								if(limboPiece != null) limboPiece.revive();
								limboPiece = null;
								System.out.print("Invalid move, King under attack.\n");
							}
						}
						
					}
				}
			}
		});
		
		grid.setLayout(new GridLayout(8,8));
		
		frame.setLayout(new BorderLayout());
		frame.add(grid, BorderLayout.CENTER);
		frame.add(buttonPanel,BorderLayout.LINE_END);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
        frame.setVisible(true);

		JOptionPane.showMessageDialog(frame,
				"Hello and welcome to CIChess! This is a two-player chess game developed as a homework\n" +
				"project for CIS 120. The rules of CIChess are identical to those of normal chess.\n" +
				"Simply find a partner, pick a color, and trade off making moves with your opponent.\n" +
				"Any move valid in chess is also valid here, including en passant as well as promotion.\n" +
				"To actually move pieces, simply click and drag them. There won't be any visual piece preview,\n" +
				"but there are a number of helpful outputs in the console to aid you.\n\n" +
				"If a mate situation is reached, simply hit the 'Declare Mate' button at right\n" +
				"and the game will check for you whether or not a checkmate or stalemate has been \n" +
				"reached. If you wish to resign, hit the 'Resign' button. Meanwhile, if you're feeling\n" +
				"diplomatic and wish to offer a draw, that option can be found at right as well. Hit the\n" +
				"'Party Colors' button to add some zazz to your game. The board will turn the color of the\n" +
				"winning player when a win or loss has occurred, or cyan if there is a draw.\n\n" +
				"The impressive features of this game consist almost entirely of the accuracy with which\n" +
				"it renders the rules of chess. Pieces are bound to only move in a certain fashion, and \n" +
				"cannot move through pieces of any color, barring knights. Pieces of the same color cannot\n" +
				"take one another, and kings cannot move into check. Pawns can only move forward, unless they\n" +
				"can capture, including capturing en passant, and can only move forward two spaces if it is\n" +
				"the piece's first move. Kings can castle with rooks, but only if intervening squares are neither\n" +
				"occupied nor under attack by opposing pieces. Castling is also invalid if either piece involved\n" +
				"has moved during the game. No rule is left unimplemented, and players are bound to follow them.\n\n" +
				"IMPORTANT GRAPHICAL CONSIDERATIONS: Keep the window at present size, as stretching it will\n" +
				"distort the controls of the game. Additionally, when pawns are promoted, they seem to \n" +
				"disappear. Fear not! Minimizing and restoring the screen will display the piece to which you've\n" +
				"promoted said pawn.\n\n" +
				"With that, best of luck! This game took some time to develop, so you should feel free to spend\n" +
				"about as much time enjoying it.");
		
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Game());
	}

}

