package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Simulates an attack on the player in case he enters a wrong letter sequence in the Indiana Jones puzzle.
 * <p>
 * This is quite a hack. The player is not really attacked by an enemy.
 * <p>
 * The idea is to kill the player in case the letter sequence is broken.
 * (It should be: "ALL HOPE ABANDON YE WHO ENTER HERE".)
 * The killing is done by creating an instance of a dummy artillery unit that "attacks" the player with 1000 points of damage.
 * <p>
 * Take note: The action is registered as a fight action when stepping on a wrong letter plate (see LetterPlate).
 * But it has nothing to do with the actual FightAction. Nevertheless, the "enemy attack" phase is of type
 * AbstractFightActionPhase and is registered in the phase registry as a fight action.
 * <p>
 * TODO: This should be refactored. Also, the player should not be moved to the start of the map but at the beginning of the puzzle.
 */
public class WrongLetterSequenceFightAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -5197319922966169468L;

	/**
	 * Executed when the action is triggered.
	 * <p>
	 * Creates a dummy instance of an artillery unit that should attack the player with 1000 points of damage.
	 * Creates a fight action result with a single phase of enemy attack.
	 * Registers an enemy attack action phase with this result as a FIGHT action phase.
	 */
	@Override
	public void onTrigger() {
		Enemy dummy = new Enemy();
		dummy.setUnitType(UnitType.ARTILLERY);
		Player player = ApplicationContextUtils.getPlayer();
		FightActionResult.FightPhaseResult enemyPhaseResult = FightPhaseResult.instance(player, dummy, 1000, false);
		AttackActionPhase attackAction = FightActionPhaseFactory.attackActionPhase(enemyPhaseResult);
		getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT, attackAction);
	}

	/**
	 * Returns true if the action is finished.
	 * The action is finished when the phase registry does not contain any phases of type FIGHT.
	 * Since there is currently only one phase, the action is finished when the enemy attack phase is finished.
	 */
	@Override
	public boolean hasFinished() {
		return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.FIGHT);
	}

}
