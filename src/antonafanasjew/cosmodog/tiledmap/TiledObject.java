package antonafanasjew.cosmodog.tiledmap;

import java.io.Serializable;
import java.util.Map;

import com.google.common.collect.Maps;

public abstract class TiledObject implements Serializable {
	
	private static final long serialVersionUID = -3148365758539892610L;

	private int id;
	private float x;
	private float y;
	
	private String name;
	private String type;
	
	private Map<String, String> properties = Maps.newHashMap();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
