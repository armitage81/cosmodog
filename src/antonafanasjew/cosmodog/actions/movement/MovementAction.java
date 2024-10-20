package antonafanasjew.cosmodog.actions.movement;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

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
import antonafanasjew.cosmodog.collision.validators.npc.GeneralCollisionValidatorForNpc;
import antonafanasjew.cosmodog.domains.ChaussieType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.fighting.AbstractEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimpleEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimplePlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimplePlayerAttackDamageCalculatorUnarmed;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.EnemiesUtils;
import antonafanasjew.cosmodog.util.FootstepUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.ActorTransitionRegistry;

/**
 * Represents a movement action.
 * <p>
 * Skipping turn counts as movement.
 * <p>
 * Four things can move in the game:
 * the player, the moveable blocks (which are of subtype of DynamicPiece), the enemies and the pieces on the
 * moving platform.
 * <p>
 * Like in the FightAction, the MovementAction precalculates movement results
 * for the player, the moveable blocks and the enemies when triggered and uses them during the duration of the action.
 * <p>
 * Take note: Collision for the player and for moveable blocks has already been checked.
 * It happened in the InGameInputHandler. This action assumes that the player (and, potentially, a moveable block)
 * can move one tile in the player's facing direction without problems. But collision for enemies is
 * still calculated in this action,
 * because it depends on the player's and the moveable block's target position.
 * <p>
 * This is a fixed length action.
 */
public class MovementAction extends FixedLengthAsyncAction {
	

	@Serial
	private static final long serialVersionUID = -693412142092974821L;

	/**
	 * Movement action result for the player. It contains the path of the movement.
	 * In case of the player, the path always contains only one step (No movement, North, West, East or South).
	 */
	private MovementActionResult playerMovementActionResult = null;

	/**
	 * Movement action result for the moveable block pushed by the player.
	 * It contains the path of the movement.
	 * <p>
	 * The path of the moveable block always correlates to the player's movement (one tile ahead).
	 * <p>
	 * Only one block can be pushed at a time.
	 */
	private MovementActionResult moveableMovementActionResult = null;

	/**
	 * Movement action results for enemies.
	 * Each result contains the path of the movement.
	 * <p>
	 * Enemies will only move if they are  in the activation distance of the player (performance optimization).
	 * <p>
	 * Enemies that are deactivated (f.i. solar tanks at night) will not be considered.
	 * <p>
	 * Stationary enemies (chassis type STATIONARY, f.i. turrets) will not be considered.
	 * <p>
	 * Player's movement (and that of moveable objects) will be considered in the collision detection
	 * for the enemies. (F.i. the player's target position will be considered blocked.)
	 * <p>
	 * Each movement action result will contain a path with the start position of the enemy as the first step.
	 * It will depend on the alert level, the player's position and the enemy's speed factor.
	 */
	private final Map<Enemy, MovementActionResult> enemyMovementActionResults = Maps.newHashMap();

	/**
	 * Defines whether the player really moves or just skips a turn. (Enemies still move in the latter case.)
	 */
	private final boolean skipTurn;

	/**
	 * Creates a new movement action. It represents the movement of the player, a potential moveable block
	 * as well as enemies.
	 * It could include skipping a turn. (Enemies still move.)
	 *
	 * @param duration Duration of the movement action.
	 * @param skipTurn Defines whether the player really moves or just skips a turn (enemies still move in that case).
	 */
	public MovementAction(int duration, boolean skipTurn) {
		super(duration);
		this.skipTurn = skipTurn;
	}

	/**
	 * First calculates movement results for the player, for a potential moveable block and for enemies
	 * (by using the latter's pathfinder algorithms).
	 * <p>
	 * Then triggers the movement of the player, the moveable block and enemies.
	 * <p>
	 * Interesting are cases when the player exits a vehicle or the platform.
	 * <p>
	 * Take note: In case the player is using the binoculars to see far, the movement will reset the zoom.
	 */
	@Override
	public void onTrigger() {
		initMovementActionResults();
		onTriggerForMoveable();
		onTriggerForPlayer();
		onTriggerForEnemies();
	}

