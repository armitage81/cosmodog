package antonafanasjew.cosmodog.actions.jingle;

import org.newdawn.slick.Music;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.util.MusicUtils;

public class PlayJingleAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = 4646336035061126558L;

	private String musicResourceId;
	float originalMusicPosition;
	
	public PlayJingleAction(int duration, String musicResourceId) {
		super(duration);
		this.musicResourceId = musicResourceId;
	}

	@Override
	public void onTrigger() {
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, new Runnable() {
			
			@Override
			public void run() {
				Music music = ApplicationContext.instance().getMusicResources().get(MusicResources.MUSIC_SOUNDTRACK);
				originalMusicPosition = music.getPosition();
				MusicUtils.playMusic(musicResourceId);
			}
		});
		
	}
	
	@Override
	public void onEnd() {
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, new Runnable() {
			
			@Override
			public void run() {
				Music music = ApplicationContext.instance().getMusicResources().get(MusicResources.MUSIC_SOUNDTRACK);
				music.setPosition(originalMusicPosition);
				MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);
			}
		});
		
	}

}
