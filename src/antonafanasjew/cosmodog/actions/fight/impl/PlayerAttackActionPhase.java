package antonafanasjew.cosmodog.actions.fight.impl;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.fight.AttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.FightActionResult;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.PositionUtils;
import antonafanasjew.cosmodog.view.transitions.AttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.PlayerAttackingFightPhaseTransition;

public class PlayerAttackActionPhase extends AttackActionPhase {

	private static final long serialVersionUID = -3853130683025678558L;

	public PlayerAttackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		super(Constants.PLAYER_ATTACK_ACTION_DURATION, fightPhaseResult);
	}

	@Override
	public void onTrigger() {
		
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
		
		AttackingFightPhaseTransition fightPhaseTransition = new PlayerAttackingFightPhaseTransition();
		fightPhaseTransition.setPlayer(getFightPhaseResult().getPlayer());
		fightPhaseTransition.setEnemy(getFightPhaseResult().getEnemy());
		fightPhaseTransition.setCompletion(0.0f);
		setFightPhaseTransition(fightPhaseTransition);

		String text = String.valueOf(getFightPhaseResult().getDamage());
		
		DirectionType playerDirection = fightPhaseTransition.getPlayer().getDirection();
		DirectionType enemyDirection = fightPhaseTransition.getEnemy().getDirection();
		DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(fightPhaseTransition.getPlayer(), fightPhaseTransition.getEnemy());
		
		boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
		boolean enemyLooksAway = enemyDirection.equals(playerDirection);
		
		if (playerLooksAtEnemy && enemyLooksAway) {
			text = text + " (x2)";
		}
		OverheadNotificationAction.registerOverheadNotification(getFightPhaseResult().getEnemy(), text);
		
		getFightPhaseResult().getPlayer().lookAtActor(getFightPhaseResult().getEnemy());
		getFightPhaseResult().getEnemy().lookAtActor(getFightPhaseResult().getPlayer());
		
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		updateCompletion(before, after, gc, sbg);
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
		
		setFightPhaseTransition(null);
	}


}
