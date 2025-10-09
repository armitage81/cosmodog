package antonafanasjew.cosmodog.actions.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
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

    Ray ray;

    public CreatePortalAction(int duration, Ray ray) {
        super(duration);
        this.ray = ray;
    }

    @Override
    public void onTrigger() {
        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_CREATED).play();
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        Position rayTargetPosition = ray.getTargetPosition();
        DirectionType directionFacingPlayer = DirectionType.reverse(ray.getLastDirection());
        Portal portal = new Portal(rayTargetPosition, directionFacingPlayer);
        game.createPortal(portal);
    }
}
