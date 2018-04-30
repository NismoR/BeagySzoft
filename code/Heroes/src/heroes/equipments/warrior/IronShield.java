package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;
import heroes.equipments.Equipment.EqType;

public class IronShield extends Equipment{
	private static AttackAbility attack=null;
	private static DefenseAbility defense=new DefenseAbility(2);

	public IronShield() {
		super(EqType.IRON_SHIELD, "IronShield", 2, HeroType.WARRIOR,attack,defense);
		// TODO Auto-generated constructor stub
	}
}
