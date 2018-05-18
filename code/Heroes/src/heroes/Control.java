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
import heroes.equipments.archer.ElvenArmor;
import heroes.equipments.archer.ElvenBoots;
import heroes.equipments.archer.LongBow;
import heroes.equipments.archer.MythrillArmor;
import heroes.equipments.archer.ShortBow;
import heroes.equipments.warrior.BladeOfRes;
import heroes.equipments.warrior.ElvenBlade;
import heroes.equipments.warrior.ElvenDagger;
import heroes.equipments.warrior.IronShield;
import heroes.equipments.warrior.IronSword;
import heroes.equipments.warrior.IronSwordOfFury;
import heroes.equipments.warrior.SwordOfRes;
import heroes.equipments.warrior.WoodenSwordOfFury;

/**
 *
 * @author ABence
 */
class Control implements IClick{
	private static int NR_OF_HEROES = 1;
	private GUI gui;
	private GameState gs;
	private ArrayList<Click> clicks_to_process;
	
	/**
	 * A játék állapotát megkapó szerver.
	 */
	private IGameState s; //bear
	/**
	 * network feladatokat kapcsolatos feladatokat kezelõ változó
	 */
	private Network net = null;//bear
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private ScheduledFuture<?> future = null;

	Control() {
		gs = new GameState();
		clicks_to_process = new ArrayList<Click>();			
	}
	void generate_new_game(){
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
						int n = r.nextInt(11);
						switch (n) {
						case 1:
							w.add_equip(new WoodenShield());
							break;
						case 2:
							w.add_equip(new WoodenSword());
							break;
						case 3:
							w.add_equip(new IronShield());
							break;
						case 4:
							w.add_equip(new IronSword());
							break;
						case 5:
							w.add_equip(new BladeOfRes());
							break;
						case 6:
							w.add_equip(new SwordOfRes());
							break;
						case 7:
							w.add_equip(new WoodenSwordOfFury());
							break;
						case 8:
							w.add_equip(new IronSwordOfFury());
							break;
						case 9:
							w.add_equip(new ElvenDagger());
							break;
						case 10:
							w.add_equip(new ElvenBlade());
							break;
						default:
							break;
						}				
					}
					gs.add_hero(w);
				}

				for (int i = 0; i < 2; i++) {
					Archer a = null;
					if(i%2==0){
						a = new Archer(PlayerID.SERVER);					
					}
					else{
						a = new Archer(PlayerID.CLIENT);	
					}

					for (int j = 0; j < 6; j++) {
						Random r = new Random();
						int n = r.nextInt(8);
						switch (n) {
						case 1:
							a.add_equip(new WoodenShield());
							break;
						case 2:
							a.add_equip(new WoodenSword());
							break;
						case 3:
							a.add_equip(new ElvenArmor());
							break;
						case 4:
							a.add_equip(new MythrillArmor());
							break;
						case 5:
							a.add_equip(new ElvenBoots());
							break;
						case 6:
							a.add_equip(new ShortBow());
							break;
						case 7:
							a.add_equip(new LongBow());
							break;
						default:
							break;
						}				
					}
					gs.add_hero(a);
				}
				
				generateBoard();
				refresh_board();
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
		if (net != null){
			s.onNewGameState(gs);			
		}
	}

	/**
	 * Szerver indítása, ha a felhasználó a megfelelõ gombra nyomott.
	 */
	void startServer() {
		//generateBoard();
		gui.onNewGameState(gs);	
		if (net != null)//bearStart
			net.stop();
		net = new Server(this, gui);
		net.start("localhost");//bearEnd
		System.out.println("start szerver megvolt");
		startScheduler();
		System.out.println("scheduler elinditva");
	}

	/**
	 * Csatlakozás a megadott IP címen található szerverre.
	 * @param s: IP cím
	 */
	void startClient(String s) {//bearStart
		if (net != null)
			net.stop();
		net = new Client(gui);
		net.start(s);//bearEnd
		System.out.println("startclient megvolt!");
	}
	
	void startScheduler(){
		this.gui.setClick(this); //bear
		s = (Server) net; //bear
		
		Runnable periodicTask = new Runnable() {
			public void run() {
				//System.out.println("Periodic task started");	
				try {
					mainProcess();					
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Error in executing periodicTask");
					e.printStackTrace();
					throw new RuntimeException(e);
				}
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

		if(clicks_to_process.size()!=1)
			System.out.println("NR_OF_CLICKS AT PROCESS "+clicks_to_process.size());
		Click c = clicks_to_process.remove(0);
		if(c.playerID!=gs.get_current_hero().get_player_id()){
			return;
		}
		gs.interact(c.x, c.y);
		refresh_board();	
	}
	
	public void check_for_periodic_change(){
		if(clicks_to_process.size()>1)
			System.out.println("NR_OF_CLICKS AT BEFORE REFRESH "+clicks_to_process.size());
		if(gs.check_and_refresh_if_dying()){
			if(clicks_to_process.size()>1)
				System.out.println("NR_OF_CLICKS AT REFRESH "+clicks_to_process.size());
			refresh_board();
		}
	}
	
	public void mainProcess(){
		check_for_periodic_change();
		processClicks();
	}

	@Override
	public void onNewClick(Click click) {
		if (future == null || future.isCancelled())
			System.out.println("SCHEDULER NOT RUNNING !!!!! ");	
			
		clicks_to_process.add(click);
		if(clicks_to_process.size()!=1)
			System.out.println("NR_OF_CLICKS "+clicks_to_process.size());					
	}
}
