package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.topology.Position;


public class Bollard extends DynamicPiece  implements Switchable, Activatable {

    public boolean open;
    public boolean initialOpen;

    public static Bollard create(Position position, boolean open) {
        Bollard bollard = new Bollard();
        bollard.setPosition(position);
        bollard.open = open;
        bollard.initialOpen = open;
        return bollard;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "";
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return open ? 1 : 0;
    }

    @Override
    public void switchToNextState() {
        open = !open;
    }

    @Override
    public void activate() {
        this.open = !initialOpen;
    }

    @Override
    public void deactivate() {
        this.open = initialOpen;
    }

    @Override
    public boolean isActive() {
        return open != initialOpen;
    }

    @Override
    public boolean canActivate(CosmodogGame game) {
        return true;
    }

    @Override
    public boolean canDeactivate(CosmodogGame game) {
        Player player = game.getPlayer();
        CosmodogMap map = game.getMaps().get(getPosition().getMapType());

        boolean moveableOnPosition = map.dynamicPieceAtPosition(MoveableDynamicPiece.class, getPosition()).isPresent();
        boolean playerOnPosition = player.getPosition().equals(getPosition());
        boolean plasmaOnPosition = false; //TODO: Implement it once plasma is in place.


        return !moveableOnPosition && !playerOnPosition;
    }
}
