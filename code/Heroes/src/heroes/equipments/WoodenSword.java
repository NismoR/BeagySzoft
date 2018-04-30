package heroes.equipments;

public class WoodenSword extends Equipment{
	

	private static AttackAbility attack=new AttackAbility(1, 0, 1);

	public WoodenSword() {
		super(EqType.WOODEN_SWORD, "WoodenSword", 1, null,attack,0);
		// TODO Auto-generated constructor stub
	}

}
