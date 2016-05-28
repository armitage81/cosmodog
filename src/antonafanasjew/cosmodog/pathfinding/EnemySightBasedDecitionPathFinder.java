package antonafanasjew.cosmodog.pathfinding;

import java.util.Set;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.sight.Sight;
import antonafanasjew.cosmodog.sight.SightModifier;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class EnemySightBasedDecitionPathFinder extends AbstractPathFinder {

	private PathFinder outOfSightPathFinder;
	private PathFinder inSightPathFinder;
	
	public EnemySightBasedDecitionPathFinder(PathFinder outOfSightPathFinder, PathFinder inSightPathFinder) {
		this.outOfSightPathFinder = outOfSightPathFinder;
		this.inSightPathFinder = inSightPathFinder;
	}


	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator, MovementActionResult playerMovementActionResult) {

		PathFinder relevantPathFinder;
		
		Enemy enemy = (Enemy)actor;
		Player player = ApplicationContextUtils.getPlayer();

		boolean playerInVisibilityRange = playerInVisibilityRange(enemy, player);
		
		
		if (playerInVisibilityRange) {
			relevantPathFinder = inSightPathFinder;
		} else {
			relevantPathFinder = outOfSightPathFinder;
		}
		
		return relevantPathFinder.calculateMovementResult(actor, costBudget, collisionValidator, travelTimeCalculator, playerMovementActionResult);
		
	}

	private boolean playerInVisibilityRange(Enemy enemy, Player player) {
		
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CustomTiledMap customTiledMap = ApplicationContextUtils.getCustomTiledMap();
		SightModifier sightModifier = cosmodog.getSightModifier();
		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
		
		Set<Sight> sights = enemy.getSights();
		
		for (Sight sight : sights) {
			Sight modifiedSight = sightModifier.modifySight(sight, planetaryCalendar);
			VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(modifiedSight, enemy, customTiledMap.getTileWidth(), customTiledMap.getTileHeight());
			if (visibilityCalculator.visible(player, customTiledMap.getTileWidth(), customTiledMap.getTileHeight())) {
				return true;
			}
		}
		
		return false;
	}
	
}
