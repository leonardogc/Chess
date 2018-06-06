package logic.util;

import java.io.Serializable;
import java.util.ArrayList;

import logic.game.Game;
import logic.pieces.Pawn;
import logic.util.GameUtil.PieceColor;

public class BoardState implements Serializable{
	ArrayList<Long> data;
	PieceColor turn;
	
	public BoardState() {
		this.data = new ArrayList<>();
		this.turn = null;
	}
	
	public void build(Game game) {
		this.turn = game.getTurn();
		
		int counter = 0;
		long number = 0;
		
		for(int x=0; x < GameUtil.boardSize; x++) {
			for(int y=0; y < GameUtil.boardSize; y++) {
				if(game.getBoard()[x][y] != null) {
					int result = 0;
					
					////

					if(game.getBoard()[x][y].getColor() == PieceColor.White) {
						result |= 1 << 11;
					}
					
					////
					
					int n = 0;
					
					switch(game.getBoard()[x][y].getType()) {
					case King:
						n = 0;
						if(game.getBoard()[x][y].testMove(x, y, x-2, y, game, null)) {
							result |= 1 << 7;
						}
						
						if(game.getBoard()[x][y].testMove(x, y, x+2, y, game, null)) {
							result |= 1 << 6;
						}
						break;
					case Rook:
						n = 1;
						break;
					case Queen:
						n = 2;
						break;
					case Pawn:
						n = 3;
						if(((Pawn)game.getBoard()[x][y]).getEnPassant()) {
							result |= 1 << 7;
						}
						
						if(((Pawn)game.getBoard()[x][y]).getEnPassantVictim()) {
							result |= 1 << 6;
						}
						break;
					case Knight:
						n = 4;
						break;
					case Bishop:
						n = 5;
						break;
					}
					
					result |= n << 8;
					
					////
					
					result |= y * GameUtil.boardSize + x;
					
					////
					
					number <<= 12;
					number |= result;
					
					counter++;
					
					if(counter == 5) {
						this.data.add(number);
						number = 0;
						counter = 0;
					}
					
				}
			}
		}	
		
		if(counter != 0) {
			this.data.add(number);
		}
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
