package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;

/**
 * Describes the collision status for a tile based on the actor and the map (passable/not passable)
 * Normally, an object of this class is the result of a collision validator.
 * Note, this class doesn't have any validation logic. It just holds the result of collision calculation for a specific tile
 * and the actor trying to enter this tile.
 * Additionally, the status gives the information about the obstacle reason if the passage is not possible. 
 */
public class CollisionStatus {
	
	private Actor actor;
	private CosmodogMap map;
	private Entrance entrance;
	private boolean passable;
	private PassageBlockerDescriptor passageBlockerDescriptor = PassageBlockerDescriptor.fromPassageBlockerType(PassageBlockerType.PASSABLE);
	
	private CollisionStatus () {
		
	}
	
	/**
	 * Factory method that creates collision status objects based on the parameters.
	 * 
	 * @param actor Actor for whom the collision status has been calculated.
	 * @param map Tiled map.
	 * @param entrance tile position of actors location and the direction from which it was entered.
	 * @param passable true if the tile is passable, false otherwise.
	 * @param passageBlockerDescriptor Reason for non passable tile.
	 * @return A new Collision status object filled with the given parameters.
	 */
	public static CollisionStatus instance(Actor actor, CosmodogMap map, Entrance entrance, boolean passable, PassageBlockerDescriptor passageBlockerDescriptor) {
		CollisionStatus collisionStatus = new CollisionStatus();
		collisionStatus.actor = actor;
		collisionStatus.map = map;
		collisionStatus.entrance = entrance;
		collisionStatus.passable = passable;
		collisionStatus.passageBlockerDescriptor = passageBlockerDescriptor;
		return collisionStatus;
	}
	
	/**
	 * This method is for the backwards compatibility of the existing code that used the passage blocker type instead of the passage blocker descriptor.
	 * Most of the callers do not need to be changed as this method will convert the passage blocker type into the passage blocker descriptor.
	 */
	public static CollisionStatus instance(Actor actor, CosmodogMap map, Entrance entrance, boolean passable, PassageBlockerType passageBlockerType) {
		return instance(actor, map, entrance, passable, PassageBlockerDescriptor.fromPassageBlockerType(passageBlockerType));
	}
	
	public static CollisionStatus instance(Actor actor, CosmodogMap map, Entrance entrance, boolean passable, PassageBlockerType passageBlockerType, Object paramValue) {
		return instance(actor, map, entrance, passable, PassageBlockerDescriptor.fromPassageBlockerTypeAndParameter(passageBlockerType, paramValue));
	}
	
	/**
	 * Returns the actor.
	 * @return Actor.
	 */
	public Actor getActor() {
		return actor;
	}

	/**
	 * Returns the tiled map.
	 * @return map.
	 */
	public CosmodogMap getMap() {
		return map;
	}

	/**
	 * Returns the entrance, that is the position and the direction from which it was entered.
	 * @return x coordinate of the tiles location.
	 */
	public Entrance getEntrance() {
		return entrance;
	}

	/**
	 * Indicates whether the tile is passable.
	 * @return true, if the tile is passable, false otherwise.
	 */
	public boolean isPassable() {
		return passable;
	}

	/**
	 * Returns the reason for the blocker
	 * @return Reason object for the blocker.
	 */
	public PassageBlockerDescriptor getPassageBlockerDescriptor() {
		return passageBlockerDescriptor;
	}

}
