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
		int buffer = 0;
		
		////

		if(color == PieceColor.White) {
			buffer = 1;
		}
		
		buffer = buffer << 11;
		
		result = result | buffer;
		
		////
		
		buffer = 0;
		
		switch(type) {
		case King:
			buffer = 0;
			break;
		case Rook:
			buffer = 1;
			break;
		case Queen:
			buffer = 2;
			break;
		case Pawn:
			buffer = 3;
			break;
		case Knight:
			buffer = 4;
			break;
		case Bishop:
			buffer = 5;
			break;
		}
		
		buffer = buffer << 8;
		
		result = result | buffer;
		
		////
		
		buffer = 0;

		if(var1) {
			buffer = 1;
		}

		buffer = buffer << 7;

		result = result | buffer;
		
		////
		
		buffer = 0;

		if(var2) {
			buffer = 1;
		}

		buffer = buffer << 6;

		result = result | buffer;
		
		////
		
		buffer = y * GameUtil.boardSize + x;
		
		result = result | buffer;
		
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
