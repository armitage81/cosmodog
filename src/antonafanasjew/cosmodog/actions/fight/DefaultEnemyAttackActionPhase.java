package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.ExplosionAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.AttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.DefaultEnemyAttackingFightPhaseTransition;

import java.io.Serial;

/**
 * Represent a melee attack action phase.
 * <p>
 * This class is in the hierarchy of EnemyAttackActionPhase, AttackActionPhase, AbstractFightActionPhase and FixedLengthAsyncAction.
 * <p>
 * At the beginning of the phase, the hit sound is played, the damage is indicated as an overhead notification and the player and the enemy are turned to each other.
 * Additionally, the fight phase transition is initialized.
 * <p>
 * During the execution of the whole phase, the fight phase transition is updated so the renderer can display the action.
 * <p>
 * At the end of the action, the damage is applied to the player. If the player has a vehicle, the vehicle's armor is decreased and potentially it explodes.
 * If the player does not have a vehicle, the player's life is decreased. (The death is handled by the life listener.)
 * Finally, the fight phase transition is set to null.
 */
public class DefaultEnemyAttackActionPhase extends EnemyAttackActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	/**
	 * Creates a melee attack action phase from a fight phase result.
	 * <p>
	 * The fight phase result includes the attacker, the target and the damage dealt.
	 * The duration of the action is hardcoded in the constant ENEMY_ATTACK_ACTION_DURATION.
	 *
	 * @param fightPhaseResult Fight phase result including the attacker, the target and the damage dealt.
	 */
	public DefaultEnemyAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.ENEMY_ATTACK_ACTION_DURATION, fightPhaseResult);
	}

	/**
	 * Initializes the melee attack action phase.
	 * <p>
	 * The hit sound is played and the damage is indicated as an overhead notification over the player.
	 * The player and the enemy are turned to each other.
	 * <p>
	 * Additionally, the fight phase transition is initialized.
	 */
	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		
		AttackingFightPhaseTransition fightPhaseTransition = new DefaultEnemyAttackingFightPhaseTransition();
		fightPhaseTransition.setPlayer(getFightPhaseResult().getPlayer());
		fightPhaseTransition.setEnemy(getFightPhaseResult().getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);

		String text = "<font:critical> " + String.valueOf(getFightPhaseResult().getDamage());
		
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getPlayer(), text);
		
		getFightPhaseResult().getPlayer().lookAtActor(getFightPhaseResult().getEnemy());
		getFightPhaseResult().getEnemy().lookAtActor(getFightPhaseResult().getPlayer());
	}

	/**
	 * Updates the melee attack action phase.
	 * <p>
	 * The completion of the fight phase transition is updated in a linear fashion and is in the interval 0..1.
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
	 * At the end of the action, the damage is applied to the player. If the player has a vehicle, the vehicle's armor is decreased and potentially it explodes.
	 * If the player does not have a vehicle, the player's life is decreased. (The death is handled by the life listener.)
	 * Finally, the fight phase transition is set to null.
	 * <p>
	 * The explosion action is registered if the vehicle is destroyed.
	 * The vehicle is removed from the player's inventory if it is destroyed.
	 * <p>
	 * Take note: The explosion of the vehicle is registered in the global action registry as an action.
	 * TODO: Does it mean that the explosion is queued up after the whole fight action is finished? That would imply that the explosion happens only after all enemies have shot.
	 * Take note: The explosion action is registered under the key AsyncActionType.MINE_EXPLOSION.
	 */
	@Override
	public void onEnd() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		Player player = getFightPhaseResult().getPlayer();
		
		int damage = getFightPhaseResult().getDamage();
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - damage);
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPositionX(), player.getPositionY()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			player.decreaseLife(damage);
		}
		
		setFightPhaseTransition(null);
	}
}
