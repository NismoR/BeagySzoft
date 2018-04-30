package heroes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import heroes.equipments.Equipment;
import heroes.equipments.Equipment.EqType;
import heroes.equipments.WoodenShield;

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
	private int last_rolled_id=-1;
	private int current_defense=0;
	
	private int health = 255;
	private boolean dying = false;
	private boolean attackable = false;
	
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
	
	public int get_health(){
		return health;
	}
	
	public boolean get_dying(){
		return dying;
	}
	
	public boolean get_attackable(){
		return attackable;
	}
	
	public void set_as_attackable(){
		this.attackable=true;
	}
	
	public void clear_attackable(){
		this.attackable=false;
	}
	
	public int get_max_eq_nr(){
		return MAX_EQUIPMENT_NR;
	}
	
	public int get_current_defense(){
		return current_defense;
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
	
	public Equipment get_last_rolled_equip(){
		return get_equip(last_rolled_id);
	}
	public EqType get_last_rolled_equip_type(){
		Equipment e = get_last_rolled_equip();
		if(e==null){
			return null;
		}
		return e.get_type();
	}
	
	public List<Equipment> get_equips(){
		return equips;
	}
	
	public void set_coordinates(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public boolean roll(){
		Random r = new Random();
		last_rolled_id = r.nextInt(MAX_EQUIPMENT_NR);
		current_defense = 0;
		if(last_rolled_id<0 || last_rolled_id>=equips.size()){
			return false;
		}
		if(get_last_rolled_equip_type() == EqType.WOODEN_SHIELD){
			current_defense=1;
		}
		return true;
	}
	
	public boolean defense(Equipment attacked_with){
		if(current_defense<attacked_with.get_attack_value()){
			dying=true;
			System.out.println("UNIT died from attack");
			return dying;
		}
		System.out.println("UNIT survived attack");
		return dying;
	}
	
	public boolean decrease_health(){
		health=health-7;
		if(health<1){
			health=0;
			return true;
		}
		return false;
	}

	abstract void draw(Graphics g, int off_x, int off_y);
	
	protected void draw_eq(Graphics g, int off_x, int off_y){
		/*Equipment e = get_last_rolled_equip();
		int num = 0;
		if(e!=null){
			num = e.get_type_in_int();
		}*/
		g.setColor(Color.black);
		g.drawString(Integer.toString(last_rolled_id), off_x+GUI.FIELD_SIZE/2-5, off_y+GUI.FIELD_SIZE/2+7);
	}
}
