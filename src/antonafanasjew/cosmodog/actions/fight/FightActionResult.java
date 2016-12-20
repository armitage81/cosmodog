package antonafanasjew.cosmodog.actions.fight;

import java.util.ArrayList;

import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Represents the result of a fight action with all intermediary steps.
 * 
 */
public class FightActionResult extends ArrayList<FightPhaseResult>{

	private static final long serialVersionUID = -761976411668201727L;

	/**
	 * Represents a single attack phase result.
	 */
	public static class FightPhaseResult {
		private Player player;
		private Enemy enemy;
		
		private int damage;
		
		private boolean playerAttack;
		
		/**
		 * Returns an instance of the fight phase result.
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
		 * @return Player character.
		 */
		public Player getPlayer() {
			return player;
		}

		/**
		 * Returns the enemy involved in the attack phase. 
		 * @return Enemy.
		 */
		public Enemy getEnemy() {
			return enemy;
		}

		/**
		 * Returns the attacker damage.
		 * @return Attacker damage.
		 */
		public int getDamage() {
			return damage;
		}

		/**
		 * True, if player character attacks in this phase, false otherwise.
		 * @return true if PC attacks, false if enemy attacks.
		 */
		public boolean isPlayerAttack() {
			return playerAttack;
		}

		/**
		 * Returns true if the dealt damage is more than enemies life and the player is attacking in this phase.
		 * @return true if dealt damage is more than enemies life and the player is attacking, false otherwise.
		 */
		public boolean enoughDamageToKillEnemy() {
			return playerAttack && damage >= enemy.getActualLife();
		}
		
		public boolean enoughDamageToKillPlayer() {
			return !playerAttack && damage >= player.getActualLife();
		}
		
	}
	
	
	/**
	 * Returns the complete damage the player suffers during the fight(with perhaps multiple enemies).
	 * This information is needed to decide whether to calculate further phases results or not. (Dead player character cannot continue fighting).
	 * @return Accumulated damage taken during the fight.
	 */
	public int accumulatedDamageForPlayer() {
		int retVal = 0;
		for (FightPhaseResult phaseResult : this) {
			if (phaseResult.playerAttack == false) {
				retVal += phaseResult.damage;
			}
		}
		return retVal;
	}

	/**
	 * Returns true if the damage accumulated by player character is more than his life points.
	 * @return true if the fight results in players death, false otherwise.
	 */
	public boolean enoughDamageToKillPlayer() {
		Player player = ApplicationContextUtils.getPlayer();
		return player.getInventory().hasVehicle() == false && accumulatedDamageForPlayer() >= player.getActualLife();
	}
	
}


