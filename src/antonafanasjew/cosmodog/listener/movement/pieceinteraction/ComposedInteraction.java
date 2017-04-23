package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.PiecesUtils;

public class ComposedInteraction extends AbstractPieceInteraction {

	@Override
	public void beforeInteraction(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {

	}
	
	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CollectibleComposed c = (CollectibleComposed)piece;
		List<Collectible> elements = c.getElements();
		
		for (Collectible element : elements) {
			String pieceType = PiecesUtils.pieceType(element); 
			PieceInteraction pieceInteraction = ApplicationContext.instance().getCosmodog().getPieceInteractionMap().get(pieceType);
			if (pieceInteraction != null){
				pieceInteraction.beforeInteraction(element, applicationContext, cosmodogGame, player);
				pieceInteraction.interactWithPiece(element, applicationContext, cosmodogGame, player);
				pieceInteraction.afterInteraction(element, applicationContext, cosmodogGame, player);
			}
		}
	}

	@Override
	public void afterInteraction(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {

	}
	
}
