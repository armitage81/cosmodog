package antonafanasjew.cosmodog.structures;

import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.AutoBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.OneWayBollard;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.PresenceDetector;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.RegionUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PortalPuzzle implements Serializable {

    private TiledObject region;
    private List<MoveableDynamicPiece> moveables = Lists.newArrayList();
    private List<Position> originalPositions = Lists.newArrayList();
    private Position playerStartPosition;
    Set<Switchable> switchables = new HashSet<>();
    Set<Activatable> activatables = new HashSet<>();
    Set<PresenceDetector> presenceDetectors = new HashSet<>();
    Set<AutoBollard> autoBollards = new HashSet<>();
    Set<OneWayBollard> oneWayBollards = new HashSet<>();

    public TiledObject getRegion() {
        return region;
    }

    public void setRegion(TiledObject region) {
        this.region = region;
    }

    public List<MoveableDynamicPiece> getMoveables() {
        return moveables;
    }

    public void setMoveables(List<MoveableDynamicPiece> moveables) {
        this.moveables = moveables;
    }

    public List<Position> getOriginalPositions() {
        return originalPositions;
    }

    public void setOriginalPositions(List<Position> originalPositions) {
        this.originalPositions = originalPositions;
    }

    public Position getPlayerStartPosition() {
        return playerStartPosition;
    }

    public void setPlayerStartPosition(Position playerStartPosition) {
        this.playerStartPosition = playerStartPosition;
    }

    public Set<Switchable> getSwitchables() {
        return switchables;
    }

    public void setSwitchables(Set<Switchable> switchables) {
        this.switchables = switchables;
    }

    public Set<Activatable> getActivatables() {
        return activatables;
    }

    public void setActivatables(Set<Activatable> activatables) {
        this.activatables = activatables;
    }

    public Set<PresenceDetector> getPresenceDetectors() {
        return presenceDetectors;
    }

    public void setPresenceDetectors(Set<PresenceDetector> presenceDetectors) {
        this.presenceDetectors = presenceDetectors;
    }

    public Set<AutoBollard> getAutoBollards() {
        return autoBollards;
    }

    public void setAutoBollards(Set<AutoBollard> autoBollards) {
        this.autoBollards = autoBollards;
    }

    public Set<OneWayBollard> getOneWayBollards() {
        return oneWayBollards;
    }

    public void setOneWayBollards(Set<OneWayBollard> oneWayBollards) {
        this.oneWayBollards = oneWayBollards;
    }

    public static void resetPortalPuzzle(CosmodogGame game) {
        int tileLength = TileUtils.tileLengthSupplier.get();

        Player player = game.getPlayer();
        CosmodogMap map = game.mapOfPlayerLocation();
        Cam cam = game.getCam();
        PortalPuzzle portalPuzzleAroundPlayer = null;
        List<PortalPuzzle> portalPuzzles = map.getPortalPuzzles();
        for (PortalPuzzle portalPuzzle : portalPuzzles) {
            if (RegionUtils.pieceInRegion(player, map.getMapType(), portalPuzzle.getRegion())) {
                portalPuzzleAroundPlayer = portalPuzzle;
                break;
            }
        }

        if (portalPuzzleAroundPlayer != null) {

            OverheadNotificationAction.registerOverheadNotification(player, "Reset");

            for (int i = 0; i < portalPuzzleAroundPlayer.moveables.size(); i++) {
                MoveableDynamicPiece moveable = portalPuzzleAroundPlayer.getMoveables().get(i);
                Position originalPosition = portalPuzzleAroundPlayer.getOriginalPositions().get(i);
                moveable.setPosition(originalPosition);
            }

            for (Switchable switchable : portalPuzzleAroundPlayer.switchables) {
                switchable.switchToInitialState();
            }

            for (Activatable activatable : portalPuzzleAroundPlayer.activatables) {
                activatable.deactivate();
            }

            for (PresenceDetector presenceDetector : portalPuzzleAroundPlayer.presenceDetectors) {
                presenceDetector.reset();
            }

            for (AutoBollard autoBollard : portalPuzzleAroundPlayer.autoBollards) {
                autoBollard.setOpen(false);
            }

            for (OneWayBollard oneWayBollard : portalPuzzleAroundPlayer.oneWayBollards) {
                oneWayBollard.setOpen(false);
            }

            game.clearPortals();
            player.beginTeleportation();
            Position playerStartingPosition = portalPuzzleAroundPlayer.getPlayerStartPosition();
            player.setPosition(playerStartingPosition);
            player.endTeleportation();
            cam.focusOnPiece(game, 0, 0, player);
        }
    }
}
