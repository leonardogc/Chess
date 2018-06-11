package logic.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
	private ArrayList<Pawn> enPassant;
	private int inactivity;
	private boolean tie;
	
	private int wk_x;
	private int wk_y;
	private int bk_x;
	private int bk_y;
	
	public Game() {
		this.turn = PieceColor.White;
		this.state = GameState.RegularMove;
		this.positions = new HashMap<>();
		this.enPassant = new ArrayList<>();
		this.inactivity = 0;
		this.tie = false;
		initialize_board();
	}
	
	public Game(Piece[][] board, PieceColor turn, GameState state, HashMap<BoardState, Integer> positions, 
			int inactivity, boolean tie, ArrayList<Pawn> enPassant, int wk_x, int wk_y, int bk_x, int bk_y) {
		this.board = board;
		this.turn = turn;
		this.state = state;
		this.positions = positions;
		this.inactivity = inactivity;
		this.tie = tie;
		this.enPassant = enPassant;
		
		this.wk_x = wk_x;
		this.wk_y = wk_y;
		this.bk_x = bk_x;
		this.bk_y = bk_y;
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

		this.wk_x = 4;
		this.wk_y = 0;


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
		
		this.bk_x = 4;
		this.bk_y = 7;
	}
	
	
	public Piece[][] getBoard() {
		return this.board;
	}
	
	public ArrayList<Pawn> getEnPassant() {
		return this.enPassant;
	}
	
	public HashMap<BoardState, Integer> getPositions() {
		return this.positions;
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
	
	public int getWK_X() {
		return this.wk_x;
	}
	
	public int getWK_Y() {
		return this.wk_y;
	}
	
	public int getBK_X() {
		return this.bk_x;
	}
	
	public int getBK_Y() {
		return this.bk_y;
	}
	
	public boolean tie() {
		return this.tie;
	}
	
	public void setState(GameState state) {
		this.state = state;
	}
	
	public void setInactivity(int inactivity) {
		this.inactivity = inactivity;
	}
	
	public void incInactivity() {
		this.inactivity++;
	}
	
	public void decInactivity() {
		this.inactivity--;
	}
	
	public void setWhiteKingCoords(int x, int y) {
		this.wk_x = x;
		this.wk_y = y;
	}
	
	public void setBlackKingCoords(int x, int y) {
		this.bk_x = x;
		this.bk_y = y;
	}
	
	public boolean move(Move move) {
		ArrayList<Move> moves = calculateMoves();
		
		while(moves.size() > 0) {
			if(move.equals(moves.remove(0))){
				applyMove(move);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean promotePawn(PieceType piece) {
		ArrayList<Move> moves = calculateMoves();
		Move move = new Move(piece);
		
		while(moves.size() > 0) {
			if(move.equals(moves.remove(0))){
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
	
	private void findKing(PieceColor color) {
		for(int x = 0; x < GameUtil.boardSize; x++) {
			for(int y = 0; y < GameUtil.boardSize; y++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getColor() == color && this.board[x][y].getType() == PieceType.King) {
						if(color == PieceColor.White) {
							this.wk_x = x;
							this.wk_y = y;
						}
						else {
							this.bk_x = x;
							this.bk_y = y;
						}
						return;
					}
				}
			}
		}
	}

	public boolean playerInCheck(PieceColor color) {
		int king_x;
		int king_y;
		
		if(color == PieceColor.White) {
			king_x = this.wk_x;
			king_y = this.wk_y;
		}
		else {
			king_x = this.bk_x;
			king_y = this.bk_y;
		}
		
		boolean foundKing = false;
		
		if(this.board[king_x][king_y] != null) {
			if(this.board[king_x][king_y].getColor() == color && this.board[king_x][king_y].getType() == PieceType.King) {
				foundKing = true;
			}
		}
		
		if(!foundKing) {
			findKing(color);
		
			if(color == PieceColor.White) {
				king_x = this.wk_x;
				king_y = this.wk_y;
			}
			else {
				king_x = this.bk_x;
				king_y = this.bk_y;
			}
		}

		/*for(int x = 0; x < GameUtil.boardSize; x++) {
			for(int y = 0; y < GameUtil.boardSize; y++) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].getColor() == color.change()) {
						if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
							return true;
						}
					}
				}
			}
		}*/
		
		int x;
		int y;

		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x+i;
			y = king_y;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x-i;
			y = king_y;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x;
			y = king_y+i;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x;
			y = king_y-i;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x+i;
			y = king_y+i;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x-i;
			y = king_y+i;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x+i;
			y = king_y-i;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		for(int i = 1; i < GameUtil.boardSize; i++) {
			x = king_x-i;
			y = king_y-i;
			
			if(GameUtil.validCoordinates(x, y)) {
				if(this.board[x][y] != null) {
					if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
						return true;
					}
					
					break;
				}
			}
			else {
				break;
			}
		}
		
		//check knights
		
		x = king_x+2;
		y = king_y+1;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x+1;
		y = king_y+2;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x-2;
		y = king_y+1;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x-1;
		y = king_y+2;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x+2;
		y = king_y-1;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x+1;
		y = king_y-2;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x-2;
		y = king_y-1;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}
		
		x = king_x-1;
		y = king_y-2;
		
		if(GameUtil.validCoordinates(x, y)) {
			if(this.board[x][y] != null) {
				if(this.board[x][y].isMoveValid(x, y, king_x, king_y, this)) {
					return true;
				}
			}
		}

		return false;
	}

	public Game makeCopy() {
		Piece[][] board = new Piece[GameUtil.boardSize][GameUtil.boardSize];
		
		ArrayList<Pawn> enPassant = new ArrayList<>();

		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(this.board[x][y] != null) {
					board[x][y] = this.board[x][y].makeCopy();
					
					if(board[x][y].getType() == PieceType.Pawn) {
						Pawn p = (Pawn)board[x][y];
						if(p.getEnPassant() || p.getEnPassantVictim()) {
							enPassant.add(p);
						}
					}
				}
			}
		}
		
		return new Game(board, this.turn, this.state, new HashMap<>(this.positions), 
				this.inactivity, this.tie, enPassant, 
					this.wk_x, this.wk_y, this.bk_x, this.bk_y);
	}
	
	public void copyFrom(Game game) {
		this.enPassant = new ArrayList<>();
		
		for(int y=0; y < GameUtil.boardSize; y++) {
			for(int x=0; x < GameUtil.boardSize; x++) {
				if(game.getBoard()[x][y] != null) {
					this.board[x][y] = game.getBoard()[x][y].makeCopy();
					
					if(this.board[x][y].getType() == PieceType.Pawn) {
						Pawn p = (Pawn)this.board[x][y];
						if(p.getEnPassant() || p.getEnPassantVictim()) {
							this.enPassant.add(p);
						}
					}
				}
				else {
					this.board[x][y] = null;
				}
			}
		}
		
		this.turn = game.getTurn();
		this.state = game.getState();
		this.positions = new HashMap<>(game.getPositions());
		this.inactivity = game.getInactivity();
		this.tie = game.tie();
		this.wk_x = game.getWK_X();
		this.wk_y = game.getWK_Y();
		this.bk_x = game.getBK_X();
		this.bk_y = game.getBK_Y();
	}
		
	public ArrayList<Move> calculateMoves(){
		ArrayList<Move> queue = new ArrayList<>();

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
		
		Collections.sort(queue, new Comparator<Move>() {
			@Override
			public int compare(Move o1, Move o2) {
				if(o1.victim == 0 && o2.victim == 0) {
					return 0;
				}

				if(o1.victim < o2.victim) {
					return 1;
				}

				if(o1.victim > o2.victim) {
					return -1;
				}
				
				//same victim

				if(o1.attacker > o2.attacker) {
					return 1;
				}
				
				if(o1.attacker < o2.attacker) {
					return -1;
				}

				return 0;
			}
		});

		return queue;
	}
	
	
	public Move generateRandomMove(){
		ArrayList<Move> queue = new ArrayList<>();

		if(this.state == GameState.RegularMove) {
			
			ArrayList<int[]> coords = new ArrayList<>();

			for(int x=0; x < GameUtil.boardSize; x++) {
				for(int y=0; y < GameUtil.boardSize; y++) {
					if(this.board[x][y] != null) {
						if(this.board[x][y].getColor() == this.turn) {
							coords.add(new int[] {x, y});
						}
					}
				}
			}
			
			Collections.shuffle(coords);
			
			while(queue.size() == 0) {
				int[] coord = coords.remove(0);
				this.board[coord[0]][coord[1]].calculateMoves(coord[0], coord[1], this, queue);
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
