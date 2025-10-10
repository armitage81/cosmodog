package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.fighting.Damage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.PositionUtils;

import java.io.Serial;

public class PlayerAttackActionPhase extends AttackActionPhase {

	@Serial
	private static final long serialVersionUID = -3853130683025678558L;

	public PlayerAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.PLAYER_ATTACK_ACTION_DURATION, fightPhaseResult);
	}

	@Override
	public void onTrigger() {

		Player player = getFightPhaseResult().getPlayer();

		WeaponType selectedWeaponType = player.getArsenal().getSelectedWeaponType();

		WeaponType.sound(selectedWeaponType).play();

		Damage damage = getFightPhaseResult().getDamage();

		String text = String.valueOf(damage.getAmount());

		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), text);

		if (damage.isIncludingOffGuard()) {
			OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), "(OFF-GUARD)");
		} else {
			if (damage.isIncludingBackstabbing()) {
				OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), "(BACK-STABBED)");
			}

			if (damage.isIncludingCriticalHit()) {
				OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), "(CRITICAL HIT)");
			}

			if (damage.isIncludingUpgradeBonus()) {
				OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), "(FIRMWARE UPGRADE)");
			}
		}
		getFightPhaseResult().getPlayer().lookAtActor(getFightPhaseResult().getEnemy());
		getFightPhaseResult().getEnemy().lookAtActor(getFightPhaseResult().getPlayer());
		
		
	}

	@Override
	public void onEnd() {
		
		Player player = getFightPhaseResult().getPlayer();
		Enemy enemy = getFightPhaseResult().getEnemy();
		
		Damage damage = getFightPhaseResult().getDamage();

		enemy.setLife(enemy.getLife() - damage.getAmount());
		Arsenal arsenal = player.getArsenal();
		WeaponType weaponType = arsenal.getSelectedWeaponType();
		if (weaponType != null) {
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
			weapon.reduceAmmunition(1);
		}
		
	}


}
