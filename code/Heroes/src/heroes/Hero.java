package heroes;

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
}
