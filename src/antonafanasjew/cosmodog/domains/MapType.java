package antonafanasjew.cosmodog.domains;

public enum MapType {

    MAIN("data/maps/map_main.tmx", 400, 400),
    ALTERNATIVE("data/maps/map_alternative.tmx", 400, 400),
    SPACE("data/maps/map_space.tmx", 400, 400);

    private final String mapPath;
    private final int width;
    private final int height;

    private MapType(String mapPath, int mapWidth, int mapHeight) {
        this.mapPath = mapPath;
        this.width = mapWidth;
        this.height = mapHeight;
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
}
