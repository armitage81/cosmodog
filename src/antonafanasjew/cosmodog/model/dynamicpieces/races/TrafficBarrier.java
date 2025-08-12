package antonafanasjew.cosmodog.model.dynamicpieces.races;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Bollard;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ArithmeticUtils;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class TrafficBarrier extends DynamicPiece {

    boolean horizontalNotVertical;
    List<Integer> opennessRhythm;
    long referenceTurn = 0;


    public static TrafficBarrier create(Position position, boolean horizontalNotVertical, int... opennessRhythm) {
        TrafficBarrier trafficBarrier = new TrafficBarrier();
        trafficBarrier.setPosition(position);
        trafficBarrier.horizontalNotVertical = horizontalNotVertical;
        trafficBarrier.opennessRhythm = Arrays.stream(opennessRhythm).boxed().toList();
        return trafficBarrier;
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
        return "";
    }

    public boolean isHorizontalNotVertical() {
        return horizontalNotVertical;
    }

    public List<Integer> getOpennessRhythm() {
        return opennessRhythm;
    }

    public void setReferenceTurn(long referenceTurn) {
        this.referenceTurn = referenceTurn;
    }

    /**
     * Indicates whether the traffic barrier should be open according to its schedule.
     * This information can be used to paint the traffic light green or red.
     * Even if the traffic barrier is closed as per schedule, it could be open in case a piece is on its tile.
     * The method openAsPerReality() indicates the real state of the traffic light.
     */
    public boolean openAsPerSchedule(long turn) {
        long turnsPassed = turn - referenceTurn;
        int[] remainingPhaseDuration = ArithmeticUtils.remainingPhaseDuration(opennessRhythm, turnsPassed);
        int phaseNumber = remainingPhaseDuration[0];
        return phaseNumber % 2 == 0;
    }

    public boolean openAsPerReality(long turn) {

        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(getPosition().getMapType());
        List<Piece> blockingPieces = map.getMapPieces().piecesAtPosition(e -> !(e instanceof TrafficBarrier), getPosition().getX(), getPosition().getY());
        if (!blockingPieces.isEmpty()) {
            return true;
        }
        //Player is not in the piece cache.
        Player player = ApplicationContextUtils.getPlayer();
        if (player.getPosition().equals(getPosition())) {
            return true;
        }
        return openAsPerSchedule(turn);
    }

}
