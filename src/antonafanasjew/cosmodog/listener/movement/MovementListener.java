package antonafanasjew.cosmodog.listener.movement;

import java.io.Serializable;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public interface MovementListener extends Serializable {

	void beforeMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);
	void onLeavingTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);
	void onEnteringTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);
	void onInteractingWithTile(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);
	void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext);
	
	void beforeWaiting(Actor actor, ApplicationContext applicationContext);
	void afterWaiting(Actor actor, ApplicationContext applicationContext);
	
	void beforeFight(Actor actor, ApplicationContext applicationContext);
	void afterFight(Actor actor, ApplicationContext applicationContext);
	
	void beforeTeleportation(Actor actor, ApplicationContext applicationContext);
	void afterTeleportation(Actor actor, ApplicationContext applicationContext);

	void beforeRespawn(Actor actor, ApplicationContext applicationContext);
	void afterRespawn(Actor actor, ApplicationContext applicationContext);
	
	
}
