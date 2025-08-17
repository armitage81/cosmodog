package antonafanasjew.cosmodog.structures;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.actions.race.LoseRaceAction;
import antonafanasjew.cosmodog.actions.race.ResetRaceAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.races.*;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;
import antonafanasjew.cosmodog.util.TileUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Race implements Serializable {

    private TiledObject region;


    /*
    Taken from the object properties this value defines which game progress property should be set to true when the race is won.
     */
    private String rewardPropertyName;
    private List<TimeBonus> timeBonuses = new ArrayList<>();
    private List<Resetter> resetters = new ArrayList<>();
    private List<TrafficBarrier> trafficBarriers = new ArrayList<>();
    private List<RaceExit> raceExits = new ArrayList<>();
    private PolePosition polePosition;
    private FinishLine finishLine;
    private Position respawnPosition;

    private int timeToSolve;

    private boolean started = false;
    private boolean solved = false;
    private int remainingTimeToSolve;

    public void start() {

        Player player = ApplicationContextUtils.getPlayer();
        int turn = player.getGameProgress().getTurn();

        for (TrafficBarrier trafficBarrier : trafficBarriers) {
            trafficBarrier.setReferenceTurn(turn);
        }

        for (TimeBonus timeBonus : timeBonuses) {
            timeBonus.setActiveInCurrentRace(true);
        }

        started = true;
        remainingTimeToSolve = timeToSolve;

    }

    public void endTurn() {
        remainingTimeToSolve -= 1;
    }

    public void cancel() {

        for (TrafficBarrier trafficBarrier : trafficBarriers) {
            trafficBarrier.setReferenceTurn(-1);
        }

        started = false;
        solved = false;
        remainingTimeToSolve = -1;
    }

    public void solve() {

        for (TrafficBarrier trafficBarrier : trafficBarriers) {
            trafficBarrier.setReferenceTurn(-1);
        }

        started = false;
        solved = true;
        remainingTimeToSolve = -1;
    }

    public List<TimeBonus> getTimeBonuses() {
        return timeBonuses;
    }

    public List<Resetter> getResetters() {
        return resetters;
    }

    public List<TrafficBarrier> getTrafficBarriers() {
        return trafficBarriers;
    }

    public List<RaceExit> getRaceExits() {
        return raceExits;
    }

    public int getTimeToSolve() {
        return timeToSolve;
    }

    public void setTimeToSolve(int timeToSolve) {
        this.timeToSolve = timeToSolve;
    }

    public TiledObject getRegion() {
        return region;
    }

    public void setRegion(TiledObject region) {
        this.region = region;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isSolved() {
        return solved;
    }

    public PolePosition getPolePosition() {
        return polePosition;
    }

    public void setPolePosition(PolePosition polePosition) {
        this.polePosition = polePosition;
    }

    public FinishLine getFinishLine() {
        return finishLine;
    }

    public void setFinishLine(FinishLine finishLine) {
        this.finishLine = finishLine;
    }

    public int getRemainingTimeToSolve() {
        return remainingTimeToSolve;
    }

    public void incRemainingTimeToSolve(int increment) {
        remainingTimeToSolve += increment;
    }

    public Position getRespawnPosition() {
        return respawnPosition;
    }

    public void setRespawnPosition(Position respawnPosition) {
        this.respawnPosition = respawnPosition;
    }

    public static void resetRace(CosmodogGame game) {

        Race race = PlayerMovementCache.getInstance().getActiveRace();

        if (race != null && race.isStarted() && !race.isSolved()) {
            ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.CUTSCENE, new ResetRaceAction(race));
        }

    }

    public String getRewardPropertyName() {
        return rewardPropertyName;
    }

    public void setRewardPropertyName(String rewardPropertyName) {
        this.rewardPropertyName = rewardPropertyName;
    }
}
