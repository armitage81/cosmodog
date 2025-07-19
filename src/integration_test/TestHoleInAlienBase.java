package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArcheologistsJournalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

public class TestHoleInAlienBase {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(193, 269, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getInventory().put(InventoryItemType.ARCHEOLOGISTS_JOURNAL, new ArcheologistsJournalInventoryItem());
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("206/270;202/266;193/269"));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
