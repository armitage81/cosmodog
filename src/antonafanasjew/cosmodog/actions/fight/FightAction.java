package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.Date;
import java.util.List;
import java.util.Set;

import antonafanasjew.cosmodog.camera.Cam;
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
 * <p>
 * This action has its own action registry to maintain the fight phases.
 * <p>
 * It implements the PhaseBasedAction interface that holds code common for phased based actions.
 * <p>
 * The fight can be initiated by the player or by an enemy.
 */
public class FightAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -5197319922966169468L;

	/**
	 * This is the enemy that is attacked by the player if the latter initiates the fight.
	 * The enemy can be alerted or not. If not killed during the attack, the enemy will retaliate unless it is inactive
	 * (which is, for example, the case for solar-driven tanks at night).
	 * <p>
	 * Additionally, there can be other enemies involved in the fight. These are the alerted enemies adjacent to the player
	 * and ranged enemies in close range.
	 * <p>
	 * Regardless of the constellation, the target enemy is the only enemy that can be damaged during the fight.
	 * <p>
	 * If the fight is initiated by the enemy, the target enemy will be null.
	 */
	private final Enemy targetEnemy;

	/**
	 * This property holds the fight result. It is calculated during the initialization of the fight action.
	 * The fight result is a list of fight phase results. Each fight phase result represents a single attack phase, be it
	 * the player attacking an enemy or an enemy attacking the player. It holds the information about the attacker, about the
	 * defender and about the damage dealt.
	 * <p>
	 * The fight action result is calculated upfront at the beginning of the action. The execution of the fight action is then
	 * going through each fight phase result and applying it to the game state. In a situation, when a player attacks an enemy
	 * and there are three other adjacent enemies and an artillery in range, the outcome is already known while player and the enemies
	 * hit each other, the grenades fly etc. The fight action result is the script of the fight.
	 */
	private final FightActionResult fightActionResult = new FightActionResult();

	/**
	 * The calculation that the player deals to an enemy is a complex process. It involves the player's selected weapon,
	 * the enemy's armor, the player's direction, the enemy's direction, the ammunition amount etc.
	 * <p>
	 * There are also critical hits to consider. There are no critical hits for turrets and artillery units.
	 * <p>
	 * All these aspects are considered in the player attack damage calculator.
	 */
	private final AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator;

	/**
	 * During the fight, the player can run out of ammunition. In such a case, the player is considered to be unarmed.
	 * Then this calculator is used to calculate the damage dealt by the player to the enemy.
	 * <p>
	 * Take note: This calculator is not used if the player has no weapon selected, only if the ammunition is depleted.
	 */
	private final AbstractPlayerAttackDamageCalculator playerAttackDamageCalculatorIfNoAmmo;

	/**
	 * The enemy attack damage calculator calculates the damage dealt by the enemy to the player.
	 * In most cases, the enemy attack damage calculation is a simple process. It involves the enemy's attack power
	 * as a single factor for damage calculation.
	 * But there is also another damage calculator. It deals the damage of zero to the player if the damage feature
	 * is deactivated.
	 */
	private final AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator;

	/**
	 * Creates a fight action where the player is the passive part of the fight, meaning that the player is attacked by an enemy.
	 * The target enemy is set to null in this case.
	 *
	 * @param playerAttackDamageCalculator The calculator that calculates the damage dealt by the player to the enemy.
	 * (actually, this calculator is irrelevant in such a situation, because the player is passive and not attacking).
	 * @param playerAttackDamageCalculatorIfNoAmmo The calculator that calculates the damage dealt by the player to the enemy
	 * if the ammunition of the player's selected weapon has depleted.
	 * (actually, this calculator is irrelevant in such a situation, because the player is passive and not attacking).
	 * @param enemyAttackDamageCalculator The calculator that calculates the damage dealt by the enemy to the player.
	 */
	public FightAction(
			AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator,
			AbstractPlayerAttackDamageCalculator playerAttackDamageCalculatorIfNoAmmo,
			AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator) {
		this(null, playerAttackDamageCalculator, playerAttackDamageCalculatorIfNoAmmo, enemyAttackDamageCalculator);
	}

	/**
	 * Creates a fight action where the player can be the active part of the fight, meaning that the player is the attacker.
	 * In that case, the target enemy is the enemy that is initially targeted by the player. Additionally, there can be multiple
	 * other enemies involved in the fight. If the target enemy is set to null, the player is the passive part of the fight, meaning
	 * that the player is attacked by an enemy.
	 *
	 * @param targetEnemy The enemy that is attacked by the player if the latter initiates the fight. Null if the player is the passive part of the fight.
	 * @param playerAttackDamageCalculator The calculator that calculates the damage dealt by the player to the enemy.
	 * @param playerAttackDamageCalculatorIfNoAmmo The calculator that calculates the damage dealt by the player to the enemy
	 * if the ammunition of the player's selected weapon has depleted.
	 * @param enemyAttackDamageCalculator The calculator that calculates the damage dealt by the enemy to the player.
	 */
	public FightAction(
			Enemy targetEnemy,
			AbstractPlayerAttackDamageCalculator playerAttackDamageCalculator,
			AbstractPlayerAttackDamageCalculator playerAttackDamageCalculatorIfNoAmmo,
			AbstractEnemyAttackDamageCalculator enemyAttackDamageCalculator) {
		this.targetEnemy = targetEnemy;
		this.playerAttackDamageCalculator = playerAttackDamageCalculator;
		this.playerAttackDamageCalculatorIfNoAmmo = playerAttackDamageCalculatorIfNoAmmo;
		this.enemyAttackDamageCalculator = enemyAttackDamageCalculator;
	}

	/**
	 * Calls the player's listener to notify it about the impending fight.
	 * <p>
	 * Then calculates the result for the whole fight. The fight result is a list of fight phase results.
	 * Each fight phase result represents a single attack phase, be it the player attacking an enemy or an enemy attacking the player.
	 * <p>
	 * The fight result will not contain unnecessary phases. For instance: If the player has 1 hit point and is adjacent to
	 * 2 alerted attackers, only the phase result for the first enemy attack will be added to the list. After all, the player will be dead
	 * after this attack and there is no need to add the result of the second attack.
	 * <p>
	 * The calculated result is set to the property fightActionResult.
	 * <p>
	 * The property fightActionResult is then used to initialize the action phase registry. Fight action phases are created based on the
	 * fight phase results. The fight action phases are then registered in the action phase registry. Fight actions contain such things as
	 * player attacking enemies, enemies attacking the player, the destruction of enemies, ranged attacks by artillery units etc.
	 * <p>
	 * At the end of this method, the fight is decided and its execution is prepared. The fight is then played in the onUpdate method.
	 * <p>
	 * Take note: In case the player uses the binoculars to zoom out, the fight action will reset the zoom.
	 */
	@Override
	public void onTriggerInternal() {
		Player player = ApplicationContextUtils.getPlayer();
		player.beginFight();
		initFightActionResult();
		initActionPhaseRegistry();
	}

	/**
	 * At the end of the action, the player's listener is called to indicate the end of the fight.
	 * <p>
	 * Usually, same calculations are executed here as the ones after the player's movement.
	 * <p>
	 * Note: afterMovement and afterFight listener methods do not trigger enemy movements or attacks.
	 * Instead, they move during the player's movement action, and they fight during the player's movement or fight action.
	 */
	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endFight();
	}

	/**
	 * Initializes the fight action result by forecasting the fights between the
	 * player and all adjacent enemies.
	 */
	private void initFightActionResult() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		//As the fight action result is prepared, the ammunition of the selected weapon must be considered.
		//It could be that only one shot is left while being surrounded by up to 4 enemies.
		//In such a case, the remaining ammunition needs to be counted down while preparing the result.
		//If the ammunition is assumed to end, the subsequent attacks should be considered as unarmed.
		Arsenal arsenal = player.getArsenal();
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		Weapon selectedWeapon = arsenal.getWeaponsCopy().get(selectedWeaponType);
		int remainingAmmo = selectedWeaponType.equals(WeaponType.FISTS) ? 100 : selectedWeapon.getAmmunition();

		//These are all melee enemies adjacent to the player. There can be max 4 of them.
		List<Enemy> adjacentEnemies = CosmodogMapUtils.enemiesAdjacentToPlayer(map, player);

		//These are all ranged enemies who have the player in sight. Mostly, artillery units.
		List<Enemy> adjacentRangedEnemies = CosmodogMapUtils.rangedEnemiesAdjacentToPlayer(map, player);

		//Ranged units could be adjacent to the player. They must be removed from there to not appear in both lists.
		adjacentEnemies.removeAll(adjacentRangedEnemies);

		//Enemy attackers are all adjacent enemies and all ranged enemies who have the player in sight.
		//But: Inactive units are not attacking the player. (Usually, they are solar-driven tanks at night.)
		//Take note: Enemies that are not alerted (player not in sight) are also considered attackers.
		// But they will be filtered out later.
		Set<Enemy> attackers = Sets.newHashSet();
		attackers.addAll(adjacentEnemies);
		attackers.addAll(adjacentRangedEnemies);
		EnemiesUtils.removeInactiveUnits(attackers);

		//This is the case when the player is the attacker. His attack result against the target enemy is calculated first.
		//Then, enemy retaliation result is calculated.
		//The target enemy is removed from the list of enemy attackers. After all, it already had the chance to attack the player
		//during its retaliation.
		//Take note: There is keeping track of the ammunition of the player's selected weapon to switch to the unarmed
		// damage calculator during calculation of the future phase results in case it is forecasted as depleted.
		if (targetEnemy != null) {
			updateFightActionResultForOneEnemy(player, targetEnemy, true, remainingAmmo <= 0);
			attackers.remove(targetEnemy);
			remainingAmmo--;
		}

		//Now, all enemy attack results are calculated. It happens in both cases when the player is the initial attacker or not.
		//But if the player was the attacker, its target enemy was already removed from the list of attackers after its retaliation.
		//Take note: Also here, the ammunition of the player's selected weapon is kept track upon.
		//Take note: If player's death is forecasted, the subsequent enemy attacks are canceled.
		for (Enemy enemy : attackers) {
			updateFightActionResultForOneEnemy(player, enemy, false, remainingAmmo <= 0);
			remainingAmmo--;
			if (fightActionResult.enoughDamageToKillPlayer()) {
				break;
			}
		}
	}

	/**
	 * Forecasts the fight result between the player and one enemy.
	 * The player can be the attacker or not.
	 *
	 * @param player The player instance.
	 * @param enemy The enemy instance involved in the fight.
	 * @param playerIsAttacker True if the player is the attacker, false if the enemy is the attacker.
	 * @param unarmedAttack This name is misleading. The flag means not the selected weapon but whether the ammunition is depleted.
	 */
	private void updateFightActionResultForOneEnemy(Player player, Enemy enemy, boolean playerIsAttacker, boolean unarmedAttack) {

		//If the player is the attacker, the enemy becomes alerted.
		//TODO: Why is it done here? This should be done in the player's attack action.
		if (playerIsAttacker) {
			enemy.increaseAlertLevelToMax();
		}

		boolean playerHasPlatform = player.getInventory().hasPlatform();

		//Damage calculators are being used to calculate the damage dealt by the player to the enemy and vice versa.
		//If the player drives the platform, the fight is skipped. The enemies are simply destroyed by the platform instead.
		//Otherwise, the fight result is calculated.
		//Take note: The result for enemies retaliation in case the player is the attacker is calculated only if the enemy survives the attack.
		//Take note: The damage calculator for the player's attack is switched to the unarmed damage calculator if the ammunition is depleted.
		//TODO: How can an enemy not be alerted at this point? If the player is the attacker, the enemy is alerted anyway. If the enemy is the attacker, it is alerted per definition.
		if (enemy.getAlertLevel() > 0 && !playerHasPlatform) {

			DamageCalculator playerDamageCalculator = unarmedAttack ? playerAttackDamageCalculatorIfNoAmmo : playerAttackDamageCalculator;
			int playerAttackDamage = playerDamageCalculator.damage(player, enemy);
			boolean playerCriticalHit = playerDamageCalculator.criticalHit(player, enemy);

			FightActionResult.FightPhaseResult playerPhaseResult = FightPhaseResult.instance(player, enemy, playerAttackDamage, true, playerCriticalHit);

			int enemyAttackDamage = enemyAttackDamageCalculator.damage(enemy, player);
			boolean enemyCriticalHit = enemyAttackDamageCalculator.criticalHit(enemy, player);
			FightActionResult.FightPhaseResult enemyPhaseResult = FightPhaseResult.instance(player, enemy, enemyAttackDamage, false, enemyCriticalHit);

			if (playerIsAttacker) {
				fightActionResult.add(playerPhaseResult);
				if (!playerPhaseResult.enoughDamageToKillEnemy() && EnemiesUtils.enemyActive(enemy)) {
					fightActionResult.add(enemyPhaseResult);
				}
			} else {
				fightActionResult.add(enemyPhaseResult);
			}

		}
	}

	/**
	 * Initializes the action phase registry. It uses the fight action result to create the fight action phases.
	 * The usual action phases are of types: PlayerAttackActionPhase, EnemyDestructionActionPhase, DefaultEnemyAttackActionPhase, ArtilleryAttackActionPhase.
	 * <p>
	 * An example for the sequence of action phases when the player is attacking (types are abbreviated, e.g. PlayerAttackActionPhase is PA):
	 * <p>
	 * - PA, ED, DEA, DEA, AA - Player attacked and destroyed an enemy, then got hit by two other adjacent enemies and by an artillery.
	 * <p>
	 * Another example for the sequence of action phases when the player is attacked
	 * <p>
	 * - DEA, DEA, DEA, AA - Player got hit by 3 adjacent enemies and then by the artillery.
	 * <p>
	 * Remember: While action result phases are calculated upfront and are forecasts of the fight, the action phases are executed in the onUpdate method.
	 */
	private void initActionPhaseRegistry() {

		for (FightActionResult.FightPhaseResult phaseResult : fightActionResult) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

			//If there is an overhead notification action, it is canceled.
			//TODO: Why is it needed?
			OverheadNotificationAction overheadNotificationAction = (OverheadNotificationAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.OVERHEAD_NOTIFICATION);
			if (overheadNotificationAction != null) {
				overheadNotificationAction.cancel();
			}

			//The attack action phase is created based on the fight phase result and registered in the local action phase registry. It can be a player attack or an enemy attack.
			AttackActionPhase attackActionPhase = FightActionPhaseFactory.attackActionPhase(phaseResult);
			getPhaseRegistry().registerPhase("attack", attackActionPhase);

			//In case the attack action phase is a player attack and the enemy is destroyed, the enemy destruction action phase is created and registered in the local action phase registry.
			if (phaseResult.isPlayerAttack() && phaseResult.enoughDamageToKillEnemy()) {
				EnemyDestructionActionPhase enemyDestructionActionPhase = FightActionPhaseFactory.enemyDestructionActionPhase(phaseResult.getPlayer(), phaseResult.getEnemy());
				getPhaseRegistry().registerPhase("enemyDestruction", enemyDestructionActionPhase);
			}
		}
	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		super.onUpdate(before, after, gc, sbg);
	}
}
