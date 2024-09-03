package antonafanasjew.cosmodog.actions.notification;

import java.io.Serial;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Represents the action that shows overhead notifications about actors' heads (damage dealt, passage blocked etc.).
 * <p>
 * This action represents all notifications that are registered at the same time.
 * <p>
 * The idea is not to register a new overhead notification action for each overhead text, but to check if there is one
 * notification action registered already. If yes, the new text will be buffered and added to the notification
 * action. Otherwise, a new action will be registered.
 * <p>
 * This is a variable length action.
 */
public class OverheadNotificationAction extends VariableLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8697185530449890051L;

	/**
	 * Duration of the overhead notification in milliseconds. It is the time the notification will be displayed on
	 * the screen while hovering up from the actor's head.
	 * <p>
	 * Take note: This is not the duration of the action itself. The latter is variable action. It can be prolonged
	 * by adding additional notifications while its being executed.
	 */
	public static final int OVERHEAD_NOTIFICATION_DURATION = 1000;

	/**
	 * Represents the transition of overhead notifications to be rendered.
	 * <p>
	 * Take note: The transition represents all notifications registered in the action and their corresponding actors.
	 * Multiple notifications can float over actors' heads at the same time. They are stored in the field texts.
	 * The field actors contains the corresponding actors. The field completions contains the progress of the
	 * notification for each text. (The closer completion is to 1.0 the higher the notification is on the screen.)
	 * <p>
	 * Additionally, there can be notifications that should appear when the previous notifications progress a bit
	 * (to avoid overlaps). They are stored in the buffered texts field and their actors are stored in the buffered actors
	 * field.
	 * <p>
	 * In short: texts are floating over actors' heads, buffered texts are waiting in queue.
	 */
	public static class OverheadNotificationTransition {

		/**
		 * Texts rendered over actors' heads.
		 */
		public List<String> texts = Lists.newArrayList();

		/**
		 * Completion of the floating texts. It is a value between 0.0 and 1.0. The higher the completion,
		 * the higher the text is on the screen.
		 */
		public List<Float> completions = Lists.newArrayList();

		/**
		 * Actors corresponding to the floating texts. the list correlates with the texts, and completions lists.
		 */
		public List<Actor> actors = Lists.newArrayList();

		/**
		 * Texts waiting in queue to be rendered over actors' heads when previous notifications progress a bit.
		 * The queue is needed to avoid overlaps when too many notifications are registered at the same time.
		 */
		public List<String> bufferedTexts = Lists.newArrayList();

		/**
		 * Actors corresponding to the buffered texts. The list correlates with the buffered texts list.
		 */
		public List<Actor> bufferedActors = Lists.newArrayList();
	}

	/**
	 * Represents the transition of overhead notifications to be rendered.
	 * The renderer will use this transition to render the notifications.
	 * <p>
	 * Take note: This transition can be updated with new texts while it is being executed.
	 */
	private final OverheadNotificationTransition transition;

	/**
	 * A convenience method to create objects of OverheadNotificationAction. The method is static.
	 * <p>
	 * Since the constructor is private, this is the only way to create notification actions.
	 *
	 * @param actor The actor over whose head the notification should appear.
	 * @param text The text that should be rendered over the actor's head.
	 * @return Instance of the OverheadNotificationAction.
	 */
	public static OverheadNotificationAction create(Actor actor, String text) {
		return new OverheadNotificationAction(actor, text);
	}

	/**
	 * Creates the overhead notification action with the given actor and text.
	 * <p>
	 * The action will create a transition to render the text over the actor's head.
	 * <p>
	 * The underlying transition is meant to be updated when new texts are added to the action.
	 *
	 * @param actor Player or the enemy over whose head the notification should appear.
	 * @param text Text that should be rendered over the actor's head.
	 */
	private OverheadNotificationAction(Actor actor, String text) {
		transition = new OverheadNotificationTransition();
		transition.texts.add(text);
		transition.completions.add(0.0f);
		transition.actors.add(actor);
	}

	/**
	 * Updates the underlying transition based on the time passed since the last update.
	 * <p>
	 * A notification is floating over an actor's head for OVERHEAD_NOTIFICATION_DURATION milliseconds.
	 * <p>
	 * Based on this, the method calculates which percentage of the notification time has passed since the last update.
	 * This percentage is added to the completions of all notifications that are currently present. If a completion
	 * exceed 1.0, then the text is removed from the transition (together with its completion and actor objects).
	 * In other words, the texts are raised a bit and if they reached their peak, they are removed.
	 * <p>
	 * Additionally, the smallest completion percentage is found out (corresponds to the lowest notification text).
	 * If this percentage is greater than 0.25, the first buffered text is added to the active texts (also the actor).
	 * Its completion is then set to 0.0.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		float delta = after - before;
		float deltaCompletion = delta / OVERHEAD_NOTIFICATION_DURATION;
		if (deltaCompletion > 1.0f) {
			deltaCompletion = 1.0f;
		}
		
		List<String> remainingTexts = Lists.newArrayList();
		List<Float> remainingCompletions = Lists.newArrayList();
		List<Actor> remainingActors = Lists.newArrayList();
		
		float leastCompletion = 1.0f;
		
		for (int i = 0; i < transition.completions.size(); i++) {
			float completion = transition.completions.get(i);
			completion += deltaCompletion;
			if (completion < 1.0f) {
				
				remainingTexts.add(transition.texts.get(i));
				remainingCompletions.add(completion);
				remainingActors.add(transition.actors.get(i));
				
				if (completion < leastCompletion) {
					leastCompletion = completion;
				}
			}
		}
		
		transition.texts = remainingTexts;
		transition.completions = remainingCompletions;
		transition.actors = remainingActors;

		if (!transition.bufferedTexts.isEmpty() && leastCompletion >= 0.25) {
			applyFirstBufferedNotification(transition);
		}
		
	}

	/**
	 * Returns the transition of this action. The transition can have many notifications for many actors and, additionally,
	 * many notifications waiting in queue to be activated.
	 */
	public OverheadNotificationTransition getTransition() {
		return transition;
	}

	/**
	 * The action finishes when no active texts are present in the transition.
	 * <p>
	 * Interestingly, it does not check for the buffered texts.
	 * <p>
	 * Take note: Additional texts can be buffered in the transition while it is being executed. They will become active
	 * with one of the next updates and prolong the existence of the action.
	 *
	 * @return true, if the action has finished, false otherwise.
	 */
	@Override
	public boolean hasFinished() {
		return transition != null && transition.texts.isEmpty();
	}

	/**
	 * Gets the first element from the buffered texts list and adds it to the active texts list.
	 * Does the same for the first actor in the buffered actors list.
	 * adds a new completion value of 0.0 to the completions list to indicate that the new text just started.
	 */
	private static void applyFirstBufferedNotification(OverheadNotificationTransition transition) {
		transition.texts.add(transition.bufferedTexts.removeFirst());
		transition.actors.add(transition.bufferedActors.removeFirst());
		transition.completions.add(0.0f);
	}
	
	/**
	 * Registers a new overhead notification action if there is none yet in the registry. Otherwise, reuses
	 * the existing one. Adds the new text and the corresponding author to the buffered lists.
	 * <p>
	 * To avoid notification overlaps, the registration only adds the text to the buffered text list.
	 * It is the task of the update method, to add the buffered texts to the transition texts when the time
	 * comes, that is, when the last notification is already in progress for enough time.
	 */
	public static void registerOverheadNotification(Actor actor, String text) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
		
		if (overheadNotificationAction == null) {
			OverheadNotificationAction action = OverheadNotificationAction.create(actor, text);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
		} else {
			overheadNotificationAction.getTransition().bufferedTexts.add(text);
			overheadNotificationAction.getTransition().bufferedActors.add(actor);
		}
	}
}