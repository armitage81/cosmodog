package antonafanasjew.cosmodog.globals;

import antonafanasjew.cosmodog.model.MapDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultMapDescriptorBuilder implements MapDescriptorBuilder {

    @Override
    public Map<String, MapDescriptor> mapDescriptors() {
        Map<String, MapDescriptor> retVal = new HashMap<>();

        MapDescriptor mapDescriptor;

        mapDescriptor = new MapDescriptor(
                MapDescriptor.MAP_NAME_MAIN,
                "assets/maps/map_main.tmx",
                400,
                400,
                true,
                true,
                true,
                true
        );

        retVal.put(mapDescriptor.getName(), mapDescriptor);

        mapDescriptor = new MapDescriptor(
                MapDescriptor.MAP_NAME_SPACE,
                "assets/maps/map_space.tmx",
                400,
                400,
                false,
                false,
                false,
                false
        );

        retVal.put(mapDescriptor.getName(), mapDescriptor);

        return retVal;
    }
}
