package antonafanasjew.cosmodog.actions.notification;

import java.io.Serial;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class OverheadNotificationAction extends VariableLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8697185530449890051L;

	public static final int OVERHEAD_NOTIFICATION_DURATION = 1000;

	public static class OverheadNotificationsState {

		public List<String> texts = Lists.newArrayList();

		public List<Float> completions = Lists.newArrayList();

		public List<Actor> actors = Lists.newArrayList();

		public List<String> bufferedTexts = Lists.newArrayList();

		public List<Actor> bufferedActors = Lists.newArrayList();
	}

	public static OverheadNotificationAction create(Actor actor, String text) {
		return new OverheadNotificationAction(actor, text);
	}

	private OverheadNotificationAction(Actor actor, String text) {
		OverheadNotificationsState overheadNotificationsState = new OverheadNotificationsState();
		overheadNotificationsState.texts.add(text);
		overheadNotificationsState.completions.add(0.0f);
		overheadNotificationsState.actors.add(actor);
		this.getProperties().put("state", overheadNotificationsState);
	}

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

		OverheadNotificationsState state = this.getProperty("state");

		for (int i = 0; i < state.completions.size(); i++) {
			float completion = state.completions.get(i);
			completion += deltaCompletion;
			if (completion < 1.0f) {
				
				remainingTexts.add(state.texts.get(i));
				remainingCompletions.add(completion);
				remainingActors.add(state.actors.get(i));
				
				if (completion < leastCompletion) {
					leastCompletion = completion;
				}
			}
		}

		state.texts = remainingTexts;
		state.completions = remainingCompletions;
		state.actors = remainingActors;

		if (!state.bufferedTexts.isEmpty() && leastCompletion >= 0.25) {
			applyFirstBufferedNotification(state);
		}
		
	}

	@Override
	public boolean hasFinished() {
		OverheadNotificationsState state = this.getProperty("state");
		return state != null && state.texts.isEmpty();
	}

	private static void applyFirstBufferedNotification(OverheadNotificationsState state) {
		state.texts.add(state.bufferedTexts.removeFirst());
		state.actors.add(state.bufferedActors.removeFirst());
		state.completions.add(0.0f);
	}
	
	public static void registerOverheadNotification(Actor actor, String text) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
		
		if (overheadNotificationAction == null) {
			OverheadNotificationAction action = OverheadNotificationAction.create(actor, text);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
		} else {
			OverheadNotificationsState state = overheadNotificationAction.getProperty("state");
			state.bufferedTexts.add(text);
			state.bufferedActors.add(actor);
		}
	}
}