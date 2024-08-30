package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

import java.io.Serial;

/**
 * This abstract class represents an action phase that is triggered when an enemy is destroyed.
 * <p>
 * The hierarchy of this class is AbstractFightActionPhase and FixedLengthAsyncAction.
 * <p>
 * This class' purpose is to hold common logic for all enemy destruction phases.
 * <p>
 * There are, indeed, at least two different enemy destruction action phases. One for normal enemies and one for the last boss (guardian).
 * Nevertheless, both those types are implemented in a single class.
 * <p>
 * That's why this abstract class currently has only one implementation.
 */
public abstract class EnemyDestructionActionPhase extends AbstractFightActionPhase {

	@Serial
	private static final long serialVersionUID = -793569752077639630L;

	/**
	 * The player that destroyed the enemy.
	 */
	private Player player;

	/**
	 * The enemy that is destroyed.
	 */
	private Enemy enemy;

	/**
	 * Constructor. (Since this is an abstract class, it is overridden in subclasses.)
	 *
	 * @param duration The duration of the destruction action.
	 * @param player The player that destroyed the enemy.
	 * @param enemy The enemy that is destroyed.
	 */
	public EnemyDestructionActionPhase(int duration, Player player, Enemy enemy) {
		super(duration);
		this.player = player;
		this.enemy = enemy;
	}

	/**
	 * Returns the player that destroyed the enemy.
	 *
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Sets the player that destroyed the enemy.
	 *
	 * @param player The player.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * Returns the enemy that is destroyed.
	 *
	 * @return The enemy.
	 */
	public Enemy getEnemy() {
		return enemy;
	}

	/**
	 * Sets the enemy that is destroyed.
	 *
	 * @param enemy	The enemy.
	 */
	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

}
