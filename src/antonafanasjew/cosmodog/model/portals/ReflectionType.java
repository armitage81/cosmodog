package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;

import java.util.Optional;

public enum ReflectionType {

    NORTH_WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST;


    public static ReflectionType next(ReflectionType reflectionType) {

        if (reflectionType == NORTH_WEST) {
            return NORTH_EAST;
        }
        if (reflectionType == NORTH_EAST) {
            return SOUTH_EAST;
        }

        if (reflectionType == SOUTH_EAST) {
            return SOUTH_WEST;
        }

        if (reflectionType == SOUTH_WEST) {
            return NORTH_WEST;
        }

        throw new RuntimeException();
    }

    public static Optional<DirectionType> reflectionDirection(ReflectionType reflectionType, DirectionType directionType) {

        Optional<DirectionType> retVal = Optional.empty();

        if (directionType == DirectionType.RIGHT) {
            if (reflectionType == NORTH_WEST) {
                retVal = Optional.of(DirectionType.UP);
            }
            if (reflectionType == SOUTH_WEST) {
                retVal = Optional.of(DirectionType.DOWN);
            }
        }

        if (directionType == DirectionType.LEFT) {
            if (reflectionType == NORTH_EAST) {
                retVal = Optional.of(DirectionType.UP);
            }
            if (reflectionType == SOUTH_EAST) {
                retVal = Optional.of(DirectionType.DOWN);
            }
        }

        if (directionType == DirectionType.UP) {
            if (reflectionType == SOUTH_EAST) {
                retVal = Optional.of(DirectionType.RIGHT);
            }
            if (reflectionType == SOUTH_WEST) {
                retVal = Optional.of(DirectionType.LEFT);
            }
        }

        if (directionType == DirectionType.DOWN) {
            if (reflectionType == NORTH_EAST) {
                retVal = Optional.of(DirectionType.RIGHT);
            }
            if (reflectionType == NORTH_WEST) {
                retVal = Optional.of(DirectionType.LEFT);
            }
        }

        return retVal;
    }

}
