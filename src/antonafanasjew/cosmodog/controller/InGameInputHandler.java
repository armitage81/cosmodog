package antonafanasjew.cosmodog.controller;

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
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.tooltip.WeaponTooltipAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.fighting.SimpleEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.SimplePlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
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
		CollisionValidator collisionValidator = cosmodog.getCollisionValidator();

		//Verify that input is allowed.
		if (cosmodogGame.getActionRegistry().inputBlocked()) {
			return;
		}
		
		//Check movement keys
		Input input = gc.getInput();
		
		boolean inputLeft = input.isKeyDown(Input.KEY_LEFT); 
		boolean inputRight = input.isKeyDown(Input.KEY_RIGHT);
		boolean inputUp = input.isKeyDown(Input.KEY_UP);
		boolean inputDown = input.isKeyDown(Input.KEY_DOWN);
		
		boolean inputMovement = inputLeft || inputRight || inputUp || inputDown;
		
		//Handle movement or skip a turn
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
    		
    		
    		//First handle the case when an enemy is standing on the target tile. In this case initialize a fight instead of a movement.
    		Set<Enemy> enemies = map.getEnemies();
    		Enemy targetEnemy = null;
    		for (Enemy enemy : enemies) {
    			if (enemy.getPositionX() == newX && enemy.getPositionY() == newY) {
    				targetEnemy = enemy;
    			}
    		}
    		
    		if (targetEnemy != null) {
    			CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
    			ActionRegistry ar = game.getActionRegistry();
    			ar.registerAction(AsyncActionType.FIGHT, new FightAction(new SimplePlayerAttackDamageCalculator(), new SimpleEnemyAttackDamageCalculator()));
    		} else {
    		
	    		CollisionStatus collisionStatus = collisionValidator.collisionStatus(cosmodogGame, player, map, newX, newY);
	    		
				if (collisionStatus.isPassable()) {
					
					if (vehicleItem != null) {
						if (vehicleItem.isExiting()) {
							Vehicle vehicle = vehicleItem.getVehicle();
							vehicle.setPositionX(player.getPositionX());
							vehicle.setPositionY(player.getPositionY());
							map.getMapPieces().add(vehicle);
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
							map.getMapPieces().add(platform);
							player.getInventory().remove(InventoryItemType.PLATFORM);
						}
					}
					
					int timePassed = cosmodog.getTravelTimeCalculator().calculateTravelTime(applicationContext, player, newX, newY);
					AsyncAction movementAction = new MovementAction(timePassed * Constants.VISIBLE_MOVEMENT_DURATION_FACTOR);
					
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
							applicationContext.getSoundResources().get(SoundResources.SOUND_NOWAY).play();
							
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
			ArsenalInventoryItem arsenal = (ArsenalInventoryItem)player.getInventory().get(InventoryItemType.ARSENAL);

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
				cam.zoomIn();
				cam.focusOnPiece(map, 0, 0, player);
			}
			
			if (input.isKeyPressed(Input.KEY_Y)) {
				cam.focusOnPiece(map, 0, 0, player);
				cam.zoomOut();
			}
		
		}

	}

}
