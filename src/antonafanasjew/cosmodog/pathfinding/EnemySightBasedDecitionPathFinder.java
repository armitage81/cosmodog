package antonafanasjew.cosmodog.pathfinding;

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

public class EnemySightBasedDecitionPathFinder extends AbstractPathFinder {

	private PathFinder outOfSightPathFinder;
	private PathFinder inSightPathFinder;
	
	public EnemySightBasedDecitionPathFinder(PathFinder outOfSightPathFinder, PathFinder inSightPathFinder) {
		this.outOfSightPathFinder = outOfSightPathFinder;
		this.inSightPathFinder = inSightPathFinder;
	}


	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator, MovementActionResult playerMovementActionResult) {
		
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		SightRadiusCalculator sightRadiusCalculator = cosmodog.getSightRadiusCalculator();
		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
		
		Enemy enemy = (Enemy)actor;
		
		int sightRadius = sightRadiusCalculator.calculateSightRadius(enemy, planetaryCalendar);
		
		Player player = ApplicationContextUtils.getPlayer();
		
		//If the player did not move, we take his real coordinates as target.
		int x = player.getPositionX();
		int y = player.getPositionY();
		
		int distanceToPlayer = (int)CosmodogMapUtils.distanceBetweenPositions(Position.fromCoordinates(x, y), Position.fromCoordinates(enemy.getPositionX(), enemy.getPositionY()));
		
		PathFinder relevantPathFinder;
		
		if (distanceToPlayer > sightRadius) {
			relevantPathFinder = outOfSightPathFinder;
		} else {
			relevantPathFinder = inSightPathFinder;
		}
		
		return relevantPathFinder.calculateMovementResult(actor, costBudget, collisionValidator, travelTimeCalculator, playerMovementActionResult);
		
	}

	
}
