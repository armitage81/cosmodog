package antonafanasjew.cosmodog.actions.fight;


public abstract class AttackActionPhase extends AbstractFightActionPhase {

	private static final long serialVersionUID = 8538547874032046446L;

	private FightActionResult.FightPhaseResult fightPhaseResult;
	
	public AttackActionPhase(int duration, FightActionResult.FightPhaseResult fightPhaseResult) {
		super(duration);
		this.fightPhaseResult = fightPhaseResult;
	}

	public FightActionResult.FightPhaseResult getFightPhaseResult() {
		return fightPhaseResult;
	}
	
}
