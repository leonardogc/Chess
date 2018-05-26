package logic.pieces;

import java.util.LinkedList;

import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class King extends Piece{
	private boolean moved;

	public King(PieceColor color) {
		super(PieceType.King, color);
		this.moved = false;
	}
	
	public King(PieceColor color, boolean moved) {
		super(PieceType.King, color);
		this.moved = moved;
	}

	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		if(game.getBoard()[x][y].getType() != PieceType.King) {
			return false;
		}
		
		int dx = dest_x - x;
		int dy = dest_y - y;
		int amount;

		if(dy == 0) {
			amount = Math.abs(dx);
			
			if(amount != 1) {
				if(amount != 2) {
					return false;
				}
				
				if(((King)game.getBoard()[x][y]).getMoved()) {
					return false;
				}
				
				
				if(dx > 0) {
					if(!GameUtil.isBoardFree(game.getBoard(), x+1, y, GameUtil.boardSize - 2, y)) {
						return false;
					}
					
					if(!rookAt(GameUtil.boardSize - 1, y , game.getBoard(), game.getBoard()[x][y].getColor())) {
						return false;
					}
				}
				else {
					if(!GameUtil.isBoardFree(game.getBoard(), x-1, y, 1, y)) {
						return false;
					}
					
					if(!rookAt(0, y , game.getBoard(), game.getBoard()[x][y].getColor())) {
						return false;
					}
				}
				
				int vx = dx/amount;
				boolean success = true;
				PieceColor color = game.getBoard()[x][y].getColor();
				
				for(int i = 0; i <= amount; i++) {
					if(game.playerInCheck(color)) {
						success = false;
					}
					
					if(i == amount) {
						break;
					}
					
					game.getBoard()[x+(i+1)*vx][y] = game.getBoard()[x+i*vx][y];
					game.getBoard()[x+i*vx][y] = null;
				}
				
				game.getBoard()[x][y] = game.getBoard()[x+amount*vx][y];
				game.getBoard()[x+amount*vx][y] = null;
				
				if(!success) {
					return false;
				}
			}
		}
		else if(dx == 0) {
			amount = Math.abs(dy);
			
			if(amount != 1) {
				return false;
			}
		}
		else {
			amount = Math.abs(dx);
			
			if(amount != 1) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		int dx = dest_x - x;
		int dy = dest_y - y;
		int amount;

		if(dy == 0) {
			amount = Math.abs(dx);
			
			if(amount != 1) {
				if(dx > 0) {
					game.getBoard()[GameUtil.boardSize-1][dest_y].move(GameUtil.boardSize-1, dest_y, dest_x-1, dest_y, game);
				}
				else {
					game.getBoard()[0][dest_y].move(0, dest_y, dest_x+1, dest_y, game);
				}
				
				game.decInactivity();
			}
		}
		
		Piece[][] board = game.getBoard();
		
		if(board[dest_x][dest_y] == null) {
			game.incInactivity();
		}
		else {
			game.setInactivity(0);
		}

		board[dest_x][dest_y] = board[x][y];

		board[x][y] = null;
		
		((King)board[dest_x][dest_y]).setMoved(true);
		
		return true;
	}
	
	private boolean rookAt(int x, int y, Piece[][] board, PieceColor color) {
		if(GameUtil.validCoordinates(x, y)) {
			if(board[x][y] != null) {
				if(board[x][y].getType() == PieceType.Rook) {
					if(board[x][y].getColor() == color) {
						if(!((Rook)board[x][y]).getMoved()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean getMoved() {
		return this.moved;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	@Override
	public Piece makeCopy() {
		return new King(this.color, this.moved);
	}

	@Override
	public void calculateMoves(int x, int y, Game game, LinkedList<Move> queue) {
			int amount = 1;
		
			testMove(x, y, x+amount, y+amount, game, queue);
			testMove(x, y, x-amount, y+amount, game, queue);
			testMove(x, y, x+amount, y-amount, game, queue);
			testMove(x, y, x-amount, y-amount, game, queue);
			
			
			testMove(x, y, x, y+amount, game, queue);
			testMove(x, y, x, y-amount, game, queue);
			testMove(x, y, x-amount, y, game, queue);
			testMove(x, y, x+amount, y, game, queue);
			
			amount = 2;
			
			testMove(x, y, x-amount, y, game, queue);
			testMove(x, y, x+amount, y, game, queue);
	}

	@Override
	public boolean canMove(int x, int y, Game game) {
		int amount = 1;
		
		if(testMove(x, y, x+amount, y+amount, game, null)||
			testMove(x, y, x-amount, y+amount, game, null)||
			testMove(x, y, x+amount, y-amount, game, null)||
			testMove(x, y, x-amount, y-amount, game, null)||
			testMove(x, y, x, y+amount, game, null)||
			testMove(x, y, x, y-amount, game, null)||
			testMove(x, y, x-amount, y, game, null)||
			testMove(x, y, x+amount, y, game, null)) {
			return true;
		}
		
		amount = 2;
		
		if(testMove(x, y, x-amount, y, game, null)||
			testMove(x, y, x+amount, y, game, null)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean testMove(int x, int y, int dest_x, int dest_y, Game game, LinkedList<Move> queue) {
		boolean success = false;
		if(isMoveValid(x, y, dest_x, dest_y, game)) {
			Piece[][] board = game.getBoard();
			
			Piece beg = board[x][y].makeCopy();
			Piece end = board[dest_x][dest_y];
			int inac = game.getInactivity();
			
			Piece rook = null;
			int rook_x=-1;
			int rook_y=-1;
			int rook_dest_x=-1;
			int rook_dest_y=-1;
		
			int dx = dest_x - x;
			int dy = dest_y - y;
			int amount;

			if(dy == 0) {
				amount = Math.abs(dx);
				
				if(amount != 1) {
					if(dx > 0) {
						rook = board[GameUtil.boardSize-1][dest_y].makeCopy();
						rook_x = GameUtil.boardSize-1;
						rook_y = dest_y;
						rook_dest_x = dest_x-1;
						rook_dest_y = dest_y;
					}
					else {
						rook = board[0][dest_y].makeCopy();
						rook_x = 0;
						rook_y = dest_y;
						rook_dest_x = dest_x+1;
						rook_dest_y = dest_y;
					}
				}
			}
			
			////
			move(x, y, dest_x, dest_y, game);
			
			if(!game.playerInCheck(game.getTurn())) {
				if(queue != null) {
					queue.add(new Move(x, y, dest_x, dest_y));
				}
				success = true;
			}
			////
			
			board[x][y] = beg;
			board[dest_x][dest_y] = end;
			
			if(rook != null) {
				board[rook_dest_x][rook_dest_y] = null;
				board[rook_x][rook_y] = rook;
			}
			
			game.setInactivity(inac);
		}
		return success;
	}

}
