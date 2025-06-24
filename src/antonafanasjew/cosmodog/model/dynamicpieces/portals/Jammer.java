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

    private boolean hidden;

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    //Hidden jammers are not visible nor audible to the player.
    //They are used to silently disable all portals after leaving a puzzle chamber.
    public static Jammer create(Position position, boolean hidden) {
        Jammer jammer = new Jammer();
        jammer.setPosition(position);
        jammer.setHidden(hidden);
        return jammer;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return true;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        if (hidden) {
            return null;
        }
        return bottomNotTop ? "dynamicPieceJammerBottom" : "dynamicPieceJammerTop";
    }

    @Override
    public void interactWhenSteppingOn() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        if (!hidden) {
            ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_CANCELED).play();
        }
        game.clearPortals();

    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
