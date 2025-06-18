package antonafanasjew.cosmodog.actions.popup;

import org.newdawn.slick.Music;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.util.MusicUtils;

import java.io.Serial;

/**
 * Asynchronous action that is executed when a tool (f.i. a boat or an axe) is found.
 * <p>
 * It is a fixed length action. It plays a jingle sound when triggered.
 * <p>
 * It is registered within the FoundToolAction.
 * <p>
 * Take note: The actual music must be stopped while the jingle is played and resumed afterward.
 * That's why the position in the music track is stored at the beginning and used to resume the music at the end.
 */
public class PlayJingleAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = 4646336035061126558L;

	/**
	 * The resource ID for the jingle sound.
	 */
	private final String musicResourceId;

	/**
	 * The position of the background music track.
	 * It is stored at the beginning of the action and used to resume the music at the end.
	 * That way, the soundtrack is not started anew everytime a tool has been found.
	 */
	float originalMusicPosition;

	/**
	 * Constructor. Creates the action with the given duration and the resource ID of the jingle sound.
	 *
	 * @param duration Duration of the action in milliseconds.
	 * @param musicResourceId The resource ID for the jingle sound.
	 */
	public PlayJingleAction(int duration, String musicResourceId) {
		super(duration);
		this.musicResourceId = musicResourceId;
	}

	/**
	 * Executed when the action is triggered.
	 * <p>
	 * Stops the background music and plays the jingle sound. The current position of the background music is stored
	 * to be resumed at the end of the action.
	 * <p>
	 * Take note: The jingle is only played when the music feature is enabled.
	 */
	@Override
	public void onTrigger() {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, () -> {
			String currentMapMusicId = MusicUtils.currentMapMusicId();
			Music music = ApplicationContext.instance().getMusicResources().get(currentMapMusicId);
			originalMusicPosition = music.getPosition();
			MusicUtils.playMusic(musicResourceId);
		});
		
	}

	/**
	 * Executed when the action is finished.
	 * <p>
	 * Resumes the background music at the position where it was stopped at the beginning of the action.
	 * <p>
	 * Take note: The music is only resumed when the music feature is enabled.
	 */
	@Override
	public void onEnd() {
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, () -> {
			String currentMapMusicId = MusicUtils.currentMapMusicId();
			Music music = ApplicationContext.instance().getMusicResources().get(currentMapMusicId);
			music.setPosition(originalMusicPosition);
			MusicUtils.loopMusic(currentMapMusicId);
		});
	}
}
