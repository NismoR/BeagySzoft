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
		if(last_rolled_id<0 || last_rolled_id>=equips.size()){
			return false;
		}
		return true;
	}

	abstract void draw(Graphics g, int off_x, int off_y);
	
	private void draw_small_triangle(Graphics g, int x, int y, int size, int rot_clckwise_90){
		Polygon p = new Polygon();
		switch (rot_clckwise_90) {
		case 0:
			p.addPoint(x+size, y);
			p.addPoint(x, y+size);
			p.addPoint(x, y);    
		    g.fillPolygon(p);
			break;
		case 1:
			p.addPoint(x-size, y);
			p.addPoint(x, y+size);
			p.addPoint(x, y);    
		    g.fillPolygon(p);
			break;
		case 2:
			p.addPoint(x-size, y);
			p.addPoint(x, y-size);
			p.addPoint(x, y);    
		    g.fillPolygon(p);
			break;
		case 3:
			p.addPoint(x+size, y);
			p.addPoint(x, y-size);
			p.addPoint(x, y);    
		    g.fillPolygon(p);
			break;

		default:
			break;
		}
		
	}
	
	protected void draw_defense(Graphics g, int off_x, int off_y){
		int size = GUI.FIELD_WIDTH/4;
		switch (current_defense) {
		case 2:
			draw_small_triangle(g, off_x+GUI.FIELD_HEIGHT, off_y, size, 1);
			draw_small_triangle(g, off_x, off_y+GUI.FIELD_WIDTH, size, 3);
			/*FALLTHRU*/
		case 1:	
			draw_small_triangle(g, off_x, off_y, size, 0);
			draw_small_triangle(g, off_x+GUI.FIELD_HEIGHT, off_y+GUI.FIELD_WIDTH, size, 2);
			break;
		default:
			break;
		}
	}
	
	protected void draw_eq(Graphics g, int off_x, int off_y){
		/*Equipment e = get_last_rolled_equip();
		int num = 0;
		if(e!=null){
			num = e.get_type_in_int();
		}*/
		g.setColor(Color.black);
		g.drawString(Integer.toString(last_rolled_id), off_x+GUI.FIELD_WIDTH/2-5, off_y+GUI.FIELD_HEIGHT/2+7);
	}
}
