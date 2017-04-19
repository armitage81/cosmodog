package antonafanasjew.cosmodog.listener.movement;

import java.util.Iterator;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;

public class ComposedMovementListener implements MovementListener {

	private static final long serialVersionUID = 2006754305962958885L;

	private Iterable<MovementListener> underlyingMovementListeners;
	
	public ComposedMovementListener(Iterable<MovementListener> underlyingMovementListeners) {
		this.underlyingMovementListeners = underlyingMovementListeners;
	}
	
	@Override
	public void beforeMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.beforeMovement(actor, x1, y1, x2, y2, applicationContext);
		}
		
	}

	@Override
	public void onLeavingTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.onLeavingTile(actor, x1, y1, x2, y2, applicationContext);
		}
	}

	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.onEnteringTile(actor, x1, y1, x2, y2, applicationContext);
		}		
	}

	@Override
	public void onInteractingWithTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.onInteractingWithTile(actor, x1, y1, x2, y2, applicationContext);
		}
	}

	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.afterMovement(actor, x1, y1, x2, y2, applicationContext);
		}		
	}

	@Override
	public void beforeWaiting(Actor actor, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.beforeWaiting(actor, applicationContext);
		}	
	}

	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.afterWaiting(actor, applicationContext);
		}	
	}

	@Override
	public void beforeFight(Actor actor, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.beforeFight(actor, applicationContext);
		}	
	}

	@Override
	public void afterFight(Actor actor, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.afterFight(actor, applicationContext);
		}
	}

	@Override
	public void beforeTeleportation(Actor actor, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.beforeTeleportation(actor, applicationContext);
		}
	}

	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		Iterator<MovementListener> it = underlyingMovementListeners.iterator();
		while (it.hasNext()) {
			MovementListener l = it.next();
			l.afterTeleportation(actor, applicationContext);
		}		
	}

}
