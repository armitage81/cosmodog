package antonafanasjew.cosmodog.pathfinding;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import antonafanasjew.cosmodog.model.portals.Entrance;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

public class TowardsPlayerPathFinder extends AbstractPathFinder {

	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		
		//Preparing static data.
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		TileBasedMapFactory tileBasedMapFactory = cosmodog.getTileBasedMapFactory();
		
		
		Enemy enemy = (Enemy)actor;
		
		int followDistance = 25;
		
		//Preparing the AStar path finder.
		TileBasedMap tileBasedMap = tileBasedMapFactory.createTileBasedMap(enemy, applicationContext, map, collisionValidator);
		AStarPathFinder pathFinder = new AStarPathFinder(tileBasedMap, followDistance, false);
		
		//Adding the actors position as the first step in the path in any case.
		Path path = new Path();
		path.appendStep((int)enemy.getPosition().getX(), (int)enemy.getPosition().getY());

		//Selecting the best match for the target position. It can be null. (See the comment of the method for exact algorithm).
		Position actorTarget = nextFreePositionNearbyPlayer(actor, player, playersTargetEntrance, collisionValidator);
		
		//If there is a target position, calculate the path (if possible)
		if (actorTarget != null) {
			Path wholePath = pathFinder.findPath(null, (int)enemy.getPosition().getX(), (int)enemy.getPosition().getY(), (int) actorTarget.getX(), (int) actorTarget.getY());

			if (wholePath != null) {
				int tilesToMove = Math.min(enemy.getSpeedFactor(), wholePath.getLength() - 1);
				for (int i = 1; i <= tilesToMove; i++) {
					path.appendStep(wholePath.getX(i), wholePath.getY(i));
				}
			}
		}

        return MovementActionResult.instance(path);
        
	}
	
	/*
	 * Here we calculate the target position for the enemy when it is about to go towards the player.
	 * Normally, the enemy will try to get to one of the tiles adjacent to player.
	 * That might not be possible if they are blocked (by other enemies or collision obstacles)
	 * In such case, the enemy will try the next best position.
	 * 
	 * The algorithm to select the target position is as follows:
	 * 
	 * Collect all tiles whose positions are at distance of max 2 to the player and which are not blocked.
	 * Sort the tiles by distance to the player then distance to the enemy. 
	 * (Distance to the player has sorting priority, in case of equidistance, distance to the enemy decides)
	 * If the resulting list is empty return null, otherwise return the last element in the list.
	 * 
	 */
	private Position nextFreePositionNearbyPlayer(Actor actor, Player player, Entrance playersTargetEntrance, CollisionValidator collisionValidator) {
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		//If the player did not move, we take his real coordinates as target.
		int x = (int)player.getPosition().getX();
		int y = (int)player.getPosition().getY();
		
		//If the player has moved, we take his movement action result as target.
		if (!playersTargetEntrance.isWaited()) {
			x = (int)playersTargetEntrance.getPosition().getX();
			y = (int)playersTargetEntrance.getPosition().getY();
		}

		final int finalX = x;
		final int finalY = y;
		
		//Log.debug("Players target position: " + x + "/" + y);
		//Log.debug("Actors Position: " + actor.getPositionX() + "/" + actor.getPositionY());
		
		//We calculate 12 closest positions to the player that are candidates to be the target of the enemy path.
		int[] xs = new int[] {x,     x + 1, x    , x - 1, x - 1, x - 1, x + 1, x + 1, x - 2, x   ,  x + 2, x    };
		int[] ys = new int[] {y - 1, y    , y + 1, y    , y - 1, y + 1, y + 1, y - 1, y    , y - 2, y    , y + 2};
		
		//Log.debug("Calculating target candidates: ");
		

		//All not blocked target candidates will be added to the list. It can be empty at the end, if all is blocked.
		List<Position> notBlockedPositions = Lists.newArrayList();
		for (int i = 0; i < xs.length; i++) {
			//Log.debug("Target candidate: " + xs[i] + "/" + ys[i]);
			Entrance entrance = Entrance.instance(Position.fromCoordinates(xs[i], ys[i], player.getPosition().getMapType()), actor.getDirection());
			if (collisionValidator.collisionStatus(game, actor, map, entrance).isPassable()) {
				//Log.debug("It is NOT blocked");
				notBlockedPositions.add(Position.fromCoordinates(xs[i], ys[i], player.getPosition().getMapType()));
			}
		}
		
		//We sort the candidates list to have the best match as last element in the list (or an empty list)
		Collections.sort(notBlockedPositions, new Comparator<Position>() {

			/*
			 * The positions are ranked primarily by their distance to the player, secondarily by their distance to the enemey. 
			 */
			@Override
			public int compare(Position p1, Position p2) {
				//Log.debug("Comparing positons: " + p1.getX() + "/" + p1.getY() + " and " + p2.getX() + "/" + p2.getY());
				
				float dp1 = distanceToActorTargetPosition(p1, Position.fromCoordinates(finalX, finalY, player.getPosition().getMapType()));
				//Log.debug("Distance of p1 to player: " + dp1);
				float dp2 = distanceToActorTargetPosition(p2, Position.fromCoordinates(finalX, finalY, player.getPosition().getMapType()));
				//Log.debug("Distance of p2 to player: " + dp2);
				
				if (dp1 < dp2) {
					//Log.debug("Result is 1");
					return 1;
				} else if (dp1 > dp2) {
					//Log.debug("Result is -1");
					return -1;
				} else {
					float de1 = distanceToActorTargetPosition(p1, actor.getPosition());
					//Log.debug("Distance of p1 to actor: " + de1);
					
					float de2 = distanceToActorTargetPosition(p2, actor.getPosition());
					//Log.debug("Distance of p2 to actor: " + de2);
					
					int result = Float.compare(de2, de1);
					//Log.debug("Result is " + result);
					return result;
				}
				
			}
			
			private float distanceToActorTargetPosition(Position p, Position pTarget) {
				return CosmodogMapUtils.distanceBetweenPositions(p, pTarget);
			}
			
		});
		
		//Log.debug("Target candidates after sort: " + notBlockedPositions.toString());
		
		//We return null in case of an empty list or it's last element as the best candidate otherwise.
		return notBlockedPositions.isEmpty() ? null : notBlockedPositions.get(notBlockedPositions.size() - 1);
		
	}

}
