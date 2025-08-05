package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Calculates the damage dealt when the attacker attacks a defender.
 */
public interface DamageCalculator {

	/**
	 * Returns the amount of damage points dealt by the attacker to the defender.
	 * @param attacker The attacking actor.
	 * @param defender The defending actor.
	 * @return Amount of damage points dealt by the attacker.
	 */
	int damage(Actor attacker, Actor defender);

	boolean criticalHit(Actor attacker, Actor defender);
	
}
