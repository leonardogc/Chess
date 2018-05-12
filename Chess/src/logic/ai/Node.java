package logic.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import logic.ai.Minimax.Turn;
import logic.game.Game;
import logic.game.Game.GameState;
import logic.game.Move;


public class Node {
	public Node parent;
	public LinkedList<Node> children;
	
	public Move move;
	public Game currentGame;
	
	public double visits;
	public double score;
	
	public Turn turn;
	
	public Node(Turn turn, Game game) {
		this.visits = 0;
		this.score = 0;
		this.currentGame = game;
		this.turn = turn;
		
		this.parent = null;
		this.children = null;
		this.move = null;
	
		this.expand();
	}
	
	public Node(Move move, Node parent) {
		this.visits = 0;
		this.score = 0;
		this.move = move;
		this.parent = parent;
		
		this.currentGame = null;
		this.turn = null;
		this.children = null;
	}
	
	public void expand() {
		if(currentGame.gameEnded()) {
			return;
		}
		
		this.children = new LinkedList<>(); 
		
		LinkedList<Move> moves = currentGame.calculateMoves();
		
		while(moves.size() > 0) {
			children.add(new Node(moves.poll(), this));
		}
	}
	
	public void updateGameAndTurn() {
		this.currentGame = parent.currentGame.makeCopy();
		
		this.currentGame.applyMove(move);
		
		if(this.currentGame.getState() == GameState.ChoosingPiece) {
			this.turn = this.parent.turn;
		}
		else {
			if(this.parent.turn == Turn.Max) {
				this.turn = Turn.Min;
			}
			else {
				this.turn = Turn.Max;
			}
		}
	}

	public void backPropagate(double score) {
		this.visits++;

		if(this.parent != null) {
			if(this.parent.turn == Turn.Max) {
				this.score+=score;
			}
			else {
				this.score-=score;
			}
			
			this.parent.backPropagate(score);
		}
	}

	public Node getBestChild() {
		return Collections.max(children, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				double ucb1_o1 = o1.calculateOCB1();
				double ucb1_o2 = o2.calculateOCB1();
				
				if(ucb1_o1 < ucb1_o2) {
					return -1;
				}
				else if(ucb1_o1 == ucb1_o2) {
					return 0;
				}
				else {
					return 1;
				}
			}
		});
	}
	
	public Node getBestChildByScore() {
		return Collections.max(children, new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				double s1 = o1.score/o1.visits;
				double s2 = o2.score/o2.visits;
				
				if(s1 < s2) {
					return -1;
				}
				else if(s1 == s2) {
					return 0;
				}
				else {
					return 1;
				}
			}
		});
	}
	
	public double calculateOCB1() {
		if(visits == 0) {
			return Double.POSITIVE_INFINITY;
		}
		
		return (score/visits)+(Math.sqrt(2)*Math.sqrt(Math.log(parent.visits)/visits));
	}
}
