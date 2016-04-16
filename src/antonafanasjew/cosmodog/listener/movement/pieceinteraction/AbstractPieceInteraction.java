package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

public abstract class AbstractPieceInteraction implements PieceInteraction {

	@Override
	public void interactWithPiece(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		interact(piece, applicationContext, cosmodogGame, player);
	}

	protected abstract void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player);

}
