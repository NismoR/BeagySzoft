package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;

public class SwordOfRes extends Equipment{
	private static AttackAbility attack=new AttackAbility(1, 0, 1,false);
	private static DefenseAbility defense=new DefenseAbility(2);

	public SwordOfRes() {
		super(EqType.SWORD_OF_RES, "SwordOfResistance", 3, HeroType.WARRIOR,attack,defense);
		// TODO Auto-generated constructor stub
	}
}
