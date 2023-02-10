package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

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
			ActionRegistry fightActionPhaseRegistry = fightAction.getActionPhaseRegistry();
			AbstractFightActionPhase fightActionPhase = (AbstractFightActionPhase)fightActionPhaseRegistry.getRegisteredAction(AsyncActionType.FIGHT);
			if (fightActionPhase == null) {
				fightActionPhase = (AbstractFightActionPhase)fightActionPhaseRegistry.getRegisteredAction(AsyncActionType.FIGHT_FROM_PLATFORM);
			}
			if (fightActionPhase != null) {
				fightPhaseTransition = fightActionPhase.getFightPhaseTransition();
			}
		}
		return fightPhaseTransition;
	}
	
}
