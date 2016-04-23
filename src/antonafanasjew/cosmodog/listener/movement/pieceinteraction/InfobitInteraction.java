package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import org.newdawn.slick.Color;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public class InfobitInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
			@Override
			public void run() {
				
				OverheadNotificationAction action = OverheadNotificationAction.create(500, player, "+" + Constants.SCORE_PER_INFOBIT, Color.white);
				cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
				
				player.getGameProgress().addToScore(Constants.SCORE_PER_INFOBIT);
				player.getGameProgress().addInfobit();
				applicationContext.getSoundResources().get(SoundResources.SOUND_COLLECTED).play();
			}
		});		
	}

}
