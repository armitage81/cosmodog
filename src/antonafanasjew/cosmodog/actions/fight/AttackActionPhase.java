package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

/**
 * This fixed length action phase is initialized with the pre-calculated fight phase attack result
 * and executes asynchronously the fight phase by modifying the global fight phase trnsition.
 * This action phase can be used for both attacks - pc and npc.
 */
public class AttackActionPhase extends FixedLengthAsyncAction  {

	private static final long serialVersionUID = -3853130683025678558L;

	private FightActionResult.FightPhaseResult fightPhaseResult;
	private CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

	/**
	 * Initialized with the pre-calculated fight action phase result.
	 * @param fightPhaseResult Result of the fight action phase.
	 */
	public AttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(fightPhaseResult.isPlayerAttack() ? Constants.PLAYER_ATTACK_ACTION_DURATION : Constants.ENEMY_ATTACK_ACTION_DURATION);
		this.fightPhaseResult = fightPhaseResult;
	}

	/**
	 * Initializes the attack transition depending on the attacker and defender.
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

		fightPhaseResult.getPlayer().lookAtActor(fightPhaseResult.getEnemy());
		fightPhaseResult.getEnemy().lookAtActor(fightPhaseResult.getPlayer());
		
		cosmodogGame.setFightPhaseTransition(fightPhaseTransition);
		
	}
	
	/**
	 * Updates the attack transition depending on the passed time. Sets the completion percentage of the transition.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		cosmodogGame.getFightPhaseTransition().completion = (float)after / (float)getDuration();
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
				VehicleInventoryItem item = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.ARSENAL);
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
		
		cosmodogGame.setFightPhaseTransition(null);
	}

}
