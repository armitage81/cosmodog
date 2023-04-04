package antonafanasjew.cosmodog.controller;

import java.util.HashSet;
import java.util.Set;

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
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.rules.actions.async.InGameMenuAction;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.view.transitions.MovementAttemptTransition;

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
		CosmodogMap map = cosmodogGame.getMap();
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
			AsyncAction movementAction = new MovementAction(timePassed * Constants.VISIBLE_MOVEMENT_DURATION_FACTOR, true);
			cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MOVEMENT, movementAction);
		}
		
		
		//Handle movement
		if (inputMovement) {

			VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			PlatformInventoryItem platformItem = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);
			
			if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {				if (vehicleItem != null) {
					vehicleItem.setExiting(true);
				}
				if (platformItem != null) {
					platformItem.setExiting(true);
				}
			}
			
			
    		if (inputLeft) {
    			player.setDirection(DirectionType.LEFT);
    		} else if (inputRight) {
    			player.setDirection(DirectionType.RIGHT);
    		} else if (inputUp) {
    			player.setDirection(DirectionType.UP);
    		} else if (inputDown) {
    			player.setDirection(DirectionType.DOWN);
    		} 
    		
    		int newX = player.getPositionX();
    		int newY = player.getPositionY();
    		
    		if (player.getDirection() == DirectionType.LEFT) {
    			newX = player.getPositionX() - 1;
    		} else if (player.getDirection() == DirectionType.RIGHT) {
    			newX = player.getPositionX() + 1;
    		} else if (player.getDirection() == DirectionType.UP) {
    			newY = player.getPositionY() - 1;
    		} else if (player.getDirection() == DirectionType.DOWN) {
    			newY = player.getPositionY() + 1;
    		} 
    		  		
    		//First handle cases when an enemy is standing on the target tile and when the platform would hit enemies. In this case initialize a fight instead of a movement.
    		Set<Enemy> enemies = map.getEnemies();
    		Enemy meleeTargetEnemy = null;
    		Set<Enemy> platformTargetEnemies = new HashSet<>();
    		for (Enemy enemy : enemies) {
    			
    			if (platformItem != null && !platformItem.isExiting()) {
    				if (CosmodogMapUtils.isTileOnPlatform(enemy.getPositionX(), enemy.getPositionY(), newX, newY)) {
    					platformTargetEnemies.add(enemy);
        			}
    			} else {
	    			if (enemy.getPositionX() == newX && enemy.getPositionY() == newY) {
	    				meleeTargetEnemy = enemy;
	    			}
    			}
    		}
    		
    		if (platformTargetEnemies.isEmpty() == false) {
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
    		
	    		CollisionStatus collisionStatus = collisionValidator.collisionStatus(cosmodogGame, player, map, newX, newY);
	    		
				if (collisionStatus.isPassable()) {
					
					if (vehicleItem != null) {
						if (vehicleItem.isExiting()) {
							Vehicle vehicle = vehicleItem.getVehicle();
							vehicle.setPositionX(player.getPositionX());
							vehicle.setPositionY(player.getPositionY());
							map.getMapPieces().put(Position.fromPiece(vehicle), vehicle);
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
							platform.setPositionX(player.getPositionX());
							platform.setPositionY(player.getPositionY());
							map.getMapPieces().put(Position.fromPiece(platform), platform);
							player.getInventory().remove(InventoryItemType.PLATFORM);
						}
					}
					
					
					int timePassed = Constants.MINUTES_PER_TURN;
					AsyncAction movementAction = new MovementAction(timePassed * Constants.VISIBLE_MOVEMENT_DURATION_FACTOR, false);
					
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
					
					final int finalNewX = newX;
					final int finalNewY = newY;
					
					AsyncAction blockingAction = new FixedLengthAsyncAction(Constants.INTERVAL_BETWEEN_COLLISION_NOTIFICATION) {
	
						private static final long serialVersionUID = 1663061093630885138L;
						
						@Override
						public void onTrigger() {
							
							
							DynamicPiece dynamicPiece = map.dynamicPieceAtPosition(finalNewX, finalNewY); 
							if (dynamicPiece == null) { //Otherwise, the dynamic piece interact method should handle the sound.
								applicationContext.getSoundResources().get(SoundResources.SOUND_NOWAY).play();
							}
							
							String text = collisionStatus.getPassageBlockerDescriptor().asText();
							
							OverheadNotificationAction.registerOverheadNotification(player, text);
							
						}
						
					};
					
					AsyncAction movementAttemptAction = new FixedLengthAsyncAction(250) {

						private static final long serialVersionUID = 1663061093630885138L;
						
						private boolean interactedWithDynamicPieceAlready = false;
						
						@Override
						public void onTrigger() {
							MovementAttemptTransition movementAttemptTransition = new MovementAttemptTransition();
							cosmodogGame.setMovementAttemptTransition(movementAttemptTransition);
						}
						
						@Override
						public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
							float fracture = (float)after / (float)getDuration();
							fracture = fracture > 1 ? 1 : fracture;
							cosmodogGame.getMovementAttemptTransition().completion = fracture;
							
							if (fracture >= 0.5f && interactedWithDynamicPieceAlready == false) {
								//Now handle the case of interacting with dynamic pieces (e.g. destroying a stone)
								//BTW, this part is not entirely correct, as interaction with dynamic pieces will happen only in 
								//case if they are blocking passage (e.g. not destroyed stones)
								//But what if we want to interact with passable dynamic pieces (e.g. add a poisoned sound to the poison spots)
								DynamicPiece dynamicPiece = map.dynamicPieceAtPosition(finalNewX, finalNewY);
								if (dynamicPiece != null) {
									dynamicPiece.interact();
								}
								interactedWithDynamicPieceAlready = true;
							}
							
							
						}
						
						@Override
						public void onEnd() {
							cosmodogGame.setMovementAttemptTransition(null);
						}
						
					};
					
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
		
			if (input.isKeyDown(Input.KEY_Z)) {
				if (cam.getZoomFactor() != Cam.ZOOM_FACTOR_FAR) {
					cam.zoomOut();
					cam.focusOnPiece(map, 0, 0, player);
				}
			} else {
				if (cam.getZoomFactor() != Cam.ZOOM_FACTOR_CLOSE) {
					cam.zoomIn();
					cam.focusOnPiece(map, 0, 0, player);
				}
			}
			
		}
		
		//Handle debugging
		DebuggerInventoryItem debugger = (DebuggerInventoryItem)player.getInventory().get(InventoryItemType.DEBUGGER);
		
		if (debugger != null) {
			
			if (input.isKeyPressed(Input.KEY_1)) {
				DebuggerInventoryItem.PlayerPosition playerPosition = debugger.nextMonolithPosition();
				player.setPositionX(playerPosition.x);
				player.setPositionY(playerPosition.y);
				cam.focusOnPiece(map, 0, 0, player);
			}
			
			if (input.isKeyPressed(Input.KEY_2)) {
				DebuggerInventoryItem.PlayerPosition playerPosition = debugger.nextCutscenePosition();
				player.setPositionX(playerPosition.x);
				player.setPositionY(playerPosition.y);
				cam.focusOnPiece(map, 0, 0, player);
			}
			
			if (input.isKeyPressed(Input.KEY_3)) {
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
				player.getGameProgress().addInfobank();
							
			}
			
			if (input.isKeyPressed(Input.KEY_9)) {
				debugger.setPositionDisplayed(!debugger.isPositionDisplayed());
			}
			
		}
		
		//Handle in-game menu
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.INVENTORY_INGAME_MENU_FRAME, InGameMenuFrame.MAP_INGAME_MENU_FRAME, InGameMenuFrame.LOG_PLAYER_INGAME_MENU_FRAME, InGameMenuFrame.PROGRESS_INGAME_MENU_FRAME, InGameMenuFrame.OPTIONS_INGAME_MENU_FRAME)));
		}
		
		//Handle inventory
		if (input.isKeyPressed(Input.KEY_I)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.INVENTORY_INGAME_MENU_FRAME)));
		}
		
		//Handle map
		if (input.isKeyPressed(Input.KEY_M)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.MAP_INGAME_MENU_FRAME)));
		}
		
		//Handle log player
		if (input.isKeyPressed(Input.KEY_L)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.LOG_PLAYER_INGAME_MENU_FRAME)));
		}
		
		//Handle game progress
		if (input.isKeyPressed(Input.KEY_P)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.PROGRESS_INGAME_MENU_FRAME)));
		}
		
		//Handle help
		if (input.isKeyPressed(Input.KEY_H)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.HELP_INGAME_MENU_FRAME)));
		}
		
		//Handle save and quit
		if (input.isKeyPressed(Input.KEY_Q)) {
			ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new InGameMenuAction(new InGameMenu(InGameMenuFrame.OPTIONS_INGAME_MENU_FRAME)));
		}
		
		if (input.isKeyPressed(Input.KEY_R)) {
			MoveableGroup.resetMoveableGroup(cosmodogGame);
			OverheadNotificationAction.registerOverheadNotification(player, "Reset");
			
		}

	}

}
