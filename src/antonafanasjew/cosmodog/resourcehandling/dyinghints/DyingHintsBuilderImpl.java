package antonafanasjew.cosmodog.resourcehandling.dyinghints;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class DyingHintsBuilderImpl implements DyingHintsBuilder {

	@Override
	public List<String> build(String resourcePath)  throws IOException {
		File file = new File(resourcePath);
		String content = Files.toString(file, Charsets.UTF_8);
		
		String[] parts = content.split("[\r\n]+");
		
		List<String> retVal = Lists.asList(parts[0], parts);
		
		return retVal;
	}

}
