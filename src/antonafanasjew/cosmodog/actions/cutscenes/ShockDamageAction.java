package antonafanasjew.cosmodog.actions.cutscenes;

import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents a shock damage action that is executed when the player steps into an electricity field.
 * (such as the one around the monolith).
 * <p>
 * This is a fixed length action. It is considered finished when the duration has passed.
 * <p>
 * At the beginning of the action, the player is notified about the shock damage via an overhead notification.
 * Additionally, a shock sound effect is played.
 * <p>
 * During the action, a transition is being updated with the completion rate. It is used to animate the shock damage.
 * <p>
 * At the end of the action, the player's life is decreased by a fixed amount of damage. If the player's life is less than or equal to
 * the damage, a death by shock action is registered in the game's main action registry.
 * <p>
 * Note how the actions are chained together to create a sequence of events that are executed one after another.
 * <p>
 * Also note how close (redundant) this class is to RadiationDamageAction.java. It should be possible to refactor.
 */
public class ShockDamageAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	/**
	 * Defines the damage taken when stepped into an electricity field.
	 */
	public static final int DAMAGE = 1;

	/**
	 * Represents a transition that is used to render the shock damage on the screen.
	 * <p>
	 * The position is nominated in tile coordinates. Usually, it will be the player's position.
	 */
	public static class ShockDamageTransition {
		/**
		 * Position of the shock damage effect in tile coordinates.
		 */
		public Position position;

		/**
		 * The completion rate of the shock damage effect. It is a value between 0.0 and 1.0.
		 */
		public float percentage = 0.0f;
	}

	/**
	 * The transition that is used to render the shock damage effect on the screen.
	 */
	private ShockDamageTransition transition;

	/**
	 * Creates a new action with the given duration.
	 * The position of the shock damage is the player's position in tile coordinates.
	 * <p>
	 * Initializes a transition instance with the position as of player's tile coordinates.
	 *
	 * @param duration Duration of the action in milliseconds.
	 */
	public ShockDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.transition = new ShockDamageTransition();
		this.transition.position = player.getPosition();
	}

	/**
	 * The transition that is used to render the shock damage effect on the screen.
	 */
	public ShockDamageTransition getTransition() {
		return transition;
	}

	/**
	 * Initializes the action by playing the shock damage sound and notifying the player about the electrocution
	 * via an overhead notification.
	 */
	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You were electrocuted.");
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> " + DAMAGE);
	}

	/**
	 * Updates the completion rate of the transition for the shock damage effect.
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
	 * Ends the action by setting the transition to null and reducing the player's life by the shock damage.
	 * <p>
	 * If the player's life is less than or equal to the damage, a death by shock action is registered in the game's main action registry
	 * to handle the player's death.
	 */
	@Override
	public void onEnd() {
		transition = null;
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		if (player.getLife() > DAMAGE) {
			player.decreaseLife(DAMAGE);
		} else {
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.DEATH_BY_SHOCK, new DeathByShockAction(1000));
		}		
	}
}
