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

import java.util.function.Predicate;
import java.util.function.Supplier;

public class SimplePlayerAttackDamageCalculator extends AbstractPlayerAttackDamageCalculator {

	public static final int CRITICAL_HIT_CHANCE_IN_PERCENT = 10;

	private Supplier<Long> timestampSupplier = System::currentTimeMillis;
	private Predicate<Enemy> enemyActivityPredicate = EnemiesUtils::enemyActive;

	@Override
	public Damage playerAttackDamageInternal(Player player, Enemy enemy) {

		Damage damage = new Damage();

		ArmorType enemyArmorType = enemy.getArmorType();
		
		Arsenal arsenal = player.getArsenal();
		
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();

		int ammo = arsenal.getWeaponsCopy().get(selectedWeaponType).getAmmunition();
		if (ammo <= 0) {
			selectedWeaponType = WeaponType.FISTS;
		}
		
		int amount;
		boolean enemyActive = enemyActivityPredicate.test(enemy);
		if (!enemyActive) {
			amount = 100;
			damage.setIncludingOffGuard(true);
		} else if (enemy.getUnitType() == UnitType.ARTILLERY) {
			amount = 100;
			damage.setIncludingOffGuard(true);
		} else {
			amount = selectedWeaponType.getDamage(enemyArmorType);
		
			DirectionType playerDirection = player.getDirection();
			DirectionType enemyDirection = enemy.getDirection();
			DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(player, enemy);
			
			boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
			boolean enemyLooksAway = enemyDirection.equals(playerDirection);
			//Disallow backstabbing hits for the ranged and stationary units as they always see the player if he approaches.
			boolean backstabbingAllowed = !enemy.getUnitType().isRangedUnit() && !(enemy.getSpeedFactor() == 0.0f);
			
			if (playerLooksAtEnemy && enemyLooksAway && backstabbingAllowed) {
				amount *= 2;
				damage.setIncludingBackstabbing(true);
			}
			
			boolean fists = selectedWeaponType == WeaponType.FISTS;
			boolean weaponFirmwareUpgrade = player.getInventory().get(InventoryItemType.WEAPON_FIRMWARE_UPGRADE) != null;
			
			if (!fists && weaponFirmwareUpgrade) {
				amount *= 3;
				damage.setIncludingUpgradeBonus(true);
			}

			boolean criticalHit = timestampSupplier.get() % 100 < CRITICAL_HIT_CHANCE_IN_PERCENT;

			if (criticalHit) {
				amount *= 3;
				damage.setIncludingCriticalHit(true);
			}

		}
		damage.setAmount(amount);
		return damage;
		
	}

	public void setTimestampSupplier(Supplier<Long> timstampSupplier) {
		this.timestampSupplier = timstampSupplier;
	}

	public void setEnemyActivityPredicate(Predicate<Enemy> enemyActivityPredicate) {
		this.enemyActivityPredicate = enemyActivityPredicate;
	}
}
