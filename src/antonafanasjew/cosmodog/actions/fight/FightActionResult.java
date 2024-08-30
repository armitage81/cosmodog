package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.ArrayList;

import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Represents the result of a fight action.
 * <p>
 * This result is calculated at the beginning of the fight and contains all the fight phases results.
 * It is used to simplify the creation of fight action phases.
 * <p>
 * Instances of this class are lists of fight phase results.
 * Each fight phase result contains the attacker, the target and the damage dealt.
 * 
 */
public class FightActionResult extends ArrayList<FightPhaseResult>{

	@Serial
	private static final long serialVersionUID = -761976411668201727L;

	/**
	 * Represents a single attack phase result.
	 * <p>
	 * This class is used to store the attacker, the target and the damage dealt in a single attack phase.
	 * <p>
	 *
	 */
	public static class FightPhaseResult {

		/**
		 * Player character.
		 */
		private Player player;

		/**
		 * Enemy involved in the fight phase.
		 */
		private Enemy enemy;

		/**
		 * Damage dealt by the attacker (player or enemy).
		 */
		private int damage;

		/**
		 * true if player character is the attacker, false if the enemy is the attacker.
		 */
		private boolean playerAttack;
		
		/**
		 * Returns an instance of the fight phase result.
		 * <p>
		 * This is a factory method to create instances of the fight phase result. It is static.
		 *
		 * @param player Player character.
		 * @param enemy Enemy involved in the fight phase.
		 * @param damage Damage dealt by the attacker.
		 * @param playerAttack true if player character is the attacker, false if the enemy is the attacker.
		 * @return Instance of the attack phase result.
		 */
		public static FightPhaseResult instance(Player player, Enemy enemy, int damage, boolean playerAttack) {
			FightPhaseResult result = new FightPhaseResult();
			result.player = player;
			result.enemy = enemy;
			result.damage = damage;
			result.playerAttack = playerAttack;
			return result;
		}
		
		/**
		 * Returns the player character.
		 *
		 * @return Player character.
		 */
		public Player getPlayer() {
			return player;
		}

		/**
		 * Returns the enemy involved in the attack phase.
		 *
		 * @return Enemy.
		 */
		public Enemy getEnemy() {
			return enemy;
		}

		/**
		 * Returns the attacker damage.
		 *
		 * @return Attacker damage.
		 */
		public int getDamage() {
			return damage;
		}

		/**
		 * True, if player character attacks in this phase, false otherwise.
		 *
		 * @return true if PC attacks, false if enemy attacks.
		 */
		public boolean isPlayerAttack() {
			return playerAttack;
		}

		/**
		 * Returns true if the dealt damage is more than enemy's life and the player is attacking in this phase.
		 * This is needed to decide whether an enemy destruction action phase should be created or not.
		 *
		 * @return true if dealt damage is more than enemies life and the player is attacking, false otherwise.
		 */
		public boolean enoughDamageToKillEnemy() {
			return playerAttack && damage >= enemy.getActualLife();
		}

	}

	/**
	 * Returns the complete damage the player suffers during the fight(with perhaps multiple enemies).
	 * This information is needed to decide whether to calculate further phases results or not. (Dead player character cannot continue fighting).
	 * <p>
	 * The calculation is done by simply adding up damage from all fight phase results where the player is not the attacker.
	 * <p>
	 * Take note: This method does not consider the case when the character has a vehicle.
	 *
	 * @return Accumulated damage taken during the fight.
	 */
	public int accumulatedDamageForPlayer() {
		int retVal = 0;
		for (FightPhaseResult phaseResult : this) {
			if (!phaseResult.playerAttack) {
				retVal += phaseResult.damage;
			}
		}
		return retVal;
	}

	/**
	 * Returns true if the damage accumulated by player character is more than his life points,
	 * and he does not have a vehicle.
	 *
	 * TODO: What if there are multiple enemies and the player has a vehicle with only one armor point and his life is
	 * also almost depleted?
	 *
	 * @return true if the fight results in players death, false otherwise.
	 */
	public boolean enoughDamageToKillPlayer() {
		Player player = ApplicationContextUtils.getPlayer();
		return !player.getInventory().hasVehicle() && accumulatedDamageForPlayer() >= player.getActualLife();
	}
}


