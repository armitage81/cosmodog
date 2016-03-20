package antonafanasjew.cosmodog.structures;

public class TileCoordinates {

	private int layerIndex;
	private int x;
	private int y;
	
	public TileCoordinates(int x, int y, int layerIndex) {
		this.x = x;
		this.y = y;
		this.layerIndex = layerIndex;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
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
		TileCoordinates other = (TileCoordinates) obj;
		if (layerIndex != other.layerIndex)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public TileCoordinates southNeighbour() {
		return new TileCoordinates(x, y - 1, layerIndex);
	}
	
	public TileCoordinates northNeighbour() {
		return new TileCoordinates(x, y + 1, layerIndex);
	}
	
	public TileCoordinates westNeighbour() {
		return new TileCoordinates(x - 1, y, layerIndex);
	}
	
	public TileCoordinates eastNeighbour() {
		return new TileCoordinates(x + 1, y, layerIndex);
	}

	public int getLayerIndex() {
		return layerIndex;
	}

}
