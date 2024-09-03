package antonafanasjew.cosmodog.actions.notification;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

import java.io.Serial;

/**
 * Represents an on-screen notification such as the "SECRET FOUND!" notification.
 * <p>
 * It zooms in on top of the screen to display important information. It does not block the input so
 * the player can move, change weapons and so on.
 * <p>
 * The notification will be displayed for a certain amount of time and can be accompanied by a sound.
 * <p>
 * The action maintains a transition during its execution that is used by a renderer to render the notification.
 * <p>
 * This is a fixed length action.
 */
public class OnScreenNotificationAction extends FixedLengthAsyncAction {

	/**
	 * Represents the transition of the on-screen notification to be rendered.
	 * <p>
	 * It mainly controls the size of the notification font to make it zoom-able for a better visual effect.
	 */
	public static class OnScreenNotificationTransition {

		/**
		 * Completion of the transition. It is a value between 0.0 and 1.0.
		 */
		public float completion = 0.0f;

		/**
		 * Returns the scale factor of the text to be rendered.
		 * <p>
		 * The text will be zoomed in for the half of the action and kept constantly at the biggest size
		 * in the second half.
		 * <p>
		 * (Example for scaling the text on completion increments of 0.1: 2, 4, 6, 8, 10, 10, 10, 10, 10, 10)
		 * <p>
		 * @return Scale factor of the text.
		 */
		public float textScale() {
			if (completion < 0.5f) {
				return completion * 20;
			} else {
				return 10.0f;
			}
		}
		
	}
	
	@Serial
	private static final long serialVersionUID = -7641468519994431789L;

	/**
	 * Transition of the on-screen notification to be rendered.
	 */
	private OnScreenNotificationTransition transition;

	/**
	 * Text of the notification.
	 */
	private final String text;

	/**
	 * Resource ID of the sound to be played when the notification is triggered.
	 */
	private final String soundResourceId;

	/**
	 * Initializes the action with the text of the notification, the duration and the sound to be played.
	 *
	 * @param text Text of the notification.
	 * @param duration Duration of the notification action.
	 * @param soundResourceId Resource ID of the sound to be played. (See f.i. SoundResources::SOUND_SECRET_FOUND)
	 *                        Can be null if no sound should be played.
	 */
	public OnScreenNotificationAction(String text, int duration, String soundResourceId) {
		super(duration);
		this.text = text;
		this.soundResourceId = soundResourceId;
	}

	/**
	 * Creates the transition of the notification for the renderer to display and plays the sound with the
	 * given sound id. If the sound id is null, no sound will be played.
	 */
	@Override
	public void onTrigger() {
		transition = new OnScreenNotificationTransition();
		if (soundResourceId != null) {
			ApplicationContext.instance().getSoundResources().get(soundResourceId).play();
		}
	}

	/**
	 * Updates the transition of the notification for the renderer to display depending on the passed time.
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
		
		transition.completion = actionPercentage;
	}

	/**
	 * Resets the transition of the notification so the renderer does not display it anymore.
	 */
	@Override
	public void onEnd() {
		this.transition = null;
	}

	/**
	 * Returns the transition of the notification to be rendered.
	 * <p>
	 * Usually, the renderer will check for the existence of an OnScreenNotificationAction in the action registry
	 * and use this method to obtain the corresponding transition.
	 *
	 * @return Transition of the notification.
	 */
	public OnScreenNotificationTransition getTransition() {
		return transition;
	}

	/**
	 * Returns the text of the notification. The renderer will use it to display the notification.
	 *
	 * @return Text of the notification.
	 */
	public String getText() {
		return text;
	}
}