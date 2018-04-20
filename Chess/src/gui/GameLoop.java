package gui;

public class GameLoop extends Thread{
	private GraphicsAndListeners gui;
	
	public GameLoop(GraphicsAndListeners gui) {
		this.gui = gui;
	}
	
	@Override
	public void run() {
		/*while(gui.game.calculateMoves().size() > 0) {
			
			
			
		}*/
		
		System.out.println(gui.game.getTurn().change()+" wins!");
	}
}
