package antonafanasjew.cosmodog.caching;

import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.NpcActor;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.*;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.AutoBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.OneWayBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.races.*;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.PresenceDetector;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;

import java.util.Collection;
import java.util.function.Predicate;

public class PiecePredicates {

    public static Predicate<Piece> ALWAYS_TRUE = ( e -> true);

    public static Predicate<Piece> NPC_ACTOR = e -> e instanceof Collectible || e instanceof NpcActor;

    public static Predicate<Piece> GATE = e -> e instanceof Gate;

    public static Predicate<Piece> BINARY_INDICATOR = e -> e instanceof BinaryIndicator;

    public static Predicate<Piece> POISON = e -> e instanceof Poison;

    public static Predicate<Piece> LETTER_PLATE = e -> e instanceof LetterPlate;

    public static Predicate<Piece> MINE = e -> e instanceof Mine;

    public static Predicate<Piece> TIME_BONUS = e -> e instanceof TimeBonus;

    public static Predicate<Piece> RESETTER = e -> e instanceof Resetter;

    public static Predicate<Piece> PRESSURE_BUTTON = e -> e instanceof PressureButton;

    public static Predicate<Piece> PRESENCE_DETECTOR = e -> e instanceof PresenceDetector;

    public static Predicate<Piece> VEHICLE = e -> e instanceof Vehicle;

    public static Predicate<Piece> PLATFORM = e -> e instanceof Platform;

    public static Predicate<Piece> ENEMY = e -> e instanceof Enemy;

    public static Predicate<Piece> COLLECTIBLE = e -> e instanceof Collectible;

    public static Predicate<Piece> GOODIE = piece -> {

        if (!(piece instanceof Collectible coll)) {
            return false;
        }

        if (coll.getCollectibleType() == Collectible.CollectibleType.GOODIE) {
            CollectibleGoodie goodie = (CollectibleGoodie)coll;
            if (goodie.getGoodieType() == CollectibleGoodie.GoodieType.cognition) {
                return false;
            }
        }

        return true;
    };

    //Positions of the undiscovered monoliths (or rather the insights closed to them)
    //are rendered as blinking green or red tiles on the map.
    //This should be moved to the MapRenderer class since the display of player's position is already there.

    public static Predicate<Piece> PIECE_INDICATED_ON_MAP = piece -> {
        boolean interestingGoodie = piece instanceof CollectibleGoodie && (
                ((CollectibleGoodie)piece).getGoodieType() == CollectibleGoodie.GoodieType.insight
                        ||
                        ((CollectibleGoodie)piece).getGoodieType() == CollectibleGoodie.GoodieType.bottle
                        ||
                        ((CollectibleGoodie)piece).getGoodieType() == CollectibleGoodie.GoodieType.foodcompartment
        );

        boolean interestingTool = piece instanceof CollectibleTool;

        boolean interestingWeapons = piece instanceof CollectibleWeapon;

        return interestingGoodie || interestingTool || interestingWeapons;

    };

    public static Predicate<Piece> MOVEABLE_DYNAMIC_PIECE = e -> e instanceof MoveableDynamicPiece;

    public static Predicate<Piece> TRAFFIC_BARRIER = e -> e instanceof TrafficBarrier;

    public static Predicate<Piece> RACE_EXIT = e -> e instanceof RaceExit;

    public static Predicate<Piece> POLE_POSITION = e -> e instanceof PolePosition;

    public static Predicate<Piece> FINISH_LINE = e -> e instanceof FinishLine;

    public static Predicate<Piece> SECRET_DOOR = e -> e instanceof SecretDoor;

    public static Predicate<Piece> SWITCHABLE = e -> e instanceof Switchable;

    public static Predicate<Piece> ACTIVATABLE = e -> e instanceof Activatable;

    public static Predicate<Piece> AUTOBOLLARD = e -> e instanceof AutoBollard;

    public static Predicate<Piece> ONE_WAY_BOLLARD = e -> e instanceof OneWayBollard;

}
