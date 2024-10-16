package antonafanasjew.cosmodog.model.mapmodifications;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.topology.Position;

public abstract class AbstractMapModification implements MapModification {

	private static final long serialVersionUID = -1895014707130822509L;

	@Override
	public int getTileId(CustomTiledMap tiledMap, Position position, int layerIndex) {
		return getTileIdInternal(tiledMap, position, layerIndex);
	}

	@Override
	public void modifyTile(Position position, int layerIndex, TileType tileType) {
		modifyTileInternal(position, layerIndex, tileType);
	}
	

	protected abstract int getTileIdInternal(CustomTiledMap tiledMap, Position position, int layerIndex);

	protected abstract void modifyTileInternal(Position position, int layerIndex, TileType tileType);
}
