package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.ExplosionAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.AttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition;

import java.io.Serial;

/**
 * Represent an artillery attack action phase.
 * <p>
 * This class is in the hierarchy of EnemyAttackActionPhase, AttackActionPhase, AbstractFightActionPhase and FixedLengthAsyncAction.
 * <p>
 * At the beginning of the phase, the artillery shots sound is played and the artillery unit is turned south.
 * <p>
 * During the execution, 4 milestones are reached at 60%, 70%, 80% and 90% completion. These are the times where the hit sound is played.
 * (Visually, four grenades are hitting the player at the same time.)
 * <p>
 * During the whole phase, the fight phase transition is updated so the renderer can display the action.
 * <p>
 * At the end of the action, the damage is applied to the player. If the player has a vehicle, the vehicle's armor is decreased and potentially it explodes.
 * If the player does not have a vehicle, the player's life is decreased. (The death is handled by the life listener.)
 * Finally, the fight phase transition is set to null.
 */
public class ArtilleryAttackActionPhase extends EnemyAttackActionPhase {

	@Serial
	private static final long serialVersionUID = -847770758457510559L;

	/**
	 * Flag for the first grenade sound.
	 */
	private boolean milestone1 = false;

	/**
	 * Flag for the second grenade sound.
	 */
	private boolean milestone2 = false;

	/**
	 * Flag for the third grenade sound.
	 */
	private boolean milestone3 = false;

	/**
	 * Flag for the fourth grenade sound.
	 */
	private boolean milestone4 = false;

	/**
	 * Creates an artillery attack action phase from a fight phase result.
	 * <p>
	 * The fight phase result includes the attacker, the target and the damage dealt.
	 * The duration of the action is hardcoded in the constant RANGED_ENEMY_ATTACK_ACTION_DURATION.
	 *
	 * @param fightPhaseResult Fight phase result including the attacker, the target and the damage dealt.
	 */
	public ArtilleryAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.RANGED_ENEMY_ATTACK_ACTION_DURATION, fightPhaseResult);
	}
	
	
	/**
	 * Initializes the artillery attack action phase.
	 * <p>
	 * The artillery shots sound is played and the artillery unit is turned south.
	 * <p>
	 * The fight phase transition (defined as property of the superclass) is set to a new artillery attacking fight phase transition.
	 * The player and the enemy are set in the transition.
	 * The completion of the transition is set to 0.0f.
	 */
	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_ARTILLERY_SHOTS).play();
		AttackingFightPhaseTransition fightPhaseTransition = new ArtilleryAttackingFightPhaseTransition();
		fightPhaseTransition.setPlayer(getFightPhaseResult().getPlayer());
		fightPhaseTransition.setEnemy(getFightPhaseResult().getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);
		getFightPhaseResult().getEnemy().setDirection(DirectionType.DOWN);
	}

	/**
	 * Updates the artillery attack action phase.
	 * <p>
	 * The completion of the fight phase transition is updated in a linear fashion and is in the interval 0..1.
	 * <p>
	 * During the execution, 4 milestones are reached at 60%, 70%, 80% and 90% completion. These are the times where the hit sound is played.
	 * (Visually, four grenades are hitting the player at the same time.)
	 * <p>
	 * The hit sound is played only once for each milestone.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		float completion = Math.min(getFightPhaseTransition().getCompletion(), 1.0f);
		
		boolean playSound = false;
		
		if (!milestone1 && (completion >= 0.6)) {
			playSound = true;
			milestone1 = true;
		}
		
		if (!milestone2 && (completion >= 0.7)) {
			playSound = true;
			milestone2 = true;
		}
		
		if (!milestone3 && (completion >= 0.8)) {
			playSound = true;
			milestone3 = true;
		}
		
		if (!milestone4 && (completion >= 0.9)) {
			playSound = true;
			milestone4 = true;
		}
		
		if (playSound) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		}

		updateCompletion(before, after, gc, sbg);
	}

	/**
	 * At the end of the action, the damage is applied to the player. If the player has a vehicle, the vehicle's armor is decreased and potentially it explodes.
	 * If the player does not have a vehicle, the player's life is decreased. (The death is handled by the life listener.)
	 * Finally, the fight phase transition is set to null.
	 * <p>
	 * The damage dealt is displayed as an overhead notification.
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
		
		String text = "<font:critical> " + String.valueOf(getFightPhaseResult().getDamage());
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getPlayer(), text);
		
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			Vehicle vehicle = item.getVehicle();
			vehicle.setLife(vehicle.getLife() - damage);
			if (vehicle.dead()) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MINE_EXPLOSION, new ExplosionAction(500, player.getPosition()));
				player.getInventory().remove(InventoryItemType.VEHICLE);
			}
		} else {
			player.decreaseLife(damage);
		}
		
		setFightPhaseTransition(null);
	}
}
