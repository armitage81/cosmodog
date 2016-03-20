package antonafanasjew.cosmodog.view.transitions;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Represents an actors transition on the map that happens during the movement.
 * As opposed to the actors model, objects of this class don't show the exact position of
 * the actor on the map, but it's temporary transition between two positions.
 * 
 * Values of objects of this class should be used only for rendering and not affect the model.
 *
 */
public class ActorTransition {

	public static ActorTransition fromActor(Actor actor, int targetPositionX, int targetPositionY) {
		ActorTransition actorTransition = new ActorTransition();
		actorTransition.setActor(actor);
		actorTransition.setTransitionalPosX(actor.getPositionX());
		actorTransition.setTransitionalPosY(actor.getPositionY());
		actorTransition.targetPosX = targetPositionX;
		actorTransition.targetPosY = targetPositionY;
		return actorTransition;
	}
	
	private Actor actor;
	
	private int transitionalPosX;
	private int transitionalPosY;
	
	private int targetPosX;
	private int targetPosY;
	
	private float transitionalOffsetX;
	private float transitionalOffsetY;
	
	/**
	 * Returns the actor of this transition
	 * @return Model object of the actor.
	 */
	public Actor getActor() {
		return actor;
	}
	
	/**
	 * Sets the actor for this transition.
	 * @param actor Actor that is moving.
	 */
	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	/**
	 * NPC actors can move multiple fields within one player turn.
	 * This method returns the X coordinate of the field the NPC reached
	 * during the transition.
	 * @return X coordinate of the field the actor reached during the transition
	 */
	public int getTransitionalPosX() {
		return transitionalPosX;
	}
	
	/**
	 * Sets the X coordinate of the field the actor reached in the transition.
	 * @param transitionalPosX X coordinate of the reached field during the transition.
	 */
	public void setTransitionalPosX(int transitionalPosX) {
		this.transitionalPosX = transitionalPosX;
	}
	
	/**
	 * NPC actors can move multiple fields within one player turn.
	 * This method returns the Y coordinate of the field the NPC reached
	 * during the transition.
	 * @return Y coordinate of the field the actor reached during the transition
	 */
	public int getTransitionalPosY() {
		return transitionalPosY;
	}
	
	/**
	 * Sets the Y coordinate of the field the actor reached in the transition.
	 * @param transitionalPosY Y coordinate of the reached field during the transition.
	 */
	public void setTransitionalPosY(int transitionalPosY) {
		this.transitionalPosY = transitionalPosY;
	}
	
	/**
	 * Returns the horizontal transition offset between actors real (or transitional) position and his
	 * target position. Negative while moving West, positive while moving East.
	 * @return Horizontal offset of the transition between two adjacent positions, which is a value between -1.0f (exclusive) and 1.0f (exclusive)
	 */
	public float getTransitionalOffsetX() {
		return transitionalOffsetX;
	}
	
	/**
	 * Sets the X offset of the transition between two adjacent positions.
	 * @param transitionalOffsetX offset as a value between -1.0f and 1.0f (exclusive ends)
	 */
	public void setTransitionalOffsetX(float transitionalOffsetX) {
		this.transitionalOffsetX = transitionalOffsetX;
	}
	
	/**
	 * Returns the vertical transition offset between actors real (or transitional) position and his
	 * target position. Negative while moving North, positive while moving South.
	 * @return Vertical offset of the transition between two adjacent positions, which is a value between -1.0f (exclusive) and 1.0f (exclusive)
	 */
	public float getTransitionalOffsetY() {
		return transitionalOffsetY;
	}
	
	/**
	 * Sets the Y offset of the transition between two adjacent positions.
	 * @param transitionalOffsetY offset as a value between -1.0f and 1.0f (exclusive ends)
	 */
	public void setTransitionalOffsetY(float transitionalOffsetY) {
		this.transitionalOffsetY = transitionalOffsetY;
	}

	public DirectionType getTransitionalDirection() {
		if (transitionalOffsetX > 0) {
			return DirectionType.RIGHT;
		} else if (transitionalOffsetX < 0) {
			return DirectionType.LEFT;
		} else if (transitionalOffsetY > 0) {
			return DirectionType.DOWN;
		} else {
			return DirectionType.UP;
		}
	}
	
	@Override
	public String toString() {
		return transitionalPosX + "/" + transitionalPosY + "/" + transitionalOffsetX + "/" + transitionalOffsetY;
	}

	public int getTargetPosX() {
		return targetPosX;
	}

	public int getTargetPosY() {
		return targetPosY;
	}

}
