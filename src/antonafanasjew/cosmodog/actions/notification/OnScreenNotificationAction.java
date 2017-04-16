package antonafanasjew.cosmodog.actions.notification;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

public class OnScreenNotificationAction extends FixedLengthAsyncAction {

	public static class OnScreenNotificationTransition {
		public float completion = 0.0f;
		
		public float textScale() {
			if (completion < 0.5f) {
				return completion * 20;
			} else {
				return 10.0f;
			}
		}
		
	}
	
	private static final long serialVersionUID = -7641468519994431789L;

	private OnScreenNotificationTransition transition;
	private String text;
	private String soundResourceId;
	
	public OnScreenNotificationAction(String text, int duration, String soundResourceId) {
		super(duration);
		this.text = text;
		this.soundResourceId = soundResourceId;
	}

	@Override
	public void onTrigger() {
		transition = new OnScreenNotificationTransition();
		if (soundResourceId != null) {
			ApplicationContext.instance().getSoundResources().get(soundResourceId).play();
		}
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		
		transition.completion = actionPercentage;
	}
	
	@Override
	public void onEnd() {
		this.transition = null;
	}

	public OnScreenNotificationTransition getTransition() {
		return transition;
	}

	public String getText() {
		return text;
	}
}
