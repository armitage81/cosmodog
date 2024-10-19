package antonafanasjew.cosmodog.tiledmap.io;

import antonafanasjew.cosmodog.CustomTiledMap;

class XmlTiledMapWriterTest {

	public static void main(String[] args) throws TiledMapIoException {
		String originalMapFile = "C:\\Workspaces\\cosmodog\\data\\maps\\map_main.tmx";
		String resultingMapFile = "C:\\Temp\\map_main.stored.tmx";
		
		TiledMapReader originalReader = new XmlTiledMapReader(originalMapFile);
		TiledMapWriter writer = new XmlTiledMapWriter(resultingMapFile);
		TiledMapReader resultingReader = new XmlTiledMapReader(resultingMapFile);
		
		CustomTiledMap originalMap = originalReader.readTiledMap();
		writer.writeTiledMap(originalMap);
		CustomTiledMap resultingTiledMap = resultingReader.readTiledMap();
		
		boolean equal = resultingTiledMap.equals(originalMap);
		
		System.out.println(equal? "OK" : "KO");
		
	}
	
	
}
