package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;
import heroes.equipments.Equipment.EqType;

public class WoodenSwordOfFury extends Equipment{

	private static AttackAbility attack=new AttackAbility(1, 0, 1,true);
	private static DefenseAbility defense=null;

	public WoodenSwordOfFury() {
		super(EqType.WOODEN_SWORD_OF_FURY, "WoodenSwordOfFury", 2, HeroType.WARRIOR,attack,defense);
		// TODO Auto-generated constructor stub
	}
}
