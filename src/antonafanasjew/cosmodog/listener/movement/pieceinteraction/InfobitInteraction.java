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
				
				String text = "+" + Constants.SCORE_PER_INFOBIT;

				OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
				
				if (overheadNotificationAction == null) {
					OverheadNotificationAction action = OverheadNotificationAction.create(player, text, Color.white);
					cosmodogGame.getActionRegistry().registerAction(AsyncActionType.OVERHEAD_NOTIFICATION, action);
				} else {
					overheadNotificationAction.getTransition().texts.add(text);
					overheadNotificationAction.getTransition().completions.add(0.0f);
				}
				
				player.getGameProgress().addToScore(Constants.SCORE_PER_INFOBIT);
				player.getGameProgress().addInfobit();
				applicationContext.getSoundResources().get(SoundResources.SOUND_COLLECTED).play();
			}
		});		
	}

}
