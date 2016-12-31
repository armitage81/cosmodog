package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

/**
 * This fixed length action phase is initialized with the pre-calculated fight phase attack result
 * and executes asynchronously the fight phase by modifying the global fight phase transition.
 * This action phase can be used for both attacks - pc and npc.
 * Take note: An action phase is technically the same as a complete action. The difference is in the way
 * of its registration. An action is registered in the global action registry. An action phase is registered only
 * in an action itself.
 */
public class AttackActionPhase extends AbstractFightActionPhase {

	private static final long serialVersionUID = -3853130683025678558L;

	private FightActionResult.FightPhaseResult fightPhaseResult;

	/**
	 * Initialized with the pre-calculated fight action phase result.
	 */
	public AttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(fightPhaseResult.isPlayerAttack() ? Constants.PLAYER_ATTACK_ACTION_DURATION : (fightPhaseResult.getEnemy().getUnitType().isRangedUnit() ? Constants.RANGED_ENEMY_ATTACK_ACTION_DURATION : Constants.ENEMY_ATTACK_ACTION_DURATION));
		this.fightPhaseResult = fightPhaseResult;
	}

	/**
	 * Initializes the attack transition depending on the attacker and defender.
	 * Turns the player and the enemy to each other.
	 * Registers the hit points notification action.
	 */
	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		
		if (fightPhaseResult.isPlayerAttack()) {
			Log.debug("Player hits " + fightPhaseResult.getEnemy() + ".");
		} else {
			Log.debug(fightPhaseResult.getEnemy() + " hits player.");
		}
		
		FightPhaseTransition fightPhaseTransition = new FightPhaseTransition();
		fightPhaseTransition.player = fightPhaseResult.getPlayer();
		fightPhaseTransition.enemy = fightPhaseResult.getEnemy();
		fightPhaseTransition.completion = 0.0f;
		fightPhaseTransition.playerAttack = fightPhaseResult.isPlayerAttack();
		setFightPhaseTransition(fightPhaseTransition);

		String text = String.valueOf(fightPhaseResult.getDamage());
		
		DirectionType playerDirection = fightPhaseTransition.player.getDirection();
		DirectionType enemyDirection = fightPhaseTransition.enemy.getDirection();
		DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(fightPhaseTransition.player, fightPhaseTransition.enemy);
		
		boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
		boolean enemyLooksAway = enemyDirection.equals(playerDirection);
		
		if (fightPhaseResult.isPlayerAttack() && playerLooksAtEnemy && enemyLooksAway) {
			text = text + " (x2)";
		}
		OverheadNotificationAction.registerOverheadNotification(fightPhaseResult.isPlayerAttack() ? fightPhaseResult.getEnemy() : fightPhaseResult.getPlayer(), text);
		
		fightPhaseResult.getPlayer().lookAtActor(fightPhaseResult.getEnemy());
		fightPhaseResult.getEnemy().lookAtActor(fightPhaseResult.getPlayer());
		
		
	}
	
	/**
	 * Updates the attack transition depending on the passed time. Sets the completion percentage of the transition.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float newCompletion = (float)after / (float)getDuration();
		newCompletion = newCompletion > 1.0f ? 1.0f : newCompletion;
		getFightPhaseTransition().completion = newCompletion;
	}
	
	/**
	 * Applies the result of the attack phase and resets the transition.
	 */
	@Override
	public void onEnd() {
		
		Player player = fightPhaseResult.getPlayer();
		Enemy enemy = fightPhaseResult.getEnemy();
		
		int damage = fightPhaseResult.getDamage();
		
		if (fightPhaseResult.isPlayerAttack()) {
			
			Log.debug("Enemy has lost " + damage + " life points.");
			enemy.setLife(enemy.getLife() - damage);
			ArsenalInventoryItem arsenal = (ArsenalInventoryItem)player.getInventory().get(InventoryItemType.ARSENAL);
			WeaponType weaponType = arsenal.getSelectedWeaponType();
			if (weaponType != null) {
				Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
				weapon.reduceAmmunition(1);
			}
		} else {
			
			if (player.getInventory().hasVehicle()) {
				VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
				Vehicle vehicle = item.getVehicle();
				vehicle.setLife(vehicle.getLife() - damage);
				if (vehicle.dead()) {
					player.getInventory().remove(InventoryItemType.VEHICLE);
				}
			} else {
				Log.debug("Player has lost " + damage + " life points.");
				player.decreaseLife(damage);
			}
		}
		
		setFightPhaseTransition(null);
	}

}
