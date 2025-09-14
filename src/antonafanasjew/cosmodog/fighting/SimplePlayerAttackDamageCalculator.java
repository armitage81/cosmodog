package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.util.EnemiesUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

/**
 * Returns the damage as difference between players constant base damage and the damage absorption from the enemy's armor type.
 * Cannot be less than 1.
 */
public class SimplePlayerAttackDamageCalculator extends AbstractPlayerAttackDamageCalculator {

	public static final int SIMPLE_PLAYER_DAMAGE = 1;
	
	private final PlanetaryCalendar planetaryCalendar;
	
	public SimplePlayerAttackDamageCalculator(PlanetaryCalendar planetaryCalendar) {
		this.planetaryCalendar = planetaryCalendar;
	}
	
	@Override
	public int playerAttackDamageInternal(Player player, Enemy enemy) {
		
		ArmorType enemyArmorType = enemy.getArmorType();
		
		Arsenal arsenal = player.getArsenal();
		
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		
		int damage;
		boolean enemyActive = EnemiesUtils.enemyActive(enemy);
		if (!enemyActive) {
			damage = 100;
		} else if (enemy.getUnitType() == UnitType.ARTILLERY) {
			damage = 100;
		} else {
			damage = selectedWeaponType == null ? SIMPLE_PLAYER_DAMAGE : selectedWeaponType.getDamage(enemyArmorType);
		
			DirectionType playerDirection = player.getDirection();
			DirectionType enemyDirection = enemy.getDirection();
			DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(player, enemy);
			
			boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
			boolean enemyLooksAway = enemyDirection.equals(playerDirection);
			//Disallow critical hits for the ranged and stationary units as they always see the player if he approaches.
			boolean criticalHitsAllowed = !enemy.getUnitType().isRangedUnit() && !(enemy.getSpeedFactor() == 0.0f);
			
			if (playerLooksAtEnemy && enemyLooksAway && criticalHitsAllowed) {
				damage *= 2;
			}
			
			boolean fists = selectedWeaponType == WeaponType.FISTS;
			boolean weaponFirmwareUpgrade = player.getInventory().get(InventoryItemType.WEAPON_FIRMWARE_UPGRADE) != null;
			
			if (!fists && weaponFirmwareUpgrade) {
				damage *= 3;
			}

		}
		
		return damage;
		
	}

}
