package antonafanasjew.cosmodog.actions.fight;

import java.io.Serial;
import java.util.ArrayList;

import antonafanasjew.cosmodog.actions.fight.FightActionResult.FightPhaseResult;
import antonafanasjew.cosmodog.fighting.Damage;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FightActionResult extends ArrayList<FightPhaseResult>{

	@Serial
	private static final long serialVersionUID = -761976411668201727L;

	public static class FightPhaseResult {

		private Player player;

		private Enemy enemy;

		private Damage damage;

		private boolean playerAttack;
		
		public static FightPhaseResult instance(Player player, Enemy enemy, Damage damage, boolean playerAttack) {
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

		public Damage getDamage() {
			return damage;
		}

		public boolean isPlayerAttack() {
			return playerAttack;
		}

		public boolean enoughDamageToKillEnemy() {
			return playerAttack && damage.getAmount() >= enemy.getActualLife();
		}

	}

	public int accumulatedDamageForPlayer() {
		int retVal = 0;
		for (FightPhaseResult phaseResult : this) {
			if (!phaseResult.playerAttack) {
				retVal += phaseResult.damage.getAmount();
			}
		}
		return retVal;
	}

	public boolean enoughDamageToKillPlayer() {
		Player player = ApplicationContextUtils.getPlayer();
		return !player.getInventory().hasVehicle() && accumulatedDamageForPlayer() >= player.getActualLife();
	}
}


