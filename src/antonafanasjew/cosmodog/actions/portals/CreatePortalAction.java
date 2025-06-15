package antonafanasjew.cosmodog.actions.portals;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.model.portals.Ray;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class CreatePortalAction extends FixedLengthAsyncAction {
    public CreatePortalAction(int duration) {
        super(duration);
    }

    @Override
    public void onTrigger() {
        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        CosmodogMap map = game.mapOfPlayerLocation();
        Ray ray = player.getPortalRay();
        Position rayTargetPosition = ray.getTargetPosition();
        if (rayTargetPosition != null) {
            int targetTileId = map.getTileId(rayTargetPosition, Layers.LAYER_META_PORTALS);
            TileType targetTileType = TileType.getByLayerAndTileId(Layers.LAYER_META_PORTALS, targetTileId);
            if (targetTileType.equals(TileType.PORTAL_RAY_ATTACHABLE)) {
                DirectionType directionFacingPlayer = DirectionType.reverse(ray.getLastDirection());
                if (game.portal(rayTargetPosition, directionFacingPlayer).isEmpty()) {
                    Portal portal = new Portal(rayTargetPosition, directionFacingPlayer);
                    game.createPortal(portal);
                }
            }
        }
    }
}
