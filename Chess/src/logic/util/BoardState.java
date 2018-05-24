package logic.util;

import java.io.Serializable;
import java.util.Vector;

import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class BoardState implements Serializable{
	Vector<Long> data;
	PieceColor turn;
	int bitsUsed;
	
	public BoardState() {
		this.data = new Vector<>();
		this.bitsUsed = 0;
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
		
		long n;
		
		if(data.size() == 0) {
			n = 0;
		}
		else if(bitsUsed < 64){
			n = data.lastElement();
			n = data.remove(data.size() - 1);
		}
		else {
			n = 0;
			bitsUsed = 0;
		}
		
		if(64-bitsUsed < 12) {
			int remaining = 64-bitsUsed;
			n = n << remaining;
			n = n | result;
		}
		else{
			
		}
		
		
	}
	
	public void add(PieceColor color) {
		this.turn = color;
	}
}
