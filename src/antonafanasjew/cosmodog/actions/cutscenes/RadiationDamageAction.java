package antonafanasjew.cosmodog.actions.cutscenes;

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
 * Represents a radiation damage action that is executed when the player steps into a radiation field.
 * <p>
 * This is a fixed length action. It is considered finished when the duration has passed.
 * <p>
 * At the beginning of the action, the player is notified about the radiation exposure via an overhead notification.
 * Additionally, a coughing sound effect is played.
 * <p>
 * During the action, a transition is being updated with the completion rate. It is used to animate the radiation damage.
 * <p>
 * At the end of the action, the player's life is decreased by a fixed amount of damage. If the player's life is less than or equal to
 * the damage, a death by radiation action is registered in the game's main action registry.
 * <p>
 * Note how the actions are chained together to create a sequence of events that are executed one after another.
 */
public class RadiationDamageAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;

	/**
	 * Defines the damage taken when stepped into a radiation field.
	 */
	public static final int DAMAGE = 9;

	/**
	 * Represents a transition that is used to render the radiation damage on the screen.
	 * <p>
	 * The position is nominated in tile coordinates. Usually, it will be the player's position.
	 */
	public static class RadiationDamageTransition {

		/**
		 * Horizontal position of the radiation damage effect in tile coordinates.
		 */
		public int positionX;

		/**
		 * Vertical position of the radiation damage effect in tile coordinates.
		 */
		public int positionY;

		/**
		 * The completion rate of the radiation damage effect. It is a value between 0.0 and 1.0.
		 */
		public float percentage = 0.0f;

	}

	/**
	 * The transition that is used to render the radiation damage effect on the screen.
	 */
	private RadiationDamageTransition transition;

	/**
	 * Creates a new action with the given duration.
	 * The position of the radiation damage is the player's position in tile coordinates.
	 * <p>
	 * Initializes a transition instance with the position as of player's tile coordinates.
	 *
	 * @param duration Duration of the action in milliseconds.
	 */
	public RadiationDamageAction(int duration) {
		super(duration);
		Player player = ApplicationContextUtils.getPlayer();
		this.transition = new RadiationDamageTransition();
		this.transition.positionX = player.getPositionX();
		this.transition.positionY = player.getPositionY();
	}

	/**
	 * Returns the radiation damage transition for rendering.
	 * The renderer will check whether an action of this type is registered.
	 * If yes, it will extract the transition and render the radiation damage at the given coordinates.
	 *
	 * @return Transition of the radiation damage effect holding the completion rate of the radiation damage and its position in tile coordinates.
	 */
	public RadiationDamageTransition getTransition() {
		return transition;
	}

	/**
	 * Initializes the action by playing the radiation damage sound and notifying the player about the radiation exposure
	 * via an overhead notification.
	 */
	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_POISONED).play();
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> You are exposed to radiation.");
		OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> " + DAMAGE);
	}

	/**
	 * Updates the completion rate of the transition for the radiation damage effect.
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
	 * Ends the action by setting the transition to null and reducing the player's life by the radiation damage.
	 * <p>
	 * If the player's life is less than or equal to the damage, a death by radiation action is registered in the game's main action registry
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
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.DEATH_BY_RADIATION, new DeathByRadiationAction(1000));
		}		
	}
}
