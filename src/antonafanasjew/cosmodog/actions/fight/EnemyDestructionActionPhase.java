package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

import java.io.Serial;

public abstract class EnemyDestructionActionPhase extends AbstractFightActionPhase {

	@Serial
	private static final long serialVersionUID = -793569752077639630L;

	public EnemyDestructionActionPhase(int duration, Player player, Enemy enemy) {
		super(duration);
		this.getProperties().put("enemy", enemy);
		this.getProperties().put("player", player);
	}

}
