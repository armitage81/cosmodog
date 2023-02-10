package antonafanasjew.cosmodog.actions.fight;

import java.util.List;
import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.fighting.AbstractEnemyAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.AbstractPlayerAttackDamageCalculator;
import antonafanasjew.cosmodog.fighting.DamageCalculator;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.EnemiesUtils;

/**
 * Represents the complete fight after a turn (with maybe multiple enemies)
 * 
 * This action has it's own action registry to maintain the fight phases.
 * 
 */
public class FightAction extends PhaseBasedAction {

	private static final long serialVersionUID = -5197319922966169468L;

	private Enemy targetEnemy;
	private FightActionResult fightActionResult = new FightActionResult();

	private AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator;
	private AbstractPlayerAttackDamageCalculator playerAttackDamageCalculatorIfNoAmmo;
	private AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator;

	
	public FightAction(AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator, AbstractPlayerAttackDamageCalculator playerAttackDamageCalculatorIfNoAmmo, AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator) {
		this(null, playerAttackDamageCalculator, playerAttackDamageCalculatorIfNoAmmo, enemyAttackDamageCalculator);
	}
	
	/**
	 * Initializes the fight action. 
	 * Target enemy is set if the player initiates the fight. Otherwise the attacker is one or more alerted adjacent enemies and the parameter is null.
	 */
	public FightAction(Enemy targetEnemy, AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator, AbstractPlayerAttackDamageCalculator playerAttackDamageCalculatorIfNoAmmo, AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator) {
		this.targetEnemy = targetEnemy;
		this.playerAttackDamageCalculator = playerAttackDamageCalculator;
		this.playerAttackDamageCalculatorIfNoAmmo = playerAttackDamageCalculatorIfNoAmmo;
		this.enemyAttackDamageCalculator = enemyAttackDamageCalculator;
	}

	/**
	 * Calculates the complete fight result. Calculates the fight phases based
	 * on the fight result and registers them in the internal action registry.
	 */
	@Override
	public void onTrigger() {
		
		Player player = ApplicationContextUtils.getPlayer();
		player.beginFight();
		
		initFightActionResult();
		initActionPhaseRegistry();
	}

	/**
	 * Updates the action registry, causing it to play forward the phase queue.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		getActionPhaseRegistry().update(after - before, gc, sbg);
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endFight();
	}
	
	/**
	 * Returns true if the fight action registry is empty, meaning that there
	 * are no unplayed fight phases.
	 */
	@Override
	public boolean hasFinished() {
		return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.FIGHT);
	}

	/*
	 * Initializes the fight action result by simulating the fights between the
	 * player and all adjacent enemies.
	 */
	private void initFightActionResult() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();

		//As we prepare the fight action result we must consider the ammunition of the selected weapon.
		//It could be that we have only one shot left but are surrounded by up to 4 enemies.
		//In such a case, we need to count down the remaining ammunition while preparing the result.
		//In the case we assume the ammunition to end, the subsequent attacks should be considered as unarmed. 
		Arsenal arsenal = player.getArsenal();
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		Weapon selectedWeapon = arsenal.getWeaponsCopy().get(selectedWeaponType);
		int remainingAmmo = selectedWeaponType.equals(WeaponType.FISTS) ? 100 : selectedWeapon.getAmmunition();
		
		List<Enemy> adjacentEnemies = CosmodogMapUtils.enemiesAdjacentToPlayer(map, player);
		List<Enemy> adjacentRangedEnemies = CosmodogMapUtils.rangedEnemiesAdjacentToPlayer(map, player);
		adjacentEnemies.removeAll(adjacentRangedEnemies);
		
		Set<Enemy> attackers = Sets.newHashSet();
		attackers.addAll(adjacentEnemies);
		attackers.addAll(adjacentRangedEnemies);

		EnemiesUtils.removeInactiveUnits(attackers);
		
		if (targetEnemy != null) {
			updateFightActionResultForOneEnemy(player, targetEnemy, true, remainingAmmo <= 0);
			attackers.remove(targetEnemy);
			remainingAmmo--;
		}
		
		for (Enemy enemy : attackers) {

			updateFightActionResultForOneEnemy(player, enemy, false, remainingAmmo <= 0);
			remainingAmmo--;
			if (fightActionResult.enoughDamageToKillPlayer()) {
				break;
			}
		}
	}

	private void updateFightActionResultForOneEnemy(Player player, Enemy enemy, boolean playerIsAttacker, boolean unarmedAttack) {
		
		boolean playerHasPlatform = player.getInventory().hasPlatform();
		
		if (playerIsAttacker) {
			 enemy.increaseAlertLevelToMax();
		}
		
		if (enemy.getAlertLevel() > 0 && !playerHasPlatform) {

			DamageCalculator playerDamageCalculator = unarmedAttack ? playerAttackDamageCalculatorIfNoAmmo : playerAttackDamageCalculator;
			int playerAttackDamage = playerDamageCalculator.damage(player, enemy);
			FightActionResult.FightPhaseResult playerPhaseResult = FightPhaseResult.instance(player, enemy, playerAttackDamage, true);

			int enemyAttackDamage = enemyAttackDamageCalculator.damage(enemy, player);
			FightActionResult.FightPhaseResult enemyPhaseResult = FightPhaseResult.instance(player, enemy, enemyAttackDamage, false);

			if (playerIsAttacker) {
				fightActionResult.add(playerPhaseResult);
				if (playerPhaseResult.enoughDamageToKillEnemy() == false && EnemiesUtils.enemyActive(enemy)) {
					fightActionResult.add(enemyPhaseResult);
				}
			} else {
				fightActionResult.add(enemyPhaseResult);
			}

		}
	}

	/*
	 * Calculates the phase queue based on the fight action result.
	 */
	private void initActionPhaseRegistry() {

		for (FightActionResult.FightPhaseResult phaseResult : fightActionResult) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

			OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);

			if (overheadNotificationAction != null) {
				overheadNotificationAction.cancel();

			}
			
			
			AttackActionPhase attackActionPhase = FightActionPhaseFactory.attackActionPhase(phaseResult);

			getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT, attackActionPhase);
			if (phaseResult.isPlayerAttack() && phaseResult.enoughDamageToKillEnemy()) {
				
				EnemyDestructionActionPhase enemyDestructionActionPhase = FightActionPhaseFactory.enemyDestructionActionPhase(phaseResult.getPlayer(), phaseResult.getEnemy());
				getActionPhaseRegistry().registerAction(AsyncActionType.FIGHT, enemyDestructionActionPhase);
			}
		}
	}

}
