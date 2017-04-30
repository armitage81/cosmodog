package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public class SoulEssenceInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_SOULESSENCE, new Runnable() {
			@Override
			public void run() {
				player.increaseMaxLife(Player.LIFE_UNITS_IN_LIFEPACK, true);
				player.getGameProgress().increaseSoulEssenses();
			}
		});		
	}
	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}

}
