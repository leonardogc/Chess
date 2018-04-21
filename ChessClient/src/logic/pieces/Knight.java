package logic.pieces;

import java.util.LinkedList;

import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Knight extends Piece{

	public Knight(PieceColor color) {
		super(PieceType.Knight, color);
	}

	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.validCoordinates(x, y) || !GameUtil.validCoordinates(dest_x, dest_y)) {
			return false;
		}
		
		if(game.getBoard()[x][y]==null) {
			return false;
		}
		
		if(game.getBoard()[x][y].getType() != PieceType.Knight) {
			return false;
		}
		
		if(game.getBoard()[dest_x][dest_y] != null) {
			if(game.getBoard()[x][y].getColor() == game.getBoard()[dest_x][dest_y].getColor()) {
				return false;
			}
		}

		int dx = dest_x - x;
		int dy = dest_y - y;

		if(!((Math.abs(dx) == 2 && Math.abs(dy) == 1) || (Math.abs(dx) == 1 && Math.abs(dy) == 2)))  {
			return false;
		}

		return true;
	}
	
	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		Piece[][] board = game.getBoard();

		board[dest_x][dest_y] = board[x][y];

		board[x][y] = null;
		
		return true;
	}

	@Override
	public Piece makeCopy() {
		return new Knight(this.color);
	}

	@Override
	public void calculateMoves(int x, int y, Game game, LinkedList<Move> queue) {
		addMove(x, y, x+2, y+1, game, queue);
		addMove(x, y, x+1, y+2, game, queue);
		addMove(x, y, x-2, y+1, game, queue);
		addMove(x, y, x-1, y+2, game, queue);
		addMove(x, y, x+2, y-1, game, queue);
		addMove(x, y, x+1, y-2, game, queue);
		addMove(x, y, x-2, y-1, game, queue);
		addMove(x, y, x-1, y-2, game, queue);
	}

}
