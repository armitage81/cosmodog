package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;

import java.util.Map;

public class ApplicationContextUtils {

	public static Player getPlayer() {
		return ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer();
	}

	public static CosmodogMap mapOfPlayerLocation() {
		return ApplicationContext.instance().getCosmodog().getCosmodogGame().mapOfPlayerLocation();
	}
	
	public static Map<MapType, CustomTiledMap> getCustomTiledMaps() {
		return ApplicationContext.instance().getCustomTiledMaps();
	}
	
	public static CosmodogGame getCosmodogGame() {
		return ApplicationContext.instance().getCosmodog().getCosmodogGame();
	}
	
	public static Cosmodog getCosmodog() {
		return ApplicationContext.instance().getCosmodog();
	}
	
	public static GameProgress getGameProgress() {
		return ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer().getGameProgress();
	}
	
}
