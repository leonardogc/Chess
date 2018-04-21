package logic.pieces;

import java.util.LinkedList;

import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Queen extends Piece{

	public Queen(PieceColor color) {
		super(PieceType.Queen, color);
	}

	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		if(game.getBoard()[x][y].getType() != PieceType.Queen) {
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
		return new Queen(this.color);
	}

	@Override
	public void calculateMoves(int x, int y, Game game, LinkedList<Move> queue) {
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			addMove(x, y, x+amount, y+amount, game, queue);
			addMove(x, y, x-amount, y+amount, game, queue);
			addMove(x, y, x+amount, y-amount, game, queue);
			addMove(x, y, x-amount, y-amount, game, queue);
			
			addMove(x, y, x+amount, y, game, queue);
			addMove(x, y, x-amount, y, game, queue);
			addMove(x, y, x, y+amount, game, queue);
			addMove(x, y, x, y-amount, game, queue);
		}
	}

}
