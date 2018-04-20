package logic.game;

import logic.util.GameUtil.PieceType;

public class Move {
	public final int x;
	public final int y;
	public final int dest_x;
	public final int dest_y;
	public final PieceType type;
	
	public Move(int x, int y, int dest_x, int dest_y) {
		this.x = x;
		this.y = y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		this.type = null;
	}
	
	public Move(PieceType type) {
		this.x = -1;
		this.y = -1;
		this.dest_x = -1;
		this.dest_y = -1;
		this.type = type;
	}
	
	public boolean equals(Move m) {
		if(this.x != m.x) {
			return false;
		}
		
		if(this.y != m.y) {
			return false;
		}
		
		if(this.dest_x != m.dest_x) {
			return false;
		}
		
		if(this.dest_y != m.dest_y) {
			return false;
		}
		
		if(this.type != m.type) {
			return false;
		}
		
		return true;
	}
}
