package antonafanasjew.cosmodog.pathfinding;

import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;

public class TileBasedMapAdapter implements TileBasedMap {

	private Actor actor;
	private ApplicationContext applicationContext;
	private TiledMap tiledMap;
	private CollisionValidator collisionValidator;
	private TravelTimeCalculator travelTimeCalculator;
	

	public TileBasedMapAdapter(Actor actor, ApplicationContext applicationContext, TiledMap tiledMap, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator) {
		this.actor = actor;
		this.applicationContext = applicationContext;
		this.tiledMap = tiledMap;
		this.collisionValidator = collisionValidator;
		this.travelTimeCalculator = travelTimeCalculator;
	}

	@Override
	public boolean blocked(PathFindingContext cx, int x, int y) {
		CosmodogGame cosmodogGame = applicationContext.getCosmodog().getCosmodogGame();
		return !collisionValidator.collisionStatus(cosmodogGame, actor, tiledMap, x, y).isPassable();
	}

	@Override
	public float getCost(PathFindingContext cx, int x, int y) {
		return travelTimeCalculator.calculateTravelTime(applicationContext, actor, x, y);
	}

	@Override
	public int getHeightInTiles() {
		return tiledMap.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return tiledMap.getWidth();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

}
