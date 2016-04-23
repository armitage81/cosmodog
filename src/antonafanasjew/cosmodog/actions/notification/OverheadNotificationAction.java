package antonafanasjew.cosmodog.actions.notification;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.actors.Actor;

public class OverheadNotificationAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -8697185530449890051L;

	public static class OverheadNotificationTransition {
		public String text;
		public Actor actor;
		public Color color;
		public float completion;
	}
	
	private OverheadNotificationTransition transition;
	
	public static OverheadNotificationAction create(int duration, Actor actor, String text, Color color) {
		return new OverheadNotificationAction(duration, actor, text, color);
	}
	
	private OverheadNotificationAction(int duration, Actor actor, String text, Color color) {
		super(duration);
		
		transition = new OverheadNotificationTransition();
		transition.actor = actor;
		transition.text = text;
		transition.color = color;
		
	}

	@Override
	public void onTrigger() {
		transition.completion = 0.0f;
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		transition.completion = (float)after / (float)getDuration();
		if (transition.completion > 1.0) {
			transition.completion = 1.0f;
		}
	}
	
	@Override
	public void onEnd() {
		transition = null;
	}

	public OverheadNotificationTransition getTransition() {
		return transition;
	}

}
