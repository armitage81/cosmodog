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

	public FightFromPlatformAction(Set<Enemy> targetEnemies) {
		this.targetEnemies = targetEnemies;
	}

	@Override
	public void onTriggerInternal() {
		Player player = ApplicationContextUtils.getPlayer();
		player.beginFight();
		initActionPhaseRegistry();
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endFight();
	}

	private void initActionPhaseRegistry() {

		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);

		if (overheadNotificationAction != null) {
			overheadNotificationAction.cancel();
		}

		Damage damage = new Damage();
		damage.setIncludingSquashed(true);
		damage.setAmount(1000);
		FightPhasePlan playerPhasePlan = FightPhasePlan.instance(player, targetEnemies.iterator().next(), damage, true);

		AttackActionPhase attackActionPhase = FightActionPhaseFactory.attackActionPhase(playerPhasePlan);
		getPhaseRegistry().registerPhase("attacking", attackActionPhase);
		DefaultEnemyDestructionActionPhase enemyDestructionActionPhase = new DefaultEnemyDestructionActionPhase(player, targetEnemies);
		getPhaseRegistry().registerPhase("enemyDestruction", enemyDestructionActionPhase);
	}
}
