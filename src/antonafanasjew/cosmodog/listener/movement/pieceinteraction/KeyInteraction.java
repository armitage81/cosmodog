package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.camera.CamMovementActionWithConstantSpeed;
import antonafanasjew.cosmodog.actions.popup.PauseAction;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.Door;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

import java.util.List;

public class KeyInteraction extends AbstractPieceInteraction {

	public static final int CAMERA_SPEED_IN_PIXELS_PER_SECOND = 240;

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		InventoryItem keyRingItem = player.getInventory().get(InventoryItemType.KEY_RING);
		
		if (keyRingItem == null) {
			keyRingItem = new KeyRingInventoryItem();
			player.getInventory().put(InventoryItemType.KEY_RING, keyRingItem);
		}
		
		KeyRingInventoryItem keyRing = (KeyRingInventoryItem)player.getInventory().get(InventoryItemType.KEY_RING);
		
		CollectibleKey collectibleKey = (CollectibleKey)piece;
		keyRing.addKey(collectibleKey.getKey());

		Door.DoorType doorType = collectibleKey.getKey().getDoorType();

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		List<Door> doorsForKey = map.getMapPieces().piecesOverall(p -> p instanceof Door door && door.getDoorType() == doorType).stream().map(p -> (Door)p).toList();

		for (Door door : doorsForKey) {

			cosmodogGame
					.getActionRegistry()
					.registerAction(
							AsyncActionType.MOVEMENT,
							new CamMovementActionWithConstantSpeed(CAMERA_SPEED_IN_PIXELS_PER_SECOND, PositionUtils.toPixelPosition(door.getPosition()), cosmodogGame));
			cosmodogGame
					.getActionRegistry()
					.registerAction(AsyncActionType.MOVEMENT,
							new PauseAction(1000));
		}

		if (!doorsForKey.isEmpty()) {
			cosmodogGame
					.getActionRegistry()
					.registerAction(AsyncActionType.MOVEMENT,
							new CamMovementActionWithConstantSpeed(CAMERA_SPEED_IN_PIXELS_PER_SECOND, PositionUtils.toPixelPosition(player.getPosition()), cosmodogGame));
		}
		
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
