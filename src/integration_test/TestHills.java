package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestHills {

    public static void main(String[] args) throws SlickException {

        PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {

            @Override
            protected void updatePlayer(Player player) {
                player.setPosition(Position.fromCoordinates(13, 58, ApplicationContextUtils.mapDescriptorMain()));
                player.setMaxLife(100);
                player.setLife(100);
                player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
                player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
                player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
                player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
                player.getGameProgress().setWormActive(false);

                List<Position> positions = new ArrayList<>();


            }
        };

        CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

        TestStarter.main(args);
    }
}
