package logic.pieces;

import logic.game.Game;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Pawn extends Piece{
	
	private boolean moved;

	public Pawn(PieceColor color) {
		super(PieceType.Pawn, color);
		this.moved=false;
	}
	
	public Pawn(PieceColor color, boolean moved) {
		super(PieceType.Pawn, color);
		this.moved=moved;
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
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
				if(moved) {
					return false;
				}

				if(GameUtil.validCoordinates(dest_x + 1, dest_y)) {
					if(game.getBoard()[dest_x + 1][dest_y] != null) {
						if(game.getBoard()[dest_x + 1][dest_y].getType() == PieceType.Pawn) {
							if(game.getBoard()[dest_x + 1][dest_y].getColor() != game.getBoard()[x][y].getColor()) {

								if(game.getBoard()[dest_x + 1][dest_y].getColor() == PieceColor.White) {
									game.getWhitePlayer().setEnPassant(true);
								}
								else {
									game.getBlackPlayer().setEnPassant(true);
								}

							}
						}
					}
				}

				if(GameUtil.validCoordinates(dest_x - 1, dest_y)) {
					if(game.getBoard()[dest_x - 1][dest_y] != null) {
						if(game.getBoard()[dest_x - 1][dest_y].getType() == PieceType.Pawn) {
							if(game.getBoard()[dest_x - 1][dest_y].getColor() != game.getBoard()[x][y].getColor()) {

								if(game.getBoard()[dest_x - 1][dest_y].getColor() == PieceColor.White) {
									game.getWhitePlayer().setEnPassant(true);
								}
								else {
									game.getBlackPlayer().setEnPassant(true);
								}

							}
						}
					}
				}

			}
		}
		else {
			if(amount == 1) {
				if(game.getBoard()[dest_x][dest_y] == null) {
					if(game.getBoard()[x][y].getColor() == PieceColor.White) {
						if(!game.getWhitePlayer().getEnPassant()) {
							return false;
						}
					}
					else {
						if(!game.getBlackPlayer().getEnPassant()) {
							return false;
						}
					}
					
					if(game.getBoard()[dest_x][y] == null) {
						return false;
					}
					
					if(game.getBoard()[dest_x][y].getType() != PieceType.Pawn) {
						return false;
					}
					
					if(game.getBoard()[dest_x][y].getColor() == game.getBoard()[x][y].getColor()) {
						return false;
					}
					
					game.getBoard()[dest_x][y] = null;
				}
			}
			else if(amount == 2) {
				return false;
			}
		}
		
		
		Piece[][] board = game.getBoard();
		
		board[dest_x][dest_y] = board[x][y];
		
		board[x][y] = null;
		
		this.moved = true;
		
		return true;
	}
	
	@Override
	public Piece makeCopy() {
		return new Pawn(this.color, this.moved);
	}
}
