package logic.game;

public class Move {
	public enum MoveType{
		RegularMove, Castling, EnPassant;
	}
	
	public final int x;
	public final int y;
	public final int dest_x;
	public final int dest_y;
	public final MoveType type;
	
	public Move(int x, int y, int dest_x, int dest_y, MoveType type) {
		this.x = x;
		this.y = y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		this.type = type;
	}
	
	public Move(int x, int y) {
		this.x = x;
		this.y = y;
		this.dest_x = -1;
		this.dest_y = -1;
		this.type = MoveType.Castling;
	}
}
