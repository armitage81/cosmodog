package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.portals.ReflectionType;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.topology.Position;

public class Reflector extends DynamicPiece implements Switchable {

    public static short VISUAL_STATE_IDLE = 0;
    public static short VISUAL_STATE_PHASE1 = 1;
    public static short VISUAL_STATE_PHASE2 = 2;
    public static short VISUAL_STATE_PHASE3 = 3;
    public static short VISUAL_STATE_PHASE4 = 4;
    public static short VISUAL_STATE_PHASE5 = 5;

    private ReflectionType reflectionType;
    private short visualState = VISUAL_STATE_IDLE;

    public static Reflector create(Position position, ReflectionType reflectionType) {
        Reflector reflector = new Reflector();
        reflector.setPosition(position);
        reflector.setReflectionType(reflectionType);
        return reflector;
    }

    public ReflectionType getReflectionType() {
        return reflectionType;
    }

    public void setReflectionType(ReflectionType reflectionType) {
        this.reflectionType = reflectionType;
    }

    @Override
    public int numberOfStates() {
        return 4;
    }

    @Override
    public int currentState() {
        return 0;
    }

    @Override
    public void switchToNextState() {
        reflectionType = ReflectionType.next(reflectionType);
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return false;
    }

    /*
    Take care: As opposed to the default rendered dynamic pieces (compare Bollard)
    this animation id does not reflect (no pun intended) the visual state of the reflector.
    The animations of Bollards and similar dynamic pieces have one image each
    and each "animation" reflects exactly the current visual state
    of the piece (f.i. a phase of raising a bollard or opening a door).
    Reflector's animations contain all sprites of turning a reflector.
    The renderer must then select the right frame based on the visual state.

    Another peculiarity as compared to the default rendering:
    There are no "top" animations. The renderer must ignore the "top" part as it is not relevant in this case.

     */
    @Override
    public String animationId(boolean bottomNotTop) {
        if (bottomNotTop) {
            if (reflectionType == ReflectionType.NORTH_EAST) {
                return "dynamicPieceReflectorClockwiseNe";
            } else if (reflectionType == ReflectionType.NORTH_WEST) {
                return "dynamicPieceReflectorClockwiseNw";
            } else if (reflectionType == ReflectionType.SOUTH_WEST) {
                return "dynamicPieceReflectorClockwiseSw";
            } else if (reflectionType == ReflectionType.SOUTH_EAST) {
                return "dynamicPieceReflectorClockwiseSe";
            }
        }
        return "";
    }

    public short getVisualState() {
        return visualState;
    }

    public void setVisualState(short visualState) {
        this.visualState = visualState;
    }
}
