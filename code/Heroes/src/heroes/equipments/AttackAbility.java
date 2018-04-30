package heroes.equipments;

public class AttackAbility {
	private int strength;
	private int minRadius;
	private int maxRadius;
	
	public AttackAbility(int str, int minR, int maxR) {
		// TODO Auto-generated constructor stub
		this.strength=str;
		this.minRadius=minR;
		this.maxRadius=maxR;
	}
	
	public int get_strength(){
		return strength;
	}
	
	public int get_minR(){
		return minRadius;
	}
	
	public int get_maxR(){
		return maxRadius;
	}
}
