package heroes.equipments;

public class DefenseAbility {
	private int strength;
	private int nr_of_rolls=0;
	
	public DefenseAbility(int str) {
		// TODO Auto-generated constructor stub
		this.strength=str;
	}
	
	public void set_nr_of_rolls(int n){
		nr_of_rolls=n;
	}
	
	public int get_strength(){
		return strength;
	}
	
	public int get_nr_of_rolls(){
		return nr_of_rolls;
	}
}
