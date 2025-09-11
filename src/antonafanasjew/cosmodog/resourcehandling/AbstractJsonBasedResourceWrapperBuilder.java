package antonafanasjew.cosmodog.resourcehandling;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public abstract class AbstractJsonBasedResourceWrapperBuilder<ENTITY_TYPE> extends AbstractResourceWrapperBuilder<ENTITY_TYPE> {

	@Override
	public Map<String, GenericResourceWrapper<ENTITY_TYPE>> build() {

		String text = resourceText();

		Map<String, GenericResourceWrapper<ENTITY_TYPE>> retVal = Maps.newHashMap();

		JSONArray content = new JSONArray(text);
		for (int i = 0; i < content.length(); i++) {
			JSONObject element = content.getJSONObject(i);
			String entityId = element.getString("id");
			ENTITY_TYPE entity = build(element);
			GenericResourceWrapper<ENTITY_TYPE> rw = new GenericResourceWrapper<ENTITY_TYPE>(entityId, entity);
			retVal.put(rw.getId(), rw);
		}
		afterBuild();
		return retVal;


	}

	protected abstract ENTITY_TYPE build(JSONObject object);

	protected abstract String resourcePath();
}
