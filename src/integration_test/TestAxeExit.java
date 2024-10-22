package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

public class TestAxeExit {

    public static void main(String[] args) throws SlickException {

        PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {

            @Override
            protected void updatePlayer(Player player) {
                player.setPosition(Position.fromCoordinates(119, 209, MapType.MAIN));
                player.setMaxLife(100);
                player.setLife(100);
                player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("119/203;119/209"));


            }
        };

        CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

        TestStarter.main(args);
    }
}
