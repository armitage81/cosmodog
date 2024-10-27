package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Represents the fight from a platform. This is a simple fight as all enemies that are touched by the platform are obliterated
 * without any chance to retaliate. No damage calculation, no ammunition depletion needed.
 * <p>
 * The player is always the attacker here. (There is no way for enemies to attack the player if he is in the platform.)
 * Other than in the normal fights, the player can attack multiple enemies at once.
 * <p>
 * This action has its own action registry to maintain the fight phases.
 * 
 */
public class FightFromPlatformAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -5197319922966169468L;

	/**
	 * The enemies that are targeted by the player. Note that the player can attack multiple enemies at once if he is on a platform.
	 */
	private final Set<Enemy> targetEnemies;

	/**
	 * The fight action result. For each enemy adjacent to the platform, a fight phase result is calculated by setting
	 * the player as attacker, the enemy as target and a fixed (huge) damage amount.
	 * Note that there is no difference in the structure of this fight action result compared to normal fights.
	 */
	private FightActionResult fightActionResult = new FightActionResult();

	/**
	 * Constructor.
	 * <p>
	 * Retrieves a set of adjacent enemies that are targeted by the player.
	 *
	 * @param targetEnemies The enemies that are targeted by the player.
	 */
	public FightFromPlatformAction(Set<Enemy> targetEnemies) {
		this.targetEnemies = targetEnemies;
	}

	/**
	 * Calls the player's listener to notify it about the impending fight.
	 * <p>
	 * Then calculates the result for the whole fight. The fight result is a list of fight phase results.
	 * Each fight phase result represents a single attack phase. The player is always the attacker in this case. (Enemies have no chance to attack the platform.)
	 * <p>
	 * The calculated result is set to the property fightActionResult.
	 * <p>
	 * The property fightActionResult is then used to initialize the action phase registry. Fight action phases are created based on the
	 * fight phase results. The fight action phases are then registered in the action phase registry. Fight actions contain player attacks
	 * and enemy destruction.
	 * <p>
	 * At the end of this method, the fight is decided and its execution is prepared. The fight is then played in the onUpdate method.
	 */
	@Override
	public void onTriggerInternal() {
		
		Player player = ApplicationContextUtils.getPlayer();
		player.beginFight();
		
		initFightActionResult();
		initActionPhaseRegistry();
	}

	/**
	 * At the end of the action, the player's listener is called to indicate the end of the fight.
	 * <p>
	 * Usually, same calculations are executed here as the ones after the player's movement.
	 * <p>
	 * Note: afterMovement and afterFight listener methods do not trigger enemy movements or attacks.
	 * Instead, they move during the player's movement action, and they fight during the player's movement or fight action.
	 */
	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endFight();
	}

	/**
	 * Initializes the fight action result by simulating the fights between the
	 * player and all adjacent enemies.
	 * Note: The player is always the attacker in this case. The enemies have no chance to attack the player.
	 * The enemies are destroyed in any case.
	 */
	private void initFightActionResult() {
		Player player = ApplicationContextUtils.getPlayer();
		for (Enemy targetEnemy : targetEnemies) {
			updateFightActionResultForOneEnemy(player, targetEnemy);
		}
	}

	/**
	 * Updates the fight action result for one enemy. The player is the attacker, the enemy is the target.
	 * The player's attack damage is set to a fixed (huge) value. It will always kill the enemy.
	 *
	 * @param player The player who is the attacker.
	 * @param enemy The enemy who is the target.
	 */
	private void updateFightActionResultForOneEnemy(Player player, Enemy enemy) {
		int playerAttackDamage = 1000;
		FightActionResult.FightPhaseResult playerPhaseResult = FightPhaseResult.instance(player, enemy, playerAttackDamage, true);
		fightActionResult.add(playerPhaseResult);
	}

	/**
	 * Calculates the phase queue based on the fight action result.
	 * <p>
	 * For each fight phase result, a player's attack action phase and an enemy destruction action phase are created.
	 * After all, the player is always the attacker if he is on the platform and each attacked enemy is destroyed.
	 * <p>
	 * If there are any overhead notifications, they are canceled.
	 */
	private void initActionPhaseRegistry() {

		for (FightActionResult.FightPhaseResult phaseResult : fightActionResult) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
			OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);

			if (overheadNotificationAction != null) {
				overheadNotificationAction.cancel();
			}
			
			AttackActionPhase attackActionPhase = FightActionPhaseFactory.attackActionPhase(phaseResult);
			getPhaseRegistry().registerPhase(attackActionPhase);
			EnemyDestructionActionPhase enemyDestructionActionPhase = FightActionPhaseFactory.enemyDestructionActionPhase(phaseResult.getPlayer(), phaseResult.getEnemy());
			getPhaseRegistry().registerPhase(enemyDestructionActionPhase);
		}
	}
}
