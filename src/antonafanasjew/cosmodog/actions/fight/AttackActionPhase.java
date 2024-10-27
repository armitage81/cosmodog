package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;

/**
 * Represents an attack action phase.
 * <p>
 * This is an abstract class for all attack action phases.
 * <p>
 * There are currently three concrete subtypes of attack action phases:
 * PlayerAttackActionPhase, DefaultAttackActionPhase and ArtilleryAttackActionPhase.
 * The two latter ones are subclasses of the abstract class EnemyAttackActionPhase.
 * <p>
 * The raison d'être of this class is to provide a common way to hold the fight action result
 * regardless of the action phase subtype.
 * <p>
 * Note: This class extends AbstractFightActionPhase which is a subclass of FixedLengthAsyncAction.
 * AbstractFightActionPhase holds all fight action phases, not only the attacks.
 */
public abstract class AttackActionPhase extends AbstractFightActionPhase {

	@Serial
	private static final long serialVersionUID = 8538547874032046446L;

	/**
	 * The underlying fight phase result which this action phase is based on.
	 */
	private final FightActionResult.FightPhaseResult fightPhaseResult;

	/**
	 * Creates an attack action phase from a duration and a fight phase result.
	 * <p>
	 * Attack action phases represent single "hits" during a fight. They could be player's, melee enemies' or artillery attacks.
	 *
	 * @param duration Duration of the attack action phase. (Attack action phases are fixed length actions.)
	 * @param fightPhaseResult Fight phase result including the attacker, the target and the damage dealt.
	 */
	public AttackActionPhase(int duration, FightActionResult.FightPhaseResult fightPhaseResult) {
		super(duration);
		this.fightPhaseResult = fightPhaseResult;
		getProperties().put("player", getFightPhaseResult().getPlayer());
		getProperties().put("enemy", getFightPhaseResult().getEnemy());
	}

	/**
	 * Returns the fight phase result which this action phase is based on.
	 *
	 * @return Fight phase result.
	 */
	public FightActionResult.FightPhaseResult getFightPhaseResult() {
		return fightPhaseResult;
	}
	
}
