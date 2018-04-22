/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heroes;

import java.awt.Point;

import heroes.GameState.GameTurn;

/**
 *
 * @author ABence
 */
class Control {
	private static int NR_OF_HEROES = 1;
	private GUI gui;
	private GameState gs;

	Control() {
		gs = new GameState();
		generateBoard();		
	}
	
	private void generateBoard(){
		gs.turn = GameTurn.INITING_MAP;
		gs.init_map();
		gs.set_starting_positions(NR_OF_HEROES);
		gs.turn = GameTurn.PLAYER_CLIENT;		
	}

	void setGUI(GUI g) {
		gui = g;
	}
	
	//todo delete later
	void refresh_board(){
		gui.onNewGameState(gs);		
	}

	void startServer() {
		generateBoard();
		gui.onNewGameState(gs);		
	}

	void startClient() {
	}

	void sendClick(Point p) {
	}

	void clickReceived(Point p) {
	}
}
