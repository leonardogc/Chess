package logic.game;

import java.io.Serializable;

import logic.pieces.Piece;
import logic.util.GameUtil.PieceType;

public class Move implements Serializable{
	public final int x;
	public final int y;
	public final int dest_x;
	public final int dest_y;
	public final PieceType type;
	
	public final int victim;
	public final int attacker;
	
	public Move(int x, int y, int dest_x, int dest_y) {
		this.x = x;
		this.y = y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		this.type = null;
		
		this.victim = 0;
		this.attacker = 0;
	}
	
	public Move(int x, int y, int dest_x, int dest_y, Piece victim, Piece attacker) {
		this.x = x;
		this.y = y;
		this.dest_x = dest_x;
		this.dest_y = dest_y;
		this.type = null;
		
		if(victim != null) {
			switch(victim.getType()) {
			case King:
				this.victim = 6;
				break;
			case Queen:
				this.victim = 5;
				break;
			case Rook:
				this.victim = 4;
				break;
			case Bishop:
				this.victim = 3;
				break;
			case Knight:
				this.victim = 2;
				break;
			case Pawn:
				this.victim = 1;
				break;
			default:
				this.victim = 0;
				break;
			}
		}
		else {
			this.victim = 0;
		}
		
		if(attacker != null) {
			switch(attacker.getType()) {
			case King:
				this.attacker = 6;
				break;
			case Queen:
				this.attacker = 5;
				break;
			case Rook:
				this.attacker = 4;
				break;
			case Bishop:
				this.attacker = 3;
				break;
			case Knight:
				this.attacker = 2;
				break;
			case Pawn:
				this.attacker = 1;
				break;
			default:
				this.attacker = 0;
				break;
			}
		}
		else {
			this.attacker = 0;
		}
	}
	
	public Move(PieceType type) {
		this.x = -1;
		this.y = -1;
		this.dest_x = -1;
		this.dest_y = -1;
		this.type = type;
		
		this.victim = 0;
		this.attacker = 0;
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
