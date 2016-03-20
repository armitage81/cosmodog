package antonafanasjew.cosmodog.tiledmap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TiledObjectGroup implements Serializable {
	
	private static final long serialVersionUID = -4447043724754054146L;

	private String name;
	private Map<String, TiledObject> objects = Maps.newHashMap();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, TiledObject> getObjects() {
		return objects;
	}
	
	public void setObjects(List<TiledObject> objects) {
		
		for (TiledObject object : objects) {
			this.objects.put(object.getName(), object);
		}
	}
	
	@Override
	public String toString() {
		return "[OBJECTGROUP: " + this.name + " OBJECTS: " + Joiner.on(" ").join(objects.values()) + "]";
	}
	
}
