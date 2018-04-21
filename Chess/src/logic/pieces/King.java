package logic.pieces;

import logic.game.Game;
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
					if(!rookAt(GameUtil.boardSize - 1, y , game.getBoard(), game.getBoard()[x][y].getColor())) {
						return false;
					}
					
					if(!GameUtil.isBoardFree(game.getBoard(), x+1, y, GameUtil.boardSize - 2, y)) {
						return false;
					}
				}
				else {
					if(!rookAt(0, y , game.getBoard(), game.getBoard()[x][y].getColor())) {
						return false;
					}
					
					if(!GameUtil.isBoardFree(game.getBoard(), x-1, y, 1, y)) {
						return false;
					}
				}
				
				int vx = dx/amount;
				Game copy = game.makeCopy();
				
				for(int i = 0; i <= amount; i++) {
					if(copy.playerInCheck(game.getBoard()[x][y].getColor())) {
						return false;
					}
					
					if(i == amount) {
						break;
					}
					
					copy.getBoard()[x+(i+1)*vx][y] = copy.getBoard()[x+i*vx][y];
					copy.getBoard()[x+i*vx][y] = null;
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
		if(!isMoveValid(x, y, dest_x, dest_y, game)){
			return false;
		}
		
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
			}
		}
		
		Piece[][] board = game.getBoard();

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
}
