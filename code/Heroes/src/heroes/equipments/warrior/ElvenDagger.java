package heroes.equipments.warrior;

import heroes.equipments.AttackAbility;
import heroes.equipments.DefenseAbility;
import heroes.equipments.Equipment;

public class ElvenDagger extends Equipment{
	private static AttackAbility attack=new AttackAbility(1, 0, 1,false);
	private static DefenseAbility defense=null;

	public ElvenDagger() {
		super(EqType.ELVEN_DAGGER, "ElvenDagger", 1, null,attack,defense);
		this.set_move_extr();
		// TODO Auto-generated constructor stub
	}
}
