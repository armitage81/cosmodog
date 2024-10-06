package antonafanasjew.cosmodog.resourcehandling;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.newdawn.slick.util.Log;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

public abstract class AbstractCsvBasedResourceWrapperBuilder<ENTITY_TYPE> extends AbstractResourceWrapperBuilder<ENTITY_TYPE> {

	@Override
	public Map<String, GenericResourceWrapper<ENTITY_TYPE>> build() {

		String text = resourceText();

		Map<String, GenericResourceWrapper<ENTITY_TYPE>> retVal = Maps.newHashMap();
		
		String[] lines = text.split(System.getProperty("line.separator"));
		//We start with 1 to skip the header;
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i].trim();
			if (line != null && line.trim().isEmpty() == false) {
				
				String entityId = line.split(";")[0];
				
				ENTITY_TYPE entity = build(line);
				
				GenericResourceWrapper<ENTITY_TYPE> rw = new GenericResourceWrapper<ENTITY_TYPE>(entityId, entity);
				
				retVal.put(rw.getId(), rw);
			}
		}
		afterBuild();
		return retVal;
	}

	protected abstract ENTITY_TYPE build(String line);

}
