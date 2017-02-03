package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;

public abstract class EnemyAttackActionPhase extends AttackActionPhase {

	private static final long serialVersionUID = 8627524656456743696L;

	public EnemyAttackActionPhase(int duration, FightPhaseResult fightPhaseResult) {
		super(duration, fightPhaseResult);
	}

}
