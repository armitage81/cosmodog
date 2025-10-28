package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.topology.Position;

import java.util.Set;

public class VisibilityCalculatorForPlayer {

    public static VisibilityCalculatorForPlayer instance = new VisibilityCalculatorForPlayer();

    private PlanetaryCalendar planetaryCalendar;
    private Player player;
    private CosmodogMap map;

    private VisibilityCalculatorForPlayer() {

    }

    public static VisibilityCalculatorForPlayer instance() {
        return instance;
    }

    public boolean visible(Player player, CosmodogMap map, PlanetaryCalendar planetaryCalendar, Position position) {

        if (!map.getMapDescriptor().isDayNightActive()) {
            return true;
        }

        if (!planetaryCalendar.isNight()) {
            return true;
        }

        if (player.getInventory().hasItem(InventoryItemType.NIGHT_VISION_GOGGLES)) {
            return true;
        }

        Vision vision;

        if (player.getInventory().hasVehicle()) {
            vision = Vision.NIGHT_VISION_FOR_PLAYER_IN_VEHICLE;
        } else if (player.getInventory().hasPlatform()) {
            vision = Vision.NIGHT_VISION_FOR_PLAYER_IN_PLATFORM;
        } else if (player.getInventory().hasItem(InventoryItemType.FLASHLIGHT)) {
            vision = Vision.NIGHT_VISION_FOR_PLAYER_WITH_FLASHLIGHT;
        } else {
            vision = Vision.NIGHT_VISION_FOR_PLAYER_DEFAULT;
        }

        Set<Position> visiblePositions = vision.visiblePositions(player, map.getMapDescriptor().getWidth(), map.getMapDescriptor().getHeight(), true);
        return visiblePositions.contains(position);


    }


}
