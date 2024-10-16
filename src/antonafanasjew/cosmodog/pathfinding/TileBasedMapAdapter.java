package antonafanasjew.cosmodog.pathfinding;

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
	private ApplicationContext applicationContext;
	private CollisionValidator collisionValidator;
	private CosmodogMap map;
	

	public TileBasedMapAdapter(Actor actor, ApplicationContext applicationContext, CosmodogMap map, CollisionValidator collisionValidator) {
		this.actor = actor;
		this.applicationContext = applicationContext;
		this.map = map;
		this.collisionValidator = collisionValidator;
	}

	@Override
	public boolean blocked(PathFindingContext cx, int x, int y) {
		CosmodogGame cosmodogGame = applicationContext.getCosmodog().getCosmodogGame();
		return !collisionValidator.collisionStatus(cosmodogGame, actor, map, Position.fromCoordinates(x, y)).isPassable();
	}

	@Override
	public float getCost(PathFindingContext cx, int x, int y) {
		return Constants.MINUTES_PER_TURN;
	}

	@Override
	public int getHeightInTiles() {
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return map.getWidth();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

}
