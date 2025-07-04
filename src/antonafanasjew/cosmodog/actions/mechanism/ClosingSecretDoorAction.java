package antonafanasjew.cosmodog.actions.mechanism;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;

import java.io.Serial;

/**
 * An asynchronous action.
 * <p>
 * Handles the closing of a secret door. These are doors that are opened and closed based on some
 * conditions. Usually, such doors are used in Sokoban puzzles, but there are also cases where a door opens and closes
 * when a flower pot is moved.
 * <p>
 * Secret doors are dynamic pieces.
 * <p>
 * Doors can have various audiovisual styles. There are sliding doors in walls, spikes, hydraulic doors, and energy doors.
 * In this action, the style difference is important to play the correct sound when the door is operated on.
 * <p>
 * This class shares a lot of code with OpeningSecretDoorAction.
 * <p>
 * This is a fixed length action.
 */
public class ClosingSecretDoorAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8462906572353901070L;

	/**
	 * The secret door that should be operated on.
	 */
	private final SecretDoor secretDoor;

	/**
	 * Creates a new action that closes a secret door.
	 *
	 * @param duration The duration of the action.
	 * @param secretDoor The secret door that should be operated on. It is a dynamic piece.
	 */
	public ClosingSecretDoorAction(int duration, SecretDoor secretDoor) {
		super(duration);
		this.secretDoor = secretDoor;
	}

	/**
	 * When the action is triggered, the sound of the secret door is played. This sound is based on the
	 * audiovisual style of the door. Energy doors are sounding differently from hydraulic doors etc.
	 */
	@Override
	public void onTrigger() {
		String soundKey = switch (secretDoor.getStil()) {
            case SecretDoor.STIL_SPIKES -> SoundResources.SOUND_SECRET_DOOR_SPIKES;
            case SecretDoor.STIL_HYDRAULICS -> SoundResources.SOUND_SECRET_DOOR_HYDRAULICS;
            case SecretDoor.STIL_ENERGY -> SoundResources.SOUND_SECRET_DOOR_ENERGY;
            case SecretDoor.STIL_WALL -> SoundResources.SOUND_SECRET_DOOR_WALL;
            default -> throw new RuntimeException("Unknown style of secret door: " + secretDoor.getStil());
        };
        ApplicationContext.instance().getSoundResources().get(soundKey).play();
	}

	/**
	 * Updates the state of the secret door based on the time passed since the last update.
	 * With advanced time, the door opening is played, but in reverse:
	 * Door is almost open, door is half open, door is slightly ajar, door is closed.
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

		if (actionPercentage < 0.333) {
			secretDoor.setState(SecretDoor.STATE_OPENING_PHASE3);
		}
		
		if (actionPercentage >= 0.333 && actionPercentage < 0.666) {
			secretDoor.setState(SecretDoor.STATE_OPENING_PHASE2);
		}
		
		if (actionPercentage >= 0.666 && actionPercentage < 1) {
			secretDoor.setState(SecretDoor.STATE_OPENING_PHASE1);
		}
		
		if (actionPercentage >= 1) {
			secretDoor.setState(SecretDoor.STATE_CLOSED);
		}
	}

	/**
	 * At the end of the action, the door state is set to closed.
	 */
	@Override
	public void onEnd() {
		secretDoor.setState(SecretDoor.STATE_CLOSED);
	}
}
