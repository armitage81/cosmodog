package antonafanasjew.cosmodog.collision;

import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Describes the collision status for a tile based on the actor and the map (passable/not passable)
 * Normally, an object of this class is the result of a collision validator.
 * Note, this class doesn't have any validation logic. It just holds the result of collision calculation for a specific tile
 * and the actor trying to enter this tile.
 * Additionally, the status gives the information about the obstacle reasonif the passage is not possible. 
 */
public class CollisionStatus {

	/**
	 * No obstacles. The tile is passable.
	 */
	public static final int NO_PASSAGE_REASON_NO_REASON = 0;
	
	/**
	 * The tile is not passable because it is blocked by obstacles.
	 */
	public static final int NO_PASSAGE_REASON_BLOCKED = 1;
	
	
	/**
	 * The tile is not passable because it is blocked as target by an other character.
	 */
	public static final int NO_PASSAGE_REASON_BLOCKED_AS_TARGET_BY_OTHER_MOVING_CHARACTER = 3;
	
	/**
	 * The tile is not passable because the vehicle has no fuel.
	 */
	public static final int NO_PASSAGE_REASON_FUEL_EMPTY = 2;

	/**
	 * The tile is out of the home region of an npc.
	 */
	public static final int NO_PASSAGE_REASON_OUT_OF_HOME_REGION = 4;
	
	/**
	 * The tile contains an energy wall generator and the costs are higher than the number of collected infobits.
	 */
	public static final int NO_PASSAGE_REASON_ENERGY_WALL_COSTS = 5;
	
	private Actor actor;
	private TiledMap map;
	private int tileX;
	private int tileY;
	
	private boolean passable;
	private int noPassageReason = NO_PASSAGE_REASON_NO_REASON;
	
	private CollisionStatus () {
		
	}
	
	/**
	 * Factory method that creates collision status objects based on the parameters.
	 * 
	 * @param actor Actor for whom the collision status has been calculated.
	 * @param map Tiled map.
	 * @param tileX horizontal tile number of actors location.
	 * @param tileY vertical tile number of actors location.
	 * @param passable true if the tile is passable, false otherwise.
	 * @param noPassageReason Reason for non passable tile.
	 * @return A new Collision status object filled with the given parameters.
	 */
	public static CollisionStatus instance(Actor actor, TiledMap map, int tileX, int tileY, boolean passable, int noPassageReason) {
		CollisionStatus collisionStatus = new CollisionStatus();
		collisionStatus.actor = actor;
		collisionStatus.map = map;
		collisionStatus.tileX = tileX;
		collisionStatus.tileY = tileY;
		collisionStatus.passable = passable;
		collisionStatus.noPassageReason = noPassageReason;
		return collisionStatus;
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
	 * @return Tiled map.
	 */
	public TiledMap getMap() {
		return map;
	}

	/**
	 * Returns the horizontal coordinate of the tile.
	 * @return x coordinate of the tiles location.
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * Returns the vertical coordinate of the tile.
	 * @return y coordinate of the tiles location.
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * Indicates whether the tile is passable.
	 * @return true, if the tile is passable, false otherwise.
	 */
	public boolean isPassable() {
		return passable;
	}

	/**
	 * Returns the reason for the blocked passage.
	 * @return The reason for blocked tiles.
	 */
	public int getNoPassageReason() {
		return noPassageReason;
	}
	
}
