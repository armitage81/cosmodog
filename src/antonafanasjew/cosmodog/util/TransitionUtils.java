package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.actions.*;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.model.CosmodogGame;

import java.util.Map;
import java.util.Optional;

public class TransitionUtils {

	public static Optional<AbstractFightActionPhase> currentFightPhase() {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		PhaseBasedAction fightAction = (PhaseBasedAction)actionRegistry.getRegisteredAction(AsyncActionType.FIGHT);
		
		if (fightAction == null) {
			fightAction = (PhaseBasedAction)actionRegistry.getRegisteredAction(AsyncActionType.FIGHT_FROM_PLATFORM);
		}
		
		if (fightAction != null) {
			PhaseRegistry fightActionPhaseRegistry = fightAction.getPhaseRegistry();
			Optional<AsyncAction> fightPhase = fightActionPhaseRegistry.currentPhase();
            return fightPhase.map(asyncAction -> (AbstractFightActionPhase) asyncAction);
		} else {
			return Optional.empty();
		}

	}
	
}
