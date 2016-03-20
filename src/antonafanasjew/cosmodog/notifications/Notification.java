package antonafanasjew.cosmodog.notifications;

import java.io.Serializable;

/**
 * Describes a text notification on the game screen.
 */
public class Notification implements Serializable {

	private static final long serialVersionUID = 2984298576445708694L;
	private String text;
	private int initialDuration;
	private int duration;

	public Notification(String text, int duration) {
		this.text = text;
		this.duration = duration;
		this.initialDuration = duration;
	}


	public static final Notification fromTextAndDuration(String text, int duration) {
		return new Notification(text, duration);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getInitialDuration() {
		return initialDuration;
	}
	
	public float durationInPercentage() {
		return (float)(initialDuration - duration) / (float) initialDuration; 
	}
	
	public int decreaseDuration(int duration) {
		
		int retVal;
		
		if (duration > this.duration) {
			retVal = duration - this.duration;
			this.duration = 0;
		} else {
			retVal = 0;
			this.duration -= duration;
		}
		
		return retVal;
		
	}

}
