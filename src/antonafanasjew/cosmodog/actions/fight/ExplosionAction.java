package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.topology.Position;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

import java.io.Serial;

public class ExplosionAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -5764923931787835528L;


	public ExplosionAction(int duration, Position position) {
		super(duration);
		this.getProperties().put("position", position);
	}

	@Override
	public void onTrigger() {
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_EXPLOSION).play();
	}

}
