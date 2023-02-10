package antonafanasjew.cosmodog.resourcehandling.dyinghints;

import java.io.IOException;
import java.util.List;

public interface DyingHintsBuilder {

	List<String> build(String resourcePath) throws IOException;
	
}
