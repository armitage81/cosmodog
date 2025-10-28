package antonafanasjew.cosmodog.globals;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.player.DefaultPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;

public class CosmodogModelHolder {

    private static PlayerBuilder playerBuilder = new DefaultPlayerBuilder();

    private static MapDescriptorBuilder mapDescriptorBuilder = new DefaultMapDescriptorBuilder();

    static {
        try {
            if (System.getProperty("playerBuilder") != null) {
                String playerBuilderTypeName = System.getProperty("playerBuilder");
                @SuppressWarnings("rawtypes")
                Class playerBuilderClass = Class.forName(playerBuilderTypeName);
                playerBuilder = (PlayerBuilder)playerBuilderClass.newInstance();

            }

            if (System.getProperty("mapDescriptorBuilder") != null) {
                String mapDescriptorBuilderTypeName = System.getProperty("mapDescriptorBuilder");
                @SuppressWarnings("rawtypes")
                Class mapDescriptorBuilderClass = Class.forName(mapDescriptorBuilderTypeName);
                mapDescriptorBuilder = (MapDescriptorBuilder) mapDescriptorBuilderClass.newInstance();

            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerBuilder retrievePlayerBuilder() {
        return playerBuilder;
    }

    public static MapDescriptorBuilder retrieveMapDescriptorBuilder() {
        return mapDescriptorBuilder;
    }

    public static void replacePlayerBuilder(PlayerBuilder playerBuilder) {
        CosmodogModelHolder.playerBuilder = playerBuilder;
    }

    public static void replaceMapDescriptorBuilder(MapDescriptorBuilder mapDescriptorBuilder) {
        CosmodogModelHolder.mapDescriptorBuilder = mapDescriptorBuilder;
    }
}
