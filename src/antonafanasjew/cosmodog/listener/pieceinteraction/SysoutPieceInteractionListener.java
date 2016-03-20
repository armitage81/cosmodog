package antonafanasjew.cosmodog.listener.pieceinteraction;

import antonafanasjew.cosmodog.model.Piece;

public class SysoutPieceInteractionListener implements PieceInteractionListener {

	private static final long serialVersionUID = -4598065631036334379L;

	@Override
	public void beforeInteraction(Piece piece) {
		System.out.println("Starting interaction with piece: " + piece.toString());
	}

	@Override
	public void afterInteraction(Piece piece) {
		System.out.println("Finished interaction with piece: " + piece.toString());
		
	}

}
