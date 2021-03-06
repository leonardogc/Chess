package logic.pieces;

import java.io.Serializable;
import java.util.ArrayList;

import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public abstract class Piece implements Serializable{
	protected PieceType type;
	protected PieceColor color;
	
	public Piece(PieceType type, PieceColor color) {
		this.type = type;
		this.color = color;
	}
	
	public abstract boolean isMoveValid(int x, int y, int dest_x, int dest_y, Game game);
	public abstract boolean move(int x, int y, int dest_x, int dest_y, Game game);
	
	public abstract void calculateMoves(int x, int y, Game game, ArrayList<Move> queue, boolean deepSort);
	public abstract boolean canMove(int x, int y, Game game);
	
	public boolean testMove(int x, int y, int dest_x, int dest_y, Game game, ArrayList<Move> queue, boolean deepSort) {
		if(isMoveValid(x, y, dest_x, dest_y, game)) {
			if(testKingNotCheck(x, y, dest_x, dest_y, game, queue, deepSort)) {
				return true;
			}
		}
		
		return false;	
	}
	
	public abstract boolean testKingNotCheck(int x, int y, int dest_x, int dest_y, Game game, ArrayList<Move> queue, boolean deepSort);
	
	public abstract Piece makeCopy();
	
	public PieceType getType() {
		return this.type;
	}
	
	public PieceColor getColor() {
		return this.color;
	}
	
	/*protected void addMove(int x, int y, int dest_x, int dest_y, Game game, LinkedList<Move> queue) {
		if(isMoveValid(x, y, dest_x, dest_y, game)) {
			Game copy = game.makeCopy();
			
			copy.getBoard()[x][y].move(x, y, dest_x, dest_y, copy);
			
			if(!copy.playerInCheck(game.getTurn())) {
				queue.add(new Move(x, y, dest_x, dest_y));
			}
		}
	}*/
}
