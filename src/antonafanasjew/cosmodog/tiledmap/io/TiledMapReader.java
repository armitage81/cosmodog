package antonafanasjew.cosmodog.tiledmap.io;

import antonafanasjew.cosmodog.CustomTiledMap;

public interface TiledMapReader {

	CustomTiledMap readTiledMap(String fileName) throws TiledMapIoException;
	
}
