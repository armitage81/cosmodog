package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public abstract class EnemyDestructionActionPhase extends AbstractFightActionPhase {

	@Serial
	private static final long serialVersionUID = -793569752077639630L;

	public EnemyDestructionActionPhase(int duration, Player player, Set<Enemy> enemies) {
		super(duration);
		this.getProperties().put("enemies", enemies);
		this.getProperties().put("player", player);
	}

}
