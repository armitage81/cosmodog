package antonafanasjew.cosmodog.actions.movement;

import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.Path;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.FightAction;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.GeneralCollisionValidatorForEnemy;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.fighting.SimpleEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimplePlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.pathfinding.TravelTimeCalculator;
import antonafanasjew.cosmodog.sight.Sight;
import antonafanasjew.cosmodog.sight.SightModifier;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.ActorTransitionRegistry;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Asynchronous action for movement.
 * Includes PC's movement and all the NPC's movements.
 * The action has fixed length.
 */
public class MovementAction extends FixedLengthAsyncAction {
	

	private static final long serialVersionUID = -693412142092974821L;

	private MovementActionResult playerMovementActionResult = null;
	private Map<Enemy, MovementActionResult> enemyMovementActionResults = Maps.newHashMap();
	
	/**
	 * Initialized with a fixed duration.
	 * @param duration Duration of the movement action.
	 */
	public MovementAction(int duration) {
		super(duration);
	}

	/**
	 * Calculates movement results. Initializes the movement for PC and NPC's by using their path finder algorithms.
	 */
	@Override
	public void onTrigger() {
		initMovementActionResults();
		onTriggerForPlayer();
		onTriggerForEnemies();
	}

	/**
	 * Updates player's movement transition based on the passed time. Focuses the cam on the player.
	 * Calculates enemies transitions based on the passed time and their time budgets.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		onUpdateForPlayer(after);
		onUpdateForEnemies(after);
		
	}

	/**
	 * Resets the PC's transition and does the actual shifting of his position in the game model triggering all the listeners.
	 * Resets the NPC's transitions. Initializes the fight action.
	 */
	@Override
	public void onEnd() {
		onEndForPlayer();		
		onEndForEnemies();
		fight();
	}

	/**
	 * Registers a short waiting action to simulate turn based movement.
	 */
	@Override
	public void afterUnregistration() {
		//Just a waiter action that does nothing but blocks input.
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		AsyncAction waiterAction = new FixedLengthAsyncAction(Constants.INTERVAL_BETWEEN_TURNS);
		cosmodogGame.getActionRegistry().registerAction(AsyncActionType.WAIT, waiterAction);
	}
	
	private void initMovementActionResults() {
		
		//Preparing static data.
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		Player player = ApplicationContextUtils.getPlayer();
		
		//Calculating the target position.
		Position targetPos = PositionUtils.neighbourPositionInFacingDirection(player);
		
		//Calculating the future result of the player movement.
		int playerMovementActionCosts = cosmodog.getTravelTimeCalculator().calculateTravelTime(applicationContext, player, (int)targetPos.getX(), (int)targetPos.getY());
		this.playerMovementActionResult = MovementActionResult.instance(player.getPositionX(), player.getPositionY(), (int)targetPos.getX(), (int)targetPos.getY(), playerMovementActionCosts);
		
		//Preparing the future results of the enemy movements.
		Set<Enemy> movingEnemies = Sets.newHashSet();
		movingEnemies.addAll(cosmodogMap.getEnemies());
		
		for (Enemy enemy : movingEnemies) {
			
			if (enemy.getChaussieType() != ChaussieType.STATIONARY) {
				MovementActionResult enemyMovementResult = calculateEnemyMovementResult(enemy, playerMovementActionResult, enemyMovementActionResults);
				if (enemyMovementResult != null) {
					enemyMovementActionResults.put(enemy, enemyMovementResult);
				}
			}
		}
		
	}
	
	private MovementActionResult calculateEnemyMovementResult(Enemy enemy, MovementActionResult playerMovementActionResult, Map<Enemy, MovementActionResult> enemyMovementActionResults) {
		
		//Preparing static data.
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		PathFinder pathFinder = cosmodog.getPathFinder();
		TravelTimeCalculator travelTimeCalculator = cosmodog.getTravelTimeCalculator();
		
		//Preparing enemy collision validator
		CollisionValidator collisionValidator = GeneralCollisionValidatorForEnemy.instance(playerMovementActionResult, enemyMovementActionResults);
		
		//Using the path finder to calculate the movement result based on the time budget generated from user movement and enemy's time overhead from the previous turns.
		return pathFinder.calculateMovementResult(enemy, (int)playerMovementActionResult.getMovementCostsInPlanetaryMinutesForFirstStep(), collisionValidator, travelTimeCalculator, playerMovementActionResult);

	}

