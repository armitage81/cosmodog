package antonafanasjew.cosmodog.model.actors;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;

public class Platform extends Transport {

	private static final long serialVersionUID = -1816039264424769137L;
	
	private boolean leftCatWalkActivated;
	private boolean rightCatWalkActivated;
	
	public Platform() {
		this.setDirection(DirectionType.DOWN);
	}

	public static Platform fromPosition(Position position) {
		Platform platform = new Platform();
		platform.setPosition(position);
		return platform;
	}
	
	public boolean isLeftCatWalkActivated() {
		return leftCatWalkActivated;
	}
	public void setLeftCatWalkActivated(boolean leftCatWalkActivated) {
		this.leftCatWalkActivated = leftCatWalkActivated;
	}
	public boolean isRightCatWalkActivated() {
		return rightCatWalkActivated;
	}
	public void setRightCatWalkActivated(boolean rightCatWalkActivated) {
		this.rightCatWalkActivated = rightCatWalkActivated;
	}
	

	

}
