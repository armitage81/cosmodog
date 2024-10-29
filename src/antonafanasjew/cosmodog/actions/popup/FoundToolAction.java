package antonafanasjew.cosmodog.actions.popup;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents an action that is executed when the player finds a tool (boat, dynamite, axe etc.).
 * <p>
 * It is a fixed length action.
 * <p>
 * The action will set the player's direction to down and register a jingle action to play the found tool jingle
 * at the beginning. Additionally, the currently found tool is set in the game instance. This is used to render the tool
 * over the head of the player during the action.
 * <p>
 * There is no update logic in this action.
 * <p>
 * At the end, a pop-up notification action is registered in the interface action registry to show the text about the found item.
 * Additionally, the currently found tool is set to null to stop rendering it over the player's head.
 */
public class FoundToolAction extends FixedLengthAsyncAction {


	@Serial
	private static final long serialVersionUID = 2996734449646067196L;

	/**
	 * Text about the found tool that will be shown in the pop-up notification when the action ends.
	 */
	private final String text;

	/**
	 * The tool that was found.
	 * <p>
	 * Take note: It is of type Collectible which is a map piece in the game, even if it is not located on the map anymore.
	 */
	private final CollectibleTool tool;

	/**
	 * Initializes the action with the given duration, text and tool.
	 *
	 * @param duration Duration of the action in milliseconds.
	 * @param text The text about the found tool that will be shown in the pop-up notification when the action ends.
	 * @param tool The tool that was found.
	 */
	public FoundToolAction(int duration, String text, CollectibleTool tool) {
		super(duration);
		this.text = text;
		this.tool = tool;
	}

	/**
	 * Sets the player's direction to down and registers a jingle action to play the found tool jingle.
	 * Additionally, sets the currently found tool as property of the game instance. This is used to render the tool
	 * over the head of the player during the action.
	 */
	@Override
	public void onTrigger() {
		ApplicationContextUtils.getPlayer().setDirection(DirectionType.DOWN);
		ApplicationContextUtils.getCosmodogGame().setCurrentlyFoundTool(tool);
		ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.FOUND_TOOL_JINGLE, new PlayJingleAction(5000, MusicResources.MUSIC_FOUND_TOOL));
		
	}

	/**
	 * Registers a pop-up notification action in the interface action registry to show the text about the found item.
	 * Additionally, sets the currently found tool to null to stop rendering it over the player's head.
	 */
	@Override
	public void onEnd() {
		ApplicationContextUtils.getCosmodogGame().setCurrentlyFoundTool(null);
		ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction(text));
	}
	
}
