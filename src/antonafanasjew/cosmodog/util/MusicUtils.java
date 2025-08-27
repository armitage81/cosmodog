package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.structures.Race;
import org.newdawn.slick.Music;

import antonafanasjew.cosmodog.ApplicationContext;

public class MusicUtils {

	public static String currentMapMusicId() {
		MapType mapType = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapType();
		String musicResource;
		if (mapType == MapType.SPACE) {
			musicResource = MusicResources.MUSIC_SOUNDTRACK_SPACE;
		} else {
			Race race = PlayerMovementCache.getInstance().getActiveRace();
			if (race != null && race.isStarted() && !race.isSolved()) {
				musicResource = MusicResources.MUSIC_SOUNDTRACK_RACING;
			} else {
				musicResource = MusicResources.MUSIC_SOUNDTRACK;
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
