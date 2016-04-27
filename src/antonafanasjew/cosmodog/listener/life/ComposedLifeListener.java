package antonafanasjew.cosmodog.listener.life;

import java.util.Iterator;

import antonafanasjew.cosmodog.model.actors.Actor;

public class ComposedLifeListener implements LifeListener {

	private static final long serialVersionUID = 7018321554145786513L;

	private Iterable<LifeListener> underlyingLifeListeners;
	
	public ComposedLifeListener(Iterable<LifeListener> underlyingLifeListeners) {
		this.underlyingLifeListeners = underlyingLifeListeners;
	}
	
	@Override
	public void onLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		Iterator<LifeListener> it = underlyingLifeListeners.iterator();
		while (it.hasNext()) {
			LifeListener l = it.next();
			l.onLifeChange(actorAfterLifeChange, lifeBeforeChange, lifeAfterChange);
		}		
	}

	@Override
	public void onMaxLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		Iterator<LifeListener> it = underlyingLifeListeners.iterator();
		while (it.hasNext()) {
			LifeListener l = it.next();
			l.onMaxLifeChange(actorAfterLifeChange, lifeBeforeChange, lifeAfterChange);
		}	
	}

	

}
