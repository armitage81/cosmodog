package antonafanasjew.cosmodog.actions.movement;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serial;
import java.util.Optional;

public class MovementAttemptAction extends FixedLengthAsyncAction {

    @Serial
    private static final long serialVersionUID = 1663061093630885138L;

    private boolean interactedWithDynamicPieceAlready = false;

    private final Position targetPosition;

    public MovementAttemptAction(int duration, Position targetPosition) {
        super(duration);
        this.targetPosition = targetPosition;
    }

    @Override
    public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
        if (getCompletionRate() >= 0.5f && !interactedWithDynamicPieceAlready) {
            Player player = ApplicationContextUtils.getPlayer();
            CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
            //Now handle the case of interacting with dynamic pieces (e.g. destroying a stone)
            //BTW, this part is not entirely correct, as interaction with dynamic pieces will happen only in
            //case if they are blocking passage (e.g. not destroyed stones)
            //But what if we want to interact with passable dynamic pieces (e.g. add a poisoned sound to the poison spots)

            CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(targetPosition.getMapType());
            Optional<DynamicPiece> optDynamicPiece = map
                    .getMapPieces()
                    .piecesAtPosition(e -> e instanceof DynamicPiece, targetPosition.getX(), targetPosition.getY())
                    .stream()
                    .map(e -> (DynamicPiece)e)
                    .findFirst();

            optDynamicPiece.ifPresent(DynamicPiece::interact);
            interactedWithDynamicPieceAlready = true;
        }
    }
}
