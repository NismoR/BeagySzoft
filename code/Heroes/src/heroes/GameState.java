package heroes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import heroes.Hero.PlayerID;

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
		OCCUPIED_C,
		OCCUPIED_S,
		ATTACKABLE,
		CURRENT
	}
	
	private static final long serialVersionUID = 1L;
	
	private int board_size = 8;
	public int time;
	public GameTurn turn;
	public boolean[][] steppable;
	public FieldType[][] board_bg;
	public List<Hero> heroes;
	
	private int current_hero_id = 0;
	
	private static float perc_if_valid_field = 0.9f;
	
	public GameState(){
		heroes = new ArrayList<Hero>();
		time = 0;
		turn = GameTurn.NOT_STARTED;
		board_bg = new FieldType[board_size][board_size];
		steppable = new boolean[board_size][board_size];
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = FieldType.NOT_AVAILABLE;
				steppable[i][j] = false;
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
					steppable[i][j] = true;					
				}
				else{
					board_bg[i][j] = FieldType.NOT_AVAILABLE;
					steppable[i][j] = false;					
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
	
	public void copy(GameState gs){
		time = gs.time;
		turn = gs.turn;
		heroes = new ArrayList<Hero>(gs.heroes);
		for(int i = 0; i < board_size; i++){
			for(int j = 0; j < board_size; j++){
				board_bg[i][j] = gs.board_bg[i][j];
				steppable[i][j] = gs.steppable[i][j];
			}
		}
	}
}
