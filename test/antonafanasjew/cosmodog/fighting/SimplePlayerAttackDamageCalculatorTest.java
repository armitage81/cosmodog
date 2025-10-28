package antonafanasjew.cosmodog.fighting;


import antonafanasjew.cosmodog.domains.*;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.WeaponFirmwareUpgradeInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SimplePlayerAttackDamageCalculatorTest {

	private Player defaultPlayer()  {
		Player player = new Player();
		Weapon weapon;
		weapon = new Weapon(WeaponType.PISTOL);
		weapon.setAmmunition(10);
		player.getArsenal().addWeaponToArsenal(weapon);

		weapon = new Weapon(WeaponType.SHOTGUN);
		weapon.setAmmunition(5);
		player.getArsenal().addWeaponToArsenal(weapon);

		weapon = new Weapon(WeaponType.RIFLE);
		weapon.setAmmunition(10);
		player.getArsenal().addWeaponToArsenal(weapon);

		weapon = new Weapon(WeaponType.MACHINEGUN);
		weapon.setAmmunition(6);
		player.getArsenal().addWeaponToArsenal(weapon);

		weapon = new Weapon(WeaponType.RPG);
		weapon.setAmmunition(3);
		player.getArsenal().addWeaponToArsenal(weapon);

		player.setPosition(Position.fromCoordinates(0, 0, ApplicationContextUtils.mapDescriptorMain()));
		player.setDirection(DirectionType.RIGHT);
        return player;
    }

	private Enemy defaultEnemy() {
		Enemy enemy = new Enemy();
		enemy.setPosition(Position.fromCoordinates(1, 0, ApplicationContextUtils.mapDescriptorMain()));
		enemy.setDirection(DirectionType.LEFT);
		enemy.setUnitType(UnitType.ROBOT);
		enemy.setArmorType(ArmorType.LIGHT);
		enemy.setSpeedFactor(1);
        return enemy;
    }

	private SimplePlayerAttackDamageCalculator calculator() {
		SimplePlayerAttackDamageCalculator calculator = new SimplePlayerAttackDamageCalculator();
		Predicate<Enemy> enemyActivityPredicate = _ -> true;
		calculator.setEnemyActivityPredicate(enemyActivityPredicate);
		calculator.setTimestampSupplier(() -> 99L);
		return calculator;
	}

	@Test
	public void testDamageAgainstDeactivatedUnit() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		calculator.setEnemyActivityPredicate(_ -> false);
		Player player = defaultPlayer();
		Enemy enemy = defaultEnemy();
		Damage damage = calculator.damage(player, enemy);
		assertEquals(100, damage.getAmount());
		assertFalse(damage.isIncludingBackstabbing());
		assertFalse(damage.isIncludingCriticalHit());
		assertFalse(damage.isIncludingUpgradeBonus());
		assertTrue(damage.isIncludingOffGuard());

	}

	@Test
	public void testDamageAgainstArtilleryUnit() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		Player player = defaultPlayer();
		Enemy enemy = defaultEnemy();
		enemy.setUnitType(UnitType.ARTILLERY);
		Damage damage = calculator.damage(player, enemy);
		assertEquals(100, damage.getAmount());
		assertFalse(damage.isIncludingBackstabbing());
		assertFalse(damage.isIncludingCriticalHit());
		assertFalse(damage.isIncludingUpgradeBonus());
		assertTrue(damage.isIncludingOffGuard());
	}

	@Test
	public void testFistDamageAgainstRobot() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		Player player = defaultPlayer();
		Enemy enemy = defaultEnemy();
		enemy.setUnitType(UnitType.ROBOT);
		Damage damage = calculator.damage(player, enemy);
		assertEquals(5, damage.getAmount());
		assertFalse(damage.isIncludingBackstabbing());
		assertFalse(damage.isIncludingCriticalHit());
		assertFalse(damage.isIncludingUpgradeBonus());
		assertFalse(damage.isIncludingOffGuard());
	}

	@Test
	public void testPistolDamageAgainstRobot() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		Player player = defaultPlayer();
		player.getArsenal().selectNextWeaponType();
		Enemy enemy = defaultEnemy();
		enemy.setUnitType(UnitType.ROBOT);
		Damage damage = calculator.damage(player, enemy);
		assertEquals(10, damage.getAmount());
		assertFalse(damage.isIncludingBackstabbing());
		assertFalse(damage.isIncludingCriticalHit());
		assertFalse(damage.isIncludingUpgradeBonus());
		assertFalse(damage.isIncludingOffGuard());
	}

	@Test
	public void testBackstabbing() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		Player player = defaultPlayer();
		player.getArsenal().selectNextWeaponType();
		Enemy enemy = defaultEnemy();
		player.setDirection(DirectionType.RIGHT);
		enemy.setDirection(DirectionType.RIGHT);
		enemy.setUnitType(UnitType.ROBOT);
		Damage damage = calculator.damage(player, enemy);
		assertEquals(20, damage.getAmount());
		assertTrue(damage.isIncludingBackstabbing());
		assertFalse(damage.isIncludingCriticalHit());
		assertFalse(damage.isIncludingUpgradeBonus());
		assertFalse(damage.isIncludingOffGuard());
	}

	@Test
	public void testCriticalHit() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		calculator.setTimestampSupplier(() -> 1L);
		Player player = defaultPlayer();
		player.getArsenal().selectNextWeaponType();
		Enemy enemy = defaultEnemy();
		enemy.setUnitType(UnitType.ROBOT);
		Damage damage = calculator.damage(player, enemy);
		assertEquals(30, damage.getAmount());
		assertFalse(damage.isIncludingBackstabbing());
		assertTrue(damage.isIncludingCriticalHit());
		assertFalse(damage.isIncludingUpgradeBonus());
		assertFalse(damage.isIncludingOffGuard());
	}

	@Test
	public void testFirmwareUpgrade() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		Player player = defaultPlayer();
		player.getInventory().put(InventoryItemType.WEAPON_FIRMWARE_UPGRADE, new WeaponFirmwareUpgradeInventoryItem());
		player.getArsenal().selectNextWeaponType();
		Enemy enemy = defaultEnemy();
		enemy.setUnitType(UnitType.ROBOT);
		Damage damage = calculator.damage(player, enemy);
		assertEquals(30, damage.getAmount());
		assertFalse(damage.isIncludingBackstabbing());
		assertFalse(damage.isIncludingCriticalHit());
		assertTrue(damage.isIncludingUpgradeBonus());
		assertFalse(damage.isIncludingOffGuard());
	}

	@Test
	public void testBackStabbingAndCriticalHitAndFirmwareUpgrade() {
		SimplePlayerAttackDamageCalculator calculator = calculator();
		calculator.setTimestampSupplier(() -> 1L);

		Player player = defaultPlayer();
		player.getArsenal().selectNextWeaponType();
		Enemy enemy = defaultEnemy();
		enemy.setUnitType(UnitType.ROBOT);

		player.setDirection(DirectionType.RIGHT);
		enemy.setDirection(DirectionType.RIGHT);
		player.getInventory().put(InventoryItemType.WEAPON_FIRMWARE_UPGRADE, new WeaponFirmwareUpgradeInventoryItem());

		Damage damage = calculator.damage(player, enemy);
		assertEquals(180, damage.getAmount());
		assertTrue(damage.isIncludingBackstabbing());
		assertTrue(damage.isIncludingCriticalHit());
		assertTrue(damage.isIncludingUpgradeBonus());
		assertFalse(damage.isIncludingOffGuard());
	}

}
