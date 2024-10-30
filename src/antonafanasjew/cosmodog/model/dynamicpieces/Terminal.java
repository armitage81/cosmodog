package antonafanasjew.cosmodog.model.dynamicpieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.util.RegionUtils;
import antonafanasjew.cosmodog.util.TileUtils;

import java.io.Serial;

public class Terminal extends DynamicPiece {

	@Serial
	private static final long serialVersionUID = 1L;

	@Override
	public void interact() {
		Player player = ApplicationContextUtils.getPlayer();
		DirectionType directionType = PositionUtils.targetDirection(player, this);
		if (directionType == DirectionType.UP) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_CONSOLE).play();
			TiledObject guideTerminalRegion = terminalRegion();
			if (guideTerminalRegion != null) {
				String terminalText = guideTerminalRegion.getProperties().get("text");
				if (terminalText != null) {
					ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
					actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction(terminalText));
				}
			}
			
			
		}
		
	}
	
	private TiledObject terminalRegion() {

		int tileLength = TileUtils.tileLengthSupplier.get();

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		TiledObjectGroup regionsObjectGroup = map.getObjectGroups().get("GuideTerminals");
		
		TiledObject retVal = null;
		
		for (TiledObject region : regionsObjectGroup.getObjects().values()) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), region)) {
				retVal = region;
				break;
			}
		}

		return retVal;
	}

	@Override
	public boolean wrapsCollectible() {
		return false;
	}

	public static Terminal create(Position position) {
		Terminal terminal = new Terminal();
		terminal.setPosition(position);
		return terminal;
	}

	@Override
	public String animationId(boolean bottomNotTop) {
        return "dynamicPieceGuideTerminal";
	}
}
