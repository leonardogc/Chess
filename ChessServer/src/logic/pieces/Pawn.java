package logic.pieces;

import java.util.LinkedList;

import logic.game.Game;
import logic.game.Game.GameState;
import logic.game.Move;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Pawn extends Piece{
	
	private boolean moved;
	private boolean enPassant;
	private boolean enPassantVictim;

	public Pawn(PieceColor color) {
		super(PieceType.Pawn, color);
		this.moved = false;
		this.enPassant = false;
		this.enPassantVictim = false;
	}
	
	public Pawn(PieceColor color, boolean moved, boolean enPassant, boolean enPassantVictim) {
		super(PieceType.Pawn, color);
		this.moved=moved;
		this.enPassant = enPassant;
		this.enPassantVictim = enPassantVictim;
	}
	
	
	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		if(game.getBoard()[x][y].getType() != PieceType.Pawn) {
			return false;
		}
		
		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(dy == 0) {
			return false;
		}
		
		if(dy > 0 && game.getBoard()[x][y].getColor() != PieceColor.White) {
			return false;
		}
		
		if(dy < 0 && game.getBoard()[x][y].getColor() != PieceColor.Black) {
			return false;
		}
		
		int amount;
		boolean diagonal;

		if(dx == 0) {
			amount = Math.abs(dy);
			diagonal = false;
		}
		else {
			amount = Math.abs(dx);
			diagonal = true;
		}
		
		if(amount > 2) {
			return false;
		}
		
		if(!diagonal) {
			if(game.getBoard()[dest_x][dest_y] != null) {
				return false;
			}
			
			if(amount == 1) {
				//do nothing
			}
			else if(amount == 2) {
				if(((Pawn)game.getBoard()[x][y]).getMoved()) {
					return false;
				}
			}
		}
		else {
			if(amount == 1) {
				if(game.getBoard()[dest_x][dest_y] == null) {
					if(!((Pawn)game.getBoard()[x][y]).getEnPassant()) {
						return false;
					}
					
					if(!pawnAt(dest_x, y, game.getBoard(), game.getBoard()[x][y].getColor().change())) {
						return false;
					}
					
					if(!((Pawn)game.getBoard()[dest_x][y]).getEnPassantVictim()) {
						return false;
					}
					
				}
			}
			else if(amount == 2){
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
		boolean diagonal;

		if(dx == 0) {
			amount = Math.abs(dy);
			diagonal = false;
		}
		else {
			amount = Math.abs(dx);
			diagonal = true;
		}
		

		if(!diagonal) {
			if(amount == 2) {

				if(pawnAt(dest_x + 1, dest_y, game.getBoard(), game.getBoard()[x][y].getColor().change())) {
					((Pawn) game.getBoard()[dest_x + 1][dest_y]).setEnPassant(true);
					((Pawn) game.getBoard()[x][y]).setEnPassantVictim(true);
				}
				
				if(pawnAt(dest_x - 1, dest_y, game.getBoard(), game.getBoard()[x][y].getColor().change())) {
					((Pawn) game.getBoard()[dest_x - 1][dest_y]).setEnPassant(true);
					((Pawn) game.getBoard()[x][y]).setEnPassantVictim(true);
				}

			}
		}
		else {
			if(game.getBoard()[dest_x][dest_y] == null) {
				game.getBoard()[dest_x][y] = null;
			}
		}


		Piece[][] board = game.getBoard();

		board[dest_x][dest_y] = board[x][y];

		board[x][y] = null;
		
		((Pawn)board[dest_x][dest_y]).setMoved(true);
		
		if(dest_y == 0 || dest_y == GameUtil.boardSize - 1) {
			game.setState(GameState.ChoosingPiece);
		}
		
		return true;
	}
	
	private boolean pawnAt(int x, int y, Piece[][] board, PieceColor color) {
		if(GameUtil.validCoordinates(x, y)) {
			if(board[x][y] != null) {
				if(board[x][y].getType() == PieceType.Pawn) {
					if(board[x][y].getColor() == color) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void setEnPassant(boolean enPassant) {
		this.enPassant = enPassant;
	}
	
	public void setEnPassantVictim(boolean enPassantVictim) {
		this.enPassantVictim = enPassantVictim;
	}
	
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	public boolean getEnPassantVictim(){
		return this.enPassantVictim;
	}
	
	public boolean getEnPassant(){
		return this.enPassant;
	}
	
	public boolean getMoved() {
		return this.moved;
	}
	
	@Override
	public Piece makeCopy() {
		return new Pawn(this.color, this.moved, this.enPassant, this.enPassantVictim);
	}

	@Override
	public void calculateMoves(int x, int y, Game game, LinkedList<Move> queue) {
		addMove(x, y, x, y+1, game, queue);
		addMove(x, y, x, y+2, game, queue);
		addMove(x, y, x+1, y+1, game, queue);
		addMove(x, y, x-1, y+1, game, queue);
		
		addMove(x, y, x, y-1, game, queue);
		addMove(x, y, x, y-2, game, queue);
		addMove(x, y, x+1, y-1, game, queue);
		addMove(x, y, x-1, y-1, game, queue);
	}

}
