package antonafanasjew.cosmodog.actions.notification;

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

public class OverheadNotificationAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = -8697185530449890051L;

	public static final int OVERHEAD_NOTIFICATION_DURATION = 1000;
	
	public static class OverheadNotificationTransition {
		public List<String> texts = Lists.newArrayList();
		public List<Float> completions = Lists.newArrayList();
		public List<Actor> actors = Lists.newArrayList();
		
		public List<String> bufferedTexts = Lists.newArrayList();
		public List<Actor> bufferedActors = Lists.newArrayList();
		
		public Color color;
	}
	
	private OverheadNotificationTransition transition;
	
	public static OverheadNotificationAction create(Actor actor, String text, Color color) {
		return new OverheadNotificationAction(actor, text, color);
	}
	
	private OverheadNotificationAction(Actor actor, String text, Color color) {
		
		transition = new OverheadNotificationTransition();
		transition.color = color;
		transition.texts.add(text);
		transition.completions.add(0.0f);
		transition.actors.add(actor);
		
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
		
		Float leastCompletion = 1.0f;
		
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

		if (transition.bufferedTexts.size() > 0 && leastCompletion >= 0.25) {
			applyFirstBufferedNotification(transition);
		}
		
	}
	
	public OverheadNotificationTransition getTransition() {
		return transition;
	}

	@Override
	public boolean hasFinished() {
		return transition != null && transition.texts.size() == 0;
	}

	private static void applyFirstBufferedNotification(OverheadNotificationTransition transition) {
		transition.texts.add(transition.bufferedTexts.remove(0));
		transition.actors.add(transition.bufferedActors.remove(0));
		transition.completions.add(0.0f);
	}
	
	/**
	 * To avoid notification overlaps, the registration only adds the text to the buffered text list.
	 * It is the task of the update method, to add the buffered texts to the transition texts when the time
	 * comes, that is, when the last notification is already in progress for enough time.
	 */
	public static void registerOverheadNotification(Actor actor, String text) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
		
		if (overheadNotificationAction == null) {
			OverheadNotificationAction action = OverheadNotificationAction.create(actor, text, Color.white);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
		} else {
			overheadNotificationAction.getTransition().bufferedTexts.add(text);
			overheadNotificationAction.getTransition().bufferedActors.add(actor);
		}
	}
}
