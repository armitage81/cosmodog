package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;

public class InfobitInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
			@Override
			public void run() {
				player.getGameProgress().addInfobit();
				if (player.getGameProgress().getInfobits() == Constants.NUMBER_OF_INFOBITS_IN_GAME) {
					cosmodogGame.getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction("Strange feeling overcomes you. It is as if you suddenly knew everything about the valley. There must be a reward somewhere for all that knowledge."));
				}
			}

		});		
	}

}
