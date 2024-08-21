package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents an action that is triggered when the player dies by radiation.
 * <p>
 * This is a fixed length action.
 * <p>
 * Normally, if the player steps into radiation without protection, his life will be decreased by an amount.
 * <p>
 * The last, lethal damage (if player's life is less than the damage amount) is done by this action.
 * The only thing this action does is to wait a bit and then kill the player off.
 * <p>
 * This is done to not make the death too sudden.
 */
public class DeathByRadiationAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	/**
	 * Creates a new action with the given duration.
	 *
	 * @param duration duration of the action in milliseconds.
	 */
	public DeathByRadiationAction(int duration) {
		super(duration);
	}

	/**
	 * This action does not have any initializer.
	 */
	@Override
	public void onTrigger() {

	}

	/**
	 * This action does not have any update logic. The only relevant logic is at the end.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
	}

	/**
	 * Decreases the player's life by a fixed amount.
	 */
	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.decreaseLife(RadiationDamageAction.DAMAGE);
	}
}
