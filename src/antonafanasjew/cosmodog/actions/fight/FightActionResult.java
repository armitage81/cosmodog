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
	 * Represents a single attack phase.
	 */
	public static class FightPhaseResult {
		private Player player;
		private Enemy enemy;
		
		private int damage;
		
		private boolean playerAttack;
		
		public static FightPhaseResult instance(Player player, Enemy enemy, int damage, boolean playerAttack) {
			FightPhaseResult result = new FightPhaseResult();
			result.player = player;
			result.enemy = enemy;
			result.damage = damage;
			result.playerAttack = playerAttack;
			return result;
		}
		

		public Player getPlayer() {
			return player;
		}

		public Enemy getEnemy() {
			return enemy;
		}

		public int getDamage() {
			return damage;
		}

		public boolean isPlayerAttack() {
			return playerAttack;
		}

		public boolean enoughDamageToKillEnemy() {
			return playerAttack && damage >= enemy.getLife();
		}
		
	}
	
	public int accumulatedDamageForPlayer() {
		int retVal = 0;
		for (FightPhaseResult phaseResult : this) {
			if (phaseResult.playerAttack == false) {
				retVal += phaseResult.damage;
			}
		}
		return retVal;
	}
	
	public boolean enoughDamageToKillPlayer() {
		Player player = ApplicationContextUtils.getPlayer();
		return player.getInventory().hasVehicle() == false && accumulatedDamageForPlayer() >= player.getLife();
	}
	
}


