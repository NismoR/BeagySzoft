package heroes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import heroes.Hero.PlayerID;
import heroes.equipments.Equipment;
import heroes.equipments.Equipment.EqType;

public class GameState implements Serializable{

	public enum GameTurn {
		NOT_STARTED,
		INITING_MAP,
		PLAYER_CLIENT,
		PLAYER_SERVER
	}
	
	public enum FieldType {
		NOT_AVAILABLE,
		FREE,
		START_CLIENT,
		START_SERVER
	}
	
	private static final long serialVersionUID = 1L;
	
	private int board_size = 8;
	public int time;
	public GameTurn turn;
	public boolean[][] valid_field;
	public List<Hero> heroes;
	
	private int current_hero_id = 0;
	private PlayerID winner=null;
	
	public Click wanna_step = null;		//Just for storing desired coordinates to step
	public List<Click> start_pos;
	public List<Click> extra_steps;
	
	public boolean should_step_again=false;
	
	private static float perc_if_valid_field = 0.9f;
	
	public GameState(){
		heroes = new ArrayList<Hero>();
		start_pos = new ArrayList<Click>();
		extra_steps = new ArrayList<Click>();
		time = 0;
		turn = GameTurn.NOT_STARTED;
		valid_field = new boolean[board_size][board_size];
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				valid_field[i][j] = false;
			}
		}
	}
	
	public Hero get_current_hero(){
		return heroes.get(current_hero_id);
	}
	
	public void step_to_next_hero(){
	    current_hero_id++;
	    if(current_hero_id >= heroes.size()){
	           current_hero_id=0;
	    }
    }
		 
    public void check_and_step_if_current_is_not_alive(){
    	Hero h = get_current_hero();
    	while(h.get_dying()){
    		step_to_next_hero();
    		h = get_current_hero();
    	}
    }
    
    public void step_to_next_alive_hero(){
    	step_to_next_hero();
    	check_and_step_if_current_is_not_alive();
    }

	
	public void init_map(){
		Random r = new Random();
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if(r.nextFloat() < perc_if_valid_field){
					valid_field[i][j] = true;					
				}
				else{
					valid_field[i][j] = false;					
				}
			}
		}
	}
	
	private boolean is_free_for_starting(int x, int y){
		for(Click c:start_pos){
			if(c.x==x && c.y==y){
				return false;
			}
		}
		return true;
	}
	
	public void set_starting_positions(int nr_of_cli_heroes, int nr_of_ser_heroes){
		int x=0;
		int y=0;
		while(nr_of_cli_heroes>0){
			System.out.println("StartC x: "+x +" y: "+y);
			if (x < 0)
				x=0;
			if (y < 0)
				y=0;
			if (x > board_size-1)
				x=board_size-1;
			if (y > board_size-1)
				y=board_size-1;
			
			if(is_free_for_starting(x, y)){
				start_pos.add(new Click(x, y, PlayerID.CLIENT));
				nr_of_cli_heroes--;
			}

			Random r = new Random();
			if(r.nextFloat() < 0.5f){
				x++;
			}
			if(r.nextFloat() < 0.1f){
				x--;
			}
			if(r.nextFloat() < 0.5f){
				y++;
			}
			if(r.nextFloat() < 0.1f){
				y--;
			}			
		}
		x=board_size-1;
		y=board_size-1;

		while(nr_of_ser_heroes>0){
			System.out.println("StartS x: "+x +" y: "+y);
			if (x < 0)
				x=0;
			if (y < 0)
				y=0;
			if (x > board_size-1)
				x=board_size-1;
			if (y > board_size-1)
				y=board_size-1;			

			if(is_free_for_starting(x, y)){
				start_pos.add(new Click(x, y, PlayerID.SERVER));
				nr_of_ser_heroes--;
			}

			Random r = new Random();
			if(r.nextFloat() < 0.5f){
				x--;
			}
			if(r.nextFloat() < 0.1f){
				x++;
			}
			if(r.nextFloat() < 0.5f){
				y--;
			}
			if(r.nextFloat() < 0.1f){
				y++;
			}			
		}
	}
	
	//TODO can be overloaded
	
	
	public void add_hero(Hero hero){
		heroes.add(hero);
	}
	

	public void set_heroes_starting_positions(){
		for(Hero h : this.heroes){
			if(h.get_x()<0){
				ListIterator<Click> iter = start_pos.listIterator();
				while(iter.hasNext()){
					Click c = iter.next();
					if(c.playerID==h.get_player_id()){
						h.set_coordinates(c.x,c.y);
						iter.remove();
						break;
					}
				}				
			}
			if(h.get_x()<0){
				System.out.println("ERROR: - COuldn't place Hero on board!!!!!!!!");
			}
		}
		start_pos = null;
	}

	boolean is_field_empty(int x, int y){
		if(!valid_field[x][y]){
			return false;
		}
		for(Hero h:heroes){
			if(h.get_x()==x && h.get_y()==y){
				return false;
			}
		}
		return true;
	}
	
	boolean is_field_occupied(int x, int y){
		return !is_field_empty(x, y);
	}
	
	boolean if_has_attackable(){
		for(Hero h:heroes){
			if(h.get_attackable()){
				return true;
			}
		}
		return false;
	}
	boolean set_attackables(Hero own){
		boolean ret = false;
		PlayerID own_id = own.get_player_id();
		for(Hero h:heroes){
			if(h.get_player_id()!=own_id){
				if(Math.abs(own.get_x()-h.get_x())<2){
					if(Math.abs(own.get_y()-h.get_y())<2){
						h.set_as_attackable();	
						ret = true;
					}
				}
			}
		}
		return ret;
	}

	
	void attack_all_attackable(){
		Hero own = get_current_hero();
		for(Hero h:heroes){
			if(h.get_attackable()){
				attack(h, own);
			}
		}
		clear_attackables();
	}
	
	void clear_attackables(){
		for(Hero h:heroes){
			h.clear_attackable();
		}		
	}
	
	void set_extra_steps(){
		Hero h = get_current_hero();
		int x=h.get_x();
		int y=h.get_y();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if(is_field_empty(x+i, y+j)){
					extra_steps.add(new Click(x+i, y+j, null));
				}
			}
		}
	}
	
	boolean attack_process(){
		Hero h = get_current_hero();
		Equipment e = h.get_last_rolled_equip();
		if(e.get_attack()!=null){
			if(set_attackables(h)){
				if(e.get_attack().get_allNearby()){
					attack_all_attackable();
				}
				return true;
			}
		}
		return false;
	}
	
	int roll(){
		should_step_again=false;
		extra_steps.clear();
		Hero h = get_current_hero();
		boolean eq_valid=h.roll();
		if(!eq_valid){
			return 0;
		}
		Equipment e = h.get_last_rolled_equip();
		if(!e.get_move_extra()){
			attack_process();
		}
		else{
			should_step_again=true;
			set_extra_steps();
		}
		return e.get_type_in_int();
	}
	
	Hero get_hero_at_given_coord(int x, int y){
		for(Hero h: heroes){
			if(h.get_x()==x && h.get_y()==y){
				return h;
			}
		}
		return null;
	}
	
	void attack(Hero def, Hero att){
		if(def.get_player_id()!=att.get_player_id())
			def.defense(att.get_last_rolled_equip());
	}
	/*Return true if clicked field is attackable, false if not*/
	boolean check_if_attackable_and_attack(int x, int y){
		if(valid_field[x][y]){
			Hero def = get_hero_at_given_coord(x, y);
			if(def!=null){
				if(def.get_attackable()){
					attack(def,get_current_hero());
					clear_attackables();
					return true;					
				}
			}			
		}
		return false;		
	}
	
	/*Return true if should step to new hero, because step was made*/
	boolean check_if_stepable_and_step(int x, int y){
		if(valid_field[x][y]){
			Hero h = get_current_hero();
			if(wanna_step!=null){
				if(x==wanna_step.x && y==wanna_step.y){
					h.set_coordinates(x, y);
					heroes.set(current_hero_id, h);
					wanna_step = null;
					return true;	
				}
			}
			if(Math.abs(x-h.get_x())<2 && Math.abs(y-h.get_y())<2){
				if(is_field_empty(x, y)){
					wanna_step = new Click(x, y, h.get_player_id());
					return false;		
				}
				if(x == h.get_x() && y==h.get_y()){
					wanna_step = new Click(x, y, h.get_player_id());
					return false;		
				}
			}
		}
		wanna_step = null;
		return false;		
	}
	
	boolean check_if_game_finished(){
		int n_of_cli_heroes = 0;
		for(Hero h: heroes){
			if(h.get_player_id()==PlayerID.CLIENT){
				n_of_cli_heroes++;
			}
		}
		if(n_of_cli_heroes==0){
			winner = PlayerID.SERVER;
			System.out.println("========= SERVER WON THE GAME ========");
		}
		if(n_of_cli_heroes==heroes.size()){
			winner = PlayerID.CLIENT;
			System.out.println("========= CLIENT WON THE GAME ========");
		}
		
		return winner!=null;
	}
	
	boolean if_game_finished(){
		return winner!=null;
	}
	
	boolean check_and_refresh_if_dying(){
		boolean should_refresh = false;
		ListIterator<Hero> iter = heroes.listIterator();
		while(iter.hasNext()){
			int iterID = iter.nextIndex();
			Hero h = iter.next();
			if(h.get_dying()){
				should_refresh = true;
				if(h.decrease_health()){
					System.out.println("iterID: "+iterID+"  currID: "+current_hero_id);
					iter.remove();
					if(iterID<=current_hero_id){
						current_hero_id--;
					}
					System.out.println("Hero Died");
					System.out.println("Num of remaining heroes: "+heroes.size());
				}
			}
		}
		if(should_refresh){
			check_if_game_finished();
		}
		return should_refresh;
	}
	
	public void interact(int x, int y){
		if(if_has_attackable()){
			if(check_if_attackable_and_attack(x,y)){
				//System.out.println("ATTACKABLE");
				step_to_next_alive_hero();
			}
			else{
				//System.out.println("NOT ATTACKABLE");				
			}
		}
		else{
			if(check_if_stepable_and_step(x,y)){
				if(!should_step_again){
					roll();
					if(!should_step_again){
						if(!if_has_attackable()){
							step_to_next_alive_hero();
						}					
					}
				}
				else{
					should_step_again=false;
					extra_steps.clear();
					if(!attack_process()){
						step_to_next_alive_hero();						
					}
				}
			}
		}
	}
	
	public void copy(GameState gs){
		time = gs.time;
		turn = gs.turn;
		heroes = new ArrayList<Hero>(gs.heroes);
		current_hero_id = gs.current_hero_id;
		wanna_step = gs.wanna_step;
		start_pos = gs.start_pos;
		should_step_again = gs.should_step_again;
		extra_steps = gs.extra_steps;
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				valid_field[i][j] = gs.valid_field[i][j];
			}
		}
	}
}
