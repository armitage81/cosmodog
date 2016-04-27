package antonafanasjew.cosmodog.listener.life;

import java.io.Serializable;

import antonafanasjew.cosmodog.model.actors.Actor;

public interface LifeListener extends Serializable {

	void onLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange);
	void onMaxLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange);
	
}
