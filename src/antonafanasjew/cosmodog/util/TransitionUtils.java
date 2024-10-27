package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.PhaseRegistry;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

import java.util.Optional;

public class TransitionUtils {

	public static FightPhaseTransition currentFightPhaseTransition() {
		FightPhaseTransition fightPhaseTransition = null;
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		PhaseBasedAction fightAction = (PhaseBasedAction)actionRegistry.getRegisteredAction(AsyncActionType.FIGHT);
		
		if (fightAction == null) {
			fightAction = (PhaseBasedAction)actionRegistry.getRegisteredAction(AsyncActionType.FIGHT_FROM_PLATFORM);
		}
		
		if (fightAction != null) {
			PhaseRegistry fightActionPhaseRegistry = fightAction.getPhaseRegistry();
			Optional<AsyncAction> fightPhase = fightActionPhaseRegistry.currentPhase();
			if (fightPhase.isPresent()) {
				fightPhaseTransition = ((AbstractFightActionPhase)fightPhase.get()).getFightPhaseTransition();
			}
		}
		return fightPhaseTransition;
	}
	
}
