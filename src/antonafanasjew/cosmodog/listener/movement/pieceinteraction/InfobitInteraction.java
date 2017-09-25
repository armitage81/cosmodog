package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;

public class InfobitInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
			@Override
			public void run() {
				player.getGameProgress().addInfobit();
				if (player.getGameProgress().getInfobits() == Constants.NUMBER_OF_INFOBITS_IN_GAME) {
					cosmodogGame.getInterfaceActionRegistry().registerAction(AsyncActionType.BLOCKING_INTERFACE, new PopUpNotificationAction("Congratulation! You found all infobits of the game. You are definitely the best expert of Phaeton in all the valley."));
				}
			}

		});		
	}

}
