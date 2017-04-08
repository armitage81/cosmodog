package antonafanasjew.cosmodog.rules.actions.gameprogress;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.CamCenteringDecoratorAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.switchingindicator.SwitchingIndicatorAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

public class UpdateAlienBaseTeleportSequenceAction extends AbstractRuleAction {

	private static final String CORRECT_SEQUENCE = "131322143";
	private static final int SEQUENCE_LENGTH = CORRECT_SEQUENCE.length();
	
	private static final String CONSOLE1_TEXT = "Gravity impuls";
	private static final String CONSOLE2_TEXT = "Dynamo";
	private static final String CONSOLE3_TEXT = "Ignition";
	private static final String CONSOLE4_TEXT = "Space warp";
	
	private static final long serialVersionUID = 3007391459686874738L;

	@Override
	public void execute(GameEvent event) {

		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		String value = gameProgress.getProgressProperties().get(GameProgress.GAME_PROGRESS_ALIEN_BASE_TELEPORT_SEQUENCE);
		String currentSequence = value == null ? "" : value;
		
		if (CORRECT_SEQUENCE.equals(currentSequence)) { //Sequence was entered successfully already, do not bother with the action.
			return;
		}
		
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		Player player = ApplicationContextUtils.getPlayer();
		TiledObjectGroup regionsObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_REGIONS);
		
		TiledObject region1 = regionsObjectGroup.getObjects().get("TeleportConsole1");
		TiledObject region2 = regionsObjectGroup.getObjects().get("TeleportConsole2");
		TiledObject region3 = regionsObjectGroup.getObjects().get("TeleportConsole3");
		TiledObject region4 = regionsObjectGroup.getObjects().get("TeleportConsole4");
		
		boolean inRegion1 = RegionUtils.pieceInRegion(player, region1, map.getTileWidth(), map.getTileHeight());
		boolean inRegion2 = RegionUtils.pieceInRegion(player, region2, map.getTileWidth(), map.getTileHeight());
		boolean inRegion3 = RegionUtils.pieceInRegion(player, region3, map.getTileWidth(), map.getTileHeight());
		boolean inRegion4 = RegionUtils.pieceInRegion(player, region4, map.getTileWidth(), map.getTileHeight());
		
		String reactionText = "";
		
		if (inRegion1) {
			reactionText = CONSOLE1_TEXT;
		}
		
		if (inRegion2) {
			reactionText = CONSOLE2_TEXT;
		}
		
		if (inRegion3) {
			reactionText = CONSOLE3_TEXT;
		}
		
		if (inRegion4) {
			reactionText = CONSOLE4_TEXT;
		}
		
		OverheadNotificationAction.registerOverheadNotification(player, reactionText);
		
		currentSequence = currentSequence + (inRegion1 ? "1" : "");
		currentSequence = currentSequence + (inRegion2 ? "2" : "");
		currentSequence = currentSequence + (inRegion3 ? "3" : "");
		currentSequence = currentSequence + (inRegion4 ? "4" : "");
		
		if (currentSequence.length() > SEQUENCE_LENGTH) {
			currentSequence = currentSequence.substring(currentSequence.length() - SEQUENCE_LENGTH);
		}
		
		if (CORRECT_SEQUENCE.equals(currentSequence)) {
			AsyncAction asyncAction = new SwitchingIndicatorAction(2000, "AlienBaseTeleportIndicator", true);
			asyncAction = new CamCenteringDecoratorAction(1000, 204, 310, asyncAction, ApplicationContextUtils.getCosmodogGame());
			ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		}
		
		gameProgress.getProgressProperties().put(GameProgress.GAME_PROGRESS_ALIEN_BASE_TELEPORT_SEQUENCE, currentSequence);
		
	}

}
