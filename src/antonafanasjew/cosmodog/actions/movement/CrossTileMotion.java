package antonafanasjew.cosmodog.actions.movement;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Contains data for the renderer to render an actor while it is in the process of moving from one tile to another.
 * It could be a movement between two adjacent tiles, or a teleportation. In the latter case, containsTeleportation is set.
 */
public class CrossTileMotion {

	public static CrossTileMotion fromActor(Actor actor, Position targetPosition) {
		return fromActor(actor, targetPosition, false, null, null);
	}

	public static CrossTileMotion fromActorWhenUsingPortal(Actor actor, Position targetPosition, Portal entrancePortal, Portal exitPortal) {
		return fromActor(actor, targetPosition, true, entrancePortal, exitPortal);
	}

	private static CrossTileMotion fromActor(Actor actor, Position targetPosition, boolean containsTeleportation, Portal entrancePortal, Portal exitPortal) {
		CrossTileMotion crossTileMotion = new CrossTileMotion();
		crossTileMotion.containsTeleportation = containsTeleportation;
		crossTileMotion.entrancePortal = entrancePortal;
		crossTileMotion.exitPortal = exitPortal;
		crossTileMotion.setActor(actor);
		crossTileMotion.setLastMidwayPosition(actor.getPosition());
		crossTileMotion.targetPosition = targetPosition;
		return crossTileMotion;
	}

	private boolean containsTeleportation;

	private Portal entrancePortal;
	private Portal exitPortal;

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

	public boolean isContainsTeleportation() {
		return containsTeleportation;
	}

	public Portal getEntrancePortal() {
		return entrancePortal;
	}

	public Portal getExitPortal() {
		return exitPortal;
	}
}
