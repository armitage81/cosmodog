package antonafanasjew.cosmodog.model;

import java.io.Serializable;

public class MapDescriptor implements Serializable {

    public static final String MAP_NAME_MAIN = "MAIN";
    public static final String MAP_NAME_SPACE = "SPACE";

    private final String name;

    private final String mapPath;
    private final int width;
    private final int height;

    private final boolean dayNightActive;
    private final boolean skyDecorationsActive;
    private final boolean resourceConsumptionActive;
    private final boolean weaponsActive;

    public MapDescriptor(String name, String mapPath, int mapWidth, int mapHeight, boolean dayNightActive, boolean skyDecorationsActive, boolean resourceConsumptionActive, boolean weaponsActive) {
        this.name = name;
        this.mapPath = mapPath;
        this.width = mapWidth;
        this.height = mapHeight;
        this.dayNightActive = dayNightActive;
        this.skyDecorationsActive = skyDecorationsActive;
        this.resourceConsumptionActive = resourceConsumptionActive;
        this.weaponsActive = weaponsActive;
    }

    public String getName() {
        return name;
    }

    public String getMapPath() {

        String mapFile = System.getProperty("cosmodog.mapFile." + getName());
        String decoratedMapPath;
        if (mapFile != null) {
            decoratedMapPath = "data/maps/" + mapFile;
        } else {
            decoratedMapPath = this.mapPath;
        }

        return decoratedMapPath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDayNightActive() {
        return dayNightActive;
    }

    public boolean isResourceConsumptionActive() {
        return resourceConsumptionActive;
    }

    public boolean isSkyDecorationsActive() {
        return skyDecorationsActive;
    }

    public boolean isWeaponsActive() {
        return weaponsActive;
    }

    public boolean equals(Object other) {
    	if (!(other instanceof MapDescriptor otherMapDescriptor)) {
    		return false;
    	}
        return this.name.equals(otherMapDescriptor.getName());
    }

    public int hashCode() {
    	return this.name.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
