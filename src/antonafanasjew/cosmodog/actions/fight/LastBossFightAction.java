package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents the action that shows the destruction of the last boss.
 * This is the enemy in the alien base that looks like a mirror with a face on it.
 * <p>
 * Take note: This is NOT a fight action. It has almost nothing to do with the actual fighting the last boss.
 * The actual fight against the guardian is handled as a normal fight, only the destruction animation is specific
 * (see DefaultEnemyDestructionActionPhase).
 * <p>
 * This action here is registered during the execution of the action DamageLastBossAction
 * that is triggered when the player reaches the terminal near the guardian.
 * Depending on the player's inventory (16 software chips), the player can deactivate the guardian.
 * In this case, the guardian is destroyed without a fight.
 * The destruction animation is handled within this action here.
 * <p>
 * But take care: since the destruction animation is the same as after a normal fight,
 * the class DefaultEnemyDestructionActionPhase is used to handle the destruction animation also in this case.
 * So even if the action itself has nothing to do with a FightAction, its internal phase is of subtype of FightAction.
 * It is also registered as an action phase of type FIGHT in the phase registry.
 * <p>
 * TODO: This should be refactored.
 * <p>
 * Use this class if additional phases are needed to make the destruction of the last boss more epic.
 * <p>
 * Do not use this class for any decisions on how the destruction itself should be handled.
 * Use DamageLastBossAction for that.
 * <p>
 * The action is a PhaseBasedAction that initializes its own phase registry.
 * <p>
 * The action is finished when the phase registry does not contain any phases.
 */
public class LastBossFightAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -5197319922966169468L;

	/**
	 * The guardian enemy is needed to set it as the target enemy
	 * for the destruction phase.
	 * Take note: The guardian is not really a target enemy in this case. There is no fight.
	 * The instance is just needed since the destruction phase is a subtype of FightAction.
	 */
	private final Enemy guardian;

	/**
	 * Constructor.
	 * @param guardian The guardian enemy. It is needed to set it as the target enemy for the destruction phase.
	 */
	public LastBossFightAction(Enemy guardian) {
		this.guardian = guardian;
	}

	/**
	 * Initializes the phase registry with the destruction phase.
	 * The destruction phase is a subtype of FightAction.
	 * The destruction phase is registered as an action of type FIGHT.
	 * <p>
	 * Take note: Since there is only one phase, the action could be simply implemented as a VariableLengthAsyncAction.
	 * But there could be additional phases in the future.
	 * <p>
	 * Take note: There is no actual fight in this action, the destruction action is just reused.
	 */
	@Override
	public void onTriggerInternal() {
		Player player = ApplicationContextUtils.getPlayer();
		EnemyDestructionActionPhase enemyDestructionActionPhase = new DefaultEnemyDestructionActionPhase(player, guardian);
		getPhaseRegistry().registerPhase(enemyDestructionActionPhase);
	}

}
