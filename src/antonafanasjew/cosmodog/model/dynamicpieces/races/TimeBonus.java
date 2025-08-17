package antonafanasjew.cosmodog.model.dynamicpieces.races;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class TimeBonus extends DynamicPiece {


    private int bonusValue;
    private Race race;
    private boolean activeInCurrentRace;

    public static TimeBonus create(Position position, int bonusValue) {
        TimeBonus timeBonus = new TimeBonus();
        timeBonus.setPosition(position);
        timeBonus.bonusValue = bonusValue;
        timeBonus.activeInCurrentRace = false;
        return timeBonus;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "timeBonus" + bonusValue;
    }

    public int getBonusValue() {
        return bonusValue;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public void interactBeforeEnteringAttempt() {
        if (race.isStarted() && !race.isSolved()) {
            if (activeInCurrentRace) {
                ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TIMEBONUS).play();
                race.incRemainingTimeToSolve(bonusValue);
                activeInCurrentRace = false;
            }

        }
    }

    public void setActiveInCurrentRace(boolean activeInCurrentRace) {
        this.activeInCurrentRace = activeInCurrentRace;
    }

    public boolean isActiveInCurrentRace() {
        return activeInCurrentRace;
    }
}
