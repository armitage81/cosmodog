package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.NarrativeSequenceUtils;
import antonafanasjew.cosmodog.util.NotificationUtils;

public class SuppliesInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundSupply()), true, false);
		//Do not fill up the food completely but add a normal unit.
		//If player has a food compartment, he will store more than one food bar.
		player.setFood(player.getFood() + Player.INITIAL_MAX_FOOD);
		player.setLifeLentForHunger(0);
		OverheadNotificationAction.registerOverheadNotification(player, "You eat the ration");
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_EATEN;
	}

}
