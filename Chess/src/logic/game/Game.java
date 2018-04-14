package logic.game;

import logic.board.Board;
import logic.player.Player;
import logic.util.GameUtil.PieceColor;

public class Game {
	
	private Board board;
	private Player white_player;
	private Player black_player;
	private PieceColor turn;
	
	public Game() {
		this.board = new Board();
		this.white_player = new Player(PieceColor.White);
		this.black_player = new Player(PieceColor.Black);
		this.turn = PieceColor.White;
	}
	
}
