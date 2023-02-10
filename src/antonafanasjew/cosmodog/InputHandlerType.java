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
	 * Normal player character controls.
	 */
	INPUT_HANDLER_INGAME,
	/**
	 * Debug console controls.
	 */
	INPUT_HANDLER_INGAME_DEBUGCONSOLE,
	/**
	 * Text frame controls.
	 */
	INPUT_HANDLER_INGAME_TEXTFRAME,
	/**
	 * Game Log controls.
	 */
	INPUT_HANDLER_INGAME_GAMELOG,
	/**
	 * In game menu controls.
	 */
	INPUT_HANDLER_INGAME_MENU;
	
}
