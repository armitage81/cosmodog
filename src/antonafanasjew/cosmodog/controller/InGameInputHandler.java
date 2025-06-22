package antonafanasjew.cosmodog.controller;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import antonafanasjew.cosmodog.actions.movement.BlockingAction;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.actions.portals.PortalShotAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.*;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Emp;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.model.portals.Ray;
import antonafanasjew.cosmodog.structures.PortalPuzzle;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.FightAction;
import antonafanasjew.cosmodog.actions.fight.FightFromPlatformAction;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.tooltip.WeaponTooltipAction;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.fighting.AbstractEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimpleEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimplePlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimplePlayerAttackDamageCalculatorUnarmed;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuFrame;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.actions.menu.InGameMenuAction;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

/**
 * Standard game controls for usual actions, like movement, zooming, weapon scrolling.
 * It will not do anything if the action registry is marked with the input blocker flag.
 */
public class InGameInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		//Prepare the globals.
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = cosmodogGame.mapOfPlayerLocation();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();
		CollisionValidator collisionValidator = cosmodog.getCollisionValidatorForPlayer();
		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();

		//Verify that input is allowed.
		if (cosmodogGame.getActionRegistry().inputBlocked()) {
			return;
		}
		
		//Check movement keys
		Input input = gc.getInput();
		
		float oneThirdOfScreenX = gc.getWidth() * 0.33333f;
		float twoThirdOfScreenX = gc.getWidth() * 0.66666f;
		
		float oneThirdOfScreenY = gc.getHeight() * 0.33333f;
		float twoThirdOfScreenY = gc.getHeight() * 0.66666f;
		
		boolean mouseButtonLeft = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);

		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		boolean inputMouseLeft = mouseButtonLeft && mouseX < oneThirdOfScreenX && mouseY > oneThirdOfScreenY && mouseY < twoThirdOfScreenY;
		boolean inputMouseRight = mouseButtonLeft && mouseX > twoThirdOfScreenX && mouseY > oneThirdOfScreenY && mouseY < twoThirdOfScreenY;
		boolean inputMouseUp = mouseButtonLeft && mouseY < oneThirdOfScreenY && mouseX > oneThirdOfScreenX && mouseX < twoThirdOfScreenX;
		boolean inputMouseDown = mouseButtonLeft && mouseY > twoThirdOfScreenY && mouseX > oneThirdOfScreenX && mouseX < twoThirdOfScreenX;
		
		
		boolean inputControllerLeft = input.isControllerLeft(0);
		boolean inputControllerRight = input.isControllerRight(0);
		boolean inputControllerUp = input.isControllerUp(0);
		boolean inputControllerDown = input.isControllerDown(0);
		
		boolean inputKeyboardLeft = input.isKeyDown(Input.KEY_LEFT); 
		boolean inputKeyboardRight = input.isKeyDown(Input.KEY_RIGHT);
		boolean inputKeyboardUp = input.isKeyDown(Input.KEY_UP);
		boolean inputKeyboardDown = input.isKeyDown(Input.KEY_DOWN);

		boolean inputLeft = inputMouseLeft || inputControllerLeft || inputKeyboardLeft;
		boolean inputRight = inputMouseRight || inputControllerRight || inputKeyboardRight;
		boolean inputUp = inputMouseUp || inputControllerUp || inputKeyboardUp;
		boolean inputDown = inputMouseDown || inputControllerDown || inputKeyboardDown;
		
		
		boolean inputMovement = inputLeft || inputRight || inputUp || inputDown;
		
		boolean inputKeyboardSkipTurn = input.isKeyPressed(Input.KEY_ENTER);
		boolean inputMouseSkipTurn = mouseButtonLeft && mouseY > oneThirdOfScreenY && mouseY < twoThirdOfScreenY && mouseX > oneThirdOfScreenX && mouseX < twoThirdOfScreenX;
		
		boolean inputSkipTurn = inputKeyboardSkipTurn || inputMouseSkipTurn;
		
		//Handle skip turn
		if (inputSkipTurn) {
			int timePassed = Constants.MINUTES_PER_TURN;
			int movementDurationFactor = Features.getInstance().featureBoundFunction(Features.FEATURE_FASTRUNNING, () -> Constants.VISIBLE_MOVEMENT_DURATION_FACTOR_WHEN_FASTRUNNING, Constants.VISIBLE_MOVEMENT_DURATION_FACTOR);
			AsyncAction movementAction = new MovementAction(timePassed * movementDurationFactor, true);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MOVEMENT, movementAction);
		}

		//Handle movement
		if (inputMovement) {

			VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			PlatformInventoryItem platformItem = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);

			if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {
				if (vehicleItem != null) {
					vehicleItem.setExiting(true);
				}
				if (platformItem != null) {
					platformItem.setExiting(true);
				}
			}

    		if (inputLeft) {
    			player.turn(DirectionType.LEFT);
    		} else if (inputRight) {
    			player.turn(DirectionType.RIGHT);
    		} else if (inputUp) {
    			player.turn(DirectionType.UP);
    		} else if (inputDown) {
    			player.turn(DirectionType.DOWN);
    		}

			Position startPosition = player.getPosition();
			Entrance targetEntrance = cosmodogGame.targetEntrance(player, player.getDirection());
			Optional<DynamicPiece> optMoveable = cosmodogGame.mapOfPlayerLocation().dynamicPiecesAtPosition(targetEntrance.getPosition()).stream().filter(e -> e instanceof  MoveableDynamicPiece).findFirst();
			Set<DynamicPiece> dynamicPieces = map.dynamicPiecesAtPosition(targetEntrance.getPosition());
			for (DynamicPiece dynamicPiece : dynamicPieces) {
				dynamicPiece.interactBeforeEnteringAttempt();
			}
			if (optMoveable.isPresent()) {
				MoveableDynamicPiece moveableDynamicPiece = (MoveableDynamicPiece) optMoveable.get();
				Entrance moveableTargetEntrance = cosmodogGame.targetEntrance(moveableDynamicPiece.asActor(), targetEntrance.getEntranceDirection());
				dynamicPieces = map.dynamicPiecesAtPosition(moveableTargetEntrance.getPosition());
				for (DynamicPiece dynamicPiece : dynamicPieces) {
					dynamicPiece.interactBeforeEnteringAttempt();
				}
			}

    		//First handle cases when an enemy is standing on the target tile and when the platform would hit enemies. In this case initialize a fight instead of a movement.
    		Set<Enemy> enemies = map.getEnemies();
    		Enemy meleeTargetEnemy = null;
    		Set<Enemy> platformTargetEnemies = new HashSet<>();
    		for (Enemy enemy : enemies) {

    			if (platformItem != null && !platformItem.isExiting()) {
    				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPosition(), targetEntrance.getPosition())) {
    					platformTargetEnemies.add(enemy);
        			}
    			} else {
	    			if (enemy.getPosition().equals(targetEntrance.getPosition())) {
	    				meleeTargetEnemy = enemy;
	    			}
    			}
    		}

    		if (!platformTargetEnemies.isEmpty()) {
    			CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
    			ActionRegistry ar = game.getActionRegistry();
    			ar.registerAction(AsyncActionType.FIGHT_FROM_PLATFORM, new FightFromPlatformAction(platformTargetEnemies));
    		} else if (meleeTargetEnemy != null) {
    			CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
    			ActionRegistry ar = game.getActionRegistry();

    			boolean damageFeatureOn = Features.getInstance().featureOn(Features.FEATURE_DAMAGE);
    			AbstractEnemyAttackDamageCalculator enemyDamageCalculator = damageFeatureOn ? new SimpleEnemyAttackDamageCalculator() : new AbstractEnemyAttackDamageCalculator() {

    				@Override
    				protected int enemyAttackDamageInternal(Enemy enemy, Player player) {
    					return 0;
    				}
    			};

    			ar.registerAction(AsyncActionType.FIGHT, new FightAction(meleeTargetEnemy, new SimplePlayerAttackDamageCalculator(planetaryCalendar), new SimplePlayerAttackDamageCalculatorUnarmed(), enemyDamageCalculator));
    		} else {

	    		CollisionStatus collisionStatus = collisionValidator.collisionStatus(cosmodogGame, player, map, targetEntrance);

				if (collisionStatus.isPassable()) {

					if (vehicleItem != null) {
						if (vehicleItem.isExiting()) {
							Vehicle vehicle = vehicleItem.getVehicle();
							vehicle.setPosition(player.getPosition());
							map.getMapPieces().put(vehicle.getPosition(), vehicle);
							player.getInventory().remove(InventoryItemType.VEHICLE);
							Sound carmotor = applicationContext.getSoundResources().get(SoundResources.SOUND_CARMOTOR);
							if (carmotor.playing()) {
								carmotor.stop();
								Sound motordies = applicationContext.getSoundResources().get(SoundResources.SOUND_MOTOR_DIES);
								motordies.play();
							}
						}
					}

					if (platformItem != null) {

						if (platformItem.isExiting()) {
							Platform platform = platformItem.getPlatform();
							platform.setPosition(player.getPosition());
							map.getMapPieces().put(platform.getPosition(), platform);
							player.getInventory().remove(InventoryItemType.PLATFORM);
						}
					}




					int timePassed = Constants.MINUTES_PER_TURN;
					int movementDurationFactor = Features.getInstance().featureBoundFunction(Features.FEATURE_FASTRUNNING, () -> Constants.VISIBLE_MOVEMENT_DURATION_FACTOR_WHEN_FASTRUNNING, Constants.VISIBLE_MOVEMENT_DURATION_FACTOR);
					AsyncAction movementAction = new MovementAction(timePassed * movementDurationFactor, false);

					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MOVEMENT, movementAction);

				} else {
					if (collisionStatus.getPassageBlockerDescriptor().getPassageBlockerType() == PassageBlockerType.FUEL_EMPTY) {
						Sound carmotor = applicationContext.getSoundResources().get(SoundResources.SOUND_CARMOTOR);
						if (carmotor.playing()) {
							carmotor.stop();
							Sound motordies = applicationContext.getSoundResources().get(SoundResources.SOUND_MOTOR_DIES);
							motordies.play();
						}
					}

					BlockingAction blockingAction = new BlockingAction(Constants.INTERVAL_BETWEEN_COLLISION_NOTIFICATION, player, cosmodogGame, targetEntrance, collisionStatus);
					MovementAttemptAction movementAttemptAction = new MovementAttemptAction(250, targetEntrance.getPosition());
					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.COLLISION_INDICATOR, blockingAction);
					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MOVEMENT_ATTEMPT, movementAttemptAction);

				}
    		}

			if (vehicleItem != null) {
				vehicleItem.setExiting(false);
			}

			if (platformItem != null) {
				platformItem.setExiting(false);
			}

		}

		//Handle weapon scrolling
		if (input.isKeyPressed(Input.KEY_TAB)) {
			Arsenal arsenal = player.getArsenal();

			WeaponType previouslySelectedWeaponType = arsenal.getSelectedWeaponType();
			
			if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {
				arsenal.selectPreviousWeaponType();
			} else {
				arsenal.selectNextWeaponType();
			}
			
			WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
			
			if (selectedWeaponType != null && !selectedWeaponType.equals(previouslySelectedWeaponType)) {
				Weapon weapon = arsenal.getWeaponsCopy().get(selectedWeaponType);
				ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
				AsyncAction previousTooltipAction = actionRegistry.getRegisteredAction(AsyncActionType.WEAPON_TOOLTIP);
				if (previousTooltipAction != null) {
					previousTooltipAction.cancel();
				}
				actionRegistry.registerAction(AsyncActionType.WEAPON_TOOLTIP, WeaponTooltipAction.create(10000, weapon));
			}
			
			applicationContext.getSoundResources().get(SoundResources.SOUND_RELOAD).play();
		}

		//Handle zooming
		
		InventoryItem binoculars = player.getInventory().get(InventoryItemType.BINOCULARS);
		
		if (binoculars != null) {
			if (input.isKeyPressed(Input.KEY_Z)) {
				if (cam.getZoomFactor() != Cam.ZOOM_FACTOR_FAR) {
					cam.zoomOut();
					cam.focusOnPiece(cosmodogGame, 0, 0, player);
				} else {
					cam.zoomIn();
					cam.focusOnPiece(cosmodogGame, 0, 0, player);
				}
			}
			
		}

		if (input.isKeyPressed(Input.KEY_SPACE)) {

			int tileId = map.getTileId(player.getPosition(), Layers.LAYER_META_PORTALS);
			TileType tileType = TileType.getByLayerAndTileId(Layers.LAYER_META_PORTALS, tileId);

			boolean hasPortalGun = player.getInventory().hasItem(InventoryItemType.PORTAL_GUN);
			boolean emittable = tileType.equals(TileType.PORTAL_RAY_EMITTABLE);
			boolean onEmpField = map.dynamicPieceAtPosition(Emp.class, player.getPosition()).isPresent();

			if (hasPortalGun && emittable && !onEmpField) {
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.CUTSCENE, new PortalShotAction());
			} else {
				ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_JAMMED).play();
			}
		}

		//TODO: This is a test for plane change. Remove it later.
		if (input.isKeyPressed(Input.KEY_T)) {
			MapType currentMapType = player.getPosition().getMapType();
			int next = (Arrays.stream(MapType.values()).toList().indexOf(currentMapType) + 1) % MapType.values().length;
			player.setPosition(Position.fromCoordinates(player.getPosition().getX(), player.getPosition().getY(), Arrays.stream(MapType.values()).toList().get(next)));
			player.endSwitchingPlane();
		}

		//Handle debugging
		DebuggerInventoryItem debugger = (DebuggerInventoryItem)player.getInventory().get(InventoryItemType.DEBUGGER);
		
		if (debugger != null) {

			if (input.isKeyPressed(Input.KEY_1)) {
				Position position;
				if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {
					position = debugger.firstPosition();
				} else {
					position = debugger.nextPosition();
				}
				player.setPosition(position);
				player.shiftHorizontal(0); //Just to trigger the listeners.
				cam.focusOnPiece(cosmodogGame,0, 0, player);
			}
			
			if (input.isKeyPressed(Input.KEY_2)) {
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
							
			}

			if (input.isKeyPressed(Input.KEY_5)) {
				cosmodogGame.getPlanetaryCalendar().addMinutes(60);
			}

			if (input.isKeyPressed(Input.KEY_9)) {
				debugger.setPositionDisplayed(!debugger.isPositionDisplayed());
			}
			
		}
		
		//Handle in-game menu
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.INVENTORY_INGAME_MENU_FRAME, InGameMenuFrame.MAP_INGAME_MENU_FRAME, InGameMenuFrame.LOG_PLAYER_INGAME_MENU_FRAME, InGameMenuFrame.PROGRESS_INGAME_MENU_FRAME, InGameMenuFrame.OPTIONS_INGAME_MENU_FRAME)));
		}
		
		//Handle inventory
		if (input.isKeyPressed(Input.KEY_I)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.INVENTORY_INGAME_MENU_FRAME)));
		}
		
		//Handle map
		if (input.isKeyPressed(Input.KEY_M)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.MAP_INGAME_MENU_FRAME)));
		}
		
		//Handle log player
		if (input.isKeyPressed(Input.KEY_L)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.LOG_PLAYER_INGAME_MENU_FRAME)));
		}
		
		//Handle game progress
		if (input.isKeyPressed(Input.KEY_P)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.PROGRESS_INGAME_MENU_FRAME)));
		}
		
		//Handle help
		if (input.isKeyPressed(Input.KEY_H)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.HELP_INGAME_MENU_FRAME)));
		}
		
		//Handle save and quit
		if (input.isKeyPressed(Input.KEY_Q)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new InGameMenuAction(new InGameMenu(InGameMenuFrame.OPTIONS_INGAME_MENU_FRAME)));
		}
		
		if (input.isKeyPressed(Input.KEY_R)) {
			MoveableGroup.resetMoveableGroup(cosmodogGame);
			PortalPuzzle.resetPortalPuzzle(cosmodogGame);

		}

	}

}
