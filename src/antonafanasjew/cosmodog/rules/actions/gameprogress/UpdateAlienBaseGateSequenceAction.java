package antonafanasjew.cosmodog.rules.actions.gameprogress;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.CamCenteringDecoratorAction;
import antonafanasjew.cosmodog.actions.loweringgate.LowerGateAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

public class UpdateAlienBaseGateSequenceAction extends AbstractRuleAction {

	private static final long serialVersionUID = 3007391459686874738L;

	@Override
	public void execute(GameEvent event) {
		
		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		String value = gameProgress.getProgressProperties().get(GameProgress.GAME_PROGRESS_ALIEN_BASE_GATE_SEQUENCE);
		int currentSequenceNumber = value == null ? -1 : Integer.valueOf(value);
		
		if (currentSequenceNumber >= 5) { //Sequence was entered successfully already, do not bother with the action.
			return;
		}
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CONSOLE).play();
		
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		TiledObjectGroup regionsObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_REGIONS);
		
		TiledObject region1 = regionsObjectGroup.getObjects().get("AlienBaseGateSwitch1");
		TiledObject region2 = regionsObjectGroup.getObjects().get("AlienBaseGateSwitch2");
		TiledObject region3 = regionsObjectGroup.getObjects().get("AlienBaseGateSwitch3");
		TiledObject region4 = regionsObjectGroup.getObjects().get("AlienBaseGateSwitch4");
		TiledObject region5 = regionsObjectGroup.getObjects().get("AlienBaseGateSwitch5");
		
		boolean inRegion1 = RegionUtils.pieceInRegion(player, region1, map.getTileWidth(), map.getTileHeight());
		boolean inRegion2 = RegionUtils.pieceInRegion(player, region2, map.getTileWidth(), map.getTileHeight());
		boolean inRegion3 = RegionUtils.pieceInRegion(player, region3, map.getTileWidth(), map.getTileHeight());
		boolean inRegion4 = RegionUtils.pieceInRegion(player, region4, map.getTileWidth(), map.getTileHeight());
		boolean inRegion5 = RegionUtils.pieceInRegion(player, region5, map.getTileWidth(), map.getTileHeight());
		
		int nextSequenceNumber = -2;
		
		if (currentSequenceNumber == -1) {
			OverheadNotificationAction.registerOverheadNotification(player, "You use the terminal");
		}
		
		if (inRegion1) {
			if (currentSequenceNumber == 0) {
				OverheadNotificationAction.registerOverheadNotification(player, "Initializing gate controls...");
				nextSequenceNumber = 1;
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Wrong order, reset");
				nextSequenceNumber = 0;
			}
			
		} else if (inRegion2) {
			if (currentSequenceNumber == 1) {
				OverheadNotificationAction.registerOverheadNotification(player, "Overriding security lock...");
				nextSequenceNumber = 2;
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Wrong order, reset");
				nextSequenceNumber = 0;
			}
			
		} else  if (inRegion3) {
			if (currentSequenceNumber == 2) {
				OverheadNotificationAction.registerOverheadNotification(player, "Unlocking gate bracket...");
				nextSequenceNumber = 3;
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Wrong order, reset");
				nextSequenceNumber = 0;
			}
		} else if (inRegion4) {
			if (currentSequenceNumber == 3) {
				OverheadNotificationAction.registerOverheadNotification(player, "Emulating entrance permission...");
				nextSequenceNumber = 4;
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Wrong order, reset");
				nextSequenceNumber = 0;
			}
		} else if (inRegion5) {
			if (currentSequenceNumber == 4) {
				OverheadNotificationAction.registerOverheadNotification(player, "Opening...");
				
				
				AsyncAction asyncAction = new LowerGateAction(2000, "GatesToLaunchPodInAlienBase");
				asyncAction = new CamCenteringDecoratorAction(1000, Position.fromCoordinates(237, 289), asyncAction, ApplicationContextUtils.getCosmodogGame());
				
				ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
				actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
				
				nextSequenceNumber = 5;
				
			} else {
				OverheadNotificationAction.registerOverheadNotification(player, "Wrong order. Reset!");
				nextSequenceNumber = 0;
			}
		}
		
		if (nextSequenceNumber != -2) {
			gameProgress.getProgressProperties().put(GameProgress.GAME_PROGRESS_ALIEN_BASE_GATE_SEQUENCE, String.valueOf(nextSequenceNumber));
		}
		
	}

}
