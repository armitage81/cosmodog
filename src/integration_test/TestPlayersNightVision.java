package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestPlayersNightVision {

    public static void main(String[] args) throws SlickException {

        PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {

            @Override
            protected void updatePlayer(Player player) {
                player.setPosition(Position.fromCoordinates(38, 38, ApplicationContextUtils.mapDescriptor("ALTERNATIVE")));
                player.setMaxLife(100);
                player.setLife(100);
                player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());


                List<Position> positions = new ArrayList<>();
                positions.add(Position.fromCoordinates(7,3, ApplicationContextUtils.mapDescriptorMain()));
                positions.add(Position.fromCoordinates(38, 38, ApplicationContextUtils.mapDescriptor("ALTERNATIVE")));

                player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(positions));

                ChartInventoryItem chart = new ChartInventoryItem();

                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        chart.discoverPiece(i,j);
                    }
                }


                player.getInventory().put(InventoryItemType.CHART, chart);


            }
        };

        CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

        TestStarter.main(args);
    }
}
