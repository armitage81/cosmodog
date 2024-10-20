package antonafanasjew.cosmodog.domains;

public enum MapType {

    MAIN("data/maps/map_main.tmx"),
    ALTERNATIVE("data/maps/map_alternative.tmx");

    private String mapPath;

    private MapType(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return this.mapPath;
    }

}
