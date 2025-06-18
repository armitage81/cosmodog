package antonafanasjew.cosmodog.actions.death;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.weather.SnowfallChangeAction;
import antonafanasjew.cosmodog.topology.Position;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.PathUtils;
import org.newdawn.slick.Music;

import java.io.Serial;

public class DyingAction extends FixedLengthAsyncAction {


	@Serial
	private static final long serialVersionUID = -7371591553509263521L;

	private final String dyingHint;

	private final Position respawnPosition;

	public DyingAction(int duration, String dyingHint, Position respawnPosition) {
		super(duration);
		this.dyingHint = dyingHint;
		this.respawnPosition = respawnPosition;
	}

	@Override
	public void onEnd() {

		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();

		player.setFood(player.getCurrentMaxFood());
		player.setWater(player.getCurrentMaxWater());
		player.resetLife();
		player.setPosition(respawnPosition);
		player.decontaminate();
		player.resetTurnsWormAlerted();

		//The map should be taken AFTER changing the player's position
		//because respawning could move him to a different map. (Example: Dying in space, respawning on land.)
		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();

		SnowfallChangeAction snowfallChangeAction = (SnowfallChangeAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.SNOWFALL_CHANGE);
		if (snowfallChangeAction != null) {
			snowfallChangeAction.resetRate();
		}

		PlayerMovementCache.getInstance().update(player, player.getPosition(), player.getPosition());
		cam.focusOnPiece(cosmodogGame, 0, 0, player);
		String currentMapMusicId = MusicUtils.currentMapMusicId();
		MusicUtils.loopMusic(currentMapMusicId);
		cosmodog.getGamePersistor().saveCosmodogGame(cosmodogGame, PathUtils.gameSaveDir() + "/" + cosmodogGame.getGameName() + ".sav");
	}

	public String getDyingHint() {
		return dyingHint;
	}

	public int animationFrameIndex() {

		int frameIndex;

		if (getCompletionRate() < 0.1f) {
			frameIndex = 0;
		} else if (getCompletionRate() >= 0.1f && getCompletionRate() < 0.15f) {
			frameIndex = 1;
		} else if (getCompletionRate() >= 0.15f && getCompletionRate() < 0.225f) {
			frameIndex = 2;
		} else if (getCompletionRate() >= 0.225f && getCompletionRate() < 0.375f) {
			frameIndex = 3;
		} else if (getCompletionRate() >= 0.375f && getCompletionRate() < 0.45f) {
			frameIndex = 4;
		} else if (getCompletionRate() >= 0.45f && getCompletionRate() < 0.525f) {
			frameIndex = 5;
		} else if (getCompletionRate() >= 0.525f && getCompletionRate() < 0.7f) {
			frameIndex = 6;
		} else {
			frameIndex = 7;
		}
		return frameIndex;
	}
}
