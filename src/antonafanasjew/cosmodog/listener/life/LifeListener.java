package antonafanasjew.cosmodog.listener.life;

import antonafanasjew.cosmodog.model.actors.Actor;

public interface LifeListener {

	void onLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange);
	void onMaxLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange);
	
}
