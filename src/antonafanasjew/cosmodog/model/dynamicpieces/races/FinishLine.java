package antonafanasjew.cosmodog.model.dynamicpieces.races;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.popup.PlayJingleAction;
import antonafanasjew.cosmodog.actions.race.InitiateRaceAction;
import antonafanasjew.cosmodog.actions.race.WinRaceAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FinishLine extends DynamicPiece {

    private boolean horizontalNotVertical;
    private Race race;

    /**
     * This is a workaround.
     *
     * The problem is the order in which the steps are executed when a race is won.
     *
     * There are the following constraints:
     *
     * If the player steps on the finish line at the same time as the last available turn expires, the game must still be won.
     * That's why the race is 'solved' in the method 'interactBeforeEnteringAttempt'
     *
     * On the other hand, the triumphant jingle must be played later, in the method 'interactWhenSteppingOn', to not interfere with the music selection after movement.
     *
     * But since the race is already solved when the jingle is played, we cannot use the '!solved' condition to decide if we want to play the jingle.
     *
     * Without this condition, the jingle would play every time the player steps on the finish line.
     *
     * This active flag makes sure that the 'interactWhenSteppingOn' method is executed only once.
     *
     */
    private boolean active = true;

    public static FinishLine create(Position position, boolean horizontalNotVertical) {
        FinishLine finishLine = new FinishLine();
        finishLine.setPosition(position);
        finishLine.horizontalNotVertical = horizontalNotVertical;
        return finishLine;
    }

    private FinishLine() {

    }

    public boolean isHorizontalNotVertical() {
        return horizontalNotVertical;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return true;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return horizontalNotVertical ? "finishLineHorizontal" : "finishLineVertical";
    }

    @Override
    public void interactBeforeEnteringAttempt() {
        //We need to solve the race puzzle before stepping on the finish line to account for the last turn, otherwise, the puzzle will be lost.
        if (race.isStarted() && !race.isSolved()) {
            race.solve();
        }
    }

    @Override
    public void interactWhenSteppingOn() {
        if (getRace().isSolved() && active) {
            ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.CUTSCENE, new WinRaceAction(race));
            active = false;
        }
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Race getRace() {
        return race;
    }
}
