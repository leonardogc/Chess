package logic.game;

import java.io.Serializable;

import logic.util.GameUtil.PieceType;

public class Move implements Serializable{
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
	
	public void print() {
		if(this.type == null) {
			System.out.println("Moved from ("+this.x+", "+this.y+") to ("+this.dest_x+", "+this.dest_y+")");
		}
		else {
			System.out.println("Promoted to "+ type);
		}
	}
}
