package heroes;

import java.awt.Graphics;

public abstract class Hero {
	
	public enum HeroType {
		WARRIOR,
		ARCHER,
		MAGE
	}
	
	public enum PlayerID {
		CLIENT,
		SERVER
	}
	
	private HeroType type;
	private PlayerID player_id;
	
	private int x=-1,y=-1;
	
	public Hero(HeroType type, PlayerID player_id){
		this.type = type;
		this.player_id = player_id;
	}
	
	public HeroType get_type(){
		return this.type;
	}
	
	public PlayerID get_player_id(){
		return this.player_id;
	}
	
	public int get_x(){
		return x;
	}
	
	public int get_y(){
		return y;
	}
	
	public void set_coordinates(int x, int y){
		this.x=x;
		this.y=y;
	}

	abstract void draw(Graphics g, int off_x, int off_y);
}
