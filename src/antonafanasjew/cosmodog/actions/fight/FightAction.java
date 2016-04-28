package antonafanasjew.cosmodog.actions.fight;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.fighting.AbstractEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.AbstractPlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * Represents the complete fight after a turn (with maybe multiple enemies)
 * 
 * This action has it's own action registry to maintain the fight phases.
 * 
 */
public class FightAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = -5197319922966169468L;

	private FightActionResult fightActionResult = new FightActionResult();
	private ActionRegistry actionPhaseRegistry = new ActionRegistry();
	
	private AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator;
	private AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator;
	
	/**
	 * Initialized with the damage calculators for the PC and the enemy.
	 * @param c1 Player's damage calculator.
	 * @param c2 Enemy's damage calculator.
	 */
	public FightAction(AbstractPlayerAttackDamageCalculator c1, AbstractEnemyAttackDamageCalculator c2) {
		playerAttackDamageCalculator = c1;
		enemyAttackDamageCalculator = c2;
	}
	
	/**
	 * Calculates the complete fight result. Calculates the fight phases based on the fight result and registers them in the internal
	 * action registry.
	 */
	@Override
	public void onTrigger() {
		initFightActionResult();
		initActionPhaseRegistry();
	}

	/**
	 * Updates the action registry, causing it to play forward the phase queue.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}

	/**
	 * Returns true if the fight action registry is empty, meaning that there are no unplayed fight phases.
	 */
	@Override
	public boolean hasFinished() {
		return !actionPhaseRegistry.isActionRegistered(AsyncActionType.FIGHT);
	}
	
	/*
	 * Initializes the fight action result by simulating the fights between the player and all adjacent enemies.
	 */
	private void initFightActionResult() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		List<Enemy> adjacenEnemies = CosmodogMapUtils.enemiesAdjacentToPlayer(map, player);
		
		for (Enemy enemy : adjacenEnemies) {
			
			int playerAttackDamage = playerAttackDamageCalculator.damage(player, enemy);
			
			FightActionResult.FightPhaseResult playerPhaseResult = FightPhaseResult.instance(player, enemy, playerAttackDamage, true);
			
			
			fightActionResult.add(playerPhaseResult);
			if (playerPhaseResult.enoughDamageToKillEnemy() == false) {
				int enemyAttackDamage = enemyAttackDamageCalculator.damage(enemy, player);
				FightActionResult.FightPhaseResult enemyPhaseResult = FightPhaseResult.instance(player, enemy, enemyAttackDamage, false);
				fightActionResult.add(enemyPhaseResult);
			} 
			
			if (fightActionResult.enoughDamageToKillPlayer()) {
				break;
			}
		}
	}
	
	/*
	 * Calculates the phase queue based on the fight action result.
	 */
	private void initActionPhaseRegistry() {
		
		
		
		
		for (FightActionResult.FightPhaseResult phaseResult : fightActionResult) {
			
			Actor defender = phaseResult.isPlayerAttack() ? phaseResult.getEnemy() : phaseResult.getPlayer();
			Color color = defender == phaseResult.getPlayer() ? Color.red : Color.green;
			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
			
			OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
			
			String text = "-" + String.valueOf(phaseResult.getDamage());
			
			if (overheadNotificationAction != null) {
				overheadNotificationAction.cancel();
				
			}

			OverheadNotificationAction action = OverheadNotificationAction.create(defender, text, color);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
			
			actionPhaseRegistry.registerAction(AsyncActionType.FIGHT, new AttackActionPhase(phaseResult));
			if(phaseResult.isPlayerAttack() && phaseResult.enoughDamageToKillEnemy()) {
				actionPhaseRegistry.registerAction(AsyncActionType.FIGHT, new EnemyDestructionActionPhase(phaseResult.getPlayer(), phaseResult.getEnemy()));
			}
		}
	}

}