	private void onTriggerForPlayer() {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Player player = ApplicationContextUtils.getPlayer();
		
		if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {

		} else if (player.getInventory().hasPlatform() && !player.getInventory().exitingPlatform()) {

			CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
			int resultX = playerMovementActionResult.getPath().getX(1);
			int resultY = playerMovementActionResult.getPath().getY(1);

			Set<Enemy> destroyedEnemies = Sets.newHashSet();
			
			for (Enemy enemy : enemyMovementActionResults.keySet()) {
				MovementActionResult enemyMovementActionResult = enemyMovementActionResults.get(enemy);
				
				int pathLength = enemyMovementActionResult.getPath().getLength();
				int enemyX = enemyMovementActionResult.getPath().getX(pathLength - 1);
				int enemyY = enemyMovementActionResult.getPath().getY(pathLength - 1);
				
				if (CosmodogMapUtils.isTileOnPlatform(enemyX, enemyY, resultX, resultY)) {
					destroyedEnemies.add(enemy);
				}
			}
			
			for (Enemy enemy : map.getEnemies()) {
				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPositionX(), enemy.getPositionY(), resultX, resultY)) {
					destroyedEnemies.add(enemy);
				}
			}
			
			map.getEnemies().removeAll(destroyedEnemies);
			
		} else {
			applicationContext.getSoundResources().get(SoundResources.SOUND_FOOTSTEPS).play();
		}
	}

	private void onTriggerForEnemies() {

	}
	
	private void onUpdateForPlayer(int timePassed) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Cam cam = cosmodogGame.getCam();

		int tileWidth = map.getTileWidth();
		
		float movementOffsetX = 0;
		float movementOffsetY = 0;
		
		Player player = cosmodogGame.getPlayer();
		
		//Take care: This works only because the tiles are quadratic.
		float ratio = (float)timePassed / getDuration();
		ratio = ratio > 1.0f ? 1.0f : ratio;
		float offset = tileWidth * ratio;
		boolean verticalNotHorizontal = true;
		boolean positiveNotNegative = true;
		
		if (player.getDirection() == DirectionType.DOWN) {
			movementOffsetY += offset;
			verticalNotHorizontal = true;
			positiveNotNegative = true;
		} else if (player.getDirection() == DirectionType.UP) {
			movementOffsetY -= offset;
			verticalNotHorizontal = true;
			positiveNotNegative = false;
		} else if (player.getDirection() == DirectionType.LEFT) {
			movementOffsetX -= offset;
			verticalNotHorizontal = false;
			positiveNotNegative = false;
		} else if (player.getDirection() == DirectionType.RIGHT) {
			movementOffsetX += offset;
			verticalNotHorizontal = false;
			positiveNotNegative = true;
		}
		
		int targetPosX = verticalNotHorizontal ? player.getPositionX() : (positiveNotNegative ? player.getPositionX() + 1 : player.getPositionX() - 1);
		int targetPosY = verticalNotHorizontal ? (positiveNotNegative ? player.getPositionY() + 1 : player.getPositionY() - 1) : player.getPositionY();
		
		ActorTransition playerTransition = ActorTransition.fromActor(player, targetPosX, targetPosY);
		if (verticalNotHorizontal) {
			playerTransition.setTransitionalOffsetY(positiveNotNegative ? ratio : -ratio);
		} else {
			playerTransition.setTransitionalOffsetX(positiveNotNegative ? ratio : -ratio);
		}
		
		cosmodogGame.getActorTransitionRegistry().put(player, playerTransition);
		
		cam.focusOnPiece(map, movementOffsetX, movementOffsetY, player);
	}
	
	private void onUpdateForEnemies(int timePassed) {
		
		float actionTimeRatio = (float)timePassed / getDuration();

		
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Set<Enemy> enemies = map.getEnemies();
		for (Enemy enemy : enemies) {
			
			int timeBudgetForMovement = (int)playerMovementActionResult.getMovementCostsInPlanetaryMinutesForFirstStep();
			float spentTimeBudget = actionTimeRatio * timeBudgetForMovement;
			
			
			MovementActionResult enemyMovementActionResult = enemyMovementActionResults.get(enemy);
			if (enemyMovementActionResult != null) {
				
				int step = enemyMovementActionResult.getMovementStepIndexForPassedPlanetaryMinutes(spentTimeBudget);
				
				//Nothing to update here as the enemy has finished his movement.
				if (step == -1) {
					
				} else {
					
					Path path = enemyMovementActionResult.getPath();
					
					if (path.getLength() > 1) { //The path can contain only the start point in case the npc does not move.
						
						float restMinutes = enemyMovementActionResult.getRemainingPlanetaryMinutesSinceLastMovementStep(spentTimeBudget);
						float costsForNextStep = enemyMovementActionResult.getMovementCostsInPlanetaryMinutes().get(step);
						float ratioForNextStep = restMinutes / costsForNextStep;
						int transitionalPosX = enemy.getPositionX();
						int transitionalPosY = enemy.getPositionY();
						int transitionalTargetPosX = path.getX(step + 1); //We need to add 1 as the path contains the initial position at index 0
						int transitionalTargetPosY = path.getY(step + 1);
						
						ActorTransitionRegistry transitionRegistry = game.getActorTransitionRegistry();
						ActorTransition enemyTransition = transitionRegistry.get(enemy);
						if (enemyTransition != null) {
							//here we do not want the target position but the current transitional position from which the movement offset will be counted
							//That's why we do not add 1 to the step. At the beginning, this will hold the initial actor position as it is before the movement starts
							transitionalPosX = path.getX(step); 
							transitionalPosY = path.getY(step);
						}
					
						
						ActorTransition newEnemyTransition = ActorTransition.fromActor(enemy, transitionalTargetPosX, transitionalTargetPosY);
						newEnemyTransition.setTransitionalPosX(transitionalPosX); 
						newEnemyTransition.setTransitionalPosY(transitionalPosY);
						
						if (transitionalPosX < transitionalTargetPosX) {
							newEnemyTransition.setTransitionalOffsetX(ratioForNextStep);
						} else if (transitionalPosX > transitionalTargetPosX) {
							newEnemyTransition.setTransitionalOffsetX(-ratioForNextStep);
						} else if (transitionalPosY < transitionalTargetPosY) {
							newEnemyTransition.setTransitionalOffsetY(ratioForNextStep);
						} else if (transitionalPosY > transitionalTargetPosY) {
							newEnemyTransition.setTransitionalOffsetY(-ratioForNextStep);
						}
												
						transitionRegistry.put(enemy, newEnemyTransition);
					}
					
				}
				
			}
			
		}
	}
	
	
	private void onEndForPlayer() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();
		
		ActorTransitionRegistry actorTransitionRegistry = cosmodogGame.getActorTransitionRegistry();
		actorTransitionRegistry.remove(player);

		boolean playerInPlatform = player.getInventory().hasPlatform();
		
		if (playerInPlatform) {
			for (Piece piece : cosmodogGame.getMap().getMapPieces()) {
				if (piece instanceof Platform == false && CosmodogMapUtils.isTileOnPlatform(piece.getPositionX(), piece.getPositionY(), player.getPositionX(), player.getPositionY())) {
					if (player.getDirection() == DirectionType.UP || player.getDirection() == DirectionType.DOWN) {
						piece.setPositionY(piece.getPositionY() + (player.getDirection() == DirectionType.UP ? -1 : 1));
					} else {
						piece.setPositionX(piece.getPositionX() + (player.getDirection() == DirectionType.LEFT ? -1 : 1));
					}
				}
			}
		}
		
		if (player.getDirection() == DirectionType.UP || player.getDirection() == DirectionType.DOWN) {
			player.shiftVertical(player.getDirection() == DirectionType.UP ? -1 : 1);
		} else {
			player.shiftHorizontal(player.getDirection() == DirectionType.LEFT ? -1 : 1);
		}
		
	}
	
	private void onEndForEnemies() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = ApplicationContextUtils.getPlayer();
		Set<Enemy> enemies = map.getEnemies();
		for (Enemy enemy : enemies) {
						
			ActorTransitionRegistry actorTransitionRegistry = cosmodogGame.getActorTransitionRegistry();
			ActorTransition enemyTransition = actorTransitionRegistry.remove(enemy);
			MovementActionResult movementActionResult = enemyMovementActionResults.get(enemy);
			if (movementActionResult != null) {
    			Path path = movementActionResult.getPath();
    			int lastPathPosX = path.getX(path.getLength() - 1);
    			int lastPathPosY = path.getY(path.getLength() - 1);
    			enemy.setPositionX(lastPathPosX);
    			enemy.setPositionY(lastPathPosY);
    			if (enemyTransition != null) {
    				enemy.setDirection(enemyTransition.getTransitionalDirection());
    			}
			}
			
			if (playerInVisibilityRange(enemy, player)) {
				enemy.increaseAlertLevelToMax();
			} else {
				enemy.reduceAlertLevel();
			}
		}
		
	}
	
	private boolean playerInVisibilityRange(Enemy enemy, Player player) {
		
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		SightModifier sightModifier = cosmodog.getSightModifier();
		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
		
		Set<Sight> sights = enemy.getSights();
		
		for (Sight sight : sights) {
			Sight modifiedSight = sightModifier.modifySight(sight, planetaryCalendar);
			VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(modifiedSight, enemy, map.getTileWidth(), map.getTileHeight());
			if (visibilityCalculator.visible(player, map.getTileWidth(), map.getTileHeight())) {
				return true;
			}
		}
		
		return false;
	}
	
	private void fight() {
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		ActionRegistry ar = game.getActionRegistry();
		ar.registerAction(AsyncActionType.FIGHT, new FightAction(new SimplePlayerAttackDamageCalculator(), new SimpleEnemyAttackDamageCalculator()));
		
	}
}
