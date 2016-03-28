package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;

/**
 * Standard game controls for movement, zooming.
 */
public class InGameInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
		CollisionValidator collisionValidator = cosmodog.getCollisionValidator();

		cosmodogGame.getActionRegistry().update(delta, gc, sbg);
		
		boolean blocked = false;
		Input input = gc.getInput();
		
		boolean inputLeft = input.isKeyDown(Input.KEY_LEFT); 
		boolean inputRight = input.isKeyDown(Input.KEY_RIGHT);
		boolean inputUp = input.isKeyDown(Input.KEY_UP);
		boolean inputDown = input.isKeyDown(Input.KEY_DOWN);
		
		boolean inputMovement = inputLeft || inputRight || inputUp || inputDown;
		
		if (inputMovement && cosmodogGame.getActionRegistry().inputBlocked() == false) {

			boolean playerWasInVehicle = player.getInventory().hasVehicle();
			
			VehicleInventoryItem vehicleItem = (VehicleInventoryItem)player.getInventory().get(InventoryItem.INVENTORY_ITEM_VEHICLE);
			
			if (input.isKeyDown(Input.KEY_LSHIFT) || input.isKeyDown(Input.KEY_RSHIFT)) {				if (vehicleItem != null) {
					vehicleItem.setExiting(true);
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
    		
    		
    		CollisionStatus collisionStatus = collisionValidator.collisionStatus(cosmodogGame, player, tiledMap, newX, newY);
    		
			if (collisionStatus.isPassable()) {
				
				if (vehicleItem != null) {
					if (vehicleItem.isExiting()) {
						Vehicle vehicle = vehicleItem.getVehicle();
						vehicle.setPositionX(player.getPositionX());
						vehicle.setPositionY(player.getPositionY());
						cosmodogMap.getMapPieces().add(vehicle);
						player.getInventory().remove(InventoryItem.INVENTORY_ITEM_VEHICLE);
						Sound carmotor = applicationContext.getSoundResources().get(SoundResources.SOUND_CARMOTOR);
						if (carmotor.playing()) {
							carmotor.stop();
							Sound motordies = applicationContext.getSoundResources().get(SoundResources.SOUND_MOTOR_DIES);
							motordies.play();
						}
					}
				}
				
				int timePassed = cosmodog.getTravelTimeCalculator().calculateTravelTime(applicationContext, player, newX, newY);
				AsyncAction movementAction = new MovementAction(timePassed * Constants.VISIBLE_MOVEMENT_DURATION_FACTOR);
				
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.MOVEMENT, movementAction);
				
			} else {
				
				if (collisionStatus.getNoPassageReason() == CollisionStatus.NO_PASSAGE_REASON_FUEL_EMPTY) {
					Sound carmotor = applicationContext.getSoundResources().get(SoundResources.SOUND_CARMOTOR);
					if (carmotor.playing()) {
						carmotor.stop();
						Sound motordies = applicationContext.getSoundResources().get(SoundResources.SOUND_MOTOR_DIES);
						motordies.play();
					}
				}
				
				AsyncAction blockingAction = new FixedLengthAsyncAction(Constants.INTERVAL_BETWEEN_COLLISION_NOTIFICATION) {

					private static final long serialVersionUID = 1663061093630885138L;
					
					@Override
					public void onTrigger() {
						applicationContext.getSoundResources().get(SoundResources.SOUND_NOWAY).play();
					}
					
				};
				
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.COLLISION_INDICATOR, blockingAction);
			}

			if (vehicleItem != null) {
				vehicleItem.setExiting(false);
			}

		}
		


		if (input.isKeyPressed(Input.KEY_Z)) {
			cam.zoomIn();
			cam.focusOnPiece(tiledMap, 0, 0, player, Cam.DEFAULT_FOCUS_PADDING);
		} else if (input.isKeyPressed(Input.KEY_Y)) {
			cam.focusOnPiece(tiledMap, 0, 0, player, Cam.DEFAULT_FOCUS_PADDING);
			cam.zoomOut();
		}

	}

}
