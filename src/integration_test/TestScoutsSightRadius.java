package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestScoutsSightRadius {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPositionX(100);
				player.setPositionY(120);
				player.setMaxLife(100);
				player.setLife(100);
				Arsenal arsenal = player.getArsenal();
				arsenal.addWeaponToArsenal(new Weapon(WeaponType.RPG));
				arsenal.addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
				arsenal.addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				arsenal.addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
				arsenal.addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem());
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
