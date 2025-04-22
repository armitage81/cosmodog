package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.mechanism.RaisingAutoBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.RaisingOneWayBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SinkingAutoBollardAction;
import antonafanasjew.cosmodog.actions.mechanism.SinkingOneWayBollardAction;
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


public class OneWayBollard extends DynamicPiece implements Switchable, Activatable {

    public boolean open;

    private DirectionType initialDirectionType;
    public DirectionType direction;

    public static final short VISUAL_STATE_CLOSED = 0;
    public static final short VISUAL_STATE_OPENING_PHASE1 = 1;
    public static final short VISUAL_STATE_OPENING_PHASE2 = 2;
    public static final short VISUAL_STATE_OPENING_PHASE3 = 3;
    public static final short VISUAL_STATE_OPEN = 4;


    private short visualState;

    public static OneWayBollard create(Position position, DirectionType direction) {
        OneWayBollard bollard = new OneWayBollard();
        bollard.setPosition(position);
        bollard.open = false;
        bollard.initialDirectionType = direction;
        bollard.direction = direction;
        bollard.visualState = VISUAL_STATE_CLOSED;
        return bollard;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return true;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        String animationIdPrefix = "dynamicPieceOneWayBollard";
        String animationIdInfix = bottomNotTop ? "Bottom" : "Top";
        animationIdInfix += direction.getRepresentation();
        String animationSuffix = String.valueOf(visualState);
        return animationIdPrefix + animationIdInfix + animationSuffix;
    }

    @Override
    public void interactBeforeEnteringAttempt() {
        Player player = ApplicationContextUtils.getPlayer();
        if (player.getDirection() == direction) {
            ApplicationContextUtils
                    .getCosmodogGame()
                    .getActionRegistry()
                    .registerAction(AsyncActionType.MOVEMENT,
                            new SinkingOneWayBollardAction(1000, this)
                    );
        }
    }

    @Override
    public void interactAfterExiting() {
        ApplicationContextUtils
                .getCosmodogGame()
                .getActionRegistry()
                .registerAction(AsyncActionType.MOVEMENT,
                        new RaisingOneWayBollardAction(1000, this)
                );
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

    @Override
    public void activate() {
        this.direction = DirectionType.reverse(initialDirectionType);
    }

    @Override
    public void deactivate() {
        this.direction = initialDirectionType;
    }

    @Override
    public boolean isActive() {
        return this.direction == DirectionType.reverse(initialDirectionType);
    }

    @Override
    public boolean canActivate(CosmodogGame game) {
        return true;
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

    public DirectionType getDirection() {
        return direction;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    @Override
    public int numberOfStates() {
        return 2;
    }

    @Override
    public int currentState() {
        return direction == DirectionType.LEFT || direction == DirectionType.UP ? 0 : 1;
    }

    @Override
    public void switchToNextState() {
        this.direction = DirectionType.reverse(this.direction);
    }
}
