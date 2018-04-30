/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package heroes;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import heroes.GameState.GameTurn;
import heroes.Hero.PlayerID;
import heroes.equipments.Equipment;
import heroes.equipments.Equipment.EqType;
import heroes.equipments.WoodenShield;
import heroes.equipments.WoodenSword;
import heroes.equipments.warrior.BladeOfRes;
import heroes.equipments.warrior.IronShield;
import heroes.equipments.warrior.IronSword;
import heroes.equipments.warrior.SwordOfRes;

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
		//Adding Warriors
		for (int i = 0; i < 4; i++) {
			Warrior w = null;
			if(i%2==0){
				w = new Warrior(PlayerID.CLIENT);				
			}
			else{
				w = new Warrior(PlayerID.SERVER);
			}

			for (int j = 0; j < 6; j++) {
				Random r = new Random();
				int n = r.nextInt(Equipment.num_of_eq);
				EqType e = EqType.values()[n];
				switch (e) {
				case WOODEN_SHIELD:
					w.add_equip(new WoodenShield());
					break;
				case WOODEN_SWORD:
					w.add_equip(new WoodenSword());
					break;
				case IRON_SHIELD:
					w.add_equip(new IronShield());
					break;
				case IRON_SWORD:
					w.add_equip(new IronSword());
					break;
				case BLADE_OF_RES:
					w.add_equip(new BladeOfRes());
					break;
				case SWORD_OF_RES:
					w.add_equip(new SwordOfRes());
					break;

				default:
					break;
				}
				
			}
			gs.add_hero(w);
		}
		
		generateBoard();		
	}
	
	private void generateBoard(){
		gs.turn = GameTurn.INITING_MAP;
		gs.init_map();
		//gs.set_starting_positions(NR_OF_HEROES);
		gs.set_starting_positions(5,5);
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
		if(c.playerID!=gs.get_current_hero().get_player_id()){
			return;
		}
		gs.interact(c.x, c.y);
		refresh_board();	
	}
	
	public void check_for_periodic_change(){
		if(gs.check_and_refresh_if_dying()){
			refresh_board();
		}
	}
	
	public void mainProcess(){
		check_for_periodic_change();
		processClicks();
	}

	@Override
	public void onNewClick(Click click) {
		clicks_to_process.add(click);				
	}
}
