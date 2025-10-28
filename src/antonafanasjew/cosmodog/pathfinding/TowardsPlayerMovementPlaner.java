package antonafanasjew.cosmodog.pathfinding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.portals.Entrance;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
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

public class TowardsPlayerMovementPlaner extends AbstractMovementPlaner {

	public static final int MAX_FOLLOWING_DISTANCE = 25;

	private Supplier<Cosmodog> cosmodogSupplier = ApplicationContextUtils::getCosmodog;
	private Supplier<CosmodogGame> cosmodogGameSupplier = ApplicationContextUtils::getCosmodogGame;
	private Supplier<CosmodogMap> cosmodogMapSupplier = ApplicationContextUtils::mapOfPlayerLocation;
	private Supplier<Player> playerSupplier = ApplicationContextUtils::getPlayer;

	@Override
	protected MovementPlan calculateMovementPlanInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		
		//Preparing static data.
		Cosmodog cosmodog = cosmodogSupplier.get();
		CosmodogGame game = cosmodogGameSupplier.get();
		CosmodogMap map = cosmodogMapSupplier.get();
		Player player = playerSupplier.get();

		TileBasedMapFactory tileBasedMapFactory = cosmodog.getTileBasedMapFactory();
		
		
		Enemy enemy = (Enemy)actor;

        //Preparing the AStar path finder.
		TileBasedMap tileBasedMap = tileBasedMapFactory.createTileBasedMap(enemy, game, map, collisionValidator);
		AStarPathFinder pathFinder = new AStarPathFinder(tileBasedMap, MAX_FOLLOWING_DISTANCE, false);
		
		Position startPosition = enemy.getPosition();

		//Selecting the best match for the target position. It can be null. (See the comment of the method for exact algorithm).
		Position targetPosition = nextFreePositionNearbyPlayer(actor, player, playersTargetEntrance, collisionValidator);

		List<Position> movementSteps = new ArrayList<>();
		//If there is a target position, calculate the path (if possible)
		if (targetPosition != null) {
			Path wholePath = pathFinder.findPath(null, (int)enemy.getPosition().getX(), (int)enemy.getPosition().getY(), (int) targetPosition.getX(), (int) targetPosition.getY());

			if (wholePath != null) {
				int tilesToMove = Math.min(enemy.getSpeedFactor(), wholePath.getLength() - 1);
				for (int i = 1; i <= tilesToMove; i++) {
					movementSteps.add(Position.fromCoordinates(wholePath.getX(i), wholePath.getY(i), enemy.getPosition().getMapDescriptor()));
				}
			}
		}

        return MovementPlan.instance(startPosition, movementSteps);
        
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
		final Position playersTargetPosition;
		if (playersTargetEntrance.isWaited()) {
			playersTargetPosition = player.getPosition();
		} else {
			playersTargetPosition = playersTargetEntrance.getPosition();
		}

		//We calculate 12 closest positions to the player that are candidates to be the target of the enemy path.
		List<Position> positionsAroundPlayer = new ArrayList<>();
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.UP));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.RIGHT));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.DOWN));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.LEFT));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.LEFT).nextPosition(DirectionType.UP));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.LEFT).nextPosition(DirectionType.DOWN));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.RIGHT).nextPosition(DirectionType.DOWN));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.RIGHT).nextPosition(DirectionType.UP));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.LEFT).nextPosition(DirectionType.LEFT));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.UP).nextPosition(DirectionType.UP));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.RIGHT).nextPosition(DirectionType.RIGHT));
		positionsAroundPlayer.add(playersTargetPosition.nextPosition(DirectionType.DOWN).nextPosition(DirectionType.DOWN));

		//All not blocked target candidates will be added to the list. It can be empty at the end, if all is blocked.
		List<Position> notBlockedPositions = Lists.newArrayList();
        for (Position oneOfPositionsAroundPlayer : positionsAroundPlayer) {
            Entrance entrance = Entrance.instance(oneOfPositionsAroundPlayer, actor.getDirection());
            if (collisionValidator.collisionStatus(game, actor, map, entrance).isPassable()) {
                notBlockedPositions.add(oneOfPositionsAroundPlayer);
            }
        }
		
		//We sort the candidates list to have the best match as last element in the list (or an empty list)
		notBlockedPositions.sort(new Comparator<Position>() {

            /*
             * The positions are ranked primarily by their distance to the player, secondarily by their distance to the enemey.
             */
            @Override
            public int compare(Position p1, Position p2) {
                float dp1 = distanceToActorTargetPosition(p1, playersTargetPosition);
                float dp2 = distanceToActorTargetPosition(p2, playersTargetPosition);

                if (dp1 < dp2) {
                    return 1;
                } else if (dp1 > dp2) {
                    return -1;
                } else {
                    float de1 = distanceToActorTargetPosition(p1, actor.getPosition());
                    float de2 = distanceToActorTargetPosition(p2, actor.getPosition());
                    return Float.compare(de2, de1);
                }

            }

            private float distanceToActorTargetPosition(Position p, Position pTarget) {
                return CosmodogMapUtils.distanceBetweenPositions(p, pTarget);
            }

        });
		
		return notBlockedPositions.isEmpty() ? null : notBlockedPositions.getLast();
		
	}

	public void setCosmodogGameSupplier(Supplier<CosmodogGame> cosmodogGameSupplier) {
		this.cosmodogGameSupplier = cosmodogGameSupplier;
	}

	public void setCosmodogMapSupplier(Supplier<CosmodogMap> cosmodogMapSupplier) {
		this.cosmodogMapSupplier = cosmodogMapSupplier;
	}

	public void setCosmodogSupplier(Supplier<Cosmodog> cosmodogSupplier) {
		this.cosmodogSupplier = cosmodogSupplier;
	}

	public void setPlayerSupplier(Supplier<Player> playerSupplier) {
		this.playerSupplier = playerSupplier;
	}
}
