package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;

import java.io.Serial;

/**
 * Represents an attack action phase of an enemy.
 * <p>
 * This class is abstract and holds the common logic for all enemy attacks.
 * (There are at least the default enemy attack action phase and the artillery attack action phase as subclasses.)
 * <p>
 * This class is in the hierarchy of AttackActionPhase, AbstractFightActionPhase and FixedLengthAsyncAction.
 */
public abstract class EnemyAttackActionPhase extends AttackActionPhase {

	@Serial
	private static final long serialVersionUID = 8627524656456743696L;

	/**
	 * Constructor. Since this is an abstract class, the constructor is overridden in subclasses.
	 * @param duration Duration of the attack action phase.
	 * @param fightPhaseResult Fight phase result including the attacker, the target and the damage dealt.
	 */
	public EnemyAttackActionPhase(int duration, FightPhaseResult fightPhaseResult) {
		super(duration, fightPhaseResult);
	}

}
