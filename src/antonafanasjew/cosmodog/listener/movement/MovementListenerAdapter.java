package antonafanasjew.cosmodog.listener.movement;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public class MovementListenerAdapter implements MovementListener {

	private static final long serialVersionUID = -5970790830114344525L;

	@Override
	public void beforeMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void onLeavingTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void onEnteringTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void onInteractingWithTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {

	}

	@Override
	public void beforeWaiting(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void beforeFight(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void afterFight(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void beforeTeleportation(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		
	}

	@Override
	public void beforeRespawn(Actor actor, ApplicationContext applicationContext) {

	}

	@Override
	public void afterRespawn(Actor actor, ApplicationContext applicationContext) {

	}

	@Override
	public void beforeSwitchingPlane(Actor actor, ApplicationContext applicationContext) {

	}

	@Override
	public void afterSwitchingPlane(Actor actor, ApplicationContext applicationContext) {
		
	}
}

