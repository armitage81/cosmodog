package antonafanasjew.cosmodog.resourcehandling;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;

public abstract class AbstractResourceWrapperBuilder<ENTITY_TYPE> implements ResourceWrapperBuilder<ENTITY_TYPE> {

    protected String resourceText() {
        URL url = Resources.getResource(resourcePath());
        String text = null;
        try {
            text = Resources.toString(url, Charsets.UTF_8);
            return text;
        } catch (IOException e) {
            throw new RuntimeException("Could not load builder properties: " + e.getLocalizedMessage(), e);
        }
    }

    protected abstract String resourcePath();

    protected void afterBuild() {

    }

}
