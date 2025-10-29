package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.Set;

import antonafanasjew.cosmodog.fighting.Damage;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.FightPlan.FightPhasePlan;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FightFromPlatformAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -5197319922966169468L;

	private final Set<Enemy> targetEnemies;

	private FightPlan fightPlan = new FightPlan();

	public FightFromPlatformAction(Set<Enemy> targetEnemies) {
		this.targetEnemies = targetEnemies;
	}

	@Override
	public void onTriggerInternal() {
		
		Player player = ApplicationContextUtils.getPlayer();
		player.beginFight();
		
		initFightPlan();
		initActionPhaseRegistry();
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endFight();
	}

	private void initFightPlan() {
		Player player = ApplicationContextUtils.getPlayer();
		for (Enemy targetEnemy : targetEnemies) {
			updateFightPlanForOneEnemy(player, targetEnemy);
		}
	}

	private void updateFightPlanForOneEnemy(Player player, Enemy enemy) {
		Damage damage = new Damage();
		damage.setAmount(1000);// Huge damage to ensure the enemy is killed.
		damage.setIncludingSquashed(true);
		FightPhasePlan playerPhasePlan = FightPhasePlan.instance(player, enemy, damage, true);
		fightPlan.add(playerPhasePlan);
	}

	private void initActionPhaseRegistry() {

		for (FightPhasePlan phasePlan : fightPlan) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
			OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);

			if (overheadNotificationAction != null) {
				overheadNotificationAction.cancel();
			}
			
			AttackActionPhase attackActionPhase = FightActionPhaseFactory.attackActionPhase(phasePlan);
			getPhaseRegistry().registerPhase("attacking", attackActionPhase);
			EnemyDestructionActionPhase enemyDestructionActionPhase = FightActionPhaseFactory.enemyDestructionActionPhase(phasePlan.getPlayer(), phasePlan.getEnemy());
			getPhaseRegistry().registerPhase("enemyDestruction", enemyDestructionActionPhase);
		}
	}
}
