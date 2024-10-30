package antonafanasjew.cosmodog.actions.spacelift;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.LadderAction;
import antonafanasjew.cosmodog.actions.ParabolicAction;
import antonafanasjew.cosmodog.actions.camera.CamMovementAction;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class SpaceLiftAction extends PhaseBasedAction {

    public SpaceLiftAction() {

    }

    @Override
    protected void onTriggerInternal() {

        CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

        Position playerPosition = ApplicationContextUtils.getPlayer().getPosition();

        FixedLengthAsyncAction closingDoor = new FixedLengthAsyncAction(1000);
        FixedLengthAsyncAction suspenseWaiting = new FixedLengthAsyncAction(5000);
        CamMovementAction focusingOnLift = new CamMovementAction(2000, playerPosition.shifted(0, -4), cosmodogGame);
        LadderAction preparingLift = new LadderAction(2000, 4);
        ParabolicAction launchingLift = new ParabolicAction(5000);
        FixedLengthAsyncAction traveling = new FixedLengthAsyncAction(5000);

        ParabolicAction arriving = new ParabolicAction(5000);
        FixedLengthAsyncAction waiting = new FixedLengthAsyncAction(2000);
        CamMovementAction focusingOnPlayer = new CamMovementAction(2000, playerPosition, cosmodogGame);
        FixedLengthAsyncAction openingDoor = new FixedLengthAsyncAction(1000);

        getPhaseRegistry().registerPhase(closingDoor);
        getPhaseRegistry().registerPhase(suspenseWaiting);
        /*
        getPhaseRegistry().registerPhase(focusingOnLift);
        getPhaseRegistry().registerPhase(preparingLift);
        getPhaseRegistry().registerPhase(launchingLift);
        getPhaseRegistry().registerPhase(traveling);
        getPhaseRegistry().registerPhase(arriving);
        getPhaseRegistry().registerPhase(waiting);
        getPhaseRegistry().registerPhase(focusingOnPlayer);
        getPhaseRegistry().registerPhase(openingDoor);
         */
    }

}
