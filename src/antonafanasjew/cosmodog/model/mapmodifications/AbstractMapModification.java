package antonafanasjew.cosmodog.model.mapmodifications;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.TileType;

public abstract class AbstractMapModification implements MapModification {

	private static final long serialVersionUID = -1895014707130822509L;

	@Override
	public int getTileId(CustomTiledMap tiledMap, int x, int y, int layerIndex) {
		return getTileIdInternal(tiledMap, x, y, layerIndex);
	}

	@Override
	public void modifyTile(int x, int y, int layerIndex, TileType tileType) {
		modifyTileInternal(x, y, layerIndex, tileType);
	}
	

	protected abstract int getTileIdInternal(CustomTiledMap tiledMap, int x, int y, int layerIndex);

	protected abstract void modifyTileInternal(int x, int y, int layerIndex, TileType tileType);
}
