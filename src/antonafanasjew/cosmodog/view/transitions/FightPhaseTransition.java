package antonafanasjew.cosmodog.view.transitions;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Represents the transition during the fight.
 *
 */
public class FightPhaseTransition {

	public Player player;
	
	public Enemy enemy;
	
	public boolean enemyDestruction;
	
	public boolean playerAttack;
	
	public float completion;
	
	
}
