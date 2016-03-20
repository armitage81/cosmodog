package antonafanasjew.cosmodog.actions.fight;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.fighting.AbstractEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.AbstractPlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * Represents the complete fight after a turn (with maybe multiple enemies)
 */
public class FightAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = -5197319922966169468L;

	private FightActionResult fightActionResult = new FightActionResult();
	private ActionRegistry actionPhaseRegistry = new ActionRegistry();
	
	private AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator;
	private AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator;
	
	
	public FightAction(AbstractPlayerAttackDamageCalculator c1, AbstractEnemyAttackDamageCalculator c2) {
		playerAttackDamageCalculator = c1;
		enemyAttackDamageCalculator = c2;
	}
	
	@Override
	public void onTrigger() {
		initFightActionResult();
		initActionPhaseRegistry();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}
	
	@Override
	public void onEnd() {
		// TODO Auto-generated method stub
		super.onEnd();
	}
	
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
	
	private void initActionPhaseRegistry() {
		for (FightActionResult.FightPhaseResult phaseResult : fightActionResult) {
			actionPhaseRegistry.registerAction(AsyncActionType.FIGHT, new AttackActionPhase(phaseResult));
			if(phaseResult.isPlayerAttack() && phaseResult.enoughDamageToKillEnemy()) {
				actionPhaseRegistry.registerAction(AsyncActionType.FIGHT, new EnemyDestructionActionPhase(phaseResult.getPlayer(), phaseResult.getEnemy()));
			}
		}
	}

}
