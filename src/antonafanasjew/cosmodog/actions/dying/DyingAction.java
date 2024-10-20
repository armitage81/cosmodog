package antonafanasjew.cosmodog.actions.dying;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.snowfall.SnowfallChangeAction;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
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

import java.io.Serial;

/**
 * Represents the dying action. This action is executed when the player's life reaches zero or for other reasons of his death.
 * <p>
 * It is a fixed length action. It is considered finished when the duration has passed.
 * <p>
 * The action is visualized by a black screen and the player slowly going down. Additionally, a game hint is shown.
 * <p>
 * The death of the player does not end the game. Instead, the player respawns at the beginning of the map (or at a checkpoint).
 * His food and water are restored to the maximum. His contamination state is reset. Also, the worm alert counter is reset.
 * The music starts playing from the beginning. The game state is persisted.
 */
public class DyingAction extends FixedLengthAsyncAction {


	@Serial
	private static final long serialVersionUID = -7371591553509263521L;

	/**
	 * This transition describes the progress of the dying player.
	 * Depending on the percentage of the action duration, it can indicate
	 * the current frame of the dying animation.
	 * There are 8 frames in total. They are presented based on the passed percentage of the animation time
	 * in the following way:
	 * <p>
	 * - 0..0.1: Frame 0;
	 * <p>
	 * - 0.1..0.15: Frame 1;
	 * <p>
	 * - 0.15..0.225: Frame 2;
	 * <p>
	 * - 0.225..0.375: Frame 3;
	 * <p>
	 * - 0.375..0.45: Frame 4;
	 * <p>
	 * - 0.45..0.525: Frame 5;
	 * <p>
	 * - 0.525..0.7: Frame 6;
	 * <p>
	 * - 0.7..1.0: Frame 7.
	 * <p>
	 * The frames show the player slowly going on his knees, bowing his head and then falling down.
	 */
	public static class DyingTransition {

		/**
		 * Percentage of the action completion. It is a value between 0.0 and 1.0.
		 * It is used to calculate the current frame of the dying animation.
		 */
		public float percentage = 0.0f;

		/**
		 * Returns the current frame of the dying animation based on the percentage of the action completion.
		 * @return The current frame of the dying animation.
		 */
		public int animationFrameIndex() {
			
			int frameIndex;
			
			if (percentage < 0.1f) {
				frameIndex = 0;
			} else if (percentage >= 0.1f && percentage < 0.15f) {
				frameIndex = 1;
			} else if (percentage >= 0.15f && percentage < 0.225f) {
				frameIndex = 2;
			} else if (percentage >= 0.225f && percentage < 0.375f) {
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

	/**
	 * The transition that is used to render the dying player on the screen.
	 */
	private DyingTransition transition;

	/**
	 * The game hint that is shown when the player dies.
	 * It represents the red text on the bottom of the black screen that is shown while the player figure is going down.
	 */
	private final String dyingHint;

	/**
	 * Defines where the player will be respawned after resurrection.
	 * <p>
	 * Normally, it will be the start location of the map, but for some cases, like the Indiana Jones
	 * puzzle, the player should respawn near the death location.
	 *
	 */
	private final Position respawnPosition;

	/**
	 * Initializes the dying action with the given duration and the dying hint.
	 *
	 * @param duration Duration of the action in milliseconds.
	 * @param dyingHint The hint that is shown when the player dies.
	 */
	public DyingAction(int duration, String dyingHint, Position respawnPosition) {
		super(duration);
		this.dyingHint = dyingHint;
		this.transition = new DyingTransition();
		this.respawnPosition = respawnPosition;
	}

	/**
	 * Updates the completion rate of the transition for the dying player effect.
	 * <p>
	 * The completion rate is updated in a linear fashion and is in the interval 0..1.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		transition.percentage = actionPercentage;
	}

	/**
	 * At the end of the action, the player respawns at the beginning of the map (or at a checkpoint).
	 * His food and water are restored to the maximum. His contamination state is reset. Also, the worm alert counter is reset.
	 * The music starts playing from the beginning. The game state is persisted.
	 * <p>
	 * Note how the player movement listener is executed after the player is moved to the new position to simulate the player's movement.
	 * Many calculations happen there, like recalculating enemy ranges, point the supply tracker to the right position etc.
	 */
	@Override
	public void onEnd() {
		transition = null;
		
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

		PlayerMovementCache.getInstance().afterMovement(player, player.getPosition(), player.getPosition(), ApplicationContext.instance());
		cam.focusOnPiece(0, 0, player);
		MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);
		cosmodog.getGamePersistor().saveCosmodogGame(cosmodogGame, PathUtils.gameSaveDir() + "/" + cosmodogGame.getGameName() + ".sav");
	}

	/**
	 * Returns the transition representing the player's dying animation.
	 * @return The transition representing the player's dying animation.
	 */
	public DyingTransition getTransition() {
		return transition;
	}

	/**
	 * Returns the hint text that is shown when the player dies.
	 * @return The hint text that is shown when the player dies.
	 */
	public String getDyingHint() {
		return dyingHint;
	}
}
