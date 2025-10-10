package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Calculates the damage dealt when the attacker attacks a defender.
 */
public interface DamageCalculator {

	Damage damage(Actor attacker, Actor defender);

}
