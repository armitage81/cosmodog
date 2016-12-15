package antonafanasjew.cosmodog;

/**
 * Possible types of input handlers.
 */
public enum InputHandlerType {

	/**
	 * Blocked input.
	 */
	INPUT_HANDLER_NO_INPUT,
	/**
	 * Save and Restore controls (Ctrl. key is pressed).
	 */	
	INPUT_HANDLER_INGAME_CONTROL,
	/**
	 * Normal player character controls.
	 */
	INPUT_HANDLER_INGAME,
	/**
	 * Dialog box controls.
	 */
	INPUT_HANDLER_INGAME_DIALOG,
	/**
	 * Debug console controls.
	 */
	INPUT_HANDLER_INGAME_DEBUGCONSOLE,
	/**
	 * Text frame controls.
	 */
	INPUT_HANDLER_INGAME_TEXTFRAME,
	
	/**
	 * In game menu controls.
	 */
	INPUT_HANDLER_INGAME_MENU;
	
}
