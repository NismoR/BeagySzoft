/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heroes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
	private ArrayList<Click> clicks_to_process;
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> future = null;

	Control() {
		gs = new GameState();
		clicks_to_process = new ArrayList<Click>();
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
	
	void startScheduler(){
		Runnable periodicTask = new Runnable() {
			public void run() {
				//System.out.println("Periodic task started");	
				mainProcess();
			}
		};

		if (future == null || future.isCancelled())
			future = executor.scheduleAtFixedRate(periodicTask, 0, 40, TimeUnit.MILLISECONDS);
		refresh_board();	
	}
	
	public void processClicks(){
		if(clicks_to_process.isEmpty()){
			return;
		}
		Click c = clicks_to_process.remove(0);
		if(c.sent_by!=gs.get_current_hero().get_player_id()){
			return;
		}
		if(gs.if_has_attackable()){
			if(gs.check_if_attackable(c.x,c.y)){
				System.out.println("ATTACKABLE");
			}
			else{
				System.out.println("NOT ATTACKABLE");				
			}
		}
		else{
			if(gs.check_if_stepable_and_step(c.x,c.y)){
				gs.roll();
				if(!gs.if_has_attackable()){
					gs.step_to_next_hero();
				}
			}
		}
		refresh_board();	
	}
	
	public void mainProcess(){
		processClicks();
	}

	@Override
	public void onNewClick(Click click) {
		clicks_to_process.add(click);				
	}
}
