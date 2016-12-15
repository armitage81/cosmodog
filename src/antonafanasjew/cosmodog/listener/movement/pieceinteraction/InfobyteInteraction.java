package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public class InfobyteInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_INFOBITS, new Runnable() {
			@Override
			public void run() {
				player.getGameProgress().addInfobyte();
				applicationContext.getSoundResources().get(SoundResources.SOUND_COLLECTED).play();
			}

		});		
	}

}
