package antonafanasjew.cosmodog.actions.dying;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

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

public class DyingAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -7371591553509263521L;

	public static class DyingTransition {
		
		public float percentage = 0.0f;
		
		public int animationFrameIndex() {
			
			int frameIndex;
			
			if (percentage < 0.1f) {
				frameIndex = 0;
			} else if (percentage >= 0.075f && percentage < 0.15f) {
				frameIndex = 1;
			} else if (percentage >= 0.15f && percentage < 0.225f) {
				frameIndex = 2;
			} else if (percentage >= 0.225f && percentage < 0.3f) {
				frameIndex = 3;
			} else if (percentage >= 0.3f && percentage < 0.375f) {
				frameIndex = 3;
			} else if (percentage >= 0.375f && percentage < 0.45f) {
				frameIndex = 4;
			} else if (percentage >= 0.45f && percentage < 0.525f) {
				frameIndex = 5;
			} else if (percentage >= 0.525f && percentage < 0.7f) {
				frameIndex = 6;
			} else {
				frameIndex = 7;
			}
			return frameIndex;
		}
		
	}
	
	private DyingTransition transition;
	private String dyingHint;
	
	public DyingAction(int duration, String dyingHint) {
		super(duration);
		this.dyingHint = dyingHint;
		this.transition = new DyingTransition();
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		transition.percentage = actionPercentage;
	}
	
	@Override
	public void onEnd() {
		transition = null;
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();

		player.setFood(player.getCurrentMaxFood());
		player.setWater(player.getCurrentMaxWater());
		player.resetLife();
		player.setPositionX(28);
		player.setPositionY(58);
		player.decontaminate();
		player.resetTurnsWormAlerted();
		PlayerMovementCache.getInstance().afterMovement(player, player.getPositionX(), player.getPositionY(), player.getPositionX(), player.getPositionY(), ApplicationContext.instance());
		cam.focusOnPiece(cosmodogMap, 0, 0, player);
		MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);
		cosmodog.getGamePersistor().saveCosmodogGame(cosmodogGame, PathUtils.gameSaveDir() + "/" + cosmodogGame.getGameName() + ".sav");
	}

	public DyingTransition getTransition() {
		return transition;
	}
	
	public String getDyingHint() {
		return dyingHint;
	}

}
