package logic.player;

import logic.util.GameUtil.PieceColor;

public class Player {
	private PieceColor color;
 	
	public Player(PieceColor color) {
		this.color = color;
	}
	
	public Player makeCopy() {
		return new Player(this.color);
	}
}
