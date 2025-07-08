package antonafanasjew.cosmodog.actions.movement;

import java.io.Serial;
import java.util.*;

import antonafanasjew.cosmodog.actions.camera.CamMovementAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.util.*;
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
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.topology.Position;

public class MovementAction extends FixedLengthAsyncAction {
	

	@Serial
	private static final long serialVersionUID = -693412142092974821L;

    private Position startPosition;
	private Entrance targetEntrance;

	private MoveableDynamicPiece moveableDynamicPiece;
	private Entrance moveableTargetEntrance;

	private final Map<Enemy, MovementActionResult> enemyMovementActionResults = Maps.newHashMap();

	private Map<Actor, CrossTileMotion> actorMotions = Maps.newHashMap();

	private final boolean skipTurn;

	public MovementAction(int duration, boolean skipTurn) {
		super(duration);
		this.skipTurn = skipTurn;
	}

	@Override
	public void onTrigger() {
		initMovementActionResults();
		onTriggerForMoveable();
		onTriggerForPlayer();
		onTriggerForEnemies();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		onUpdateForMoveable(after);
		onUpdateForPlayer(after);
		onUpdateForEnemies(after);
		
	}

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

	@Override
	public void afterUnregistration() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		AsyncAction waiterAction = new FixedLengthAsyncAction(Constants.INTERVAL_BETWEEN_TURNS);
		cosmodogGame.getActionRegistry().registerAction(AsyncActionType.WAIT, waiterAction);
	}

	private void initMovementActionResults() {
		//Preparing static data.
		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap cosmodogMap = game.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		Position playerPosition = player.getPosition();

        //Position before movement
        startPosition = player.getPosition();

		//Calculating the target entrance.
		targetEntrance = skipTurn ?
				Entrance.instanceForWaiting(playerPosition, player.getDirection())
				:
				game.targetEntrance(player, player.getDirection());
		
		//There could be a moveable block in player's path. In this case, it also would be moved (collision has been checked already.)
		//Take care: Player's movement has to be calculated before this.

		Optional<DynamicPiece> optMoveable = cosmodogMap.dynamicPiecesAtPosition(targetEntrance.getPosition()).stream().filter(e -> e instanceof  MoveableDynamicPiece).findFirst();
		if (optMoveable.isPresent()) {
			this.moveableDynamicPiece = (MoveableDynamicPiece) optMoveable.get();
			moveableTargetEntrance = game.targetEntrance(moveableDynamicPiece.asActor(), targetEntrance.getEntranceDirection());
		}

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

	private MovementActionResult calculateEnemyMovementResult(Enemy enemy) {
		
		//Preparing static data.
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		PathFinder pathFinder = cosmodog.getPathFinder();
		
		//Preparing enemy collision validator
		CollisionValidator collisionValidator = GeneralCollisionValidatorForNpc.instance(targetEntrance, moveableTargetEntrance, enemyMovementActionResults);
		
		//Using the pathfinder to calculate the movement result based on the time budget generated from user movement and enemy's time overhead from the previous turns.
		int costBudget = Constants.MINUTES_PER_TURN * enemy.getSpeedFactor();
		return pathFinder.calculateMovementResult(enemy, costBudget, collisionValidator, targetEntrance);

	}

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

			Set<Enemy> destroyedEnemies = Sets.newHashSet();
			
			for (Enemy enemy : enemyMovementActionResults.keySet()) {
				MovementActionResult enemyMovementActionResult = enemyMovementActionResults.get(enemy);
				
				int pathLength = enemyMovementActionResult.getPath().getLength();
				int enemyX = enemyMovementActionResult.getPath().getX(pathLength - 1);
				int enemyY = enemyMovementActionResult.getPath().getY(pathLength - 1);

				Position enemyPosition = Position.fromCoordinatesOnPlayerLocationMap(enemyX, enemyY);

				if (CosmodogMapUtils.isTileOnPlatform(enemyPosition, targetEntrance.getPosition())) {
					destroyedEnemies.add(enemy);
				}
			}
			
			for (Enemy enemy : map.allEnemies()) {
				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPosition(), targetEntrance.getPosition())) {
					destroyedEnemies.add(enemy);
				}
			}

			for (Enemy enemy : destroyedEnemies) {
				map.getMapPieces().removePiece(enemy);
			}

		} else {
			if (!skipTurn) {
				if (targetEntrance.isUsedPortal()) {
					applicationContext.getSoundResources().get(SoundResources.SOUND_PORTALS_TELEPORTED).play();
				} else {
					String footStepsSoundType = FootstepUtils.footStepsSoundType(targetEntrance.getPosition());
					applicationContext.getSoundResources().get(footStepsSoundType).play();
				}
			}
		}
	}

	private void onTriggerForMoveable() {
		if (moveableTargetEntrance != null) {
			if (moveableTargetEntrance.isUsedPortal()) {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_TELEPORTED).play();
			} else {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CAR_MOVES).play();
			}
		}
	}

	private void onTriggerForEnemies() {

	}

	private void onUpdateForPlayer(int timePassed) {
		
		if (targetEntrance.isWaited()) {
			return;
		}

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		
		Cam cam = cosmodogGame.getCam();

		int tileLength = TileUtils.tileLengthSupplier.get();
		
		float movementOffsetX = 0;
		float movementOffsetY = 0;
		

		//Take care: This works only because the tiles are square.
		float ratio = (float)timePassed / getDuration();
		ratio = Math.min(ratio, 1.0f);
		float offset = tileLength * ratio;
		boolean verticalNotHorizontal = true;
		boolean positiveNotNegative = true;

		Player player = ApplicationContextUtils.getPlayer();
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
		Position targetPosition = targetEntrance.getPosition();

		CrossTileMotion crossTileMotion;

		if (targetEntrance.isUsedPortal()) {
			crossTileMotion = CrossTileMotion.fromActorWhenUsingPortal(player, targetPosition, targetEntrance.getEntrancePortal(), targetEntrance.getExitPortal());
		} else {
			crossTileMotion = CrossTileMotion.fromActor(player, targetPosition);
		}

		if (verticalNotHorizontal) {
			crossTileMotion.setCrossTileOffsetY(positiveNotNegative ? ratio : -ratio);
		} else {
			crossTileMotion.setCrossTileOffsetX(positiveNotNegative ? ratio : -ratio);
		}

		actorMotions.put(player, crossTileMotion);

		if (!targetEntrance.isUsedPortal()) {
			cam.focusOnPiece(cosmodogGame, movementOffsetX, movementOffsetY, player);
		}
	}

	private void onUpdateForMoveable(int timePassed) {

		if (skipTurn) {
			return;
		}

		if (moveableTargetEntrance == null) {
			return;
		}

		Position position = moveableDynamicPiece.getPosition();
		Position targetPosition = moveableTargetEntrance.getPosition();

		float ratio = (float)timePassed / getDuration();
		ratio = Math.min(ratio, 1.0f);

		boolean verticalNotHorizontal;
		boolean positiveNotNegative;

		if (targetPosition.getY() > position.getY()) {
			verticalNotHorizontal = true;
			positiveNotNegative = true;
		} else if (targetPosition.getY() < position.getY()) {
			verticalNotHorizontal = true;
			positiveNotNegative = false;
		} else if (targetPosition.getX() < position.getX()) {
			verticalNotHorizontal = false;
			positiveNotNegative = false;
		} else {
			verticalNotHorizontal = false;
			positiveNotNegative = true;
		}

		boolean usingPortal = moveableTargetEntrance.isUsedPortal();
		CrossTileMotion moveableCrossTileMotion;
		if (usingPortal) {
			moveableCrossTileMotion = CrossTileMotion.fromActorWhenUsingPortal(moveableDynamicPiece.asActor(), targetPosition, moveableTargetEntrance.getEntrancePortal(), moveableTargetEntrance.getExitPortal());
		} else {
			moveableCrossTileMotion = CrossTileMotion.fromActor(moveableDynamicPiece.asActor(), targetPosition);
		}

		if (verticalNotHorizontal) {
			moveableCrossTileMotion.setCrossTileOffsetY(positiveNotNegative ? ratio : -ratio);
		} else {
			moveableCrossTileMotion.setCrossTileOffsetX(positiveNotNegative ? ratio : -ratio);
		}
		actorMotions.put(moveableDynamicPiece.asActor(), moveableCrossTileMotion);
	}

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

			int lastMidwayPosX = path.getX(movedDistanceInTiles);
			int lastMidwayPosY = path.getY(movedDistanceInTiles);

			//The starting position of the movement is stored in the path at index = 0.
			//While the actor still moves from its starting position tile, the position at index = 1 of the path is taken as target.
			//While the actor moves from the first to the second tile, the position at index = 2 of the path is taken as target.
			//etc.
			int nextMidwayPosX = path.getX(movedDistanceInTiles + 1);
			int nextMidwayPosY = path.getY(movedDistanceInTiles + 1);

			Position lastMidwayPosition = Position.fromCoordinatesOnPlayerLocationMap(lastMidwayPosX, lastMidwayPosY);

			CrossTileMotion enemyCrossTileMotion = actorMotions.get(enemy);

			CrossTileMotion crossTileMotion = CrossTileMotion.fromActor(enemy, lastMidwayPosition);
			crossTileMotion.setLastMidwayPosition(lastMidwayPosition);

			if (lastMidwayPosX < nextMidwayPosX) {
				crossTileMotion.setCrossTileOffsetX(ratioForNextStep);
			} else if (lastMidwayPosX > nextMidwayPosX) {
				crossTileMotion.setCrossTileOffsetX(-ratioForNextStep);
			} else if (lastMidwayPosY < nextMidwayPosY) {
				crossTileMotion.setCrossTileOffsetY(ratioForNextStep);
			} else if (lastMidwayPosY > nextMidwayPosY) {
				crossTileMotion.setCrossTileOffsetY(-ratioForNextStep);
			}

			actorMotions.put(enemy, crossTileMotion);

		}
	}

	private void onEndForPlayer() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Player player = ApplicationContextUtils.getPlayer();

		actorMotions.remove(player);

		if (targetEntrance.isWaited()) {
			player.skipTurn();
			return;
		}
		
		boolean playerInPlatform = player.getInventory().hasPlatform();

		if (targetEntrance.isUsedPortal()) {

			player.transport(targetEntrance);


			int tileLength = TileUtils.tileLengthSupplier.get();

			Position playerPixelPosition = Position.fromCoordinates(
					player.getPosition().getX() * tileLength + tileLength / 2.0f,
					player.getPosition().getY() * tileLength + tileLength / 2.0f,
					player.getPosition().getMapType());

			CamMovementAction camMovementAction = new CamMovementAction(250, playerPixelPosition, cosmodogGame);
			ActionRegistry ar = cosmodogGame.getActionRegistry();
			ar.registerAction(AsyncActionType.FOCUSING_ON_PLAYER_AFTER_USING_PORTAL, camMovementAction);

		} else {

			if (playerInPlatform) {

				Map<Position, Piece> oldPositionsForPiecesOnPlatform = Maps.newHashMap();

				for (Piece piece : cosmodogGame.mapOfPlayerLocation().getMapPieces().piecesOverall(e -> true)) {
					if (CosmodogMapUtils.isTileOnPlatform(piece.getPosition(), player.getPosition())) {

						//As we move the piece on the platform, we need to update it in the mapValues cache
						Position position = piece.getPosition();
						oldPositionsForPiecesOnPlatform.put(position, piece);

						if (player.getDirection() == DirectionType.UP || player.getDirection() == DirectionType.DOWN) {
							cosmodogGame.mapOfPlayerLocation().getMapPieces().removePiece(piece);
							piece.getPosition().shift(0, player.getDirection() == DirectionType.UP ? -1 : 1);
							cosmodogGame.mapOfPlayerLocation().getMapPieces().addPiece(piece);
						} else {
							cosmodogGame.mapOfPlayerLocation().getMapPieces().removePiece(piece);
							piece.getPosition().shift(player.getDirection() == DirectionType.LEFT ? -1 : 1, 0);
							cosmodogGame.mapOfPlayerLocation().getMapPieces().addPiece(piece);
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

	}

	private void onEndForMoveable() {
		
		if (moveableTargetEntrance != null) {

			if (!moveableTargetEntrance.getPosition().equals(moveableDynamicPiece.getPosition())) { //The start and the target positions can be equal in case the moveable does not move.
				
				CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
				
				actorMotions.remove(moveableDynamicPiece.asActor());
				
				moveableDynamicPiece.setPosition(moveableTargetEntrance.getPosition());

			}
		}
	}

	private void onEndForEnemies() {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		Set<Enemy> enemies = map.allEnemies();
		for (Enemy enemy : enemies) {
						
			CrossTileMotion enemyCrossTileMotion = actorMotions.remove(enemy);
			MovementActionResult movementActionResult = enemyMovementActionResults.get(enemy);
			if (movementActionResult != null) {
    			Path path = movementActionResult.getPath();
    			int lastPathPosX = path.getX(path.getLength() - 1);
    			int lastPathPosY = path.getY(path.getLength() - 1);
				Position lastPathPosition = Position.fromCoordinatesOnPlayerLocationMap(lastPathPosX, lastPathPosY);
    			enemy.setPosition(lastPathPosition);
    			if (enemyCrossTileMotion != null) {
    				enemy.setDirection(enemyCrossTileMotion.getMotionDirection());
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

	private void onEndForDynamicPieces() {

		Player player = ApplicationContextUtils.getPlayer();

		Position targetPosition = targetEntrance.getPosition();
		
		boolean noMovement = targetEntrance.isWaited();

		if (noMovement) {
			return;
		}

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		List<DynamicPiece> dynamicPiecesAtStartPosition = map
				.getMapPieces()
				.piecesAtPosition(e -> e instanceof DynamicPiece, startPosition.getX(), startPosition.getY())
				.stream()
				.map(e -> (DynamicPiece)e)
				.toList();

		List<DynamicPiece> dynamicPiecesAtTargetPosition = map
				.getMapPieces()
				.piecesAtPosition(e -> e instanceof DynamicPiece, targetPosition.getX(), targetPosition.getY())
				.stream()
				.map(e -> (DynamicPiece)e)
				.toList();

		dynamicPiecesAtStartPosition.forEach(DynamicPiece::interactWhenLeaving);
		dynamicPiecesAtTargetPosition.forEach(DynamicPiece::interactWhenSteppingOn);
		dynamicPiecesAtStartPosition.forEach(DynamicPiece::interactAfterExiting);

	}

	public Map<Actor, CrossTileMotion> getActorMotions() {
		return actorMotions;
	}
}