package antonafanasjew.cosmodog.fighting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.WeaponFirmwareUpgradeInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import org.junit.jupiter.api.Test;

public class SimplePlayerAttackDamageCalculatorTest {

	private PlanetaryCalendar planetaryCalendar = new PlanetaryCalendar();
	private SimplePlayerAttackDamageCalculator out = new SimplePlayerAttackDamageCalculator(planetaryCalendar);
	
	@Test
	public void testGodFists() {

		Features.getInstance().setFeature(Features.FEATURE_GODFISTS, true);
		
		Player player = Player.fromPosition(1, 1);
		player.setDirection(DirectionType.RIGHT);
		
		Enemy enemy = new Enemy();
		enemy.setPositionX(2);
		enemy.setPositionY(1);
		enemy.setDirection(DirectionType.LEFT);
		
		int damage = out.damage(player, enemy);
		
		assertEquals(1000, damage);
		
	}

	@Test
	public void testSimpleHit() {

		Features.getInstance().setFeature(Features.FEATURE_GODFISTS, false);
		
		Player player = Player.fromPosition(1, 1);
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.setDirection(DirectionType.RIGHT);
		
		int damage;
		
		Enemy enemy = new Enemy();
		enemy.setPositionX(2);
		enemy.setPositionY(1);
		enemy.setUnitType(UnitType.PIGRAT);
		enemy.setDirection(DirectionType.LEFT);

		
		//FISTS
		
		enemy.setArmorType(ArmorType.NONE);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.LIGHT);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.MEDIUM);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.HEAVY);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.FORTIFIED);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		//PISTOL
		
		player.getArsenal().selectNextWeaponType();
		
		enemy.setArmorType(ArmorType.NONE);
		damage = out.damage(player, enemy);
		assertEquals(15, damage);
		
		enemy.setArmorType(ArmorType.LIGHT);
		damage = out.damage(player, enemy);
		assertEquals(10, damage);
		
		enemy.setArmorType(ArmorType.MEDIUM);
		damage = out.damage(player, enemy);
		assertEquals(10, damage);
		
		enemy.setArmorType(ArmorType.HEAVY);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.FORTIFIED);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		//SHOTGUN
		
		player.getArsenal().selectNextWeaponType();
		
		enemy.setArmorType(ArmorType.NONE);
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
		enemy.setArmorType(ArmorType.LIGHT);
		damage = out.damage(player, enemy);
		assertEquals(15, damage);
		
		enemy.setArmorType(ArmorType.MEDIUM);
		damage = out.damage(player, enemy);
		assertEquals(10, damage);
		
		enemy.setArmorType(ArmorType.HEAVY);
		damage = out.damage(player, enemy);
		assertEquals(10, damage);
		
		enemy.setArmorType(ArmorType.FORTIFIED);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		//RIFLE
		
		player.getArsenal().selectNextWeaponType();
		
		enemy.setArmorType(ArmorType.NONE);
		damage = out.damage(player, enemy);
		assertEquals(40, damage);
		
		enemy.setArmorType(ArmorType.LIGHT);
		damage = out.damage(player, enemy);
		assertEquals(20, damage);
		
		enemy.setArmorType(ArmorType.MEDIUM);
		damage = out.damage(player, enemy);
		assertEquals(15, damage);
		
		enemy.setArmorType(ArmorType.HEAVY);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.FORTIFIED);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		//MACHINEGUN
		
		player.getArsenal().selectNextWeaponType();
		
		enemy.setArmorType(ArmorType.NONE);
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
		enemy.setArmorType(ArmorType.LIGHT);
		damage = out.damage(player, enemy);
		assertEquals(30, damage);
		
		enemy.setArmorType(ArmorType.MEDIUM);
		damage = out.damage(player, enemy);
		assertEquals(20, damage);
		
		enemy.setArmorType(ArmorType.HEAVY);
		damage = out.damage(player, enemy);
		assertEquals(15, damage);
		
		enemy.setArmorType(ArmorType.FORTIFIED);
		damage = out.damage(player, enemy);
		assertEquals(10, damage);
		
		
		//RPG
		
		player.getArsenal().selectNextWeaponType();
		
		enemy.setArmorType(ArmorType.NONE);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.LIGHT);
		damage = out.damage(player, enemy);
		assertEquals(5, damage);
		
		enemy.setArmorType(ArmorType.MEDIUM);
		damage = out.damage(player, enemy);
		assertEquals(20, damage);
		
		enemy.setArmorType(ArmorType.HEAVY);
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
		enemy.setArmorType(ArmorType.FORTIFIED);
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
	}
	
	@Test
	public void testCriticalHit() {
		
		Features.getInstance().setFeature(Features.FEATURE_GODFISTS, false);
		
		Player player = Player.fromPosition(1, 1);
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.setDirection(DirectionType.RIGHT);
		
		int damage;
		
		Enemy enemy = new Enemy();
		enemy.setPositionX(2);
		enemy.setPositionY(1);
		enemy.setUnitType(UnitType.PIGRAT);
		enemy.setDirection(DirectionType.LEFT);
		enemy.setArmorType(ArmorType.NONE);

		
		//SHOTGUN
		player.getArsenal().selectNextWeaponType();
		player.getArsenal().selectNextWeaponType();

		//Player looks at enemy and vice verse => normal hit
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
		//Player hits from behind, but enemy is a turret.
		enemy.setDirection(DirectionType.RIGHT);
		enemy.setSpeedFactor(0);
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
		//Player hits from behind, but enemy is ranged.
		enemy.setDirection(DirectionType.RIGHT);
		enemy.setSpeedFactor(1);
		enemy.setUnitType(UnitType.ARTILLERY);
		damage = out.damage(player, enemy);
		assertEquals(100, damage);
		
		//Player hits from behind, and enemy is normal.
		enemy.setDirection(DirectionType.RIGHT);
		enemy.setSpeedFactor(1);
		enemy.setUnitType(UnitType.PIGRAT);
		damage = out.damage(player, enemy);
		assertEquals(100, damage);
		
	}
	
	
	@Test
	public void testTripleDamage() {
		
		Features.getInstance().setFeature(Features.FEATURE_GODFISTS, false);
		
		Player player = Player.fromPosition(1, 1);
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.setDirection(DirectionType.RIGHT);
		
		int damage;
		
		Enemy enemy = new Enemy();
		enemy.setPositionX(2);
		enemy.setPositionY(1);
		enemy.setUnitType(UnitType.PIGRAT);
		enemy.setDirection(DirectionType.LEFT);
		enemy.setArmorType(ArmorType.NONE);

		
		//SHOTGUN
		player.getArsenal().selectNextWeaponType();
		player.getArsenal().selectNextWeaponType();

		//Player looks at enemy and vice verse => normal hit
		damage = out.damage(player, enemy);
		assertEquals(50, damage);
		
		//Now player has the weapon upgrade firmware and does triple damage
		
		player.getInventory().put(InventoryItemType.WEAPON_FIRMWARE_UPGRADE, new WeaponFirmwareUpgradeInventoryItem());
		damage = out.damage(player, enemy);
		assertEquals(150, damage);
		
	}
	
	@Test
	public void testDamageOnDeactivatedUnits() {
		
		
		planetaryCalendar.setHour(0);
		
		Features.getInstance().setFeature(Features.FEATURE_GODFISTS, false);
		
		Player player = Player.fromPosition(1, 1);
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.setDirection(DirectionType.RIGHT);
		
		int damage;
		
		Enemy enemy = new Enemy();
		enemy.setPositionX(2);
		enemy.setPositionY(1);
		enemy.setUnitType(UnitType.PIGRAT);
		enemy.setDirection(DirectionType.LEFT);

		
		enemy.setArmorType(ArmorType.NONE);
		enemy.setActiveAtDayTimeOnly(true);
		
		damage = out.damage(player, enemy);
		assertEquals(100, damage);
	}
	

}
