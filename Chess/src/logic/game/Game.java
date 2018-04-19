package logic.game;

import java.util.LinkedList;

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
import logic.util.GameUtil.PieceType;

public class Game {
	
	public enum GameState {
		RegularMove, ChoosingPiece;
	}
	
	private Piece[][] board;
	private Player white_player;
	private Player black_player;
	private PieceColor turn;
	private GameState state;
	
	public Game() {
		this.white_player = new Player(PieceColor.White);
		this.black_player = new Player(PieceColor.Black);
		this.turn = PieceColor.White;
		this.state = GameState.RegularMove;
		initialize_board();
	}
	
	public Game(Piece[][] board, Player white_player, Player black_player, PieceColor turn, GameState state) {
		this.board = board;
		this.white_player = white_player;
		this.black_player = black_player;
		this.turn = turn;
		this.state = state;
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
	
	public void setTurn(PieceColor turn) {
		this.turn = turn;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}

	public boolean move(int x, int y, int dest_x, int dest_y) {
		LinkedList<Move> moves = calculateMoves();
		Move move = new Move(x, y, dest_x, dest_y);
		
		while(moves.size() > 0) {
			if(move.equals(moves.poll())){
				applyMove(move);
				return true;
			}
		}
		
		return false;
	}

	public boolean playerInCheck(PieceColor color) {
		int king_x = -1;
		int king_y = -1;

		search: {
			for(int y = 0; y < GameUtil.boardSize; y++) {
				for(int x = 0; x < GameUtil.boardSize; x++) {
					if(this.board[x][y] != null) {
						if(this.board[x][y].getColor() == color && this.board[x][y].getType() == PieceType.King) {
							king_x = x;
							king_y = y;
							break search;
						}
					}
				}
			}
		}
		
		if(king_x == -1 || king_y == -1) {
			System.out.println("There is no king on the board!");
			return false;
		}
		
		for(int y = 0; y < GameUtil.boardSize; y++) {
			for(int x = 0; x < GameUtil.boardSize; x++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getColor() == color.change()) {
						if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public Game makeCopy() {
		Piece[][] board = new Piece[GameUtil.boardSize][GameUtil.boardSize];

		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(this.board[x][y] != null) {
					board[x][y] = this.board[x][y].makeCopy();
				}
			}
		}
		
		return new Game(board, this.white_player.makeCopy(), this.black_player.makeCopy(), this.turn, this.state);
	}

	public LinkedList<Move> calculateMoves(){
		LinkedList<Move> queue = new LinkedList<>();

		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getColor() == turn) {

						for(int dest_y=0; dest_y < GameUtil.boardSize; dest_y++) {
							for(int dest_x=0; dest_x < GameUtil.boardSize; dest_x++) {
								Game game_copy = this.makeCopy();
								if(game_copy.applyMove(new Move(x, y, dest_x, dest_y))) {
									if(!game_copy.playerInCheck(turn)) {
										queue.add(new Move(x, y, dest_x, dest_y));
									}
								}
							}
						}


					}
				}
			}
		}

		
		return queue;
	}
	
	
	public boolean applyMove(Move move) {
		if(this.board[move.x][move.y].move(move.x, move.y, move.dest_x, move.dest_y, this)) {
			changeTurns();
			return true;
		}
		
		return false;
	}

	private void changeTurns() {
		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getType() == PieceType.Pawn) {
						if(this.board[x][y].getColor() == turn) {
							((Pawn)this.board[x][y]).setEnPassant(false);
						}
						else {
							((Pawn)this.board[x][y]).setEnPassantVictim(false);
						}
					}
				}
			}
		}
		
		this.turn = turn.change();
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		
		System.out.println(g.calculateMoves().size());
		
	}
}
