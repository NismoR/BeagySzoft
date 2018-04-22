package heroes;

public abstract class Hero {
	
	public enum HeroType {
		WARRIOR,
		ARCHER,
		MAGE
	}
	
	private HeroType type;
	private int player_id;
	
	public Hero(HeroType type, int player_id){
		this.type = type;
		this.player_id = player_id;
	}
	
	public HeroType get_type(){
		return this.type;
	}
	
	public int get_player_id(){
		return this.player_id;
	}
}