	/**
	 * Updates the action based on the passed time.
	 * <p>
	 * In this case, the update slowly updates transitions of the player, a potential moveable and enemies
	 * so the renderer can show the transitional movement between tiles.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		onUpdateForMoveable(after);
		onUpdateForPlayer(after);
		onUpdateForEnemies(after);
		
	}

	/**
	 * Only at the end of the action, all positions of the player, moveable blocks and enemies are updated in the model.
	 * <p>
	 * If the player is sitting in the platform, all pieces on the platform are moved as well.
	 * (The platform itself, as well as a vehicle are not pieces but inventory items
	 * in case the player occupies them, so they do not have to be moved.)
	 * <p>
	 * The movement transitions are removed so the renderer knows that the pieces are static again.
	 * <p>
	 * Listener for the player's movement is called.
	 * <p>
	 * If, after the movement, there are alerted adjacent enemies, the fight action is registered in the action registry.
	 * <p>
	 * Take care: The moveable blocks must be handled before the player, because player's listener checks
	 * the status of Sokoban riddles and this status could be changed by the latest movement of a moveable block.
	 * Having the order changed would cause the listener to notice the solution one turn too late.
	 */
	@Override
	public void onEnd() {
		/*
		 * This must be called before onEndForPlayer because the latter calls the movement listener
		 * and this listener checks whether a potential sokoban riddle is solved.
		 * But if the moveable block's position has not been updated yet, the solution comes one turn later.
		 */
		onEndForMoveable();
		onEndForPlayer();
		onEndForEnemies();
		fight();
		onEndForDynamicPieces();
	}

