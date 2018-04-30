package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;
import heroes.equipments.Equipment.EqType;

public class BladeOfRes extends Equipment{
	private static AttackAbility attack=new AttackAbility(1, 0, 1);
	private static DefenseAbility defense=new DefenseAbility(1);

	public BladeOfRes() {
		super(EqType.BLADE_OF_RES, "BladeOfResistance", 2, HeroType.WARRIOR,attack,defense);
		// TODO Auto-generated constructor stub
	}
}
