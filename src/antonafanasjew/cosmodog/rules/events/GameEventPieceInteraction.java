package antonafanasjew.cosmodog.rules.events;

import antonafanasjew.cosmodog.model.Piece;

public class GameEventPieceInteraction extends AbstractGameEvent {

	private static final long serialVersionUID = -1397431437807724522L;
	private Piece piece;

	public GameEventPieceInteraction(Piece piece) {
		this.piece = piece;
	}

	public Piece getPiece() {
		return piece;
	}

}
