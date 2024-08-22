package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;


public abstract class EnemyDestructionActionPhase extends AbstractFightActionPhase {

	private static final long serialVersionUID = -793569752077639630L;

	private Player player;
	private Enemy enemy;
	
	public EnemyDestructionActionPhase(int duration, Player player, Enemy enemy) {
		super(duration);
		this.player = player;
		this.enemy = enemy;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

}
