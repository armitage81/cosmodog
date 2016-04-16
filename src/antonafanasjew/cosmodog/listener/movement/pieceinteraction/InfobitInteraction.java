package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.notifications.Notification;

public class InfobitInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
			@Override
			public void run() {
				cosmodogGame.getPlayerStatusNotificationList().add(new Notification("+" + Constants.SCORE_PER_INFOBIT, 500));
				player.getGameProgress().addToScore(Constants.SCORE_PER_INFOBIT);
				player.getGameProgress().addInfobit();
				applicationContext.getSoundResources().get(SoundResources.SOUND_COLLECTED).play();
			}
		});		
	}

}
