package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Abstract damage calculator to hold the common logic.
 */
public abstract class AbstractDamageCalculator implements DamageCalculator {

	@Override
	public Damage damage(Actor attacker, Actor defender) {
		return damageInternal(attacker, defender);
	}

	protected abstract Damage damageInternal(Actor attacker, Actor defender);

}
