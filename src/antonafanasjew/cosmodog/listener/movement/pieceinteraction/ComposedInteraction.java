package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public class ComposedInteraction extends AbstractPieceInteraction {

	@Override
	public void beforeInteraction(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {

	}
	
	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CollectibleComposed c = (CollectibleComposed)piece;
		List<Collectible> elements = c.getElements();
		
		for (Collectible element : elements) {
			String pieceType = null;
			if (element instanceof CollectibleComposed) {
				pieceType = CollectibleComposed.class.getSimpleName();
			} else if (element instanceof CollectibleTool) {
				CollectibleTool collectibleTool = (CollectibleTool)element;
				pieceType = collectibleTool.getToolType().name();
			} else if (element instanceof CollectibleWeapon) {
				pieceType = CollectibleWeapon.class.getSimpleName();
			} else if (element instanceof CollectibleAmmo) {
				pieceType = CollectibleAmmo.class.getSimpleName();
			} else if (element instanceof CollectibleKey) {
				pieceType = CollectibleKey.class.getSimpleName();
			} else {
				CollectibleGoodie collectibleGoodie = (CollectibleGoodie)element;
				pieceType = collectibleGoodie.getGoodieType().name();
			} 
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
