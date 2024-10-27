package antonafanasjew.cosmodog.actions.fight;

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
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();

		String text = String.valueOf(getFightPhaseResult().getDamage());

		DirectionType playerDirection = getFightPhaseResult().getPlayer().getDirection();
		DirectionType enemyDirection = getFightPhaseResult().getEnemy().getDirection();
		DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(getFightPhaseResult().getPlayer(), getFightPhaseResult().getEnemy());
		
		boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
		boolean enemyLooksAway = enemyDirection.equals(playerDirection);
		Enemy enemy = getFightPhaseResult().getEnemy();
		boolean criticalHitsAllowed = !enemy.getUnitType().isRangedUnit() && !(enemy.getSpeedFactor() == 0.0f);
		
		if (playerLooksAtEnemy && enemyLooksAway && criticalHitsAllowed) {
			text = text + " (x2)";
		}
		
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), text);
		
		getFightPhaseResult().getPlayer().lookAtActor(getFightPhaseResult().getEnemy());
		getFightPhaseResult().getEnemy().lookAtActor(getFightPhaseResult().getPlayer());
		
		
	}

	@Override
	public void onEnd() {
		
		Player player = getFightPhaseResult().getPlayer();
		Enemy enemy = getFightPhaseResult().getEnemy();
		
		int damage = getFightPhaseResult().getDamage();
		
		enemy.setLife(enemy.getLife() - damage);
		Arsenal arsenal = player.getArsenal();
		WeaponType weaponType = arsenal.getSelectedWeaponType();
		if (weaponType != null) {
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
			weapon.reduceAmmunition(1);
		}
		
	}


}
