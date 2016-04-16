package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Describes the interaction of the player with a piece, such as a collectible.
 *
 */
public interface PieceInteraction {

	void interactWithPiece(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player);
	
}
