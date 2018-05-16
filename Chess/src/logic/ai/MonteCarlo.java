package logic.ai;

import java.util.LinkedList;
import java.util.Random;

import logic.ai.Minimax.Turn;
import logic.game.Game;
import logic.game.Move;
import logic.util.GameUtil.PieceColor;


public class MonteCarlo {
	private Node root;
	
	public MonteCarlo(Game game) {
		this.root = new Node(Turn.Max, game);
	}
	
	public Move calculate(long seconds) {
		long start = System.nanoTime();
		Node current;
		
		while((System.nanoTime()-start)/1000000000 < seconds) {
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
			
			double score = rollout(current);
			
			current.backPropagate(score);
		}
		
		/*for(int i = 0; i < root.children.size(); i++) {
			System.out.println("visits: " + root.children.get(i).visits + " score: "+ root.children.get(i).score + " average: " + root.children.get(i).score/root.children.get(i).visits);
		}*/
		
		System.out.println(root.visits);
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

	/*private double rollout(Node node) {
		boolean lost_game = node.currentGame.lost_game();

		if(lost_game) {
			if(node.turn == Turn.Max) {
				return -10000;
			}
			else {
				return 10000;
			}
		}
		
		Tone tone;

		if(node.turn == Turn.Max) {
			tone = node.currentGame.getTurn();
		}
		else{
			tone = node.currentGame.getTurn().change();
		}

		return node.currentGame.heuristic(tone);
	}*/
}
