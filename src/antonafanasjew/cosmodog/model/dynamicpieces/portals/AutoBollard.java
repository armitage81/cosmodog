package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.mechanism.RaisingAutoBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SinkingAutoBollardAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;


public class AutoBollard extends DynamicPiece {

    public boolean open;

    public static final short VISUAL_STATE_CLOSED = 0;
    public static final short VISUAL_STATE_OPENING_PHASE1 = 1;
    public static final short VISUAL_STATE_OPENING_PHASE2 = 2;
    public static final short VISUAL_STATE_OPENING_PHASE3 = 3;
    public static final short VISUAL_STATE_OPEN = 4;


    private short visualState;

    public static AutoBollard create(Position position) {
        AutoBollard bollard = new AutoBollard();
        bollard.setPosition(position);
        bollard.open = false;
        bollard.visualState = VISUAL_STATE_CLOSED;
        return bollard;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return this.isOpen();
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        String animationIdPrefix = "dynamicPieceAutoBollard";
        String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
        String animationSuffix = String.valueOf(visualState);
        return animationIdPrefix + animationIdInfix + animationSuffix;
    }

    @Override
    public void interactBeforeEnteringAttempt() {
        if (!open) {
            ApplicationContextUtils
                    .getCosmodogGame()
                    .getActionRegistry()
                    .registerAction(AsyncActionType.MOVEMENT,
                            new SinkingAutoBollardAction(SinkingAutoBollardAction.DURATION, this)
                    );
        }
    }

    @Override
    public void interactAfterExiting() {
        if (open) {
            ApplicationContextUtils
                    .getCosmodogGame()
                    .getActionRegistry()
                    .registerAction(AsyncActionType.MOVEMENT,
                            new RaisingAutoBollardAction(RaisingAutoBollardAction.DURATION, this)
                    );
        }
    }

    @Override
    public void interact() {
        if (!open) {
            ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_NOWAY).play();
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean canDeactivate(CosmodogGame game) {
        Player player = game.getPlayer();
        CosmodogMap map = game.getMaps().get(getPosition().getMapType());

        boolean moveableOnPosition = map.dynamicPieceAtPosition(MoveableDynamicPiece.class, getPosition()).isPresent();
        boolean playerOnPosition = player.getPosition().equals(getPosition());
        boolean plasmaOnPosition = false; //TODO: Implement it once plasma is in place.


        return !moveableOnPosition && !playerOnPosition;
    }

    public void setVisualState(short visualState) {
        this.visualState = visualState;
    }

    @Override
    public int renderingPriority() {
        return open ? 1 : 10;
    }
}
