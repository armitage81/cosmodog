package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.model.actors.Enemy;

import java.io.Serial;
import java.util.HashSet;
import java.util.Set;

public abstract class AttackActionPhase extends AbstractFightActionPhase {

	@Serial
	private static final long serialVersionUID = 8538547874032046446L;

	private final FightPlan.FightPhasePlan fightPhasePlan;

	public AttackActionPhase(int duration, FightPlan.FightPhasePlan fightPhasePlan) {
		super(duration);
		this.fightPhasePlan = fightPhasePlan;
		Set<Enemy> enemies = new HashSet<>();
		enemies.add(getFightPhasePlan().getEnemy());
		getProperties().put("player", getFightPhasePlan().getPlayer());
		getProperties().put("enemies", enemies);
	}

	public FightPlan.FightPhasePlan getFightPhasePlan() {
		return fightPhasePlan;
	}
	
}
