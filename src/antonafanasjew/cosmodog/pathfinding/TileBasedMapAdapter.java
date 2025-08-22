package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public class TileBasedMapAdapter implements TileBasedMap {

	private Actor actor;
	private CollisionValidator collisionValidator;

	private CosmodogGame game;
	private CosmodogMap map;
	

	public TileBasedMapAdapter(Actor actor, CosmodogGame game, CosmodogMap map, CollisionValidator collisionValidator) {
		this.actor = actor;
		this.game = game;
		this.map = map;
		this.collisionValidator = collisionValidator;
	}

	@Override
	public boolean blocked(PathFindingContext cx, int x, int y) {
		Entrance entrance = Entrance.instance(Position.fromCoordinates(x, y, map.getMapType()), DirectionType.UP);
		return !collisionValidator.collisionStatus(game, actor, map, entrance).isPassable();
	}

	@Override
	public float getCost(PathFindingContext cx, int x, int y) {
		return Constants.MINUTES_PER_TURN;
	}

	@Override
	public int getHeightInTiles() {
		return map.getMapType().getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return map.getMapType().getWidth();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

}
