package antonafanasjew.cosmodog.actions.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.camera.CamMovementAction;
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

    @Override
    protected void onTriggerInternal() {

        Player player = ApplicationContextUtils.getPlayer();
        Ray ray = player.getPortalRay();
        List<Position> positions = ray.getRayPositions();
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        for (Position position : positions) {
            getPhaseRegistry().registerPhase(position.toString(), new CamMovementAction(125, PositionUtils.toPixelPosition(position), game));
        }
        getPhaseRegistry().registerPhase("CreatingPortal", new CreatePortalAction(1000));
        getPhaseRegistry().registerPhase("BackToPlayer", new CamMovementAction(500, PositionUtils.toPixelPosition(player.getPosition()), game));
    }

}
