package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import org.newdawn.slick.Music;

import antonafanasjew.cosmodog.ApplicationContext;

public class MusicUtils {

	public static String currentMapMusicId() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
		MapType mapType = map.getMapType();
		String musicResource;
		if (mapType == MapType.SPACE) {
			musicResource = MusicResources.MUSIC_SOUNDTRACK_SPACE;
		} else {
			Race race = PlayerMovementCache.getInstance().getActiveRace();
			if (race != null && race.isStarted() && !race.isSolved()) {
				musicResource = MusicResources.MUSIC_SOUNDTRACK_RACING;
			} else {
				TiledObjectGroup roofRegions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_ROOFS);
				TiledObject alienBaseRoofRegion = roofRegions.getObjects().get("AlienBaseRoof");
				TiledObject alienBaseRoofRemovalBlockerRegion = map
						.getObjectGroups()
						.get(ObjectGroups.OBJECT_GROUP_ID_ROOF_REMOVAL_BLOCKERS)
						.getObjects()
						.get("AlienBaseRoofRemovalBlocker");

				boolean inAlienBase = RegionUtils.pieceInRegion(player, mapType, alienBaseRoofRegion) && !RegionUtils.pieceInRegion(player, mapType, alienBaseRoofRemovalBlockerRegion);

				if (inAlienBase) {
					musicResource = MusicResources.MUSIC_SOUNDTRACK_MYSTERY;
				} else {
					musicResource = MusicResources.MUSIC_SOUNDTRACK;
				}


			}

		}
		return musicResource;
	}

	public static void loopMusic(String musicResource) {

		Music music = ApplicationContext.instance().getMusicResources().get(musicResource);
		if (!music.playing()) {
			music.loop();
		}

	}
	
	public static void playMusic(String musicResource) {

		Music music = ApplicationContext.instance().getMusicResources().get(musicResource);
		if (!music.playing()) {
			music.play();
		}

	}
	
}
