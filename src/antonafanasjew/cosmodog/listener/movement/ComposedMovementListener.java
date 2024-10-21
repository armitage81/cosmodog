package antonafanasjew.cosmodog.listener.movement;

import java.util.Iterator;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public class ComposedMovementListener implements MovementListener {

	private static final long serialVersionUID = 2006754305962958885L;

	private Iterable<MovementListener> underlyingMovementListeners;
	
	public ComposedMovementListener(Iterable<MovementListener> underlyingMovementListeners) {
		this.underlyingMovementListeners = underlyingMovementListeners;
	}
	
	@Override
	public void beforeMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.beforeMovement(actor, position1, position2, applicationContext);
        }
		
	}

	@Override
	public void onLeavingTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.onLeavingTile(actor, position1, position2, applicationContext);
        }
	}

	@Override
	public void onEnteringTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.onEnteringTile(actor, position1, position2, applicationContext);
        }
	}

	@Override
	public void onInteractingWithTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.onInteractingWithTile(actor, position1, position2, applicationContext);
        }
	}

	@Override
	public void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.afterMovement(actor, position1, position2, applicationContext);
        }
	}

	@Override
	public void beforeWaiting(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.beforeWaiting(actor, applicationContext);
        }
	}

	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.afterWaiting(actor, applicationContext);
        }
	}

	@Override
	public void beforeFight(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.beforeFight(actor, applicationContext);
        }
	}

	@Override
	public void afterFight(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.afterFight(actor, applicationContext);
        }
	}

	@Override
	public void beforeTeleportation(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.beforeTeleportation(actor, applicationContext);
        }
	}

	@Override
	public void beforeRespawn(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.beforeRespawn(actor, applicationContext);
        }
	}

	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.afterTeleportation(actor, applicationContext);
        }
	}

	@Override
	public void afterRespawn(Actor actor, ApplicationContext applicationContext) {
        for (MovementListener l : underlyingMovementListeners) {
            l.afterRespawn(actor, applicationContext);
        }
	}

	@Override
	public void beforeSwitchingPlane(Actor actor, ApplicationContext applicationContext) {
		for (MovementListener l : underlyingMovementListeners) {
			l.beforeSwitchingPlane(actor, applicationContext);
		}
	}

	@Override
	public void afterSwitchingPlane(Actor actor, ApplicationContext applicationContext) {
		for (MovementListener l : underlyingMovementListeners) {
			l.afterSwitchingPlane(actor, applicationContext);
		}
	}
}
