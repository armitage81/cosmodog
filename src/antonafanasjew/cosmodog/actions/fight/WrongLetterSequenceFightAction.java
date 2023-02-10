package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WrongLetterSequenceFightAction extends PhaseBasedAction {

	private static final long serialVersionUID = -5197319922966169468L;

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
		Enemy dummy = new Enemy();
		dummy.setUnitType(UnitType.ARTILLERY);
		Player player = ApplicationContextUtils.getPlayer();
		FightActionResult.FightPhaseResult enemyPhaseResult = FightPhaseResult.instance(player, dummy, 1000, false);
		AttackActionPhase attackAction = FightActionPhaseFactory.attackActionPhase(enemyPhaseResult);
		getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT, attackAction);
	}

}
