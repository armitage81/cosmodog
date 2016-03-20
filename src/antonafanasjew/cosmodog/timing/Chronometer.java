package antonafanasjew.cosmodog.timing;

import java.io.Serializable;

public class Chronometer implements Serializable {

	private static final long serialVersionUID = 7374779203317494435L;

	private long time = 0;
	
	public void update(int n) {
		time += n;
	}

	public long getTime() {
		return time;
	}
	
	public int timeFrameInLoop(int noOfFrames, int frameDuration) {
		int loopDuration = noOfFrames * frameDuration;
		int loopRest = (int)(time % loopDuration);
		return loopRest / frameDuration;
	}

}
