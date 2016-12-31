package antonafanasjew.cosmodog.listener.movement;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;

public class MovementListenerAdapter implements MovementListener {

	private static final long serialVersionUID = -5970790830114344525L;

	@Override
	public void beforeMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {

	}

	@Override
	public void onLeavingTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {

	}

	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {

	}

	@Override
	public void onInteractingWithTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {

	}

	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {

	}

	@Override
	public void beforeWaiting(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
		
	}

}
