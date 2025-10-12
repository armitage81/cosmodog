package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;

public abstract class AttackActionPhase extends AbstractFightActionPhase {

	@Serial
	private static final long serialVersionUID = 8538547874032046446L;

	private final FightPlan.FightPhasePlan fightPhasePlan;

	public AttackActionPhase(int duration, FightPlan.FightPhasePlan fightPhasePlan) {
		super(duration);
		this.fightPhasePlan = fightPhasePlan;
		getProperties().put("player", getFightPhasePlan().getPlayer());
		getProperties().put("enemy", getFightPhasePlan().getEnemy());
	}

	public FightPlan.FightPhasePlan getFightPhasePlan() {
		return fightPhasePlan;
	}
	
}
