package logic.ai;

import java.util.Collections;
import java.util.LinkedList;

import logic.ai.Minimax.Turn;
import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil.PieceColor;

public class MonteCarlo {
	private Node root;
	
	public static double seconds = 10;
	
	public MonteCarlo(Game game) {
		this.root = new Node(Turn.Max, game);
	}
	
	public Move calculate() {
		long start = System.nanoTime();
		Node current;
		
		while(((double)(System.nanoTime()-start)/1000000000) < seconds) {
			current = root;
			
			while(current.children != null) {
				current=current.getBestChild();
			}
			
			if(current.visits == 0) {
				current.updateGameAndTurn();
			}
			else {
				current.expand();
				
				if(current.children != null) {
					current=current.children.get(0);
					current.updateGameAndTurn();
				}
			}
			
			double score = better_rollout(current);
			
			current.backPropagate(score);
		}
		
		System.out.println("Nodes: " + root.visits);
		return root.getBestChildByVisits().move;
	}
	
	private double rollout(Node node) {
		Game game_copy = node.currentGame.makeCopy();
		
		while(!game_copy.noAvailableMoves() && !game_copy.tie()) {
			game_copy.applyMove(game_copy.generateRandomMove());
		}

		if(game_copy.tie()) {
			return 0;
		}
		
		if(!game_copy.playerInCheck(game_copy.getTurn())) {
			//stalemate
			return 0;
		}

		PieceColor max_color;

		if(node.turn == Turn.Max) {
			max_color = node.currentGame.getTurn();
		}
		else {
			max_color = node.currentGame.getTurn().change();
		}

		if(game_copy.getTurn() == max_color) {
			//max lost
			return -10;
		}
		else {
			//max won
			return 10;
		}
	}
	
	private double better_rollout(Node node) {
		if(node.currentGame.tie()) {
			return 0;
		}
		
		if(node.currentGame.noAvailableMoves()) {
			if(node.currentGame.playerInCheck(node.currentGame.getTurn())) {
				if(node.turn == Turn.Max) {
					//max lost
					return -50000;
				}
				else {
					//max won
					return 50000;
				}
			}
			else {
				return 0;
			}
		}
		
		
		PieceColor max_color;

		if(node.turn == Turn.Max) {
			max_color = node.currentGame.getTurn();
		}
		else {
			max_color = node.currentGame.getTurn().change();
		}
		
		return (double)Minimax.better_heuristic(node.currentGame, max_color);
	}
}