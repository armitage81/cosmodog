package antonafanasjew.cosmodog.resourcehandling;

import java.util.Map;

import com.google.common.collect.Maps;


public class GenericResourceWrapper<ENTITY> implements ResourceWrapper {

	private ENTITY entity;
	private String id;
	private Map<String, String> properties = Maps.newHashMap();

	public GenericResourceWrapper(String id, ENTITY entity) {
		this.id = id;
		this.entity = entity;
	}
	
	@Override
	public String getId() {
		return this.id;
	}

	public ENTITY getEntity() {
		return entity;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
