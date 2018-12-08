package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public class MedipackInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		player.setLife(player.getMaxLife());
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_EATEN;
	}

}
