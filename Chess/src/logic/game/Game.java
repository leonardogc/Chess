package logic.game;

import logic.pieces.Bishop;
import logic.pieces.King;
import logic.pieces.Knight;
import logic.pieces.Pawn;
import logic.pieces.Piece;
import logic.pieces.Queen;
import logic.pieces.Rook;
import logic.player.Player;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;

public class Game {
	
	private Piece[][] board;
	private Player white_player;
	private Player black_player;
	private PieceColor turn;
	
	public Game() {
		this.white_player = new Player(PieceColor.White);
		this.black_player = new Player(PieceColor.Black);
		this.turn = PieceColor.White;
		initialize_board();
	}

	private void initialize_board() {
		this.board = new Piece[GameUtil.boardSize][GameUtil.boardSize];
		
		//add white pawns
		for(int x=0; x < GameUtil.boardSize; x++) {
			board[x][1]=new Pawn(PieceColor.White);
		}

		//add white rooks
		board[0][0] = new Rook(PieceColor.White);
		board[7][0] = new Rook(PieceColor.White);

		//add white knights
		board[1][0] = new Knight(PieceColor.White);
		board[6][0] = new Knight(PieceColor.White);

		//add white bishops
		board[2][0] = new Bishop(PieceColor.White);
		board[5][0] = new Bishop(PieceColor.White);

		//add white queen
		board[3][0] = new Queen(PieceColor.White);

		//add white king
		board[4][0] = new King(PieceColor.White);



		//add black pawns
		for(int x=0; x < GameUtil.boardSize; x++) {
			board[x][6]=new Pawn(PieceColor.Black);
		}

		//add black rooks
		board[0][7] = new Rook(PieceColor.Black);
		board[7][7] = new Rook(PieceColor.Black);

		//add black knights
		board[1][7] = new Knight(PieceColor.Black);
		board[6][7] = new Knight(PieceColor.Black);

		//add black bishops
		board[2][7] = new Bishop(PieceColor.Black);
		board[5][7] = new Bishop(PieceColor.Black);

		//add black queen
		board[3][7] = new Queen(PieceColor.Black);

		//add black king
		board[4][7] = new King(PieceColor.Black);
	}
	
	
	public Piece[][] getBoard() {
		return this.board;
	}
	
	public Player getWhitePlayer() {
		return this.white_player;
	}
	
	public Player getBlackPlayer() {
		return this.black_player;
	}
	
	public PieceColor getTurn() {
		return this.turn;
	}
	
	public boolean move(int x, int y, int dest_x, int dest_y) {
		return this.board[x][y].move(x, y, dest_x, dest_y, this);
	}
}
