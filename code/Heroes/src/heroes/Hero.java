package heroes;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import heroes.equipments.Equipment;

public abstract class Hero {
	
	private int MAX_EQUIPMENT_NR=6;
	
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
	private List<Equipment> equips;
	
	private int x=-1,y=-1;
	
	public Hero(HeroType type, PlayerID player_id){
		equips = new ArrayList<Equipment>();
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
	
	public int get_max_eq_nr(){
		return MAX_EQUIPMENT_NR;
	}
	
	public boolean add_equip(Equipment e){
		if(equips.size()>=MAX_EQUIPMENT_NR){
			return false;
		}
		equips.add(e);
		return true;
	}
	
	public Equipment get_equip(int index){
		if(index<0 || index>=equips.size()){
			return null;
		}
		return equips.get(index);
	}
	
	public List<Equipment> get_equips(){
		return equips;
	}
	
	public void set_coordinates(int x, int y){
		this.x=x;
		this.y=y;
	}

	abstract void draw(Graphics g, int off_x, int off_y);
}
