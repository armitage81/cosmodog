package antonafanasjew.cosmodog.rules.events;

import antonafanasjew.cosmodog.model.Piece;

public class GameEventPieceInteraction extends AbstractGameEvent {

	private Piece piece;

	public GameEventPieceInteraction(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	}

}
