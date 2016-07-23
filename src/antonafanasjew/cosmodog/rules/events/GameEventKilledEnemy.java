package antonafanasjew.cosmodog.rules.events;

import antonafanasjew.cosmodog.model.actors.Enemy;

public class GameEventKilledEnemy extends AbstractGameEvent {

	private static final long serialVersionUID = 1449669751320828469L;

	private Enemy enemy;
	
	public GameEventKilledEnemy(Enemy enemy) {
		this.enemy = enemy;
	}
	
	public Enemy getEnemy() {
		return enemy;
	}
}
