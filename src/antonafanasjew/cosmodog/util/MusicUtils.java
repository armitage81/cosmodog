package antonafanasjew.cosmodog.util;

import org.newdawn.slick.Music;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.Features;

public class MusicUtils {

	public static void loopMusic(String musicResource) {
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, new Runnable() {
			
			@Override
			public void run() {
				Music music = ApplicationContext.instance().getMusicResources().get(musicResource);
				if (!music.playing()) {
					music.loop();				
				}
			}
		});
		
	}
	
	public static void playMusic(String musicResource) {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, new Runnable() {
			
			@Override
			public void run() {
				Music music = ApplicationContext.instance().getMusicResources().get(musicResource);
				if (!music.playing()) {
					music.play();				
				}
			}
		});
	}
	
}
