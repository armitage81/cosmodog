package antonafanasjew.cosmodog.view.transitions;



public class EndingTransition implements Transition {

	public static final int DURATION_DARKNESS = 3000;
	public static final int DURATION_PICTURE_FADES_IN = 15000;
	public static final int DURATION_PICTURE = 5000;
	public static final int DURATION_PICTURE_FADES_OUT = 500;
	public static final float INITIAL_PICTURE_OPACITY = 1f;
	public static final float TEXT_PICTURE_OPACITY = 0.7f;
	
	public enum ActionPhase {
		DARKNESS,
		PICTURE_FADES_IN,
		PICTURE,
		PICTURE_FADES_OUT,
		TEXT
	}
	
	public ActionPhase phase = ActionPhase.DARKNESS;
	
	public long phaseStart;
	
	public float phaseCompletion() {
		
		float duration = System.currentTimeMillis() - phaseStart;

		if (phase == ActionPhase.DARKNESS) {
			return duration < DURATION_DARKNESS ? duration / DURATION_DARKNESS : 1.0f; 
		}
		
		if (phase == ActionPhase.PICTURE_FADES_IN) {
			return duration < DURATION_PICTURE_FADES_IN ? duration / DURATION_PICTURE_FADES_IN : 1.0f; 
		}
		
		if (phase == ActionPhase.PICTURE) {
			return duration < DURATION_PICTURE ? duration / DURATION_PICTURE : 1.0f; 
		}
		
		if (phase == ActionPhase.PICTURE_FADES_OUT) {
			return duration < DURATION_PICTURE_FADES_OUT ? duration / DURATION_PICTURE_FADES_OUT : 1.0f; 
		}
		
		return 0.0f;
	}
	
}
