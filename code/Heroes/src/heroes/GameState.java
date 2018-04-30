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
		START_SERVER,
		OCCUPIED_C,//TODO delete
		OCCUPIED_S,
		ATTACKABLE,
		CURRENT,
		STEPABLE
	}
	
	private static final long serialVersionUID = 1L;
	
	private int board_size = 8;
	public int time;
	public GameTurn turn;
	public boolean[][] valid_field;
	public FieldType[][] board_bg;
	public List<Hero> heroes;
	
	private int current_hero_id = 0;
	private boolean has_stepable = false;
	private boolean has_attackable = false;
	
	private static float perc_if_valid_field = 0.9f;
	
	public GameState(){
		heroes = new ArrayList<Hero>();
		time = 0;
		turn = GameTurn.NOT_STARTED;
		board_bg = new FieldType[board_size][board_size];
		valid_field = new boolean[board_size][board_size];
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = FieldType.NOT_AVAILABLE;
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

	
	public void init_map(){
		Random r = new Random();
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if(r.nextFloat() < perc_if_valid_field){
					board_bg[i][j] = FieldType.FREE;
					valid_field[i][j] = true;					
				}
				else{
					board_bg[i][j] = FieldType.NOT_AVAILABLE;
					valid_field[i][j] = false;					
				}
			}
		}
	}
	
	//TODO can be overloaded
	public void set_starting_positions(int nr_of_heroes){
		boolean waiting_for_good_roll = true;
		int x,y, xc=0,yc=0,xs=0,ys=0;
		while(waiting_for_good_roll){
			Random r = new Random();
			int should_set = 2 * nr_of_heroes;
			boolean plus = false;
			if(r.nextFloat() < 0.5f){
				plus = true;
			}
			while(waiting_for_good_roll){
				if(plus){
					x = r.nextInt(board_size)+r.nextInt(board_size/2);
					y = r.nextInt(board_size)+r.nextInt(board_size/2);					
				}
				else{
					x = r.nextInt(board_size)-r.nextInt(board_size/2);
					y = r.nextInt(board_size)-r.nextInt(board_size/2);
				}
				if (x < 0 || y<0 || x>board_size-1 || y>board_size-1){
					continue;
				}
				
				if(board_bg[x][y]==FieldType.FREE){
					if(should_set > nr_of_heroes){
						board_bg[x][y]=FieldType.START_CLIENT;
						xc=x;
						yc=y;
					}
					else{
						board_bg[x][y]=FieldType.START_SERVER;
						xs=x;
						ys=y;
						if(should_set == nr_of_heroes){
							plus = !plus;
						}
					}
					should_set--;
					if(should_set < 1){
						waiting_for_good_roll = false;
					}
				}				
			}
			int xd_sq = (xs-xc)*(xs-xc);
			int yd_sq = (ys-yc)*(ys-yc);
			//System.out.println("X:" + xd_sq + " Y:" + yd_sq + " SUM:" + (xd_sq+yd_sq));
			if((xd_sq+yd_sq)<20){
				waiting_for_good_roll = true;
				board_bg[xs][ys]=FieldType.FREE;
				board_bg[xc][yc]=FieldType.FREE;				
			}
		}
	}
	
	public void add_hero(Hero hero){
		heroes.add(hero);
	}
	
	public void set_heroes_starting_positions(){
		for (int i = 0; i < board_size; i++) {
			for (int j = 0; j < board_size; j++) {
				if(board_bg[i][j] == FieldType.START_CLIENT){
					for(Hero h : this.heroes){
						if(h.get_x()<0){
							if(h.get_player_id() == PlayerID.CLIENT){
								h.set_coordinates(i, j);
								board_bg[i][j] = FieldType.OCCUPIED_C;
							}
						}
					}
				}
				if(board_bg[i][j] == FieldType.START_SERVER){
					for(Hero h : this.heroes){
						if(h.get_x()<0){
							if(h.get_player_id() == PlayerID.SERVER){
								h.set_coordinates(i, j);	
								board_bg[i][j] = FieldType.OCCUPIED_S;							
							}
						}
					}
				}
				
								
			}
		}
	}
	
	boolean if_has_attackable(){
		return has_attackable;
	}
	
	void set_attackables(Hero h){
		int cent_x = h.get_x();
		int cent_y = h.get_y();
		PlayerID own_id = h.get_player_id();
		
		for(int i = -1; i < 2; i++){
			int x=cent_x+i;
			if(x<0 || x>= board_size){
				continue;
			}
			for(int j = -1; j < 2; j++){
				int y=cent_y+j;
				if(y<0 || y>= board_size){
					continue;
				}
				if(own_id == PlayerID.CLIENT){
					if(board_bg[x][y] == FieldType.OCCUPIED_S){
						board_bg[x][y] = FieldType.ATTACKABLE;
						has_attackable = true;
					}
				}
				else{
					if(board_bg[x][y] == FieldType.OCCUPIED_C){
						board_bg[x][y] = FieldType.ATTACKABLE;
						has_attackable = true;
					}
				}
			}
		}	
	}
	
	int roll(){
		Hero h = get_current_hero();
		boolean eq_valid=h.roll();
		if(!eq_valid){
			return 0;
		}
		EqType et = h.get_last_rolled_equip().get_type();
		switch (et) {
		case WOODEN_SWORD:
			set_attackables(h);
			return 1;
		case WOODEN_SHIELD:
			
			return 2;

		default:
			break;
		}
		return 0;
	}
	
	void clear_stepables(int curr_x, int curr_y){
		for(int i = -1; i < 2; i++){
			int x=curr_x+i;
			if(x<0 || x>= board_size){
				continue;
			}
			for(int j = -1; j < 2; j++){
				int y=curr_y+j;
				if(y<0 || y>= board_size){
					continue;
				}
				if(board_bg[x][y] == FieldType.STEPABLE){
					board_bg[x][y] = FieldType.FREE;
					has_stepable = false;
					return;
				}
			}
		}		
	}
	boolean check_if_attackable(int x, int y){
		if(valid_field[x][y]){
			if(board_bg[x][y] == FieldType.ATTACKABLE){
				return true;
			}			
		}
		return false;		
	}
	
	boolean check_if_not_occupied(int x, int y){
		if(board_bg[x][y] != FieldType.FREE){
			return false;
		}
		return true;
	}
	
	boolean check_if_stepable_and_step(int x, int y){
		if(valid_field[x][y]){
			Hero h = get_current_hero();
			if(has_stepable){
				if(board_bg[x][y] == FieldType.STEPABLE){
					board_bg[h.get_x()][h.get_y()] = FieldType.FREE;	
					if(h.get_player_id()==PlayerID.CLIENT){
						board_bg[x][y] = FieldType.OCCUPIED_C;						
					}
					else{
						board_bg[x][y] = FieldType.OCCUPIED_S;
					}
					h.set_coordinates(x, y);
					heroes.set(current_hero_id, h);					
					return true;
				}
				clear_stepables(h.get_x(),h.get_y());
			}
			if(Math.abs(h.get_x()-x)>1){
				return false;
			}
			if(Math.abs(h.get_y()-y)>1){
				return false;
			}
			if(check_if_not_occupied(x,y)){
				board_bg[x][y] = FieldType.STEPABLE;
				has_stepable = true;
			}
		}
		return false;
	}
	
	boolean check_and_refresh_if_dying(){
		boolean should_refresh = false;
		ListIterator<Hero> iter = heroes.listIterator();
		while(iter.hasNext()){
			Hero h = iter.next();
			if(h.get_dying()){
				should_refresh = true;
				if(h.decrease_health()){
					iter.remove();
				}
				//System.out.println("decreasing health");
			}
		}
		return should_refresh;
	}
	
	public void copy(GameState gs){
		time = gs.time;
		turn = gs.turn;
		heroes = new ArrayList<Hero>(gs.heroes);
		current_hero_id = gs.current_hero_id;
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = gs.board_bg[i][j];
				valid_field[i][j] = gs.valid_field[i][j];
			}
		}
	}
}
