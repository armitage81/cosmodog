package antonafanasjew.cosmodog.resourcehandling;

import java.util.Map;

public interface ResourceWrapperBuilder<ENTITY_TYPE> {

	Map<String, GenericResourceWrapper<ENTITY_TYPE>> build();
	
}
