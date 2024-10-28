package antonafanasjew.cosmodog.actions.notification;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

import java.io.Serial;


public class OnScreenNotificationAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -7641468519994431789L;

	private final String text;

	private final String soundResourceId;

	public OnScreenNotificationAction(String text, int duration, String soundResourceId) {
		super(duration);
		this.text = text;
		this.soundResourceId = soundResourceId;
	}

	@Override
	public void onTrigger() {
		if (soundResourceId != null) {
			ApplicationContext.instance().getSoundResources().get(soundResourceId).play();
		}
	}

	public String getText() {
		return text;
	}

	public float textScale() {
		if (getCompletionRate() < 0.5f) {
			return getCompletionRate() * 20;
		} else {
			return 10.0f;
		}
	}
}