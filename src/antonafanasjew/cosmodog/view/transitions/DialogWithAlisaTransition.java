package antonafanasjew.cosmodog.view.transitions;

public class DialogWithAlisaTransition implements Transition {

	public static final int DURATION_ARM_APPEARS = 1000;
	public static final int DURATION_DEVICE_TURNS_ON = 1000;
	public static final int DURATION_PICTURE_FADES = 10;
	public static final float MAX_PICTURE_OPACITY = 0.9f;
	
	public enum ActionPhase {
		ARM_APPEARS,
		DEVICE_TURNS_ON,
		PICTURE_FADES,
		TEXT
	}
	
	public ActionPhase phase = ActionPhase.ARM_APPEARS;
	
	public long phaseStart;
	public long pageStart;
	public boolean pageIsDynamic;
	
	public float phaseCompletion() {
		float duration = System.currentTimeMillis() - phaseStart;

		if (phase == ActionPhase.ARM_APPEARS) {
			return duration < DURATION_ARM_APPEARS ? duration / DURATION_ARM_APPEARS : 1.0f; 
		}
		
		if (phase == ActionPhase.DEVICE_TURNS_ON) {
			return duration < DURATION_DEVICE_TURNS_ON ? duration / DURATION_DEVICE_TURNS_ON : 1.0f; 
		}
		
		if (phase == ActionPhase.PICTURE_FADES) {
			return duration < DURATION_PICTURE_FADES ? duration / DURATION_PICTURE_FADES : 1.0f; 
		}
		
		return 0.0f;
	}
	
}
