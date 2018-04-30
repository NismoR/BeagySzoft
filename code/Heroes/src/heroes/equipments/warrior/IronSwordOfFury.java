package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;

public class IronSwordOfFury extends Equipment {

	private static AttackAbility attack=new AttackAbility(2, 0, 1,true);
	private static DefenseAbility defense=null;

	public IronSwordOfFury() {
		super(EqType.IRON_SWORD_OF_FURY, "IronSwordOfFury", 2, HeroType.WARRIOR,attack,defense);
		// TODO Auto-generated constructor stub
	}
}
