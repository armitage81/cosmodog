package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;

public interface DamageCalculator {

	int damage(Actor attacker, Actor defender);
	
}
