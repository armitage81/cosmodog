package antonafanasjew.cosmodog.actions.cutscenes;

import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction.MineExplosionTransition;

import java.io.Serial;

/**
 * Represents an action that executes an explosion.
 * <p>
 * This is a fixed length action.
 * <p>
 * Take care: This action uses a mine explosion transition, but it is not a mine explosion action.
 * This action is for showing explosions when a wall is destroyed with dynamite.
 * The reason why the mine explosion transition is used is that the explosion rendering is done by the MineExplosionRenderer.
 * It was a quick and dirty solution to reuse the existing rendering code.
 * <p>
 * At the beginning, the explosion sound is played.
 * <p>
 * Then, during the whole duration of the action, an explosion transition rate is being updated in a linear fashion.
 * <p>
 * The transition is used to render the explosion effect on the screen.
 * <p>
 * At the end, the transition is set to null.
 */
public class ExplosionAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	/**
	 * Transition that is used to render the explosion effect on the screen.
	 * Take care: This is not a mine explosion action. The transition is used for rendering purposes only.
	 * It is a quick and dirty solution to reuse the existing rendering code.
	 */
	private MineExplosionTransition transition;

	/**
	 * Creates a new action with the given duration and position.
	 * The position of the explosion is given in tile coordinates.
	 *
	 * @param duration Duration of the action in milliseconds.
	 * @param position position of the explosion in tile coordinates.
	 */
	public ExplosionAction(int duration, Position position) {
		super(duration);
		this.transition = new MineExplosionTransition();
		this.transition.position = position;
	}

	/**
	 * Returns the transition that is used to render the explosion effect on the screen.
	 *
	 * @return Transition of the explosion effect holding the completion rate of the explosion and its position in tile coordinates.
	 */
	public MineExplosionTransition getTransition() {
		return transition;
	}

	/**
	 * Initializes the action by playing the explosion sound.
	 */
	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
	}

	/**
	 * Updates the completion rate of the transition for the explosion effect.
	 * <p>
	 * The completion rate is updated in a linear fashion and is in the interval 0..1.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		getTransition().percentage = actionPercentage;
	}

	/**
	 * Cleans up the transition after the action has ended so the renderer does not render the explosion anymore.
	 */
	@Override
	public void onEnd() {
		transition = null;
	}
}
