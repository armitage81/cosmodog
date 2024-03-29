package antonafanasjew.cosmodog.rules.actions.sokoban;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.CamCenteringDecoratorAction;
import antonafanasjew.cosmodog.actions.operatingsecretdoor.ClosingSecretDoorAction;
import antonafanasjew.cosmodog.actions.operatingsecretdoor.OpeningSecretDoorAction;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

public class OperateSecretDoorsInSokobanAction extends AbstractRuleAction {

	private static final long serialVersionUID = 3007391459686874738L;

	@Override
	public void execute(GameEvent event) {
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		CosmodogMap map = applicationContext.getCosmodog().getCosmodogGame().getMap();
		Player player = applicationContext.getCosmodog().getCosmodogGame().getPlayer();
		
		List<MoveableGroup> moveableGroups = map.getMoveableGroups();
		MoveableGroup groupAroundPlayer = null;
		for (MoveableGroup group : moveableGroups) {
			if (RegionUtils.pieceInRegion(player, group.getRegion(), map.getTileWidth(), map.getTileHeight())) {
				groupAroundPlayer = group;
				break;
			}
		}
		
		if (groupAroundPlayer != null) {
			boolean wasSolvedBefore = groupAroundPlayer.isSolvedStatus();
			boolean solved = groupAroundPlayer.solved();
			
			if (wasSolvedBefore != solved) {
				groupAroundPlayer.setSolvedStatus(solved);
				
				List<SecretDoor> doors = groupAroundPlayer.getSecretDoors();
				
				for (SecretDoor door : doors) {
					AsyncAction asyncAction;
					if (solved) {
						asyncAction = new OpeningSecretDoorAction(500, door);
					} else {
						asyncAction = new ClosingSecretDoorAction(500, door);
					}
					asyncAction = new CamCenteringDecoratorAction(1000, door, asyncAction, ApplicationContextUtils.getCosmodogGame());
					ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
					actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
				}
			}
		}
		
		
		
		
	}

}
