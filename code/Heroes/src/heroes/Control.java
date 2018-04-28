/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heroes;

import java.awt.Point;

import heroes.GameState.GameTurn;
import heroes.Hero.PlayerID;

/**
 *
 * @author ABence
 */
class Control implements IClick{
	private static int NR_OF_HEROES = 1;
	private GUI gui;
	private GameState gs;

	Control() {
		gs = new GameState();
		Warrior wc = new Warrior(PlayerID.CLIENT);
		Warrior ws = new Warrior(PlayerID.SERVER);
		gs.add_hero(ws);
		gs.add_hero(wc);
		generateBoard();		
	}
	
	private void generateBoard(){
		gs.turn = GameTurn.INITING_MAP;
		gs.init_map();
		gs.set_starting_positions(NR_OF_HEROES);
		gs.set_heroes_starting_positions();
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

	@Override
	public void onNewClick(int x, int y) {
		// TODO Auto-generated method stub
		//gs.check_if_stepable(x, y);
		gui.onNewGameState(gs);			
	}
}
