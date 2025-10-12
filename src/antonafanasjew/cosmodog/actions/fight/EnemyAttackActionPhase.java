package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.fight.FightPlan.FightPhasePlan;

import java.io.Serial;

public abstract class EnemyAttackActionPhase extends AttackActionPhase {

	@Serial
	private static final long serialVersionUID = 8627524656456743696L;

	public EnemyAttackActionPhase(int duration, FightPhasePlan fightPhasePlan) {
		super(duration, fightPhasePlan);
	}

}
