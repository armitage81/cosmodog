package antonafanasjew.cosmodog.globals;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.player.DefaultPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;

public class CosmodogModelHolder {

    private static PlayerBuilder playerBuilder = new DefaultPlayerBuilder();

    static {
        try {
            if (System.getProperty("playerBuilder") != null) {
                String playerBuilderTypeName = System.getProperty("playerBuilder");
                @SuppressWarnings("rawtypes")
                Class playerBuilderClass = Class.forName(playerBuilderTypeName);
                playerBuilder = (PlayerBuilder)playerBuilderClass.newInstance();

            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static PlayerBuilder retrievePlayerBuilder() {
        return playerBuilder;
    }

    public static void replacePlayerBuilder(PlayerBuilder playerBuilder) {
        CosmodogModelHolder.playerBuilder = playerBuilder;
    }
}
