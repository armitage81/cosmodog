package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.model.actors.Player;

import java.util.Map;

public class ApplicationContextUtils {

	public static Map<String, MapDescriptor> mapDescriptors() {
		return ApplicationContext.instance().mapDescriptors();
	}

	public static MapDescriptor mapDescriptorMain() {
		return ApplicationContext.instance().mapDescriptors().get(MapDescriptor.MAP_NAME_MAIN);
	}

	public static MapDescriptor mapDescriptorSpace() {
		return ApplicationContext.instance().mapDescriptors().get(MapDescriptor.MAP_NAME_SPACE);
	}

	public static MapDescriptor mapDescriptor(String name) {
		return ApplicationContext.instance().mapDescriptors().get(name);
	}

	public static Player getPlayer() {
		return ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlayer();
	}

	public static CosmodogMap mapOfPlayerLocation() {
		return ApplicationContext.instance().getCosmodog().getCosmodogGame().mapOfPlayerLocation();
	}
	
	public static Map<MapDescriptor, CustomTiledMap> getCustomTiledMaps() {
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
