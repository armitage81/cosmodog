package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Map;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class AmmoInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CollectibleAmmo collectibleAmmo = (CollectibleAmmo)piece;
		
		WeaponType weaponType = collectibleAmmo.getWeaponType();
		
		Inventory inventory = player.getInventory();
		ArsenalInventoryItem arsenal = (ArsenalInventoryItem)inventory.get(InventoryItemType.ARSENAL);
		Map<WeaponType, Weapon> owningWeapons = arsenal.getWeaponsCopy();
		
		Weapon weapon = owningWeapons.get(weaponType);
		
		if (weapon != null) {
			weapon.setAmmunition(weapon.getWeaponType().getMaxAmmo());
			
			//Will assign the first available weapon if not selected yet.
			arsenal.getSelectedWeaponType();
			
		}
		
		applicationContext.getSoundResources().get(SoundResources.SOUND_RELOAD).play();		
	}

}
