package antonafanasjew.cosmodog.actions.movement;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;
import java.util.Optional;

public class BlockingAction extends FixedLengthAsyncAction {

    @Serial
    private static final long serialVersionUID = 1663061093630885138L;

    private Player player;
    private CosmodogGame game;
    private Entrance targetEntrance;
    private  CollisionStatus collisionStatus;

    public BlockingAction(int duration, Player player, CosmodogGame game, Entrance targetEntrance, CollisionStatus collisionStatus) {
        super(duration);
        this.player = player;
        this.game = game;
        this.targetEntrance = targetEntrance;
        this.collisionStatus = collisionStatus;
    }

    @Override
    public void onTrigger() {

        player.getMovementListener().beforeBlock(player, player.getPosition(), targetEntrance.getPosition());

        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(targetEntrance.getPosition().getMapType());
        Optional<DynamicPiece> optDynamicPiece = map
                .getMapPieces()
                .piecesAtPosition(e -> e instanceof DynamicPiece, targetEntrance.getPosition().getX(), targetEntrance.getPosition().getY())
                .stream()
                .map(e -> (DynamicPiece)e)
                .findFirst();

        if (optDynamicPiece.isEmpty()) { //Otherwise, the dynamic piece interact method should handle the sound.
            //ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_NOWAY).play();
        }

        String text = collisionStatus.getPassageBlockerDescriptor().asText();

        OverheadNotificationAction.registerOverheadNotification(player, text);

    }

    @Override
    public void onEnd() {
        player.getMovementListener().afterBlock(player, player.getPosition(), targetEntrance.getPosition());
    }
};
