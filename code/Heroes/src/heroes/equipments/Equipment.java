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
	private HeroType available_for;
	
	public Equipment(EqType eq_type,String eq_name,int value,HeroType available_for){
		this.eq_type = eq_type;
		this.eq_name = eq_name;
		this.value = value;
		this.available_for = available_for;
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
	
	public HeroType get_available_for(){
		return available_for;
	}
}
