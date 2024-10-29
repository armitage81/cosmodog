package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.popup.FoundToolAction;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public abstract class ToolInteraction extends AbstractPieceInteraction {

	@Override
	public void interactWithPiece(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		cosmodogGame.getActionRegistry().registerAction(AsyncActionType.PRESENTING_NEW_TOOL, new FoundToolAction(4000, text(), (CollectibleTool)piece));
		super.interactWithPiece(piece, applicationContext, cosmodogGame, player);
	}
	
	protected abstract String text();
	
}
