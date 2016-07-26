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
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class OverheadNotificationAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = -8697185530449890051L;

	public static final int OVERHEAD_NOTIFICATION_DURATION = 1000;
	
	public static class OverheadNotificationTransition {
		public List<String> texts = Lists.newArrayList();
		public List<Float> completions = Lists.newArrayList();
		
		public Actor actor;
		public Color color;
	}
	
	private OverheadNotificationTransition transition;
	
	public static OverheadNotificationAction create(Actor actor, String text, Color color) {
		return new OverheadNotificationAction(actor, text, color);
	}
	
	private OverheadNotificationAction(Actor actor, String text, Color color) {
		
		transition = new OverheadNotificationTransition();
		transition.actor = actor;
		transition.color = color;
		transition.texts.add(text);
		transition.completions.add(0.0f);
		
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

		for (int i = 0; i < transition.completions.size(); i++) {
			float completion = transition.completions.get(i);
			completion += deltaCompletion;
			if (completion < 1.0f) {
				
				remainingTexts.add(transition.texts.get(i));
				remainingCompletions.add(completion);
			
			}
		}
		
		transition.texts = remainingTexts;
		transition.completions = remainingCompletions;

	}
	
	public OverheadNotificationTransition getTransition() {
		return transition;
	}

	@Override
	public boolean hasFinished() {
		return transition != null && transition.texts.size() == 0;
	}

	public static void registerOverheadNotification(String text) {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();
		
		OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
		
		if (overheadNotificationAction == null) {
			OverheadNotificationAction action = OverheadNotificationAction.create(player, text, Color.white);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
		} else {
			overheadNotificationAction.getTransition().texts.add(text);
			overheadNotificationAction.getTransition().completions.add(0.0f);
		}
	}
}
