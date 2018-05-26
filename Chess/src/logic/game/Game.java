package logic.game;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import logic.pieces.Bishop;
import logic.pieces.King;
import logic.pieces.Knight;
import logic.pieces.Pawn;
import logic.pieces.Piece;
import logic.pieces.Queen;
import logic.pieces.Rook;
import logic.util.BoardState;
import logic.util.GameUtil;
import logic.util.GameUtil.PieceColor;
import logic.util.GameUtil.PieceType;

public class Game implements Serializable{
	
	public enum GameState {
		RegularMove, ChoosingPiece;
	}
	
	private Piece[][] board;
	private PieceColor turn;
	private GameState state;
	private HashMap<BoardState, Integer> positions;
	private LinkedList<Pawn> enPassant;
	private int inactivity;
	private boolean tie;
	
	public Game() {
		this.turn = PieceColor.White;
		this.state = GameState.RegularMove;
		this.positions = new HashMap<>();
		this.enPassant = new LinkedList<>();
		this.inactivity = 0;
		this.tie = false;
		initialize_board();
	}
	
	public Game(Piece[][] board, PieceColor turn, GameState state, HashMap<BoardState, Integer> positions, int inactivity, boolean tie, LinkedList<Pawn> enPassant) {
		this.board = board;
		this.turn = turn;
		this.state = state;
		this.positions = positions;
		this.inactivity = inactivity;
		this.tie = tie;
		this.enPassant = enPassant;
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
	
	public LinkedList<Pawn> getEnPassant() {
		return this.enPassant;
	}
	
	public PieceColor getTurn() {
		return this.turn;
	}
	
	public GameState getState() {
		return this.state;
	}
	
	public int getInactivity() {
		return this.inactivity;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	public void setInactivity(int inactivity) {
		this.inactivity = inactivity;
	}
	
	public void setEnPassant(LinkedList<Pawn> enPassant) {
		this.enPassant = enPassant;
	}
	
	public void incInactivity() {
		this.inactivity++;
	}
	
	public void decInactivity() {
		this.inactivity--;
	}
	
	public boolean move(Move move) {
		LinkedList<Move> moves = calculateMoves();
		
		while(moves.size() > 0) {
			if(move.equals(moves.poll())){
				applyMove(move);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean promotePawn(PieceType piece) {
		LinkedList<Move> moves = calculateMoves();
		Move move = new Move(piece);
		
		while(moves.size() > 0) {
			if(move.equals(moves.poll())){
				applyMove(move);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean noAvailableMoves() {
		if(this.state == GameState.ChoosingPiece) {
			return false;
		}

		for(int x=0; x < GameUtil.boardSize; x++) {
			for(int y=0; y < GameUtil.boardSize; y++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getColor() == this.turn) {
						if(this.board[x][y].canMove(x, y, this)) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public boolean playerInCheck(PieceColor color) {
		int king_x = -1;
		int king_y = -1;

		search: {

			for(int x = 0; x < GameUtil.boardSize; x++) {
				for(int y = 0; y < GameUtil.boardSize; y++) {
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


		for(int x = 0; x < GameUtil.boardSize; x++) {
			for(int y = 0; y < GameUtil.boardSize; y++) {
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
	
	public boolean tie() {
		return this.tie;
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
		
		return new Game(board, this.turn, this.state, new HashMap<>(this.positions), this.inactivity, this.tie, new LinkedList<>(this.enPassant));
	}
		
	public LinkedList<Move> calculateMoves(){
		LinkedList<Move> queue = new LinkedList<>();

		if(this.state == GameState.RegularMove) {

			for(int x=0; x < GameUtil.boardSize; x++) {
				for(int y=0; y < GameUtil.boardSize; y++) {
					if(this.board[x][y] != null) {
						if(this.board[x][y].getColor() == this.turn) {
							this.board[x][y].calculateMoves(x, y, this, queue);
						}
					}
				}
			}

		}
		else if(this.state == GameState.ChoosingPiece) {
			queue.add(new Move(PieceType.Rook));
			queue.add(new Move(PieceType.Knight));
			queue.add(new Move(PieceType.Queen));
			queue.add(new Move(PieceType.Bishop));
		}

		Collections.shuffle(queue);

		return queue;
	}
	
	public Move generateRandomMove(){
		LinkedList<Move> queue = new LinkedList<>();

		if(this.state == GameState.RegularMove) {
			
			LinkedList<int[]> pieces = new LinkedList<>();

			for(int x=0; x < GameUtil.boardSize; x++) {
				for(int y=0; y < GameUtil.boardSize; y++) {
					if(this.board[x][y] != null) {
						if(this.board[x][y].getColor() == this.turn) {
							pieces.add(new int[] {x, y});
						}
					}
				}
			}
			
			Collections.shuffle(pieces);
			
			while(queue.size() == 0) {
				int[] coords = pieces.poll();
				this.board[coords[0]][coords[1]].calculateMoves(coords[0], coords[1], this, queue);
			}
		}
		else if(this.state == GameState.ChoosingPiece) {
			queue.add(new Move(PieceType.Rook));
			queue.add(new Move(PieceType.Knight));
			queue.add(new Move(PieceType.Queen));
			queue.add(new Move(PieceType.Bishop));
		}

		Collections.shuffle(queue);

		return queue.get(0);
	}


	public boolean applyMove(Move move) {
		if(move.type != null) {
			int y;

			if(this.turn == PieceColor.White) {
				y = GameUtil.boardSize - 1;
			}
			else {
				y = 0;
			}

			for(int x=0; x < GameUtil.boardSize; x++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getType() == PieceType.Pawn) {
						if(this.board[x][y].getColor() == this.turn) {
							switch(move.type) {
							case Bishop:
								this.board[x][y] = new Bishop(this.turn);
								break;
							case Queen:
								this.board[x][y] = new Queen(this.turn);
								break;
							case Knight:
								this.board[x][y] = new Knight(this.turn);
								break;
							case Rook:
								this.board[x][y] = new Rook(this.turn);
								break;
							default:
								return false;
							}

							break;
						}
					}
				}
			}

			this.state = GameState.RegularMove;
			changeTurns();
			updateTie();
		}
		else {
			this.board[move.x][move.y].move(move.x, move.y, move.dest_x, move.dest_y, this);

			if(this.state != GameState.ChoosingPiece) {
				changeTurns();
				updateTie();
			}
		}
		
		return true;
	}
	
	private void updateTie() {
		BoardState bs = new BoardState();
		
		bs.build(this);
		
		Integer amount = this.positions.get(bs);
		
		if(amount == null) {
			amount = 0;
		}
		
		this.positions.put(bs, amount + 1);
		
		if(amount + 1 >= GameUtil.repToTie) {
			this.tie = true;
		}
		
		if(this.inactivity >= GameUtil.inacToTie) {
			this.tie = true;
		}
	}

	private void changeTurns() {
		for(int i = 0; i < this.enPassant.size(); i++) {
			if(this.enPassant.get(i).getColor() == turn) {
				this.enPassant.get(i).setEnPassant(false);
			}
			else {
				this.enPassant.get(i).setEnPassantVictim(false);
			}
			
			if(!this.enPassant.get(i).getEnPassant() && !this.enPassant.get(i).getEnPassantVictim()) {
				this.enPassant.remove(i);
				i--;
			}
		}
		
		this.turn = turn.change();
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		
		System.out.println(g.calculateMoves().size());
		
	}
}
