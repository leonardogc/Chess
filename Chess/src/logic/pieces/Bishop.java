package logic.pieces;

import java.util.LinkedList;

import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Bishop extends Piece{

	public Bishop(PieceColor color) {
		super(PieceType.Bishop, color);
	}

	@Override
	public boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game) {
		if(!GameUtil.isPathValid(game.getBoard(), x, y, dest_x, dest_y)) {
			return false;
		}
		
		if(game.getBoard()[x][y].getType() != PieceType.Bishop) {
			return false;
		}
		
		int dx = dest_x - x;
		int dy = dest_y - y;
		
		if(!(Math.abs(dx) == Math.abs(dy))) {
			return false;
		}

		return true;
	}

	@Override
	public boolean move(int x, int y, int dest_x, int dest_y, Game game) {
		Piece[][] board = game.getBoard();
		
		if(board[dest_x][dest_y] == null) {
			game.incInactivity();
		}
		else {
			game.setInactivity(0);
		}

		board[dest_x][dest_y] = board[x][y];

		board[x][y] = null;
		
		return true;
	}

	@Override
	public Piece makeCopy() {
		return new Bishop(this.color);
	}

	@Override
	public void calculateMoves(int x, int y, Game game, LinkedList<Move> queueFront, LinkedList<Move> queueBack) {
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x+amount, y+amount, game)) {
				testKingNotCheck(x, y, x+amount, y+amount, game, queueFront, queueBack);
			}
			else {
				break;
			}
		}
		
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x-amount, y+amount, game)) {
				testKingNotCheck(x, y, x-amount, y+amount, game, queueFront, queueBack);
			}
			else {
				break;
			}
		}
		
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x+amount, y-amount, game)) {
				testKingNotCheck(x, y, x+amount, y-amount, game, queueFront, queueBack);
			}
			else {
				break;
			}
		}
		
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x-amount, y-amount, game)) {
				testKingNotCheck(x, y, x-amount, y-amount, game, queueFront, queueBack);
			}
			else {
				break;
			}
		}
	}

	@Override
	public boolean canMove(int x, int y, Game game) {
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x+amount, y+amount, game)) {
				if(testKingNotCheck(x, y, x+amount, y+amount, game, null, null)){
					return true;
				}
			}
			else {
				break;
			}
		}
		
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x-amount, y+amount, game)) {
				if(testKingNotCheck(x, y, x-amount, y+amount, game, null, null)){
					return true;
				}
			}
			else {
				break;
			}
		}
		
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x+amount, y-amount, game)) {
				if(testKingNotCheck(x, y, x+amount, y-amount, game, null, null)){
					return true;
				}
			}
			else {
				break;
			}
		}
		
		for(int amount = 1; amount < GameUtil.boardSize; amount++) {
			if(isMoveValid(x, y, x-amount, y-amount, game)) {
				if(testKingNotCheck(x, y, x-amount, y-amount, game, null, null)){
					return true;
				}
			}
			else {
				break;
			}
		}

		return false;
	}

	@Override
	public boolean testKingNotCheck(int x, int y, int dest_x, int dest_y, Game game,  LinkedList<Move> queueFront, LinkedList<Move> queueBack) {
		boolean success = false;
		
		Piece[][] board = game.getBoard();
		
		Piece beg = board[x][y];
		Piece end = board[dest_x][dest_y];
		int inac = game.getInactivity();
		
		move(x, y, dest_x, dest_y, game);
		
		if(!game.playerInCheck(game.getTurn())) {
			if(queueFront != null && queueBack != null) {
				if(game.getInactivity() == 0) {
					queueFront.addLast(new Move(x, y, dest_x, dest_y));
				}
				else {
					queueBack.addLast(new Move(x, y, dest_x, dest_y));
				}
			}
			success = true;
		}
		
		board[x][y] = beg;
		board[dest_x][dest_y] = end;
		game.setInactivity(inac);
		
		return success;
	}

}
