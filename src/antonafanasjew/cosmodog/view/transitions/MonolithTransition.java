package antonafanasjew.cosmodog.view.transitions;



public class MonolithTransition implements Transition {

//	public static final int DURATION_MONOLITH = 1000;
//	public static final int DURATION_PICTURE_FADES_IN = 3000;
//	public static final int DURATION_PICTURE_FADES = 500;
	
	public static final int DURATION_MONOLITH = 1;
	public static final int DURATION_PICTURE_FADES_IN = 3;
	public static final int DURATION_PICTURE_FADES = 5;
	
	public static final float MAX_PICTURE_OPACITY = 0.7f;
	
	public enum ActionPhase {
		MONOLITH_FADES_IN,
		MONOLITH,
		PICTURE_FADES,
		TEXT
	}
	
	public ActionPhase phase = ActionPhase.MONOLITH_FADES_IN;
	
	public long phaseStart;
	public long pageStart;
	public boolean pageIsDynamic;
	
	public float phaseCompletion() {
		float duration = System.currentTimeMillis() - phaseStart;

		if (phase == ActionPhase.MONOLITH_FADES_IN) {
			return duration < DURATION_PICTURE_FADES_IN ? duration / DURATION_PICTURE_FADES_IN : 1.0f; 
		}
		
		if (phase == ActionPhase.MONOLITH) {
			return duration < DURATION_MONOLITH ? duration / DURATION_MONOLITH : 1.0f; 
		}
		
		if (phase == ActionPhase.PICTURE_FADES) {
			return duration < DURATION_PICTURE_FADES ? duration / DURATION_PICTURE_FADES : 1.0f; 
		}
		
		return 0.0f;
	}
	
}
