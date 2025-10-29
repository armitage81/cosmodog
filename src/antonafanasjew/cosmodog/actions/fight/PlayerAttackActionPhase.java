package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.fighting.Damage;

import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class PlayerAttackActionPhase extends AttackActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	public static final Map<WeaponType, Integer> WEAPON_BASED_ATTACK_DURATIONS = new HashMap<>();
	static {
		WEAPON_BASED_ATTACK_DURATIONS.put(WeaponType.FISTS, 250);
		WEAPON_BASED_ATTACK_DURATIONS.put(WeaponType.PISTOL, 500);
		WEAPON_BASED_ATTACK_DURATIONS.put(WeaponType.SHOTGUN, 500);
		WEAPON_BASED_ATTACK_DURATIONS.put(WeaponType.RIFLE, 800);
		WEAPON_BASED_ATTACK_DURATIONS.put(WeaponType.MACHINEGUN, 1000);
		WEAPON_BASED_ATTACK_DURATIONS.put(WeaponType.RPG, 1200);
	}
	public static final Map<WeaponType, Float> WEAPON_BASED_DAMAGE_TIMES = new HashMap<>();
	static {
		WEAPON_BASED_DAMAGE_TIMES.put(WeaponType.FISTS, 0.5f);
		WEAPON_BASED_DAMAGE_TIMES.put(WeaponType.PISTOL, 0.4f);
		WEAPON_BASED_DAMAGE_TIMES.put(WeaponType.SHOTGUN, 0.3f);
		WEAPON_BASED_DAMAGE_TIMES.put(WeaponType.RIFLE, 0.5f);
		WEAPON_BASED_DAMAGE_TIMES.put(WeaponType.MACHINEGUN, 0.0f);
		WEAPON_BASED_DAMAGE_TIMES.put(WeaponType.RPG, 0.6f);
	}

	private boolean shotHappened = false;
	private WeaponType originallySelectedWeaponType;

	public static int duration(FightPlan.FightPhasePlan fightPhasePlan) {
		if (fightPhasePlan.getDamage().isIncludingOffGuard()) {
			return WEAPON_BASED_ATTACK_DURATIONS.get(WeaponType.FISTS);
		}
		return WEAPON_BASED_ATTACK_DURATIONS.get(fightPhasePlan.getPlayer().getArsenal().getSelectedWeaponType());
	}

	public PlayerAttackActionPhase(FightPlan.FightPhasePlan fightPhasePlan) {
		super(duration(fightPhasePlan), fightPhasePlan);
	}

	@Override
	public void onTrigger() {
		getFightPhasePlan().getPlayer().lookAtActor(getFightPhasePlan().getEnemy());
		getFightPhasePlan().getEnemy().lookAtActor(getFightPhasePlan().getPlayer());

		Damage damage = getFightPhasePlan().getDamage();
		if (!damage.weaponUsed()) {
			originallySelectedWeaponType = getFightPhasePlan().getPlayer().getArsenal().getSelectedWeaponType();
			getFightPhasePlan().getPlayer().getArsenal().selectWeaponType(WeaponType.FISTS);
		}

	}

	@Override
	protected void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {

		if (shotHappened) {
			return;
		}

		Player player = getFightPhasePlan().getPlayer();
		WeaponType selectedWeaponType = player.getArsenal().getSelectedWeaponType();
		Damage damage = getFightPhasePlan().getDamage();

		if (getCompletionRate() < WEAPON_BASED_DAMAGE_TIMES.get(selectedWeaponType)) {
			return;
		}

		shotHappened = true;
		WeaponType.sound(selectedWeaponType).play();
		String text = String.valueOf(damage.getAmount());
		OverheadNotificationAction.registerOverheadNotification(getFightPhasePlan().getEnemy(), text);
		if (damage.isIncludingOffGuard()) {
			OverheadNotificationAction.registerOverheadNotification(getFightPhasePlan().getEnemy(), "(OFF-GUARD)");
		} else if (damage.isIncludingSquashed()) {
			OverheadNotificationAction.registerOverheadNotification(getFightPhasePlan().getEnemy(), "(SQUASHED)");
		} else {
			if (damage.isIncludingBackstabbing()) {
				OverheadNotificationAction.registerOverheadNotification(getFightPhasePlan().getEnemy(), "(BACK-STABBED)");
			}

			if (damage.isIncludingCriticalHit()) {
				OverheadNotificationAction.registerOverheadNotification(getFightPhasePlan().getEnemy(), "(CRITICAL HIT)");
			}

			if (damage.isIncludingUpgradeBonus()) {
				OverheadNotificationAction.registerOverheadNotification(getFightPhasePlan().getEnemy(), "(FIRMWARE UPGRADE)");
			}
		}
	}

	@Override
	public void onEnd() {
		
		Player player = getFightPhasePlan().getPlayer();
		Enemy enemy = getFightPhasePlan().getEnemy();
		Damage damage = getFightPhasePlan().getDamage();
		enemy.setLife(enemy.getLife() - damage.getAmount());

		Arsenal arsenal = player.getArsenal();
		WeaponType weaponType = arsenal.getSelectedWeaponType();
		if (weaponType != null) {
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
			weapon.reduceAmmunition(1);
		}

		if (!damage.weaponUsed()) {
			player.getArsenal().selectWeaponType(originallySelectedWeaponType);
		}

	}


}
