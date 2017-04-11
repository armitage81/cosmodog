package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.impl.DefaultEnemyDestructionActionPhase;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class LastBossFightAction extends PhaseBasedAction {

	private static final long serialVersionUID = -5197319922966169468L;

	private Enemy targetEnemy;
	
	public LastBossFightAction(Enemy targetEnemy) {
		this.targetEnemy = targetEnemy;
	}

	@Override
	public void onTrigger() {
		initActionPhaseRegistry();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		getActionPhaseRegistry().update(after - before, gc, sbg);
	}

	@Override
	public boolean hasFinished() {
		return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.FIGHT);
	}

	private void initActionPhaseRegistry() {
		Player player = ApplicationContextUtils.getPlayer();
		EnemyDestructionActionPhase enemyDestructionActionPhase = new DefaultEnemyDestructionActionPhase(player, targetEnemy);
		getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT, enemyDestructionActionPhase);
	}

}
