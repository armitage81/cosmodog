package antonafanasjew.cosmodog.tiledmap.io;

import antonafanasjew.cosmodog.CustomTiledMap;

public class ReadAndWriteMap {

    public static void main(String[] args) throws TiledMapIoException {

        String inputPath = System.getProperty("mapPath.in");
        String outputPath = System.getProperty("mapPath.out");

        XmlTiledMapReader reader = new XmlTiledMapReader();
        XmlTiledMapWriter writer = new XmlTiledMapWriter();
        CustomTiledMap map = reader.readTiledMap(inputPath);
        writer.writeTiledMap(map, outputPath);
    }

}
