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
		boolean pieceCaptured = false;
		
		Piece[][] board = game.getBoard();
		
		if(board[dest_x][dest_y] == null) {
			game.incInactivity();
		}
		else {
			game.setInactivity(0);
			pieceCaptured = true;
		}

		board[dest_x][dest_y] = board[x][y];

		board[x][y] = null;
		
		return pieceCaptured;
	}

	@Override
	public Piece makeCopy() {
		return new Knight(this.color);
	}

	@Override
	public void calculateMoves(int x, int y, Game game, LinkedList<Move> queueFront, LinkedList<Move> queueBack) {
		testMove(x, y, x+2, y+1, game, queueFront, queueBack);
		testMove(x, y, x+1, y+2, game, queueFront, queueBack);
		testMove(x, y, x-2, y+1, game, queueFront, queueBack);
		testMove(x, y, x-1, y+2, game, queueFront, queueBack);
		testMove(x, y, x+2, y-1, game, queueFront, queueBack);
		testMove(x, y, x+1, y-2, game, queueFront, queueBack);
		testMove(x, y, x-2, y-1, game, queueFront, queueBack);
		testMove(x, y, x-1, y-2, game, queueFront, queueBack);
	}
	
	@Override
	public boolean canMove(int x, int y, Game game) {
		if(testMove(x, y, x+2, y+1, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x+1, y+2, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x-2, y+1, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x-1, y+2, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x+2, y-1, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x+1, y-2, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x-2, y-1, game, null, null)) {
			return true;
		}
		
		if(testMove(x, y, x-1, y-2, game, null, null)) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean testKingNotCheck(int x, int y, int dest_x, int dest_y, Game game, LinkedList<Move> queueFront, LinkedList<Move> queueBack) {
		boolean success = false;
		
		Piece[][] board = game.getBoard();
		
		Piece beg = board[x][y];
		Piece end = board[dest_x][dest_y];
		int inac = game.getInactivity();
		
		boolean pieceCaptured = move(x, y, dest_x, dest_y, game);
		
		if(!game.playerInCheck(game.getTurn())) {
			if(queueFront != null && queueBack != null) {
				if(pieceCaptured) {
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
