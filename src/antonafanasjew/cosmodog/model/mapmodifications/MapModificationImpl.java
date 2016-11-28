package antonafanasjew.cosmodog.model.mapmodifications;

import java.io.Serializable;
import java.util.Map;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.TileType;

import com.google.common.collect.Maps;

public class MapModificationImpl extends AbstractMapModification {

	
	private static final long serialVersionUID = 7838421344982306133L;


	static class TileAddress implements Serializable {
		
		private static final long serialVersionUID = 4907188271296115453L;

		public int x;
		public int y;
		public int layerIndex;
		
		public static TileAddress fromCoordinates(int x, int y, int layerIndex) {
			TileAddress tileAddress = new TileAddress();
			tileAddress.x = x;
			tileAddress.y = y;
			tileAddress.layerIndex = layerIndex;
			return tileAddress;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + layerIndex;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TileAddress other = (TileAddress) obj;
			if (layerIndex != other.layerIndex)
				return false;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		
	}
	
	private Map<TileAddress, TileType> modifications = Maps.newHashMap();
	
	
	@Override
	protected int getTileIdInternal(CustomTiledMap tiledMap, int x, int y, int layerIndex) {
		TileAddress tileAddress = TileAddress.fromCoordinates(x, y, layerIndex);
		TileType tileType = modifications.get(tileAddress);
		return tileType == null ? tiledMap.getTileId(x, y, layerIndex) : tileType.getTileId();
	}


	@Override
	protected void modifyTileInternal(int x, int y, int layerIndex, TileType tileType) {
		TileAddress tileAddress = TileAddress.fromCoordinates(x, y, layerIndex);
		modifications.put(tileAddress, tileType);
	}

	

}
