package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.view.transitions.AttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.PlayerAttackingFightPhaseTransition;

import java.io.Serial;

/**
 * Represents the player's attack action phase.
 * <p>
 * This phase is usually executed within a fight action and is based on the corresponding phase result
 * that is precalculated there.
 * <p>
 * It is responsible for everything that happens when the player attacks an enemy:
 * damage and critical hit calculation, attack sound, overhead notification, weapon ammunition reduction etc.
 * The life of the enemy is reduced within this action.
 * <p>
 * Take note: If the enemy is destroyed, its destruction is handled by a different action phase
 * (DefaultEnemyDestructionActionPhase). This action does not decide whether the enemy should be destroyed or not.
 * The destruction phase is registered outside of this phase depending on the precalculated fight phase result.
 * Enemy's retaliation is also handled in a different action phase.
 * <p>
 * The hierarchy of this class is AttackActionPhase, AbstractFightActionPhase and FixedLengthAsyncAction.
 */
public class PlayerAttackActionPhase extends AttackActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	/**
	 * Constructor.
	 * <p>
	 * The duration of the attack action is defined in the constant PLAYER_ATTACK_ACTION_DURATION.
	 *
	 * @param fightPhaseResult The fight phase result that contains all necessary information for the attack.
	 *                         Damage will be applied based on this result.
	 */
	public PlayerAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.PLAYER_ATTACK_ACTION_DURATION, fightPhaseResult);
	}

	/**
	 * Triggers the attack action phase.
	 * <p>
	 * This method is usually called from the FightAction to trigger the player attack phase.
	 * At this stage, the FightAction already has precalculated the fight result.
	 * <p>
	 * Following happens at the beginning of the attack phase:
	 * <p>
	 * - The hit sound is played.
	 * <p>
	 * - The attacking fight phase transition is created and set. A renderer will use it to render the attack.
	 * <p>
	 * - The overhead notification shows the damage dealt. If the attack is a critical hit, it is marked with (x2).
	 * TODO: The critical hit calculation should be reused from the result preparation since it is calculated there anyway.
	 * <p>
	 * - The enemy turns toward the player. The player turns toward the enemy.
	 * (The latter is actually not needed since the player is always looking at the enemy when attacking.)
	 */
	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		
		AttackingFightPhaseTransition fightPhaseTransition = new PlayerAttackingFightPhaseTransition();
		fightPhaseTransition.setPlayer(getFightPhaseResult().getPlayer());
		fightPhaseTransition.setEnemy(getFightPhaseResult().getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);

		String text = String.valueOf(getFightPhaseResult().getDamage());
		
		DirectionType playerDirection = fightPhaseTransition.getPlayer().getDirection();
		DirectionType enemyDirection = fightPhaseTransition.getEnemy().getDirection();
		DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(fightPhaseTransition.getPlayer(), fightPhaseTransition.getEnemy());
		
		boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
		boolean enemyLooksAway = enemyDirection.equals(playerDirection);
		Enemy enemy = fightPhaseTransition.getEnemy();
		boolean criticalHitsAllowed = !enemy.getUnitType().isRangedUnit() && !(enemy.getSpeedFactor() == 0.0f);
		
		if (playerLooksAtEnemy && enemyLooksAway && criticalHitsAllowed) {
			text = text + " (x2)";
		}
		
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), text);
		
		getFightPhaseResult().getPlayer().lookAtActor(getFightPhaseResult().getEnemy());
		getFightPhaseResult().getEnemy().lookAtActor(getFightPhaseResult().getPlayer());
		
		
	}

	/**
	 * Updates the player attack action phase.
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
	 * At the end of the phase, the damage is applied to the enemy. The enemy's life is reduced.
	 * The player's ammunition is reduced by 1 if using a weapon.
	 * <p>
	 * The fight phase transition is set to null so the renderer stops considering the attack phase.
	 */
	@Override
	public void onEnd() {
		
		Player player = getFightPhaseResult().getPlayer();
		Enemy enemy = getFightPhaseResult().getEnemy();
		
		int damage = getFightPhaseResult().getDamage();
		
		enemy.setLife(enemy.getLife() - damage);
		Arsenal arsenal = player.getArsenal();
		WeaponType weaponType = arsenal.getSelectedWeaponType();
		if (weaponType != null) {
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
			weapon.reduceAmmunition(1);
		}
		
		setFightPhaseTransition(null);
	}


}
