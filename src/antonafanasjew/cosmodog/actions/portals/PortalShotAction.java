package antonafanasjew.cosmodog.actions.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.camera.CamMovementAction;
import antonafanasjew.cosmodog.actions.camera.CamMovementActionWithConstantSpeed;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.actions.popup.WaitAction;
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
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.List;

public class PortalShotAction extends PhaseBasedAction {

    private Ray ray;

    @Override
    protected void onTriggerInternal() {

        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_GUNSHOT).play();

        boolean portalShouldBeCreated = false;

        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        CosmodogMap map = game.mapOfPlayerLocation();
        ray = Ray.create(map, player);
        Position rayTargetPosition = ray.getTargetPosition();
        if (rayTargetPosition != null) {
            int targetTileId = map.getTileId(rayTargetPosition, Layers.LAYER_META_PORTALS);
            TileType targetTileType = TileType.getByLayerAndTileId(Layers.LAYER_META_PORTALS, targetTileId);
            if (targetTileType.equals(TileType.PORTAL_RAY_ATTACHABLE)) {
                DirectionType directionFacingPlayer = DirectionType.reverse(ray.getLastDirection());
                if (game.portal(rayTargetPosition, directionFacingPlayer).isEmpty()) {
                    portalShouldBeCreated = true;
                }
            }
        }

        List<Position> positions = ray.getRayPositions();
        for (Position position : positions) {
            getPhaseRegistry().registerPhase(position.toString(), new CamMovementAction(125, PositionUtils.toPixelPosition(position)));
        }
        if (portalShouldBeCreated) {
            getPhaseRegistry().registerPhase("CreatingPortal", new CreatePortalAction(500, ray));
        } else {
            getPhaseRegistry().registerPhase("NotCreatingPortal", new FailPortalAction(1));
        }
        getPhaseRegistry().registerPhase("BackToPlayer", new CamMovementActionWithConstantSpeed(16*10, PositionUtils.toPixelPosition(player.getPosition()), game));
    }

    @Override
    public void onEnd() {
        ray = null;
    }
}
