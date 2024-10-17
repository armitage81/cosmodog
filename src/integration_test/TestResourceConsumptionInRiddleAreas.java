package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

public class TestResourceConsumptionInRiddleAreas {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(242, 187));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
				player.getInventory().put(InventoryItemType.AXE, new AxeInventoryItem());
				player.getInventory().put(InventoryItemType.MACHETE, new MacheteInventoryItem());
				player.getInventory().put(InventoryItemType.PICK, new PickInventoryItem());
				player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
				player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
				player.getInventory().put(InventoryItemType.ANTIDOTE, new AntidoteInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("181/64;86/145;298/180;77/213;355/327;242/187"));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
