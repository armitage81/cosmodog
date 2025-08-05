package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.ArrayList;

import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FightActionResult extends ArrayList<FightPhaseResult>{

	@Serial
	private static final long serialVersionUID = -761976411668201727L;

	public static class FightPhaseResult {

		private Player player;

		private Enemy enemy;

		private int damage;

		private boolean criticalHit;

		private boolean playerAttack;
		
		public static FightPhaseResult instance(Player player, Enemy enemy, int damage, boolean playerAttack, boolean criticalHit) {
			FightPhaseResult result = new FightPhaseResult();
			result.player = player;
			result.enemy = enemy;
			result.damage = damage;
			result.playerAttack = playerAttack;
			result.criticalHit = criticalHit;
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

		public boolean isCriticalHit() {
			return criticalHit;
		}

		public boolean enoughDamageToKillEnemy() {
			return playerAttack && damage >= enemy.getActualLife();
		}

	}

	public int accumulatedDamageForPlayer() {
		int retVal = 0;
		for (FightPhaseResult phaseResult : this) {
			if (!phaseResult.playerAttack) {
				retVal += phaseResult.damage;
			}
		}
		return retVal;
	}

	public boolean enoughDamageToKillPlayer() {
		Player player = ApplicationContextUtils.getPlayer();
		return !player.getInventory().hasVehicle() && accumulatedDamageForPlayer() >= player.getActualLife();
	}
}


