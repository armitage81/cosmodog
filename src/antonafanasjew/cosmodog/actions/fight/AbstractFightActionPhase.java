package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

public abstract class AbstractFightActionPhase extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -8572769280800088275L;

	private FightPhaseTransition fightPhaseTransition;
	
	public AbstractFightActionPhase(int duration) {
		super(duration);
	}

	public FightPhaseTransition getFightPhaseTransition() {
		return fightPhaseTransition;
	}

	public void setFightPhaseTransition(FightPhaseTransition fightPhaseTransition) {
		this.fightPhaseTransition = fightPhaseTransition;
	}

}
