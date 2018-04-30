package heroes.equipments;

public class WoodenShield extends Equipment{

	private static AttackAbility attack=null;
	private static DefenseAbility defense=new DefenseAbility(1);

	public WoodenShield() {
		super(EqType.WOODEN_SHIELD, "WoodenShield", 1, null,attack,defense);
		// TODO Auto-generated constructor stub
	}

}
