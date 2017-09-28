package antonafanasjew.cosmodog.actions.cutscenes;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.jingle.PlayJingleAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FoundToolAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = 2996734449646067196L;

	private String text;
	private CollectibleTool tool;
	
	public FoundToolAction(int duration, String text, CollectibleTool tool) {
		super(duration);
		this.text = text;
		this.tool = tool;
	}

	@Override
	public void onTrigger() {
		ApplicationContextUtils.getPlayer().setDirection(DirectionType.DOWN);
		ApplicationContextUtils.getCosmodogGame().setCurrentlyFoundTool(tool);
		ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.FOUND_TOOL_JINGLE, new PlayJingleAction(5000, MusicResources.MUSIC_FOUND_TOOL));
		
	}

	@Override
	public void onEnd() {
		ApplicationContextUtils.getCosmodogGame().setCurrentlyFoundTool(null);
		ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry().registerAction(AsyncActionType.BLOCKING_INTERFACE, new PopUpNotificationAction(text));
	}
	
}
