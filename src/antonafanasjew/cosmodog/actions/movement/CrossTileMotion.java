package antonafanasjew.cosmodog.actions.movement;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public class CrossTileMotion {

	public static CrossTileMotion fromActor(Actor actor, Position targetPosition) {
		CrossTileMotion crossTileMotion = new CrossTileMotion();
		crossTileMotion.setActor(actor);
		crossTileMotion.setLastMidwayPosition(actor.getPosition());
		crossTileMotion.targetPosition = targetPosition;
		return crossTileMotion;
	}
	
	private Actor actor;
	
	private Position lastMidwayPosition;
	
	private Position targetPosition;
	
	private float crossTileOffsetX;
	private float crossTileOffsetY;
	
	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	public Position getlastMidwayPosition() {
		return lastMidwayPosition;
	}
	
	public void setLastMidwayPosition(Position lastMidwayPosition) {
		this.lastMidwayPosition = lastMidwayPosition;
	}

	public float getCrossTileOffsetX() {
		return crossTileOffsetX;
	}
	
	public void setCrossTileOffsetX(float crossTileOffsetX) {
		this.crossTileOffsetX = crossTileOffsetX;
	}

	public float getCrossTileOffsetY() {
		return crossTileOffsetY;
	}

	public void setCrossTileOffsetY(float crossTileOffsetY) {
		this.crossTileOffsetY = crossTileOffsetY;
	}

	public DirectionType getMotionDirection() {
		if (crossTileOffsetX > 0) {
			return DirectionType.RIGHT;
		} else if (crossTileOffsetX < 0) {
			return DirectionType.LEFT;
		} else if (crossTileOffsetY > 0) {
			return DirectionType.DOWN;
		} else {
			return DirectionType.UP;
		}
	}
	
	@Override
	public String toString() {
		return lastMidwayPosition + "/" + crossTileOffsetX + "/" + crossTileOffsetY;
	}

	public Position getTargetPosition() {
		return targetPosition;
	}

}
