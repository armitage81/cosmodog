package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public class FirstAidKitInteraction extends AbstractPieceInteraction {

	public static int HP_HEALED = 5;

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {

		int lifeAfterHealing = player.getLife() + HP_HEALED;

		player.setLife(Math.min(player.getMaxLife(), lifeAfterHealing));
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_EATEN;
	}

}
