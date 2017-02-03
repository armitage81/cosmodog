package antonafanasjew.cosmodog.view.transitions;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Represents the transition during the fight.
 *
 */
public abstract class FightPhaseTransition extends AbstractTransition {

	private Player player;
	private Enemy enemy;
	private float completion;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public float getCompletion() {
		return completion;
	}

	public void setCompletion(float completion) {
		this.completion = completion;
	}
	
}
