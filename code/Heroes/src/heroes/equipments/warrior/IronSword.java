package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;

public class IronSword extends Equipment{
	private static AttackAbility attack=new AttackAbility(2, 0, 1);
	private static DefenseAbility defense=null;

	public IronSword() {
		super(EqType.IRON_SWORD, "IronSword", 2, HeroType.WARRIOR,attack,defense);
		// TODO Auto-generated constructor stub
	}
}
