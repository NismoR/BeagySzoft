package heroes.equipments;

import heroes.Hero.HeroType;

public abstract class Equipment {
	public enum EqType {
		WOODEN_SWORD,
		WOODEN_SHIELD
	}
	private EqType eq_type;
	private String eq_name;
	private int value;
	private int defense_value=0;
	private HeroType available_for;
	private AttackAbility attack=null;
	
	public Equipment(EqType eq_type,String eq_name,int value,HeroType available_for, AttackAbility att, int def){
		this.eq_type = eq_type;
		this.eq_name = eq_name;
		this.value = value;
		this.available_for = available_for;
		this.attack = att;
		this.defense_value = def;
	}
	
	public EqType get_type() {
		return eq_type;
	}
	
	public int get_type_in_int(){
		switch (eq_type) {
		case WOODEN_SWORD:			
			return 1;
		case WOODEN_SHIELD:			
			return 2;
		default:		
			return 0;
		}
	}
	
	public String get_name(){
		return eq_name;
	}
	
	public int get_value(){
		return value;
	}
	public AttackAbility get_attack(){
		return attack;
	}
	public int get_defense_value(){
		return defense_value;
	}
	
	public HeroType get_available_for(){
		return available_for;
	}
}
