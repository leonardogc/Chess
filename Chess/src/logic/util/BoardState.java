package logic.util;

import java.io.Serializable;
import java.util.Vector;

import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class BoardState implements Serializable{
	Vector<Integer> data;
	PieceColor turn;
	
	public BoardState() {
		this.data = new Vector<>();
		this.turn = null;
	}
	
	public void add(PieceColor color, PieceType type, boolean var1, boolean var2, int x, int y) {
		int result = 0;
		
		////

		if(color == PieceColor.White) {
			result = result | (1 << 11);
		}
		
		////
		
		int n = 0;
		
		switch(type) {
		case King:
			n = 0;
			break;
		case Rook:
			n = 1;
			break;
		case Queen:
			n = 2;
			break;
		case Pawn:
			n = 3;
			break;
		case Knight:
			n = 4;
			break;
		case Bishop:
			n = 5;
			break;
		}
		
		result = result | (n << 8);

		////

		if(var1) {
			result = result | (1 << 7);
		}
		
		////

		if(var2) {
			result = result | (1 << 6);
		}
		
		////
		
		result = result | (y * GameUtil.boardSize + x);
		
		////
		
		data.add(result);
	}
	
	public void add(PieceColor color) {
		this.turn = color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((turn == null) ? 0 : turn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		BoardState other = (BoardState) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} 
		else if (!data.equals(other.data)) {
			return false;
		}
		
		if (turn != other.turn) {
			return false;
		}
		return true;
	}
}
