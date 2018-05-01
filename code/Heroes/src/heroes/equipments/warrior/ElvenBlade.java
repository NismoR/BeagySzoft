package heroes.equipments.warrior;

import heroes.Hero.HeroType;
import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;

public class ElvenBlade extends Equipment{
	private static AttackAbility attack=new AttackAbility(2, 0, 1,false);
	private static DefenseAbility defense=null;

	public ElvenBlade() {
		super(EqType.ELVEN_BLADE, "ElvenBlade", 1, HeroType.WARRIOR,attack,defense);
		this.set_move_extr();
		// TODO Auto-generated constructor stub
	}
}
