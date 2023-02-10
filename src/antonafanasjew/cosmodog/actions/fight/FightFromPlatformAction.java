package antonafanasjew.cosmodog.actions.fight;

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
 * 
 * This action has it's own action registry to maintain the fight phases.
 * 
 */
public class FightFromPlatformAction extends PhaseBasedAction {

	private static final long serialVersionUID = -5197319922966169468L;

	private Set<Enemy> targetEnemies;
	private FightActionResult fightActionResult = new FightActionResult();
	
	public FightFromPlatformAction(Set<Enemy> targetEnemies) {
		this.targetEnemies = targetEnemies;
	}

	/**
	 * Calculates the complete fight result. Calculates the fight phases based
	 * on the fight result and registers them in the internal action registry.
	 */
	@Override
	public void onTrigger() {
		
		Player player = ApplicationContextUtils.getPlayer();
		player.beginFight();
		
		initFightActionResult();
		initActionPhaseRegistry();
	}

	/**
	 * Updates the action registry, causing it to play forward the phase queue.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		getActionPhaseRegistry().update(after - before, gc, sbg);
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endFight();
	}
	
	/**
	 * Returns true if the fight action registry is empty, meaning that there
	 * are no unplayed fight phases.
	 */
	@Override
	public boolean hasFinished() {
		return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.FIGHT_FROM_PLATFORM);
	}

	/*
	 * Initializes the fight action result by simulating the fights between the
	 * player and all adjacent enemies.
	 */
	private void initFightActionResult() {
		Player player = ApplicationContextUtils.getPlayer();
		for (Enemy targetEnemy : targetEnemies) {
			updateFightActionResultForOneEnemy(player, targetEnemy);
		}
	}

	private void updateFightActionResultForOneEnemy(Player player, Enemy enemy) {
		int playerAttackDamage = 1000;
		FightActionResult.FightPhaseResult playerPhaseResult = FightPhaseResult.instance(player, enemy, playerAttackDamage, true);
		fightActionResult.add(playerPhaseResult);
	}

	/*
	 * Calculates the phase queue based on the fight action result.
	 */
	private void initActionPhaseRegistry() {

		for (FightActionResult.FightPhaseResult phaseResult : fightActionResult) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
			OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);

			if (overheadNotificationAction != null) {
				overheadNotificationAction.cancel();
			}
			
			AttackActionPhase attackActionPhase = FightActionPhaseFactory.attackActionPhase(phaseResult);
			getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT_FROM_PLATFORM, attackActionPhase);
			EnemyDestructionActionPhase enemyDestructionActionPhase = FightActionPhaseFactory.enemyDestructionActionPhase(phaseResult.getPlayer(), phaseResult.getEnemy());
			getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT_FROM_PLATFORM, enemyDestructionActionPhase);
		}
	}

}
