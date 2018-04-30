/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heroes;

import java.awt.Point;

import heroes.GameState.GameTurn;
import heroes.Hero.PlayerID;
import heroes.equipments.WoodenShield;
import heroes.equipments.WoodenSword;

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
		WoodenSword eq_wsw = new WoodenSword();
		WoodenShield eq_wsh = new WoodenShield();
		Warrior wc = new Warrior(PlayerID.CLIENT);
		Warrior ws = new Warrior(PlayerID.SERVER);
		wc.add_equip(eq_wsw);
		wc.add_equip(eq_wsw);
		wc.add_equip(eq_wsh);
		ws.add_equip(eq_wsw);
		ws.add_equip(eq_wsw);
		ws.add_equip(eq_wsh);
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
	public void onNewClick(Click click) {
		// TODO Auto-generated method stub
		if(gs.if_has_attackable()){
			if(gs.check_if_attackable(click.x,click.y)){
				System.out.println("ATTACKABLE");
			}
			else{
				System.out.println("NOT ATTACKABLE");				
			}
		}
		else{
			if(gs.check_if_stepable_and_step(click.x,click.y)){
				gs.roll();
				if(!gs.if_has_attackable()){
					gs.step_to_next_hero();
				}
			}
		}
		gui.onNewGameState(gs);			
	}
}
