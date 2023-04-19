package antonafanasjew.cosmodog.tiledmap.io;

import antonafanasjew.cosmodog.CustomTiledMap;

public interface TiledMapWriter {
	
	void writeTiledMap(CustomTiledMap map) throws TiledMapIoException;
	
}
