package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Describes the vision of an enemy, that is all tiles that are visible to that enemy.
 */
public class Vision implements Serializable {

    private final Set<Position> elements = new HashSet<>();

    public Set<Position> getElements() {
        return elements;
    }

    public Set<Position> visiblePositions(Actor observer, int mapWidth, int mapHeight) {
        return visiblePositions(observer, mapWidth, mapHeight, false);
    }

    public Set<Position> visiblePositions(Actor observer, int mapWidth, int mapHeight, boolean includeNegative) {
        DirectionType directionType = observer.getDirection();

        Set<Position> retVal = new HashSet<Position>();

        for (Position visionElement : elements) {
            float visionElementX = visionElement.getX();
            float visionElementY = visionElement.getY();

            if (directionType == DirectionType.DOWN) {
                visionElementY = -visionElementY;
            } else if (directionType == DirectionType.LEFT) {
                float temp = visionElementX;
                visionElementX = visionElementY;
                visionElementY = temp;
            } else if (directionType == DirectionType.RIGHT) {
                float temp = visionElementX;
                visionElementX = -visionElementY;
                visionElementY = temp;
            }
            visionElementX = observer.getPosition().getX() + visionElementX;
            visionElementY = observer.getPosition().getY() + visionElementY;

            if (!includeNegative) {
                if (visionElementX < 0 || visionElementX >= mapWidth) {
                    continue;
                }

                if (visionElementY < 0 || visionElementY >= mapHeight) {
                    continue;
                }
            }

            retVal.add(Position.fromCoordinates(
                    visionElementX,
                    visionElementY,
                    observer.getPosition().getMapDescriptor()
            ));
        }

        return retVal;
    }

    public boolean visible(Actor observer, Position observable, int mapWidth, int mapHeight) {
        return visiblePositions(observer, mapWidth, mapHeight).contains(observable);
    }

    public static boolean playerHidden(CosmodogMap map, Player player) {
        boolean retVal;
        int tileId = map.getTileId(player.getPosition(), Layers.LAYER_META_GROUNDTYPES);
        retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_PLANTS);
        return retVal;
    }

    public static boolean playerNotHiddenAndItIsNight(PlanetaryCalendar planetaryCalendar, CosmodogMap map, Player player) {
        return !playerHidden(map, player) && planetaryCalendar.isNight();
    }

    public static boolean playerNotHiddenAndItIsDay(PlanetaryCalendar planetaryCalendar, CosmodogMap map, Player player) {
        return !playerHidden(map, player) && !planetaryCalendar.isNight();
    }

    public static Vision NIGHT_VISION_FOR_PLAYER_DEFAULT;

    static {
        NIGHT_VISION_FOR_PLAYER_DEFAULT = new Vision();


        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-1, 0, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(0, 0, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(1, 0, null));

        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-1, -1, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(0, -1, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(1, -1, null));

        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-1, -2, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(0, -2, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(1, -2, null));

        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-2, -3, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-1, -3, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(0, -3, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(1, -3, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(2, -3, null));

        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-2, -4, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(-1, -4, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(0, -4, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(1, -4, null));
        NIGHT_VISION_FOR_PLAYER_DEFAULT.getElements().add(Position.fromCoordinates(2, -4, null));
    }

    public static Vision NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT;

    static {
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT = new Vision();

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, 0, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, 0, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, 0, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -1, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -1, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -1, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-2, -2, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -2, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -2, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -2, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(2, -2, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-2, -3, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -3, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -3, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -3, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(2, -3, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-2, -4, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -4, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -4, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -4, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(2, -4, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-3, -5, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-2, -5, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -5, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -5, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -5, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(2, -5, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(3, -5, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-3, -6, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-2, -6, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -6, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -6, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -6, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(2, -6, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(3, -6, null));

        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-3, -7, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-2, -7, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(-1, -7, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(0, -7, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(1, -7, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(2, -7, null));
        NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT.getElements().add(Position.fromCoordinates(3, -7, null));
    }

    public static Vision NIGHT_VISION_FOR_PLAYER_IN_VEHICLE;

    static {
        NIGHT_VISION_FOR_PLAYER_IN_VEHICLE = new Vision();
        NIGHT_VISION_FOR_PLAYER_IN_VEHICLE.getElements().add(Position.fromCoordinates(0, 0, null));
        NIGHT_VISION_FOR_PLAYER_IN_VEHICLE.getElements().add(Position.fromCoordinates(-1, -1, null));
        NIGHT_VISION_FOR_PLAYER_IN_VEHICLE.getElements().add(Position.fromCoordinates(0, -1, null));
        NIGHT_VISION_FOR_PLAYER_IN_VEHICLE.getElements().add(Position.fromCoordinates(1, -1, null));
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j >= -20; j--) {
                NIGHT_VISION_FOR_PLAYER_IN_VEHICLE.getElements().add(Position.fromCoordinates(i, j, null));
            }
        }
    }

    public static Vision NIGHT_VISION_FOR_PLAYER_IN_PLATFORM;

    static {
        NIGHT_VISION_FOR_PLAYER_IN_PLATFORM = new Vision();
        int radius = 9;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                int distanceX = Math.abs(i);
                int distanceY = Math.abs(j);
                int distance = distanceX + distanceY;
                if (distance <= radius) {
                    NIGHT_VISION_FOR_PLAYER_IN_PLATFORM.getElements().add(Position.fromCoordinates(i, j, null));
                }
            }
        }
    }
}
