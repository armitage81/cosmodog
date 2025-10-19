package antonafanasjew.cosmodog.structures;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.RegionUtils;

/**
 * Moveable objects on the map, such as boxes, can be pushed by the player, but not pulled.
 * 
 * By pushing objects, the player can enter a deadlock state when it is not possible to move a moveable object.
 * This can happen when a box is pushed into a corner.
 * 
 * We don't want such deadlocks to be permanent, so in each puzzle with moveables, there will be a button which resets
 * the moveable objects to their original places.
 * 
 * This approach is only possible, if we know which moveable objects belong to the same set.
 * We group them via the tiled map regions in the object group 'MoveableSets'.
 * 
 * Each of the moveable sets has a region the set is assigned to, a set of moveables including their original positions
 * and a reset button.
 * 
 * This class represents such a structure.
 * 
 */
public class MoveableGroup implements Serializable {
	
	private static final long serialVersionUID = 2146440333970844578L;
	private TiledObject region;
	private List<MoveableDynamicPiece> moveables = Lists.newArrayList();
	private List<Position> originalPositions = Lists.newArrayList();
	private List<Position> goalPositions = Lists.newArrayList();
	private Position playerStartPosition;
	private List<SecretDoor> secretDoors = Lists.newArrayList();
	private boolean solvedStatus = false;
	private boolean resetable = false;
	private boolean deactivateEnemies = true;
	
	
	public TiledObject getRegion() {
		return region;
	}
	
	public void setRegion(TiledObject region) {
		this.region = region;
	}
	
	public List<MoveableDynamicPiece> getMoveables() {
		return moveables;
	}
	
	public List<Position> getOriginalPositions() {
		return originalPositions;
	}
	
	public List<Position> getGoalPositions() {
		return goalPositions;
	}
	
	public Position getPlayerStartingPosition() {
		return playerStartPosition;
	}
	
	public void setPlayerStartPosition(Position playerStartPosition) {
		this.playerStartPosition = playerStartPosition;
	}
	
	public List<SecretDoor> getSecretDoors() {
		return secretDoors;
	}

	public boolean isResetable() {
		return resetable;
	}

	public void setResetable(boolean resetable) {
		this.resetable = resetable;
	}

	public boolean solved() {
		List<Position> moveablePositions = moveables
				.stream()
				.map(Piece::getPosition)
				.toList()
		;
		
		return moveablePositions.containsAll(goalPositions);
	}
	
	public static void resetMoveableGroup(CosmodogGame game) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		Player player = game.getPlayer();
		CosmodogMap map = game.mapOfPlayerLocation();
		Cam cam = game.getCam();
		MoveableGroup moveableGroupAroundPlayer = null;
		List<MoveableGroup> moveableGroups = map.getMoveableGroups();
		for (MoveableGroup moveableGroup : moveableGroups) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), moveableGroup.getRegion())) {
				moveableGroupAroundPlayer = moveableGroup;
				break;
			}
		}
		
		if (moveableGroupAroundPlayer != null) {

			if (!moveableGroupAroundPlayer.isResetable()) {
				return;
			}

			if (moveableGroupAroundPlayer.solved()) {
				return;
			}

			OverheadNotificationAction.registerOverheadNotification(player, "Reset");

			for (int i = 0; i < moveableGroupAroundPlayer.moveables.size(); i++) {
				MoveableDynamicPiece moveable = moveableGroupAroundPlayer.getMoveables().get(i);
				Position originalPosition = moveableGroupAroundPlayer.getOriginalPositions().get(i);
				moveable.setPosition(originalPosition);
			}
			player.beginTeleportation();
			Position playerStartingPosition = moveableGroupAroundPlayer.getPlayerStartingPosition();
			player.setPosition(playerStartingPosition);
			player.endTeleportation();
			cam.focusOnPiece(game,0, 0, player);
		}
	}

	public void setSolvedStatus(boolean solvedStatus) {
		this.solvedStatus = solvedStatus;
	}
	
	public boolean isSolvedStatus() {
		return solvedStatus;
	}

	public void setDeactivateEnemies(boolean deactivateEnemies) {
		this.deactivateEnemies = deactivateEnemies;
	}

	public boolean isDeactivateEnemies() {
		return deactivateEnemies;
	}
}
