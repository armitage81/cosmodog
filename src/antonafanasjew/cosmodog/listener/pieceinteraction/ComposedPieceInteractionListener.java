package antonafanasjew.cosmodog.listener.pieceinteraction;

import java.util.Iterator;

import antonafanasjew.cosmodog.model.Piece;

public class ComposedPieceInteractionListener implements PieceInteractionListener {

	private static final long serialVersionUID = 7182541366844579122L;

	private Iterable<PieceInteractionListener> underlyingListeners;
	
	public ComposedPieceInteractionListener(Iterable<PieceInteractionListener> underlyingListeners) {
		this.underlyingListeners = underlyingListeners;
	}

	@Override
	public void beforeInteraction(Piece piece) {
		Iterator<PieceInteractionListener> it = underlyingListeners.iterator();
		while (it.hasNext()) {
			PieceInteractionListener l = it.next();
			l.beforeInteraction(piece);
		}		
	}

	@Override
	public void afterInteraction(Piece piece) {
		Iterator<PieceInteractionListener> it = underlyingListeners.iterator();
		while (it.hasNext()) {
			PieceInteractionListener l = it.next();
			l.afterInteraction(piece);
		}
	}
	
}
