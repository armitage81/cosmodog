package antonafanasjew.cosmodog.domains;

public enum MapType {

    MAIN("assets/maps/map_main.tmx", 400, 400, true, true, true, true),
    ALTERNATIVE("assets/maps/map_alternative.tmx", 400, 400, true, true, true, true),
    SPACE("assets/maps/map_space.tmx", 400, 400, false, false, false, false);

    private final String mapPath;
    private final int width;
    private final int height;

    private final boolean dayNightActive;
    private final boolean skyDecorationsActive;
    private final boolean resourceConsumptionActive;
    private final boolean weaponsActive;

    private MapType(String mapPath, int mapWidth, int mapHeight, boolean dayNightActive, boolean skyDecorationsActive, boolean resourceConsumptionActive, boolean weaponsActive) {
        this.mapPath = mapPath;
        this.width = mapWidth;
        this.height = mapHeight;
        this.dayNightActive = dayNightActive;
        this.skyDecorationsActive = skyDecorationsActive;
        this.resourceConsumptionActive = resourceConsumptionActive;
        this.weaponsActive = weaponsActive;
    }

    public String getMapPath() {
        return this.mapPath;
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
}
