package antonafanasjew.cosmodog.actions.fight;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

import java.io.Serial;

/**
 * Holds common logic for all fight action phases.
 * <p>
 * This class is abstract. Its subclasses include all types of attacks and enemy destruction phases.
 * <p>
 * This class holds an instance of a fight phase transition used by the renderer to display the fight phase.
 * That means that every fight phase has its own fight phase transition, be it an attack phase or a destruction phase.
 * <p>
 * This is a fixed length asynchronous action.
 */
public abstract class AbstractFightActionPhase extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8572769280800088275L;

	/**
	 * The fight phase transition instance used by the renderer to display the fight phase.
	 */
	private FightPhaseTransition fightPhaseTransition;

	/**
	 * Constructor. (Since this is an abstract class, it is overridden in subclasses.)
	 * @param duration The duration of the fight phase.
	 */
	public AbstractFightActionPhase(int duration) {
		super(duration);
	}

	/**
	 * Returns the fight phase transition for the renderer
	 * @return The fight phase transition.
	 */
	public FightPhaseTransition getFightPhaseTransition() {
		return fightPhaseTransition;
	}

	/**
	 * Sets the fight phase transition. This is usually done in the onTrigger methods of subclasses and reset to null
	 * in the onEnd methods of subclasses.
	 * @param fightPhaseTransition The fight phase transition.
	 */
	public void setFightPhaseTransition(FightPhaseTransition fightPhaseTransition) {
		this.fightPhaseTransition = fightPhaseTransition;
	}

	/**
	 * Updates the completion rate of the transition for the fight action phase effect.
	 * <p>
	 * The completion rate is updated in a linear fashion and is in the interval 0..1.
	 * <p>
	 * Take note: This is not the regular onUpdate method but a helper method that can be called by regular onUpdate methods
	 * in subclasses.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	protected void updateCompletion(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float newCompletion = (float)after / (float)getDuration();
		newCompletion = Math.min(newCompletion, 1.0f);
		getFightPhaseTransition().setCompletion(newCompletion);
	}
}
