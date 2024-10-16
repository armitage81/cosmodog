package antonafanasjew.cosmodog.model.mapmodifications;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.TileType;

import antonafanasjew.cosmodog.topology.Position;
import com.google.common.collect.Maps;

public class MapModificationImpl extends AbstractMapModification {

	
	@Serial
	private static final long serialVersionUID = 7838421344982306133L;


	static class TileAddress implements Serializable {
		
		@Serial
		private static final long serialVersionUID = 4907188271296115453L;

		public Position position;
		public int layerIndex;
		
		public static TileAddress fromCoordinates(Position position, int layerIndex) {
			TileAddress tileAddress = new TileAddress();
			tileAddress.position = position;
			tileAddress.layerIndex = layerIndex;
			return tileAddress;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			float result = 1;
			result = prime * result + layerIndex;
			result = prime * result + position.getX();
			result = prime * result + position.getY();
			return (int)result;
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
			if (!this.position.equals(other.position))
				return false;
			return true;
		}
		
	}
	
	private Map<TileAddress, TileType> modifications = Maps.newHashMap();
	
	
	@Override
	protected int getTileIdInternal(CustomTiledMap tiledMap, Position position, int layerIndex) {
		TileAddress tileAddress = TileAddress.fromCoordinates(position, layerIndex);
		TileType tileType = modifications.get(tileAddress);
		return tileType == null ? tiledMap.getTileId(position, layerIndex) : tileType.getTileId();
	}


	@Override
	protected void modifyTileInternal(Position position, int layerIndex, TileType tileType) {
		TileAddress tileAddress = TileAddress.fromCoordinates(position, layerIndex);
		modifications.put(tileAddress, tileType);
	}

	

}