	/**
	 * This method is called by the Action registry when an action has been unregistered.
	 * <p>
	 * Registers a short waiting action to simulate turn based movement.
	 * Without it, the player's movement would be too smooth.
	 * <p>
	 * This action blocks the player's input, meaning that the player cannot move or change weapons
	 * until the action has finished.
	 */
	@Override
	public void afterUnregistration() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		AsyncAction waiterAction = new FixedLengthAsyncAction(Constants.INTERVAL_BETWEEN_TURNS);
		cosmodogGame.getActionRegistry().registerAction(AsyncActionType.WAIT, waiterAction);
	}

	/**
	 * Initializes the movement results of the player, a potential moveable block and enemies.
	 * Collision for the player and moveable blocks has already been checked in the InGameInputHandler.
	 * Both will simply move one tile in the facing direction of the player.
	 * <p>
	 * Enemies will move based on collision validators and their pathfinders.
	 * <p>
	 * The calculated results will be set on the corresponding fields of this action and used
	 * during the duration of the action.
	 * <p>
	 * The player's "movement" could be a skip turn. In this case, the player's position will not change.
	 * <p>
	 * Stationary, deactivated and far enemies will not move.
	 */
	private void initMovementActionResults() {
		//Preparing static data.
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		Position playerPosition = player.getPosition();
		
		//Calculating the target position.
		Position targetPosition = skipTurn ? playerPosition : PositionUtils.neighbourPositionInFacingDirection(player);
		
		//Calculating the future result of the player movement.
		this.playerMovementActionResult = MovementActionResult.instance(playerPosition, targetPosition);
		
		//There could be a moveable block in player's path. In this case, it also would be moved (collision has been checked already.)
		//Take care: Player's movement has to be calculated before this.
		this.moveableMovementActionResult = calculateMoveableMovementActionResult();
		
		//Preparing the future results of the enemy movements.
		Set<Enemy> movingEnemies = Sets.newHashSet();
		
		movingEnemies.addAll(cosmodogMap.nearbyEnemies(player.getPosition(), Constants.ENEMY_ACTIVATION_DISTANCE));
		
		EnemiesUtils.removeInactiveUnits(movingEnemies);
		
		for (Enemy enemy : movingEnemies) {
			
			if (enemy.getChaussieType() != ChaussieType.STATIONARY) {
				MovementActionResult enemyMovementResult = calculateEnemyMovementResult(enemy);
				if (enemyMovementResult != null) {
					enemyMovementActionResults.put(enemy, enemyMovementResult);
				}
			}
		}
		
	}

	/**
	 * Calculates the movement result for a moveable block. A block moves when the player pushes it.
	 * The block's movement always correlates with the player's movement.
	 * That's why the player's movement result must already exist when calculating the moveable block's movement.
	 * <p>
	 * If there is no moveable block in front of the player or if the player's movement is a skip turn,
	 * the returned movement result will be null.
	 */
	private MovementActionResult calculateMoveableMovementActionResult() {
		
		MovementActionResult retVal = null;
		
		if (skipTurn) {
			return retVal;
		}

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();
		
		//We check if the position to which the player moves contains a moveable.
		Path path = playerMovementActionResult.getPath();
		Step lastStep = path.getStep(path.getLength() - 1);
		int moveablePosX = lastStep.getX();
		int moveablePosY = lastStep.getY();
		
		//If a moveable is there, we calculate its target position (always away from the player as he pushes it)
		DynamicPiece dynamicPiece = cosmodogGame.dynamicPieceAtPosition(Position.fromCoordinatesOnPlayerLocationMap(moveablePosX, moveablePosY));
		if (dynamicPiece instanceof MoveableDynamicPiece) {
			int newMoveablePosX = (int)dynamicPiece.getPosition().getX();
			int newMoveablePosY = (int)dynamicPiece.getPosition().getY();
			DirectionType directionType = player.getDirection();
			if (directionType == DirectionType.UP) {
				newMoveablePosY--;
			}
			if (directionType == DirectionType.DOWN) {
				newMoveablePosY++;
			}
			if (directionType == DirectionType.LEFT) {
				newMoveablePosX--;
			}
			if (directionType == DirectionType.RIGHT) {
				newMoveablePosX++;
			}
			
			retVal = MovementActionResult.instance(
					Position.fromCoordinatesOnPlayerLocationMap(moveablePosX, moveablePosY),
					Position.fromCoordinatesOnPlayerLocationMap(newMoveablePosX, newMoveablePosY));
		}
		return retVal;
		
	}

	/**
	 * Calculates the movement result for an enemy. Player's movement, a potential moveable block's movement and other enemies'
	 * movements are considered in the collision detection. (Their target positions are considered blocked.)
	 * That's why the player's and the moveable block's movement must be calculated before this. Also,
	 * the movement of this enemy must be added to the collection of moving enemies to be
	 * considered by the pathfinders of subsequent enemies.
	 * <p>
	 * The actual movement of the enemy is calculated by its pathfinder. It can be an idle patrol movement or a chase
	 * in case the enemy is alerted. The result is not a simple movement step but a path with potentially multiple steps.
	 * (F.i. drones can move up to three tiles in a round.)
	 *
	 * @param enemy Enemy for which the movement should be calculated.
	 * @return Movement action result for the enemy. It contains a path that will be followed by the enemy during the action.
	 */
	private MovementActionResult calculateEnemyMovementResult(Enemy enemy) {
		
		//Preparing static data.
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		PathFinder pathFinder = cosmodog.getPathFinder();
		
		//Preparing enemy collision validator
		CollisionValidator collisionValidator = GeneralCollisionValidatorForNpc.instance(playerMovementActionResult, moveableMovementActionResult, enemyMovementActionResults);
		
		//Using the pathfinder to calculate the movement result based on the time budget generated from user movement and enemy's time overhead from the previous turns.
		int costBudget = Constants.MINUTES_PER_TURN * enemy.getSpeedFactor();
		return pathFinder.calculateMovementResult(enemy, costBudget, collisionValidator, playerMovementActionResult);

	}

	/**
	 * Handles the beginning of the movement for the player.
	 * <p>
	 * When moving within the platform or a vehicle (and not skipping turn), the driving sound is played.
	 * <p>
	 * When moving within the platform, all enemies that are touched by the movement are destroyed
	 * and removed from the map. In this process, only target positions are considered so
	 * the enemies have a chance to escape the platform.
	 * <p>
	 * If the player is on foot (or in a boat), the footstep sound (or water splash) is played.
	 * The sound depends on the terrain on the target position.
	 */
	private void onTriggerForPlayer() {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Player player = ApplicationContextUtils.getPlayer();
		
		if (player.getInventory().hasVehicle() && !player.getInventory().exitingVehicle()) {
			if (!skipTurn) {
				applicationContext.getSoundResources().get(SoundResources.SOUND_CAR_MOVES).play();
			}
		} else if (player.getInventory().hasPlatform() && !player.getInventory().exitingPlatform()) {

			if (!skipTurn) {
				applicationContext.getSoundResources().get(SoundResources.SOUND_CAR_MOVES).play();
			}
			
			CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
			int resultX = playerMovementActionResult.getPath().getX(1);
			int resultY = playerMovementActionResult.getPath().getY(1);

			Position resultPosition = Position.fromCoordinatesOnPlayerLocationMap(resultX, resultY);

			Set<Enemy> destroyedEnemies = Sets.newHashSet();
			
			for (Enemy enemy : enemyMovementActionResults.keySet()) {
				MovementActionResult enemyMovementActionResult = enemyMovementActionResults.get(enemy);
				
				int pathLength = enemyMovementActionResult.getPath().getLength();
				int enemyX = enemyMovementActionResult.getPath().getX(pathLength - 1);
				int enemyY = enemyMovementActionResult.getPath().getY(pathLength - 1);

				Position enemyPosition = Position.fromCoordinatesOnPlayerLocationMap(enemyX, enemyY);

				if (CosmodogMapUtils.isTileOnPlatform(enemyPosition, resultPosition)) {
					destroyedEnemies.add(enemy);
				}
			}
			
			for (Enemy enemy : map.getEnemies()) {
				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPosition(), resultPosition)) {
					destroyedEnemies.add(enemy);
				}
			}
			
			map.getEnemies().removeAll(destroyedEnemies);
			
		} else {
			if (!skipTurn) {
				int resultX = playerMovementActionResult.getPath().getX(1);
				int resultY = playerMovementActionResult.getPath().getY(1);
				Position position = Position.fromCoordinatesOnPlayerLocationMap(resultX, resultY);
				String footStepsSoundType = FootstepUtils.footStepsSoundType(position);
				applicationContext.getSoundResources().get(footStepsSoundType).play();
			}
		}
	}

	/**
	 * Handles the beginning of the movement for a potential moveable block.
	 * <p>
	 * If the corresponding movement result is not null, plays the sound of moving a block.
	 * (Interestingly, it is the same sound as driving a car.)
	 */
	private void onTriggerForMoveable() {
		if (moveableMovementActionResult != null) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CAR_MOVES).play();
		}
	}

	/**
	 * Does nothing at the beginning of the movement for enemies.
	 */
	private void onTriggerForEnemies() {

	}

	/**
	 * Updates the player's movement transition. The player's position in the game model does not change here.
	 * It will happen only at the end of the action. The transition only handles the offset between two tiles
	 * to represent a smooth movement.
	 * <p>
	 * If the player skips a turn, nothing happens here.
	 * <p>
	 * If the player moves, the camera will focus on the player's movement considering his offset between
	 * two tiles depending on the ratio time passed since the action triggered to the duration of the action.
	 * <p>
	 * Take note: The player's movement transition is stored in the transition registry of the game.
	 *
	 * @param timePassed Time passed since the action triggered in milliseconds.
	 */
	private void onUpdateForPlayer(int timePassed) {
		
		if (skipTurn) {
			return;
		}
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		
		Cam cam = cosmodogGame.getCam();

		int tileWidth = map.getTileWidth();
		
		float movementOffsetX = 0;
		float movementOffsetY = 0;
		
		Player player = cosmodogGame.getPlayer();
		
		//Take care: This works only because the tiles are square.
		float ratio = (float)timePassed / getDuration();
		ratio = Math.min(ratio, 1.0f);
		float offset = tileWidth * ratio;
		boolean verticalNotHorizontal = true;
		boolean positiveNotNegative = true;
		
		if (player.getDirection() == DirectionType.DOWN) {
			movementOffsetY += offset;
		} else if (player.getDirection() == DirectionType.UP) {
			movementOffsetY -= offset;
			positiveNotNegative = false;
		} else if (player.getDirection() == DirectionType.LEFT) {
			movementOffsetX -= offset;
			verticalNotHorizontal = false;
			positiveNotNegative = false;
		} else if (player.getDirection() == DirectionType.RIGHT) {
			movementOffsetX += offset;
			verticalNotHorizontal = false;
		}

		int targetPosX = playerMovementActionResult.getPath().getStep(1).getX();
		int targetPosY = playerMovementActionResult.getPath().getStep(1).getY();
		Position targetPosition = Position.fromCoordinatesOnPlayerLocationMap(targetPosX, targetPosY);

		ActorTransition playerTransition = ActorTransition.fromActor(player, targetPosition);
		if (verticalNotHorizontal) {
			playerTransition.setTransitionalOffsetY(positiveNotNegative ? ratio : -ratio);
		} else {
			playerTransition.setTransitionalOffsetX(positiveNotNegative ? ratio : -ratio);
		}
		
		cosmodogGame.getActorTransitionRegistry().put(player, playerTransition);
		
		cam.focusOnPiece(cosmodogGame, movementOffsetX, movementOffsetY, player);
	}

	/**
	 * Updates the moveable block's movement transition. The block's position in the game model does not change here.
	 * It will happen only at the end of the action.
	 * <p>
	 * If the player skips a turn, nothing happens here.
	 * <p>
	 * If the player does not push a moveable block, nothing happens here.
	 * <p>
	 * Otherwise, the same transition type is used as the one for the player.
	 * <p>
	 * Take note: The transition is of type ActorTransition because it is used to represent actors like the player
	 * and the enemies. The moveable block is a subtype of DynamicPiece and must be converted to an actor to be
	 * able to use the transition type. This is quite a hack. Actors and dynamic pieces were supposed to represent
	 * different concepts. The difference was that actors could move while dynamic pieces were stationary.
	 * But with the moveable dynamic pieces, this difference is blurred.
	 * <p>
	 * Take note: The moveable block's movement transition is stored in the transition registry of the game.
	 *
	 * @param timePassed Time passed since the action triggered in milliseconds.
	 */
	private void onUpdateForMoveable(int timePassed) {

		if (skipTurn) {
			return;
		}

		if (moveableMovementActionResult == null) {
			return;
		}

		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

		Path path = moveableMovementActionResult.getPath();

		int posX = path.getX(0);
		int posY = path.getY(0);
		int targetPosX = path.getX(1); //We need to add 1 as the path contains the initial position at index 0
		int targetPosY = path.getY(1);

		Position position = Position.fromCoordinatesOnPlayerLocationMap(posX, posY);
		Position targetPosition = Position.fromCoordinatesOnPlayerLocationMap(targetPosX, targetPosY);

		MoveableDynamicPiece moveable = (MoveableDynamicPiece)ApplicationContextUtils
				.getCosmodogGame()
				.dynamicPieceAtPosition(position);

		float ratio = (float)timePassed / getDuration();
		ratio = Math.min(ratio, 1.0f);

		boolean verticalNotHorizontal;
		boolean positiveNotNegative;

		if (targetPosY > posY) {
			verticalNotHorizontal = true;
			positiveNotNegative = true;
		} else if (targetPosY < posY) {
			verticalNotHorizontal = true;
			positiveNotNegative = false;
		} else if (targetPosX < posX) {
			verticalNotHorizontal = false;
			positiveNotNegative = false;
		} else {
			verticalNotHorizontal = false;
			positiveNotNegative = true;
		}

		ActorTransition moveableTransition = ActorTransition.fromActor(moveable.asActor(), targetPosition);
		if (verticalNotHorizontal) {
			moveableTransition.setTransitionalOffsetY(positiveNotNegative ? ratio : -ratio);
		} else {
			moveableTransition.setTransitionalOffsetX(positiveNotNegative ? ratio : -ratio);
		}
		cosmodogGame.getActorTransitionRegistry().put(moveable.asActor(), moveableTransition);
	}

	/**
	 * Updates the enemies' movement transition. Does it for all enemies from the enemy movement results.
	 * <p>
	 * First, calculates how far the enemy can move. This depends on the enemy's speed factor.
	 * Then checks how much of the available movement budget has been spent so far (depending on passed time since
	 * the action started). Calculates which step of the path the enemy is currently on.
	 * (Example: If the enemy has a path with 3 steps and the enemy has spent 50% of the movement budget, the enemy is
	 * between the first and the second step.) Then the transitional position is calculated based on the current step
	 * and the remaining offset to the next step.
	 * <p>
	 * Take note: Enemies' transitions are stored in the transition registry of the game.
	 *
	 * @param timePassed Time passed since the action triggered in milliseconds.
	 */
	private void onUpdateForEnemies(int timePassed) {

		float actionTimeRatio = (float)timePassed / getDuration();

		//The enemy has moved completely already. Note: It is important to
		//skip the case actionTimeRation == 1 to avoid the indexoutofbounds exception.
		if (actionTimeRatio >= 1) {
			return;
		}

		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		Set<Enemy> enemies = enemyMovementActionResults.keySet();
		for (Enemy enemy : enemies) {

			MovementActionResult enemyMovementActionResult = enemyMovementActionResults.get(enemy);
			Path path = enemyMovementActionResult.getPath();

			//The path can contain only the start point in case the npc does not move.
			//This is others than in case of the player's "skip turn" where two steps are added to the path
			// (start and target positions that are equal).
			if (path.getLength() == 1) {
				continue;
			}

			float movedDistanceInTilesWithOffset = actionTimeRatio * (path.getLength() - 1);
			int movedDistanceInTiles = (int)movedDistanceInTilesWithOffset;
			float ratioForNextStep = movedDistanceInTilesWithOffset - movedDistanceInTiles;

			int transitionalPosX = path.getX(movedDistanceInTiles);
			int transitionalPosY = path.getY(movedDistanceInTiles);

			//The starting position of the movement is stored in the path at index = 0.
			//While the actor still moves from its starting position tile, the position at index = 1 of the path is taken as target.
			//While the actor moves from the first to the second tile, the position at index = 2 of the path is taken as target.
			//etc.
			int transitionalTargetPosX = path.getX(movedDistanceInTiles + 1);
			int transitionalTargetPosY = path.getY(movedDistanceInTiles + 1);

			Position transitionalPosition = Position.fromCoordinatesOnPlayerLocationMap(transitionalPosX, transitionalPosY);

			ActorTransitionRegistry transitionRegistry = game.getActorTransitionRegistry();
			ActorTransition enemyTransition = transitionRegistry.get(enemy);

			ActorTransition newEnemyTransition = ActorTransition.fromActor(enemy, transitionalPosition);
			newEnemyTransition.setTransitionalPosition(transitionalPosition);

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

	/**
	 * Executed when the action has finished and handles the part for the player.
	 * <p>
	 * Removes the actor transition from the registry.
	 * <p>
	 * If the action was about skipping turn, the listeners for skipping turn are notified.
	 * (They update time, increase the worm counter, reduce water and food and many other things,
	 * see PlayerMovementListener.)
	 * <p>
	 * If the player is in the platform (that is he has it in his inventory, technically) then all pieces that are
	 * on the platform are moved as well. Their movement correlates to the player's movement.
	 * The movement means changing the x/y coordinates of the pieces, delete the references to them from the map
	 * and add them to the map again at new places.
	 * <p>
	 * Finally, the player's position is shifted to the target tile.
	 * This triggers all movement listeners for the player.
	 */
	private void onEndForPlayer() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();
		
		ActorTransitionRegistry actorTransitionRegistry = cosmodogGame.getActorTransitionRegistry();
		actorTransitionRegistry.remove(player);

		if (skipTurn) {
			player.skipTurn();
			return;
		}
		
		boolean playerInPlatform = player.getInventory().hasPlatform();
		
		if (playerInPlatform) {
			
			Map<Position, Piece> oldPositionsForPiecesOnPlatform = Maps.newHashMap();
			
			for (Piece piece : cosmodogGame.mapOfPlayerLocation().getMapPieces().values()) {
				if (CosmodogMapUtils.isTileOnPlatform(piece.getPosition(), player.getPosition())) {
					
					//As we move the piece on the platform, we need to update it in the mapValues cache
					Position position = piece.getPosition();
					oldPositionsForPiecesOnPlatform.put(position, piece);
					
					if (player.getDirection() == DirectionType.UP || player.getDirection() == DirectionType.DOWN) {
						piece.getPosition().shift(0, player.getDirection() == DirectionType.UP ? -1 : 1);
					} else {
						piece.getPosition().shift(player.getDirection() == DirectionType.LEFT ? -1 : 1, 0);
					}
				}
			}
			
			//When we modify positions of pieces on platform, we need to modify the mapPieces cache as well.
			for (Position oldPosition : oldPositionsForPiecesOnPlatform.keySet()) {
				cosmodogGame.mapOfPlayerLocation().getMapPieces().remove(oldPosition);
			}
			
			for (Position oldPosition : oldPositionsForPiecesOnPlatform.keySet()) {
				Piece piece = oldPositionsForPiecesOnPlatform.get(oldPosition);
				Position newPosition = piece.getPosition();
				cosmodogGame.mapOfPlayerLocation().getMapPieces().put(newPosition, piece);
			}
		}
		
		if (player.getDirection() == DirectionType.UP || player.getDirection() == DirectionType.DOWN) {
			player.shiftVertical(player.getDirection() == DirectionType.UP ? -1 : 1);
		} else {
			player.shiftHorizontal(player.getDirection() == DirectionType.LEFT ? -1 : 1);
		}
	}

	/**
	 * Executed when the action has finished and handles the part for a potential moveable object.
	 * <p>
	 * Uses the moveable object's action result.
	 * If there was a movement (it could've been a skip turn) the dynamic piece coordinates of the moveable object
	 * are set to the target tile and the actor transition is removed from the registry. (Remember, the moveable
	 * object is handled as an actor here even if it is technically a dynamic piece.)
	 */
	private void onEndForMoveable() {
		
		MovementActionResult moveableMovementActionResult = this.moveableMovementActionResult;
		if (moveableMovementActionResult != null) {
			Path path = moveableMovementActionResult.getPath();
			
			if (path.getLength() > 1) { //The path can contain only the start point in case the moveable does not move.
				
				CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
				
				int posX = path.getX(0);
				int posY = path.getY(0);

				Position position = Position.fromCoordinatesOnPlayerLocationMap(posX, posY);

				int targetPosX = path.getX(1); //We need to add 1 as the path contains the initial position at index 0
				int targetPosY = path.getY(1);
				Position targetPosition = Position.fromCoordinatesOnPlayerLocationMap(targetPosX, targetPosY);
				
				MoveableDynamicPiece moveable = (MoveableDynamicPiece)ApplicationContextUtils.getCosmodogGame().dynamicPieceAtPosition(position);
				
				ActorTransitionRegistry actorTransitionRegistry = cosmodogGame.getActorTransitionRegistry();
				actorTransitionRegistry.remove(moveable.asActor());
				
				moveable.setPosition(targetPosition);

			}
		}
	}

	/**
	 * Executed when the action has finished and handles the part for the enemies.
	 * <p>
	 * Removes actor transitions for all enemies from the registry.
	 * <p>
	 * Sets the coordinates of each enemy to the target position as defined in their corresponding
	 * movement action results. Also sets each enemy's direction to its transitional direction.
	 * (During the transition the displayed direction was only temporary, now it is fixed in the model as well.
	 * If it hadn't been done, the enemy would face in the wrong direction after the transition would be over.)
	 * <p>
	 * Finally, the enemy alert level is handled. If it sees the player after the movement, the alert is set
	 * to the highest level. If not, it is reduced by one unless it is 0 already. This way, the enemy can "lose scent"
	 * of the player.
	 */
	private void onEndForEnemies() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
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
				Position lastPathPosition = Position.fromCoordinatesOnPlayerLocationMap(lastPathPosX, lastPathPosY);
    			enemy.setPosition(lastPathPosition);
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
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
		
		VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(enemy.getDefaultVision(), enemy.getNightVision(), enemy.getStealthVision());
        return visibilityCalculator.visible(enemy, planetaryCalendar, map, player);
    }

	/**
	 * Handles the case when an enemy ends up alerted and adjacent to the player after the movement.
	 * In this case, the enemy will attack the player.
	 * <p>
	 * The resulting fight action is registered in the action registry and will be executed before the next turn can be
	 * done by the player.
	 * <p>
	 * Take note: This version of a fight will happen exclusively when (at least one) enemy is the attacker. It will
	 * never contain a phase when player attacks or an enemy is destroyed. (Don't be distracted by the player's
	 * damage calculator passed to the fight action. It is needed only to satisfy the contract of the class.)
	 * <p>
	 * Take note: The damage dealt to the player by enemies' attacks can be toggled off by deactivating the feature
	 * DAMAGE. This is useful for debugging and testing.
	 * <p>
	 * The calculation of damage dealt to the player (given that the DAMAGE feature is toggled on) is done by
	 * the damage calculator of the enemy. Usually, it will just take the enemy's weapon and deal constant damage
	 * to the player depending on it.
	 */
	private void fight() {
		
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		ActionRegistry ar = game.getActionRegistry();
		boolean damageFeatureOn = Features.getInstance().featureOn(Features.FEATURE_DAMAGE);
		
		AbstractEnemyAttackDamageCalculator enemyDamageCalculator = damageFeatureOn ? new SimpleEnemyAttackDamageCalculator() : new AbstractEnemyAttackDamageCalculator() {
			
			@Override
			protected int enemyAttackDamageInternal(Enemy enemy, Player player) {
				return 0;
			}
		};
		
		ar.registerAction(AsyncActionType.FIGHT, new FightAction(new SimplePlayerAttackDamageCalculator(game.getPlanetaryCalendar()), new SimplePlayerAttackDamageCalculatorUnarmed(), enemyDamageCalculator));
		
	}

	/**
	 * At the end of the movement action, dynamic pieces are updated.
	 * <p>
	 * If there was no actual movement ("skip turn"), nothing happens.
	 * <p>
	 * Otherwise, all dynamic pieces from the map are collected and grouped by their type.
	 * Each group is then iterated on.
	 * <p>
	 * For each piece, the interaction when stepping on is called if the piece is on the target tile of the movement.
	 * The interaction when leaving is called if the piece is on the start tile of the movement.
	 * <p>
	 * Take note: These updates are mainly done for the letter plates in the Indiana Jones puzzle.
	 * <p>
	 * (Interestingly, mines are not handled in an analog way when stepped on. This is probably because
	 * the method interactWhenSteppingOn was added after mines had been implemented already. Mine explosions
	 * happen within the player's movement listener, instead.)
	 */
	private void onEndForDynamicPieces() {
		int startX = playerMovementActionResult.getPath().getX(0);
		int startY = playerMovementActionResult.getPath().getY(0);
		int targetX = playerMovementActionResult.getPath().getX(1);
		int targetY = playerMovementActionResult.getPath().getY(1);

		Position startPosition = Position.fromCoordinatesOnPlayerLocationMap(startX, startY);
		Position targetPosition = Position.fromCoordinatesOnPlayerLocationMap(targetX, targetY);
		
		boolean noMovement = startX == targetX && startY == targetY;

		if (noMovement) {
			return;
		}

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Multimap<Class<?>, DynamicPiece> pieces = map.getDynamicPieces();
		for (Class<?> pieceType : pieces.keySet()) {
			Collection<DynamicPiece> piecesOfOneType = pieces.get(pieceType);
			for (DynamicPiece piece : piecesOfOneType) {
				if (piece.getPosition().equals(targetPosition)) {
					piece.interactWhenSteppingOn();
				}
				
				if (piece.getPosition().equals(startPosition)) {
					piece.interactWhenLeaving();
				}
			}
		}
	}
}