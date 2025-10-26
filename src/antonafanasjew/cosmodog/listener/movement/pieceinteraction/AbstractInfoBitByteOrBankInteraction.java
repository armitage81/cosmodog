package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public abstract class AbstractInfoBitByteOrBankInteraction extends AbstractPieceInteraction {

    protected void handleLastInfobitInGame() {
        Player player = ApplicationContextUtils.getPlayer();
        CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
        if (player.getGameProgress().getInfobits() == Constants.NUMBER_OF_INFOBITS_IN_GAME) {
            cosmodogGame.getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction("Strange feeling overcomes you. It is as if you suddenly knew everything about the valley. There must be a reward somewhere for all that knowledge."));
        }
    }

}
