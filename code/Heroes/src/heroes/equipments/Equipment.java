package heroes.equipments;

import heroes.Hero.HeroType;

public abstract class Equipment {
	public enum EqType {
		NONE			(0),
		WOODEN_SWORD	(1),
		WOODEN_SHIELD	(2),
		IRON_SWORD		(3),
		IRON_SHIELD		(4),
		BLADE_OF_RES	(5),
		SWORD_OF_RES	(6);
		
		public final int id;
		private EqType(int id) {
			this.id=id;
		}
	}
	private EqType eq_type;
	private String eq_name;
	private int value;
	private DefenseAbility defense=null;
	private HeroType available_for;
	private AttackAbility attack=null;
	
	public Equipment(EqType eq_type,String eq_name,int value,HeroType available_for, AttackAbility att, DefenseAbility def){
		this.eq_type = eq_type;
		this.eq_name = eq_name;
		this.value = value;
		this.available_for = available_for;
		this.attack = att;
		this.defense = def;
	}
	
	public EqType get_type() {
		return eq_type;
	}
	
	public int get_type_in_int(){
		return eq_type.id;
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
	public DefenseAbility get_defense(){
		return defense;
	}
	
	public HeroType get_available_for(){
		return available_for;
	}
}
