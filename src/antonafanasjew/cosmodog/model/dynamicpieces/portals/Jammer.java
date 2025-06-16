package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import com.apple.eawt.Application;

public class Jammer extends DynamicPiece {
    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    public static Jammer create(Position position) {
        Jammer jammer = new Jammer();
        jammer.setPosition(position);
        return jammer;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return true;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return bottomNotTop ? "dynamicPieceJammerBottom" : "dynamicPieceJammerTop";
    }

    @Override
    public void interactWhenSteppingOn() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_CANCELED).play();
        game.clearPortals();

    }
}
