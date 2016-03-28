package antonafanasjew.cosmodog.filesystem;

/**
 * Checked exception defining problems while persisting or reading cosmodog data.
 */
public class CosmodogPersistenceException extends Exception {

	private static final long serialVersionUID = 3710913693959590168L;

	/**
	 * Initialized with a message and underlying exception.
	 * @param message Error message.
	 * @param ex The underlying exception.
	 */
	public CosmodogPersistenceException(String message, Exception ex) {
		super(message, ex);
	}


}
