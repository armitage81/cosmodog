package antonafanasjew.cosmodog.pathfinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.sight.SightRadiusCalculator;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

import com.google.common.collect.Lists;

public class TowardsPlayerPathFinder extends AbstractPathFinder {

	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator, MovementActionResult playerMovementActionResult) {
		
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CustomTiledMap tiledMap = ApplicationContextUtils.getCustomTiledMap();
		Player player = ApplicationContextUtils.getPlayer();
		
		Enemy enemy = (Enemy)actor;
		
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		SightRadiusCalculator sightRadiusCalculator = cosmodog.getSightRadiusCalculator();
		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
		
		int sightRadius = sightRadiusCalculator.calculateSightRadius(enemy, planetaryCalendar);
		int followDistance = sightRadius * 3;
		
		
		TileBasedMapFactory tileBasedMapFactory = cosmodog.getTileBasedMapFactory();
		TileBasedMap tileBasedMap = tileBasedMapFactory.createTileBasedMap(enemy, applicationContext, tiledMap, collisionValidator, travelTimeCalculator);
		AStarPathFinder pathFinder = new AStarPathFinder(tileBasedMap, followDistance, false);
		
		
		//Add the actors position as the first step in the path in any case.
		Path subPath = new Path();
		subPath.appendStep(enemy.getPositionX(), enemy.getPositionY());
		List<Float> costs = Lists.newArrayList();
		
		//Select the best match for the target position. It can be null.
		Position actorTarget = nextFreePositionNearbyPlayer(actor, player, playerMovementActionResult, collisionValidator);
		
		if (actorTarget != null) {
			
    		Path path = pathFinder.findPath(null, enemy.getPositionX(), enemy.getPositionY(), (int)actorTarget.getX(), (int)actorTarget.getY());
    		
    		if (path != null) {
        		
        		int accumulatedCosts = 0;
        		
        		for (int i = 1; i < path.getLength(); i++) {
        			int costsForTile = travelTimeCalculator.calculateTravelTime(applicationContext, enemy, path.getX(i), path.getY(i));
        			if (accumulatedCosts + costsForTile <= costBudget) {
        				costs.add((float)costsForTile);
        				subPath.appendStep(path.getX(i), path.getY(i));
        				accumulatedCosts += costsForTile;
        			} else {
        				break;
        			}
        		}
        		
    		}
		}
		
		MovementActionResult retVal = MovementActionResult.instance(subPath, costs);
		return retVal;
        
	}
	
	/*
	 * Here we calculate the target position for the enemy when it is about to go towards the player.
	 * Normally, the enemy will try to get to one of the tiles adjacent to player.
	 * That might not be possible if it they are blocked (by other enemies, or collision obstacles)
	 * In such case, the enemy will try the next best position.
	 * 
	 * The algorithm to select the target position is as follows:
	 * 
	 * Collect all tiles whose positions are at distance of max 2 to the player and which are not blocked.
	 * Sort the tiles by distance to the player then distance to the enemy. (Distance to the player has sorting priority, in case of equidistance, distance to the enemy decides)
	 * If the resulting list is empty return null, otherwise return the last element in the list.
	 * 
	 */
	private Position nextFreePositionNearbyPlayer(Actor actor, Player player, MovementActionResult playerMovementActionResult, CollisionValidator collisionValidator) {
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CustomTiledMap map = ApplicationContextUtils.getCustomTiledMap();
		
		//If the player did not move, we take his real coordinates as target.
		int x = player.getPositionX();
		int y = player.getPositionY();
		
		//If the player has moved, we take his movement action result as target.
		if (playerMovementActionResult != null) {
			Path path = playerMovementActionResult.getPath();
			x = path.getX(path.getLength() - 1);
			y = path.getY(path.getLength() - 1);
		}

		final int finalX = x;
		final int finalY = y;
		
		Log.debug("Players target position: " + x + "/" + y);
		Log.debug("Actors Position: " + actor.getPositionX() + "/" + actor.getPositionY());
		
		//We calculate 12 closest positions to the player that are candidates to be the target of the enemy path.
		int[] xs = new int[] {x,     x + 1, x    , x - 1, x - 1, x - 1, x + 1, x + 1, x - 2, x   ,  x + 2, x    };
		int[] ys = new int[] {y - 1, y    , y + 1, y    , y - 1, y + 1, y + 1, y - 1, y    , y - 2, y    , y + 2};
		
		Log.debug("Calculating target candidates: ");
		

		//All not blocked target candidates will be added to the list. It can be empty at the end, if all is blocked.
		List<Position> notBlockedPositions = Lists.newArrayList();
		for (int i = 0; i < xs.length; i++) {
			Log.debug("Target candidate: " + xs[i] + "/" + ys[i]);
			if (collisionValidator.collisionStatus(game, actor, map, xs[i], ys[i]).isPassable()) {
				Log.debug("It is NOT blocked");
				notBlockedPositions.add(Position.fromCoordinates(xs[i], ys[i]));
			} else {
				Log.debug("It is blocked");
			}
		}
		
		Log.debug("Sorting remaining target candidates.");
		
		//We sort the candidates list to have the best match as last element in the list (or an empty list)
		Collections.sort(notBlockedPositions, new Comparator<Position>() {

			/*
			 * The positions are ranked primarily by their distance to the player, secondarily by their distance to the enemey. 
			 */
			@Override
			public int compare(Position p1, Position p2) {
				Log.debug("Comparing positons: " + p1.getX() + "/" + p1.getY() + " and " + p2.getX() + "/" + p2.getY());
				
				float dp1 = distanceToActorTargetPosition(p1, Position.fromCoordinates(finalX, finalY));
				Log.debug("Distance of p1 to player: " + dp1);
				float dp2 = distanceToActorTargetPosition(p2, Position.fromCoordinates(finalX, finalY));
				Log.debug("Distance of p2 to player: " + dp2);
				
				if (dp1 < dp2) {
					Log.debug("Result is 1");
					return 1;
				} else if (dp1 > dp2) {
					Log.debug("Result is -1");
					return -1;
				} else {
					float de1 = distanceToActorTargetPosition(p1, Position.fromCoordinates(actor.getPositionX(), actor.getPositionY()));
					Log.debug("Distance of p1 to actor: " + de1);
					
					float de2 = distanceToActorTargetPosition(p2, Position.fromCoordinates(actor.getPositionX(), actor.getPositionY()));
					Log.debug("Distance of p2 to actor: " + de2);
					
					int result = (de1 < de2) ? 1 : ((de1 > de2) ? -1 : 0);
					Log.debug("Result is " + result);
					return result;
				}
				
			}
			
			private float distanceToActorTargetPosition(Position p, Position pTarget) {
				return CosmodogMapUtils.distanceBetweenPositions(p, pTarget);
			}
			
		});
		
		Log.debug("Target candidates after sort: " + notBlockedPositions.toString());
		
		//We return null in case of an empty list or it's last element as the best candidate otherwise.
		return notBlockedPositions.isEmpty() ? null : notBlockedPositions.get(notBlockedPositions.size() - 1);
		
	}

}
