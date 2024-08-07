package antonafanasjew.cosmodog.tiledmap;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class TiledObjectGroup implements Serializable {
	
	private static final long serialVersionUID = -4447043724754054146L;

	private String id;
	private String name;
	private Map<String, TiledObject> objects = Maps.newHashMap();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((objects == null) ? 0 : objects.hashCode());
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
		TiledObjectGroup other = (TiledObjectGroup) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (objects == null) {
			if (other.objects != null)
				return false;
		} else if (!objects.equals(other.objects))
			return false;
		return true;
	}

}
