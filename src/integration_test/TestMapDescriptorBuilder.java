package integration_test;

import antonafanasjew.cosmodog.globals.DefaultMapDescriptorBuilder;
import antonafanasjew.cosmodog.model.MapDescriptor;

import java.util.Map;

public class TestMapDescriptorBuilder extends DefaultMapDescriptorBuilder {

    public final static String MAP_NAME_ALTERNATIVE = "ALTERNATIVE";

    @Override
    public Map<String, MapDescriptor> mapDescriptors() {
        Map<String, MapDescriptor> mapDescriptors = super.mapDescriptors();

        MapDescriptor mapDescriptorAlternative = new MapDescriptor(
                MAP_NAME_ALTERNATIVE,
                "assets/maps/map_alternative.tmx",
                400,
                400,
                true,
                true,
                true,
                true
        );

        mapDescriptors.put(mapDescriptorAlternative.getName(), mapDescriptorAlternative);
        return mapDescriptors;

    }
}
