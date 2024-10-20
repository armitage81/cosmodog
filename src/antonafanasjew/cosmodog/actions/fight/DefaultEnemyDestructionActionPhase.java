package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DroppedCollectibleFactory;
import antonafanasjew.cosmodog.view.transitions.EnemyDestructionFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.DefaultEnemyDestructionFightPhaseTransition;

import java.io.Serial;

/**
 * Represents an action phase that is triggered when an enemy is destroyed.
 * <p>
 * The hierarchy of this class is EnemyDestructionActionPhase, AbstractFightActionPhase and FixedLengthAsyncAction.
 * <p>
 * The destruction phase happens when the player destroys an enemy.
 * Usually, it is represented by an explosion. But the last boss (guardian) has a different destruction animation.
 * Additionally, the enemy may drop an item during the action.
 */
public class DefaultEnemyDestructionActionPhase extends EnemyDestructionActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	/**
	 * Returns the duration of the destruction action.
	 * <p>
	 * The duration is different for different enemy types.
	 * In the most cases, it is the same, but for the last boss (guardian), it is different.
	 * The distinction is done to make the destruction of the last boss more epic.
	 * <p>
	 * For guardians, the duration is 8000 milliseconds.
	 * For all other enemies, the duration is the constant ENEMY_DESTRUCTION_ACTION_DURATION.
	 * <p>
	 * This method is static to be used in the constructor.
	 *
	 * @param enemy enemy to calculate the duration for.
	 * @return duration of the destruction action in milliseconds.
	 */
	private static int enemyDestructionActionDuration(Enemy enemy) {
		if (enemy.getUnitType().equals(UnitType.GUARDIAN)) {
			return 8000;
		}
		return Constants.ENEMY_DESTRUCTION_ACTION_DURATION;
	}

	/**
	 * Creates a destruction action phase for the given player and enemy.
	 * <p>
	 * The duration of the destruction action is calculated based on the enemy type.
	 *
	 * @param player player that destroyed the enemy.
	 * @param enemy enemy that is destroyed.
	 */
	public DefaultEnemyDestructionActionPhase(Player player, Enemy enemy) {
		super(enemyDestructionActionDuration(enemy), player, enemy);
	}

	/**
	 * Initializes the destruction action phase.
	 * <p>
	 * Usually, the sound of the explosion is played. But for the last boss, there is another sound.
	 * <p>
	 * Additionally, the fight phase transition is initialized for the renderer to display the enemy destruction.
	 * (The renderer will usually search for a fight action in the action registry and extract the transition from there if any.)
	 */
	@Override
	public void onTrigger() {
		if (getEnemy().getUnitType().equals(UnitType.GUARDIAN)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_GUARDIAN_DESTROYED).play();
		} else {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
		}
		EnemyDestructionFightPhaseTransition fightPhaseTransition = new DefaultEnemyDestructionFightPhaseTransition();
		fightPhaseTransition.setPlayer(getPlayer());
		fightPhaseTransition.setEnemy(getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);
		
	}

	/**
	 * Simply updates the transition with the completion rate.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		updateCompletion(before, after, gc, sbg);
	}

	/**
	 * Ends the action phase by removing the enemy from the map and dropping an item if the enemy has one.
	 * The item is then transformed from the enemy's inventory item to a collectible.
	 * When an item is dropped, a sound is played.
	 * <p>
	 * Take note: If the enemy stays at the same position as a collectible and drops an item,
	 * then the original collectible is replaced by a composed collectible consisting of the original collectible
	 * and the dropped item.
	 * <p>
	 * The fight phase transition is also set to null.
	 */
	@Override
	public void onEnd() {
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		
		Enemy enemy = getEnemy();
		
		InventoryItem item = enemy.getInventoryItem();
		
		if (item != null) {
			Collectible dropped = DroppedCollectibleFactory.createCollectibleFromDroppedItem(item);
			if (dropped != null) {
				
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_DROPPED_ITEM).play();

				dropped.setPosition(enemy.getPosition());

				Piece piece = cosmodogMap.pieceAtTile(enemy.getPosition());
				if (piece instanceof Collectible) {
					CollectibleComposed newCollectible = new CollectibleComposed();
					newCollectible.setPosition(enemy.getPosition());
					newCollectible.addElement((Collectible)piece);
					newCollectible.addElement(dropped);
					cosmodogMap.getMapPieces().remove(piece.getPosition());
					dropped = newCollectible;
				}
				
				cosmodogMap.getMapPieces().put(dropped.getPosition(), dropped);
			}
		}
		cosmodogMap.getEnemies().remove(getEnemy());
		setFightPhaseTransition(null);
	}
}
