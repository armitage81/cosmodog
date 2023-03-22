package antonafanasjew.cosmodog.actions.operatingsecretdoor;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;

public class OpeningSecretDoorAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -8462906572353901070L;

	private SecretDoor secretDoor;
	
	public OpeningSecretDoorAction(int duration, SecretDoor secretDoor) {
		super(duration);
		this.secretDoor = secretDoor;
	}

	@Override
	public void onTrigger() {
		
		String soundKey;
		if (secretDoor.getStil().equals(SecretDoor.STIL_SPIKES)) {
			soundKey = SoundResources.SOUND_SECRET_DOOR_SPIKES;
		} else if (secretDoor.getStil().equals(SecretDoor.STIL_HYDRAULICS)) {
			soundKey = SoundResources.SOUND_SECRET_DOOR_HYDRAULICS;
		} else if (secretDoor.getStil().equals(SecretDoor.STIL_ENERGY)) {
			soundKey = SoundResources.SOUND_SECRET_DOOR_ENERGY;
		} else if (secretDoor.getStil().equals(SecretDoor.STIL_WALL)) {
			soundKey = SoundResources.SOUND_SECRET_DOOR_WALL;
		} else {
			throw new RuntimeException("Unknown stil of secret door: " + secretDoor.getStil());
		}
		
		ApplicationContext.instance().getSoundResources().get(soundKey).play();
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		float actionPercentage = (float)after / (float)getDuration();
		
		if (actionPercentage > 1.0f) {
			actionPercentage = 1.0f;
		}
		
			
		if (actionPercentage < 0.333) {
			secretDoor.setState(SecretDoor.STATE_OPENING_PHASE1);
		}
		
		if (actionPercentage >= 0.333 && actionPercentage < 0.666) {
			secretDoor.setState(SecretDoor.STATE_OPENING_PHASE2);
		}
		
		if (actionPercentage >= 0.666 && actionPercentage < 1) {
			secretDoor.setState(SecretDoor.STATE_OPENING_PHASE3);
		}
		
		if (actionPercentage >= 1) {
			secretDoor.setState(SecretDoor.STATE_OPEN);
		}
	}
	
	@Override
	public void onEnd() {
		secretDoor.setState(SecretDoor.STATE_OPEN);
	}
}
